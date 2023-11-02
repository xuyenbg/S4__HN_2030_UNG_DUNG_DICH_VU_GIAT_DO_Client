package datn.fpoly.myapplication.di

import android.content.Context
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import datn.fpoly.myapplication.data.model.roomdb.RoomDb
import datn.fpoly.myapplication.data.network.APICategory
import datn.fpoly.myapplication.data.network.APIPost
import datn.fpoly.myapplication.data.network.APIStore
import datn.fpoly.myapplication.data.network.AuthApi
import datn.fpoly.myapplication.data.network.RemoteDataSource
import datn.fpoly.myapplication.data.network.*
import datn.fpoly.myapplication.utils.LocalHelper

@Module
object AppModule {
    @Provides
    fun providerLocaleHelper(): LocalHelper = LocalHelper()

    @Provides
    fun provideRemoteDataSource(): RemoteDataSource {
        return RemoteDataSource()
    }

    @Provides
    fun providerAuthApi(remoteDataSource: RemoteDataSource, context: Context): AuthApi {
        return remoteDataSource.buildApi(AuthApi::class.java, context)
    }
    @Provides
    fun providerRoomDb(context: Context): RoomDb = RoomDb.getDatabase(context)

    @Provides
    fun providerGson():Gson = Gson()

    @Provides
    fun providerApiService(remoteDataSource: RemoteDataSource, context: Context) : APIService {
        return remoteDataSource.buildApi(APIService::class.java, context)
    }

    @Provides
    fun providerApiCategory(remoteDataSource: RemoteDataSource, context: Context): APICategory {
        return remoteDataSource.buildApi(APICategory::class.java, context)
    }

    @Provides
    fun providerApiStore(remoteDataSource: RemoteDataSource, context: Context): APIStore {
        return remoteDataSource.buildApi(APIStore::class.java, context)
    }

    @Provides
    fun providerApiPost(remoteDataSource: RemoteDataSource, context: Context): APIPost {
        return remoteDataSource.buildApi(APIPost::class.java, context)
    }
    @Provides
    fun providerApiOrder(remoteDataSource: RemoteDataSource,context: Context): APIOrder{
        return remoteDataSource.buildApi(APIOrder::class.java,context)
    }
}