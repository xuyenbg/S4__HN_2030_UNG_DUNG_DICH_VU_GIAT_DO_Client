package datn.fpoly.myapplication.ui.listServiceByName

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.viewModel
import datn.fpoly.myapplication.AppApplication
import datn.fpoly.myapplication.R
import datn.fpoly.myapplication.core.BaseActivity
import datn.fpoly.myapplication.data.model.ServiceExtend
import datn.fpoly.myapplication.databinding.ActivityListServiceByNameBinding
import datn.fpoly.myapplication.ui.adapter.AdapterService
import datn.fpoly.myapplication.ui.listService.ListServiceViewModel
import datn.fpoly.myapplication.ui.service.DetailServiceActivity
import datn.fpoly.myapplication.utils.Common
import datn.fpoly.myapplication.utils.Dialog_Loading
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class ListServiceByNameActivity : BaseActivity<ActivityListServiceByNameBinding>(), ListServiceByNameViewModel.Factory {
    @Inject
    lateinit var listServiceFactory: ListServiceByNameViewModel.Factory
    private val viewModel: ListServiceByNameViewModel by viewModel()
    private lateinit var adapterService: AdapterService
    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as AppApplication).appComponent.inject(this);
        super.onCreate(savedInstanceState)
        intent.getStringExtra(Common.KEY_NAME_SERVICE)
            ?.let { ListServiceByNameViewAction.GetListServiceByName(it) }
            ?.let { viewModel.handle(it) }
        viewModel.subscribe(this){
            getListServiceByName(it)
        }
        views.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        views.tvTitle.text = intent.getStringExtra(Common.KEY_NAME_SERVICE)

        adapterService = AdapterService(false)
        views.rcvListService.adapter = adapterService
        adapterService.setListenner(object : AdapterService.ServiceListenner{
            override fun ServiceOnClick(item: ServiceExtend, position: Int) {
                val intent = Intent(this@ListServiceByNameActivity, DetailServiceActivity::class.java)
                intent.putExtra(Common.KEY_ID_SERVICE, item.id)
                startActivity(intent)
            }

            override fun EditService(serviceExtend: ServiceExtend) {

            }
        })
    }

    override fun onRestart() {
        super.onRestart()
        intent.getStringExtra(Common.KEY_NAME_SERVICE)
            ?.let { ListServiceByNameViewAction.GetListServiceByName(it) }
            ?.let { viewModel.handle(it) }
    }
    fun getListServiceByName(state: ListServiceByNameViewState){
        when(state.stateService){
            is Loading->{
//                Dialog_Loading.getInstance().show(supportFragmentManager, "Loading")
            }
            is Success->{
                runBlocking {
                    launch {
//                        Dialog_Loading.getInstance().dismiss()
                        state.stateService.invoke()?.let {
                            adapterService.setData(it)
                        }
                    }
                }
            }
            is Fail->{
//                Dialog_Loading.getInstance().dismiss()
            }
            else->{}
        }
    }

    override fun getBinding(): ActivityListServiceByNameBinding {
        return ActivityListServiceByNameBinding.inflate(layoutInflater)
    }

    override fun create(initialState: ListServiceByNameViewState): ListServiceByNameViewModel {
        return listServiceFactory.create(initialState)
    }
}