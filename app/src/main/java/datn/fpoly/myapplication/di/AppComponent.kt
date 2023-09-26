package datn.fpoly.myapplication.di

import datn.fpoly.myapplication.AppApplication
import android.content.Context
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(
    modules = [
        AppModule::class,
        FragmentModule::class
    ]
)
@Singleton
interface AppComponent {
    fun inject(application: AppApplication)
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }
}