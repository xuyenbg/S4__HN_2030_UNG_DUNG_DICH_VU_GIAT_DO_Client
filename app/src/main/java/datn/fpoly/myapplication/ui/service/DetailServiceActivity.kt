package datn.fpoly.myapplication.ui.service

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.viewModel
import com.orhanobut.hawk.Hawk
import datn.fpoly.myapplication.AppApplication
import datn.fpoly.myapplication.core.BaseActivity
import datn.fpoly.myapplication.data.model.ItemService
import datn.fpoly.myapplication.data.model.Order
import datn.fpoly.myapplication.data.model.ServiceModel
import datn.fpoly.myapplication.databinding.ActivityDetailServiceBinding
import datn.fpoly.myapplication.ui.adapter.AdapterAttribute
import datn.fpoly.myapplication.ui.adapter.AdapterService
import datn.fpoly.myapplication.utils.Common
import datn.fpoly.myapplication.utils.Common.formatCurrency
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

class DetailServiceActivity : BaseActivity<ActivityDetailServiceBinding>(), DetailServiceViewModel.Factory {
    @Inject
    lateinit var detailServiceFactory: DetailServiceViewModel.Factory
    private val viewModel: DetailServiceViewModel by viewModel()
    private var serviceModel : ServiceModel? = null
    private lateinit var adapterService: AdapterService
    private lateinit var adapterAttribute : AdapterAttribute
    private lateinit var cart: Order
    private var quality = 0.0
    private var total = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as AppApplication).appComponent.inject(this);
        super.onCreate(savedInstanceState)
        serviceModel = Hawk.get(Common.KEY_SERVICE_DETAIL)
        views.tvNameService.text = serviceModel?.name
        views.tvPrice.text = serviceModel?.price?.formatCurrency(unit = serviceModel?.unit) ?: ""
        views.tvQuantity.text = quality.toString()

        var nameAttribute=""
        for (index in 0 until (serviceModel?.attributeList?.size ?: 0)){
            if(serviceModel?.attributeList?.size!=0){
                if(index== (serviceModel?.attributeList?.size?.minus(1))){
                    nameAttribute+= (serviceModel?.attributeList?.get(index)?.name ?: "" )
                }else{
                    nameAttribute+= (serviceModel?.attributeList?.get(index)?.name ?: "" ) +">"
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
        viewModel.getCart().observe(this) {
            if (it != null) {
                cart = it
            }
        }
        adapterService.setListenner(object : AdapterService.ServiceListenner{
            override fun ServiceOnClick(item: ServiceModel, position: Int) {
                Hawk.put(Common.KEY_SERVICE_DETAIL, item)
                val intent = Intent(this@DetailServiceActivity, DetailServiceActivity::class.java)
                startActivity(intent)
                finish()
            }
        })
        views.tvNameService.text = serviceModel?.name
        adapterAttribute = AdapterAttribute()
        views.rcvItemAttribute.adapter = adapterAttribute
        serviceModel?.attributeList?.let {
           adapterAttribute.setData(it)
        }
        views.btnAddCart.setOnClickListener {
            for (index in 0 until adapterAttribute.listAttributeSelect.size){
                Log.e("AAAAAAAAAAA", "onCreate: name: "+adapterAttribute.listAttributeSelect[index].name )
            }
            Toast.makeText(this, "list attribute select: size: "+adapterAttribute.listAttributeSelect.size, Toast.LENGTH_SHORT).show()
            if(serviceModel != null){
                if (!cart.idStore.equals(serviceModel!!.idStore)){
                    Toast.makeText(this, "Bạn có chắc chắn muốn đặt lại không? Nếu bạn tiếp tục, giỏ hàng của bạn sẽ bị xóa.", Toast.LENGTH_SHORT).show()
                    cart.idStore = serviceModel!!.idStore
                    cart.listItem.clear()
                    cart.listItem.add(ItemService(service = serviceModel, number = quality, total = getTotalItem(), attributeList = adapterAttribute.listAttributeSelect))
                    viewModel.updateCart(cart)
                }else{
                    cart.listItem.add(ItemService(service = serviceModel, number = quality, total = getTotalItem(), attributeList = adapterAttribute.listAttributeSelect))
                    viewModel.updateCart(cart)
                }
            }
        }
        views.btnAddition.setOnClickListener {
            quality+=1
            views.tvQuantity.text = quality.toString()
        }
        views.btnSubtraction.setOnClickListener {
            if(quality != 0.0){
               quality-=1
                views.tvQuantity.text = quality.toString()
            }
        }

    }

    private fun getTotalItem() : Double {
        var priceAttr = 0.0
        adapterAttribute.listAttributeSelect.forEach{ attr -> priceAttr += attr.price }
        total = (serviceModel!!.price!! + priceAttr) * quality
        return total
    }

    override fun onResume() {
        super.onResume()
        serviceModel?.idStore?.let {
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