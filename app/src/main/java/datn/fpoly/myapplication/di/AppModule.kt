package datn.fpoly.myapplication.di

import dagger.Module
import dagger.Provides
import datn.fpoly.myapplication.data.network.RemoteDataSource
import datn.fpoly.myapplication.utils.LocalHelper

@Module
object AppModule {
    @Provides
    fun providerLocaleHelper(): LocalHelper = LocalHelper()
    @Provides
    fun provideRemoteDataSource(): RemoteDataSource {
        return RemoteDataSource()
    }
}