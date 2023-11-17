package datn.fpoly.myapplication.ui.listService

import android.content.Intent
import android.os.Bundle
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.viewModel
import com.orhanobut.hawk.Hawk
import datn.fpoly.myapplication.AppApplication
import datn.fpoly.myapplication.core.BaseActivity
import datn.fpoly.myapplication.data.model.ServiceExtend
import datn.fpoly.myapplication.databinding.ActivityListServiceBinding
import datn.fpoly.myapplication.ui.adapter.AdapterService
import datn.fpoly.myapplication.ui.service.DetailServiceActivity
import datn.fpoly.myapplication.utils.Common
import datn.fpoly.myapplication.utils.DataRaw
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

class ListServiceActivity : BaseActivity<ActivityListServiceBinding>(), ListServiceViewModel.Factory {
    @Inject
    lateinit var listServiceFactory: ListServiceViewModel.Factory
    private val viewModel: ListServiceViewModel by viewModel()
    private lateinit var adapter: AdapterService
    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as AppApplication).appComponent.inject(this);
        super.onCreate(savedInstanceState)
        viewModel.handle(ListServiceViewAction.GetListServiceByCategory(DataRaw.getDataIdCategory()))
        adapter = AdapterService(false)
        views.rcvList.adapter= adapter
        adapter.setListenner(object :AdapterService.ServiceListenner{
            override fun ServiceOnClick(item: ServiceExtend, position: Int) {
                Hawk.put(Common.KEY_SERVICE_DETAIL, item)
                val intent = Intent(this@ListServiceActivity, DetailServiceActivity::class.java)
                startActivity(intent)
                finish()
            }

            override fun EditService(serviceExtend: ServiceExtend) {

            }
        })
        viewModel.subscribe(this){
            getListService(it)
        }
        views.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

    }
    fun getListService(state: ListServiceViewState){
        when(state.stateService){
            is Success ->{
                runBlocking {
                    launch {
                        state.stateService.invoke()?.let{
                            adapter.setData(it)
                            Timber.tag("AAAAAAAAA").e("getListService: list service size: "+it.size )
                        }
                    }
                }
            }
            is Loading ->{
                Timber.tag("AAAAAAAAA").e("getListService: Loading")
            }
            is Fail -> {
                Timber.tag("AAAAAAAAA").e("getListService: Call Fail")
            }
            else->{

            }
        }
    }


    override fun getBinding(): ActivityListServiceBinding {
        return ActivityListServiceBinding.inflate(layoutInflater)
    }

    override fun create(initialState: ListServiceViewState): ListServiceViewModel {
        return listServiceFactory.create(initialState)
    }
}