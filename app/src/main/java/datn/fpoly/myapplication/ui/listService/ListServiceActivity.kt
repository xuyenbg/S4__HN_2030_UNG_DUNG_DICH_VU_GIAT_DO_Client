package datn.fpoly.myapplication.ui.listService

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.airbnb.mvrx.*
import com.orhanobut.hawk.Hawk
import datn.fpoly.myapplication.AppApplication
import datn.fpoly.myapplication.core.BaseActivity
import datn.fpoly.myapplication.data.model.ServiceExtend
import datn.fpoly.myapplication.databinding.ActivityListServiceBinding
import datn.fpoly.myapplication.ui.adapter.AdapterService
import datn.fpoly.myapplication.ui.listServiceByName.ListServiceByNameActivity
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
        views.tvNoti.visibility=View.GONE
        views.imgNoti.visibility=View.GONE
        viewModel.handle(ListServiceViewAction.GetListServiceByCategory(DataRaw.getDataIdCategory()))
        adapter = AdapterService(false, true)
        views.rcvList.adapter= adapter
        adapter.setListenner(object :AdapterService.ServiceListenner{
            override fun ServiceOnClick(item: ServiceExtend, position: Int) {
                val intent = Intent(this@ListServiceActivity, ListServiceByNameActivity::class.java)
                intent.putExtra(Common.KEY_NAME_SERVICE, item.name)
                startActivity(intent)
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
        views.swipeToRefresh.setOnRefreshListener {
            viewModel.handle(ListServiceViewAction.GetListServiceByCategory(DataRaw.getDataIdCategory()))

        }

    }
    fun getListService(state: ListServiceViewState){
        when(state.stateService){
            is Success ->{
                runBlocking {
                    launch {
                        state.stateService.invoke()?.let{
                            views.swipeToRefresh.isRefreshing=false
                            views.shimmer.visibility= View.GONE
                            views.rcvList.visibility=View.VISIBLE
                            adapter.setData(it)
                            if(it.size!=0){
                                views.tvNoti.visibility=View.GONE
                                views.imgNoti.visibility=View.GONE
                                views.rcvList.visibility=View.VISIBLE
                            }else{
                                views.tvNoti.visibility=View.VISIBLE
                                views.imgNoti.visibility=View.VISIBLE
                                views.rcvList.visibility=View.GONE
                            }
                            Timber.tag("AAAAAAAAA").e("getListService: list service size: "+it.size )
                        }
                    }
                }
                state.stateService=Uninitialized
            }
            is Loading ->{
                views.shimmer.visibility= View.VISIBLE
                views.rcvList.visibility=View.GONE
                views.shimmer.startShimmer()
                Timber.tag("AAAAAAAAA").e("getListService: Loading")
            }
            is Fail -> {
                views.shimmer.visibility= View.GONE
                views.rcvList.visibility=View.VISIBLE
                state.stateService=Uninitialized
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