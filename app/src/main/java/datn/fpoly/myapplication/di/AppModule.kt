package datn.fpoly.myapplication.di

import android.content.Context
import dagger.Module
import dagger.Provides
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
    fun providerAuthApi(remoteDataSource: RemoteDataSource, context: Context) : AuthApi{
        return remoteDataSource.buildApi( AuthApi::class.java , context)
    }
    @Provides
    fun providerApiCategory(remoteDataSource: RemoteDataSource, context: Context) : APICategory{
        return remoteDataSource.buildApi( APICategory::class.java , context)
    }
    @Provides
    fun providerApiStore(remoteDataSource: RemoteDataSource, context: Context) : APIStore{
        return remoteDataSource.buildApi( APIStore::class.java , context)
    }
    @Provides
    fun providerApiService(remoteDataSource: RemoteDataSource, context: Context) : APIService{
        return remoteDataSource.buildApi( APIService::class.java , context)
    }
}