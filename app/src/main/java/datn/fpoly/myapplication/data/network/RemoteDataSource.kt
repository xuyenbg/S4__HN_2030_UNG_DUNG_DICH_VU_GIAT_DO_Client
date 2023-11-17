package datn.fpoly.myapplication.data.network

import android.content.Context
import android.os.Build
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import datn.fpoly.myapplication.BuildConfig
import datn.fpoly.myapplication.utils.format
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.net.CookieManager
import java.net.CookiePolicy
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Singleton
class RemoteDataSource {
    companion object {
        const val BASE_URL =
            "https://s4-hn-2030-ung-dung-dich-vu-giat-do.onrender.com/"
        private const val DEFAULT_USER_AGENT = "QLBH-Android"
        private const val DEFAULT_CONTENT_TYPE = "application/json"
    }

    fun <Api> buildApi(
        api: Class<Api>,
        context: Context
    ): Api {
        val gson = GsonBuilder()
            .registerTypeAdapter(Date::class.java, UnitEpochDateTypeAdapter())
            .setLenient()
            .create()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(getRetrofitClient())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(api)
    }

    inner class UnitEpochDateTypeAdapter : TypeAdapter<Date>() {
        override fun write(out: JsonWriter?, value: Date?) {
            if (value == null) {
                out?.nullValue()
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    out?.value(value.format("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"))
                }else{
                    out?.value(SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",Locale.ROOT).format(value))
                }
            }
        }
        override fun read(_in: JsonReader?) =
            if (_in != null) {
                if (_in.peek() == JsonToken.NULL) {
                    _in.nextNull()
                    null
                } else {
                    try {
                        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",Locale.ROOT).parse(_in.nextString())
                    }catch (e:Exception){
                        null
                    }
                }
            } else null
    }

    private fun getRetrofitClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .writeTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .cookieJar(cookieJar())
            .addNetworkInterceptor(customInterceptor())
            .also {
                if (BuildConfig.DEBUG) {
                    it.addInterceptor(loggingInterceptor())
                }
            }
            .build()
    }

    private fun customInterceptor(): Interceptor {
        return Interceptor { chain: Interceptor.Chain ->
            val original = chain.request()
            // rewrite the request
            val request: Request = original.newBuilder()
                .header("User-Agent", DEFAULT_USER_AGENT)
                .header("Accept", DEFAULT_CONTENT_TYPE)
                .method(original.method, original.body)
                .build()
            chain.proceed(request)
        }
    }

    private fun cookieJar(): CookieJar {
        val cookieManager = CookieManager().apply {
            setCookiePolicy(CookiePolicy.ACCEPT_ALL)
        }
        return JavaNetCookieJar(cookieManager)
    }

    private fun loggingInterceptor(): Interceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }
}