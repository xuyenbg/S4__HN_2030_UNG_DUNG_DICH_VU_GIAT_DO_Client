package datn.fpoly.myapplication.di

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module

@Module
interface ViewModelModule{
    @Binds
    fun bindViewModelFactory(factory: AppViewModelFactory): ViewModelProvider.Factory
}