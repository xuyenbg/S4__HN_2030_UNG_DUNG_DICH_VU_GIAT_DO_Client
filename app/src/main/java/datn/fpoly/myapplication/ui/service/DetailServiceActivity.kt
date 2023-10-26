package datn.fpoly.myapplication.ui.service

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.viewModel
import com.orhanobut.hawk.Hawk
import datn.fpoly.myapplication.AppApplication
import datn.fpoly.myapplication.R
import datn.fpoly.myapplication.core.BaseActivity
import datn.fpoly.myapplication.data.model.ServiceModel
import datn.fpoly.myapplication.databinding.ActivityDetailServiceBinding
import datn.fpoly.myapplication.ui.adapter.AdapterAttribute
import datn.fpoly.myapplication.ui.adapter.AdapterService
import datn.fpoly.myapplication.ui.detailstore.DetailStoreViewModel
import datn.fpoly.myapplication.ui.detailstore.DetailStoreViewState
import datn.fpoly.myapplication.utils.Common
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

class DetailServiceActivity : BaseActivity<ActivityDetailServiceBinding>(), DetailServiceViewModel.Factory {
    @Inject
    lateinit var detailServiceFactory: DetailServiceViewModel.Factory
    private val viewModel: DetailServiceViewModel by viewModel()
    private var itemService : ServiceModel?=null
    private lateinit var adapterService: AdapterService
    private lateinit var adapterAttribute : AdapterAttribute
    private var quality =0

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as AppApplication).appComponent.inject(this);
        super.onCreate(savedInstanceState)
        itemService = Hawk.get(Common.KEY_SERVICE_DETAIL)
        views.tvNameService.text = itemService?.name
        views.tvPrice.text = ""+itemService?.price
        views.tvUnit.text = "đ/"+itemService?.unit

        views.tvQuantity.text = quality.toString()

        var nameAttribute=""
        for (index in 0 until (itemService?.attributeList?.size ?: 0)){
            if(itemService?.attributeList?.size!=0){
                if(index== (itemService?.attributeList?.size?.minus(1))){
                    nameAttribute+= (itemService?.attributeList?.get(index)?.name ?: "" )
                }else{
                    nameAttribute+= (itemService?.attributeList?.get(index)?.name ?: "" ) +">"
                }

            }
        }
        views.tvAttribute.text = nameAttribute
        viewModel.subscribe(this){
            getListService(it)
        }
        views.imgBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        adapterService = AdapterService()
        views.rcvService.adapter = adapterService

        adapterService.setListenner(object : AdapterService.ServiceListenner{
            override fun ServiceOnClick(item: ServiceModel, position: Int) {
                Hawk.put(Common.KEY_SERVICE_DETAIL, item)
                val intent = Intent(this@DetailServiceActivity, DetailServiceActivity::class.java)
                startActivity(intent)
                finish()
            }
        })
        views.tvNameService.text = itemService?.name
        adapterAttribute = AdapterAttribute()
        views.rcvItemAttribute.adapter = adapterAttribute
        itemService?.attributeList?.let {
           adapterAttribute.setData(it)
        }

        views.btnAddCart.setOnClickListener {
            for (index in 0 until adapterAttribute.listAttributeSelect.size){
                Log.e("AAAAAAAAAAA", "onCreate: name: "+adapterAttribute.listAttributeSelect[index].name )
            }
            Toast.makeText(this, "list attribute select: size: "+adapterAttribute.listAttributeSelect.size, Toast.LENGTH_SHORT).show()
        }
        views.btnAddition.setOnClickListener {
            quality+=1
            views.tvQuantity.text = quality.toString()
        }
        views.btnSubtraction.setOnClickListener {
            if(quality!=0){
               quality-=1
                views.tvQuantity.text = quality.toString()
            }
        }





    }

    override fun onResume() {
        super.onResume()
        itemService?.isStore?.let {
            DetailServiceViewAction.GetListServiceByStore(
                it
            )
        }?.let { viewModel.handle(it) }
    }
    fun getListService( state: DetailServiceViewState){
        when(state.stateService){
            is Success ->{
                runBlocking {
                    launch {
                        state.stateService.invoke()?.let{
                            adapterService.setData(it)
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

    override fun getBinding(): ActivityDetailServiceBinding {
        return ActivityDetailServiceBinding.inflate(layoutInflater)
    }

    override fun create(initialState: DetailServiceViewState): DetailServiceViewModel {
        return detailServiceFactory.create(initialState)
    }
}