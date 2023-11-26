package datn.fpoly.myapplication.ui.detailstore

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import datn.fpoly.myapplication.AppApplication
import datn.fpoly.myapplication.core.BaseActivity
import datn.fpoly.myapplication.databinding.ActivityDetailStoreBinding
import com.airbnb.mvrx.viewModel
import com.orhanobut.hawk.Hawk
import datn.fpoly.myapplication.data.model.ServiceExtend
import datn.fpoly.myapplication.data.model.StoreModel
import datn.fpoly.myapplication.data.model.account.AccountModel
import datn.fpoly.myapplication.ui.adapter.AdapterRate
import datn.fpoly.myapplication.ui.adapter.AdapterService
import datn.fpoly.myapplication.ui.service.DetailServiceActivity
import datn.fpoly.myapplication.utils.Common
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

class DetailStoreActivity :BaseActivity<ActivityDetailStoreBinding>(), DetailStoreViewModel.Factory {
    @Inject
    lateinit var detailStoreFactory: DetailStoreViewModel.Factory
    private val viewModel: DetailStoreViewModel by viewModel()
    private lateinit var itemStoreDetail: StoreModel
    private lateinit var adapterService: AdapterService
    private lateinit var adapterRate: AdapterRate
    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as AppApplication).appComponent.inject(this);
        super.onCreate(savedInstanceState)
        adapterService = AdapterService(false)
        val account = Hawk.get<AccountModel>("Account",null)
        var store = intent.getStringExtra(Common.KEY_ID_STORE)
        var idUser = account.id.toString()
        val accountModel = AccountModel()
        intent.getStringExtra(Common.KEY_ID_STORE)?.let { viewModel.handle(DetailStoreViewAction.GetListServiceByStore(it)) }
        intent.getStringExtra(Common.KEY_ID_STORE)?.let { viewModel.handle(DetailStoreViewAction.GetStoreById(it)) }
        intent.getStringExtra(Common.KEY_ID_STORE)?.let { viewModel.handle(DetailStoreViewAction.GetListRateByStore(it)) }
        views.imgBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        viewModel.subscribe(this){
            getListService(it)
            getStore(it)
            getListRate(it)
        }

        views.rcvDetailStore.adapter =adapterService
        adapterService.setListenner(object : AdapterService.ServiceListenner{
            override fun ServiceOnClick(item: ServiceExtend, position: Int) {
//                Hawk.put(Common.KEY_SERVICE_DETAIL, item)
                val intent = Intent(this@DetailStoreActivity, DetailServiceActivity::class.java)
                intent.putExtra(Common.KEY_ID_SERVICE, item.id)
                startActivity(intent)
            }

            override fun EditService(serviceExtend: ServiceExtend) {

            }
        })

        accountModel.favoriteStores = ArrayList()
        (accountModel.favoriteStores as ArrayList<String>)?.add(store.toString())

        views.imgAddFavoriteStore.setOnClickListener(View.OnClickListener {
            viewModel.addFavoriteStore(idUser,accountModel)
            Toast.makeText(this, "Đã thêm cửa hàng vào danh sách yêu thích", Toast.LENGTH_SHORT).show()
        })

        adapterRate = AdapterRate()
        views.rcvRates.adapter= adapterRate



    }
    fun getListService( state: DetailStoreViewState){
        when(state.stateService){
            is Success->{
                runBlocking {
                    launch {
                        state.stateService.invoke()?.let{
                            adapterService.setData(it)
                            Timber.tag("AAAAAAAAA").e("getListService: list service size: "+it.size )
                        }
                    }
                }
            }
            is Loading->{
                Timber.tag("AAAAAAAAA").e("getListService: Loading")
            }
            is Fail-> {
                Timber.tag("AAAAAAAAA").e("getListService: Call Fail")
            }
            else->{

            }
        }
    }
    fun getStore( state: DetailStoreViewState){
        when(state.stateStore){
            is Success->{
                runBlocking {
                    launch {
                        state.stateStore.invoke()?.let{
                            itemStoreDetail = it
                            views.tvNameStore.text = itemStoreDetail.name
                            views.tvAddress.text = itemStoreDetail.idAddress?.address
                            views.tvRate.text= itemStoreDetail.rate.toString()
                            views.tvPhone.text= itemStoreDetail.iduser?.phone
                        }
                    }
                }
            }
            is Loading->{
                Timber.tag("AAAAAAAAA").e("getListService: Loading")
            }
            is Fail-> {
                Timber.tag("AAAAAAAAA").e("getListService: Call Fail")
            }
            else->{

            }
        }
    }
    fun getListRate(state: DetailStoreViewState){
        when(state.stateListRateStore){
            is Loading-> {
                Timber.tag("AAAAAAAAAAAA").e("getListRate: loading ")
            }
            is Success->{
                Timber.tag("AAAAAAAAAAAA").e("getListRate: Success ")
                state.stateListRateStore.invoke()?.let{
                    adapterRate.initData(it)
                }
            }
            is Fail->{
                Timber.tag("AAAAAAAAAAAA").e("getListRate: Fail ")
            }
            else->{}
        }
    }

    override fun getBinding(): ActivityDetailStoreBinding = ActivityDetailStoreBinding.inflate(layoutInflater)

    override fun create(initialState: DetailStoreViewState): DetailStoreViewModel {
       return detailStoreFactory.create(initialState)
    }

}