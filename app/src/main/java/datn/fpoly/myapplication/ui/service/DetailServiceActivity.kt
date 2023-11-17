package datn.fpoly.myapplication.ui.service

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.viewModel
import com.orhanobut.hawk.Hawk
import datn.fpoly.myapplication.AppApplication
import datn.fpoly.myapplication.core.BaseActivity
import datn.fpoly.myapplication.data.model.ItemServiceBase
import datn.fpoly.myapplication.data.model.Order
import datn.fpoly.myapplication.data.model.ServiceExtend
import datn.fpoly.myapplication.data.repository.AuthRepo
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
    @Inject
    lateinit var authRepo: AuthRepo
    private val viewModel: DetailServiceViewModel by viewModel()
    private var serviceExtend : ServiceExtend? = null
    private lateinit var adapterService: AdapterService
    private lateinit var adapterAttribute : AdapterAttribute
    private lateinit var cart: Order
    private var quality = 1.0
    private var total = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as AppApplication).appComponent.inject(this);
        super.onCreate(savedInstanceState)
        serviceExtend = Hawk.get(Common.KEY_SERVICE_DETAIL)
        views.tvNameService.text = serviceExtend?.name
        views.tvPrice.text = serviceExtend?.price?.formatCurrency(unit = serviceExtend?.unit) ?: ""
        views.tvQuantity.text = quality.toInt().toString()

        var nameAttribute=""
        for (index in 0 until (serviceExtend?.attributeList?.size ?: 0)){
            if(serviceExtend?.attributeList?.size!=0){
                if(index== (serviceExtend?.attributeList?.size?.minus(1))){
                    nameAttribute+= (serviceExtend?.attributeList?.get(index)?.name ?: "" )
                }else{
                    nameAttribute+= (serviceExtend?.attributeList?.get(index)?.name ?: "" ) +">"
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
        adapterService = AdapterService(false)
        views.rcvService.adapter = adapterService
        viewModel.getCart().observe(this) {
            if (it != null) {
                cart = it
            }
        }
        adapterService.setListenner(object : AdapterService.ServiceListenner{
            override fun ServiceOnClick(item: ServiceExtend, position: Int) {
                Hawk.put(Common.KEY_SERVICE_DETAIL, item)
                val intent = Intent(this@DetailServiceActivity, DetailServiceActivity::class.java)
                startActivity(intent)
                finish()
            }

            override fun EditService(serviceExtend: ServiceExtend) {}
        })
        views.tvNameService.text = serviceExtend?.name
        adapterAttribute = AdapterAttribute()
        views.rcvItemAttribute.adapter = adapterAttribute
        serviceExtend?.attributeList?.let {
            adapterAttribute.setData(it)
        }
        views.btnAddCart.setOnClickListener {
            for (index in 0 until adapterAttribute.listAttributeSelect.size){
                Log.e("AAAAAAAAAAA", "onCreate: name: "+adapterAttribute.listAttributeSelect[index].name )
            }
            Toast.makeText(this, "list attribute select: size: "+adapterAttribute.listAttributeSelect.size, Toast.LENGTH_SHORT).show()
            if(serviceExtend != null){
                Log.d("USER", authRepo.getUser().toString())
                if (cart.idStore != null && cart.idStore?.equals(serviceExtend!!.idStore?.id) == false){
                    Toast.makeText(this, "Bạn có chắc chắn muốn đặt lại không? Nếu bạn tiếp tục, giỏ hàng của bạn sẽ bị xóa.", Toast.LENGTH_SHORT).show()

                    cart.idStore = serviceExtend?.idStore?.id

                    cart.listItem.clear()
                    cart.listItem.add(ItemServiceBase(service = serviceExtend, idService = serviceExtend?.id, number = quality, total = getTotalItem(), attributeListExtend = adapterAttribute.listAttributeSelect, attributeList = adapterAttribute.listAttributeSelect.map { attr ->  attr.id}.toMutableList()))
                    viewModel.updateCart(cart)

                }else{
                    cart.idStore = serviceExtend!!.idStore?.id
                    cart.listItem.add(ItemServiceBase(service = serviceExtend, idService = serviceExtend?.id, number = quality, total = getTotalItem(), attributeListExtend = adapterAttribute.listAttributeSelect, attributeList = adapterAttribute.listAttributeSelect.map { attr ->  attr.id}.toMutableList()))
                    viewModel.updateCart(cart)
                }
            }
        }
        views.btnAddition.setOnClickListener {
            quality += 1
            views.tvQuantity.text = quality.toInt().toString()
        }
        views.btnSubtraction.setOnClickListener {
            if(quality != 0.0){
                quality -= 1
                views.tvQuantity.text = quality.toInt().toString()
            }
        }

    }

    private fun getTotalItem() : Double {
        var priceAttr = 0.0
        adapterAttribute.listAttributeSelect.forEach{ attr -> priceAttr += attr.price }
        total = (serviceExtend!!.price!! + priceAttr) * quality
        return total
    }

    override fun onResume() {
        super.onResume()
        serviceExtend?.idStore?.id?.let {
            serviceExtend?.id?.let { it1 ->
                DetailServiceViewAction.GetListServiceByStore(
                    it, it1
                )
            }
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