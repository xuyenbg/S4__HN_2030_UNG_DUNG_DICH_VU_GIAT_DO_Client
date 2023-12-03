package datn.fpoly.myapplication.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import datn.fpoly.myapplication.AppApplication
import datn.fpoly.myapplication.ui.address.AddressActivity
import datn.fpoly.myapplication.ui.addAddress.AddAddressActivity
import datn.fpoly.myapplication.ui.updateAddress.UpdateAddressActivity
import datn.fpoly.myapplication.ui.address.check_out.CheckOutActivity
import datn.fpoly.myapplication.ui.detailstore.DetailStoreActivity
import datn.fpoly.myapplication.ui.editpost.EditPostActivity
import datn.fpoly.myapplication.ui.favoriteStore.FavoriteStoreActivity
import datn.fpoly.myapplication.ui.historyOrderUser.HistoryOrderActivity
import datn.fpoly.myapplication.ui.home.HomeActivity
import datn.fpoly.myapplication.ui.homeStore.HomeStoreActivity
import datn.fpoly.myapplication.ui.listService.ListServiceActivity
import datn.fpoly.myapplication.ui.listServiceByName.ListServiceByNameActivity
import datn.fpoly.myapplication.ui.login.OTPLoginActivity
import datn.fpoly.myapplication.ui.myProfileUser.MyProfileActivity
import datn.fpoly.myapplication.ui.myshop.MyShopActivity
import datn.fpoly.myapplication.ui.notification.NotificationActivity
import datn.fpoly.myapplication.ui.order.OrderDetailActivity
import datn.fpoly.myapplication.ui.order.OrderDetailStoreActivity
import datn.fpoly.myapplication.ui.postService.AddServiceActivity
import datn.fpoly.myapplication.ui.poststore.AddPostActivity
import datn.fpoly.myapplication.ui.registerstore.RegisterStoreActivity
import datn.fpoly.myapplication.ui.searchService.SearchServiceActivity
import datn.fpoly.myapplication.ui.service.DetailServiceActivity
import datn.fpoly.myapplication.ui.signup.RegisterInforActivity
import datn.fpoly.myapplication.ui.signup.SignUpActivity
import datn.fpoly.myapplication.ui.splashScreen.SplashsActivity
import javax.inject.Singleton

@Component(
    modules = [
        AppModule::class,
        FragmentModule::class,
        ViewModelModule::class
    ]
)
@Singleton
interface AppComponent {
    fun inject(application: AppApplication)
    fun inject(otpLoginActivity: OTPLoginActivity)
    fun inject(signUpActivity: SignUpActivity)
    fun inject(registerInforActivity: RegisterInforActivity)
    fun inject(homeUser: HomeActivity)
    fun inject(detailStore: DetailStoreActivity)
    fun inject(addPostActivity: AddPostActivity)
    fun inject(homeStore: HomeStoreActivity)
    fun inject(detailService: DetailServiceActivity)
    fun inject(listService: ListServiceActivity)
    fun inject(registerStore: RegisterStoreActivity)
    fun inject(activity: CheckOutActivity)
    fun inject(addService: AddServiceActivity)
    fun inject(editPost: EditPostActivity)
    fun inject(activity: OrderDetailActivity)
    fun inject(activity: OrderDetailStoreActivity)
    fun inject(activity: ListServiceByNameActivity)

    fun inject(activity: MyShopActivity)

    fun inject(activity: FavoriteStoreActivity)

    fun inject(activity: SplashsActivity)
    fun inject(activity: MyProfileActivity)
    fun inject(activity: AddressActivity)
    fun inject(activity: AddAddressActivity)
    fun inject(activity: UpdateAddressActivity)
    fun inject(activity: SearchServiceActivity)
    fun inject(activity: HistoryOrderActivity)
    fun inject(activity: NotificationActivity)


    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }
}