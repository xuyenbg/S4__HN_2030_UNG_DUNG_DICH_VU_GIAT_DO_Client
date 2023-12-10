package datn.fpoly.myapplication.ui.listServiceByName

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.airbnb.mvrx.*
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
        views.tvNoti.visibility=View.GONE
        views.imgNoti.visibility=View.GONE
        views.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        views.tvTitle.text = intent.getStringExtra(Common.KEY_NAME_SERVICE)

        adapterService = AdapterService(false, false)
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
        views.swipeToRefresh.setOnRefreshListener {
            intent.getStringExtra(Common.KEY_NAME_SERVICE)
                ?.let { ListServiceByNameViewAction.GetListServiceByName(it) }
                ?.let { viewModel.handle(it) }
        }
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
                views.shimmer.visibility= View.VISIBLE
                views.rcvListService.visibility= View.GONE
                views.shimmer.startShimmer()
//                Dialog_Loading.getInstance().show(supportFragmentManager, "Loading")
            }
            is Success->{
                runBlocking {
                    launch {
                        views.swipeToRefresh.isRefreshing=false
                        views.shimmer.visibility= View.GONE
                        views.rcvListService.visibility= View.VISIBLE
                        state.stateService.invoke()?.let {
                            adapterService.setData(it)
                            if(it.size!=0){
                                views.tvNoti.visibility=View.GONE
                                views.imgNoti.visibility=View.GONE
                                views.rcvListService.visibility=View.VISIBLE
                            }else{
                                views.tvNoti.visibility=View.VISIBLE
                                views.imgNoti.visibility=View.VISIBLE
                                views.rcvListService.visibility=View.GONE
                            }
                        }
                    }
                }
                state.stateService=Uninitialized
            }
            is Fail->{
                views.shimmer.visibility= View.GONE
                views.rcvListService.visibility= View.VISIBLE
                state.stateService=Uninitialized
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