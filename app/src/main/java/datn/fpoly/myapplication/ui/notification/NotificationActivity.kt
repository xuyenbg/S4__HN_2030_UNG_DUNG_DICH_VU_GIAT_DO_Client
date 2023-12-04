package datn.fpoly.myapplication.ui.notification

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.viewModel
import datn.fpoly.myapplication.AppApplication
import datn.fpoly.myapplication.R
import datn.fpoly.myapplication.core.BaseActivity
import datn.fpoly.myapplication.databinding.ActivityNotificationBinding
import datn.fpoly.myapplication.ui.adapter.AdapterNotify
import datn.fpoly.myapplication.ui.myshop.MyShopViewModel
import datn.fpoly.myapplication.utils.Common
import datn.fpoly.myapplication.utils.ItemSpacingDecoration
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class NotificationActivity : BaseActivity<ActivityNotificationBinding>(), NotifycationViewModel.Factory {
    @Inject
    lateinit var factory: NotifycationViewModel.Factory
    private val viewModel: NotifycationViewModel by viewModel()
    private lateinit var adapterNoti : AdapterNotify
    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as AppApplication).appComponent.inject(this);
        super.onCreate(savedInstanceState)
        val idUser = intent.getStringExtra(Common.KEY_ID_USER)
        idUser?.let { NotifycationViewAction.GetListNotifyById(it) }?.let { viewModel.handle(it) }
        viewModel.subscribe(this){
            updateState(it)
        }
        adapterNoti= AdapterNotify()
        views.rcvNotification.adapter = adapterNoti
        views.rcvNotification.addItemDecoration(ItemSpacingDecoration(32))
        views.imgBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

    }
    private fun updateState(state: NotifycationViewState){
        when(state.stateNotify){
            is Loading->{
                views.shimmner.visibility= View.VISIBLE
                views.rcvNotification.visibility=View.GONE
                views.shimmner.startShimmer()
            }
            is Success->{
                runBlocking {
                   launch {
                       state.stateNotify.invoke()?.let {
                           views.shimmner.visibility= View.GONE
                           views.rcvNotification.visibility=View.VISIBLE
                           adapterNoti.initData(it)
                       }

                   }
                }
            }
            is Fail->{
                views.shimmner.visibility= View.GONE
                views.rcvNotification.visibility=View.VISIBLE
            }
            else->{}
        }
    }

    override fun getBinding(): ActivityNotificationBinding {
        return  ActivityNotificationBinding.inflate(layoutInflater)
    }

    override fun create(initialState: NotifycationViewState): NotifycationViewModel {
      return  factory.create(initialState)
    }
}