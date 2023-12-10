package datn.fpoly.myapplication.ui.service

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.Toast
import com.airbnb.mvrx.*
import com.bumptech.glide.Glide
import com.orhanobut.hawk.Hawk
import datn.fpoly.myapplication.AppApplication
import datn.fpoly.myapplication.MainActivity
import datn.fpoly.myapplication.R
import datn.fpoly.myapplication.core.BaseActivity
import datn.fpoly.myapplication.data.model.AttributeModel
import datn.fpoly.myapplication.data.model.ItemServiceBase
import datn.fpoly.myapplication.data.model.OrderBase
import datn.fpoly.myapplication.data.model.ServiceExtend
import datn.fpoly.myapplication.data.repository.AuthRepo
import datn.fpoly.myapplication.databinding.ActivityDetailServiceBinding
import datn.fpoly.myapplication.ui.adapter.AdapterAttribute
import datn.fpoly.myapplication.ui.adapter.AdapterService
import datn.fpoly.myapplication.ui.address.check_out.CheckOutActivity
import datn.fpoly.myapplication.ui.detailstore.DetailStoreActivity
import datn.fpoly.myapplication.ui.home.HomeActivity
import datn.fpoly.myapplication.utils.Common
import datn.fpoly.myapplication.utils.Common.formatCurrency
import datn.fpoly.myapplication.utils.Dialog_Loading
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import java.text.DecimalFormat
import javax.inject.Inject

class DetailServiceActivity : BaseActivity<ActivityDetailServiceBinding>(),
    DetailServiceViewModel.Factory {
    @Inject
    lateinit var detailServiceFactory: DetailServiceViewModel.Factory

    @Inject
    lateinit var authRepo: AuthRepo
    private val viewModel: DetailServiceViewModel by viewModel()
    private var serviceExtend: ServiceExtend? = null
    private lateinit var adapterService: AdapterService
    private lateinit var adapterAttribute: AdapterAttribute
    private lateinit var cart: OrderBase
    private var quality = 1.0
    private var total = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as AppApplication).appComponent.inject(this);
        super.onCreate(savedInstanceState)
        viewModel.subscribe(this) {
            getListService(it)
            getService(it)
        }
        views.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        views.tvNameServiceTitle.text = "Chi Tiết"
        views.apply {
            if (intent.getBooleanExtra("isStore", false)) {
                btnOrder.visibility = View.GONE
                btnAddCart.visibility = View.GONE
                btnShowShop.visibility = View.GONE
                imgBuy.visibility = View.GONE
                btnSubtraction.visibility = View.GONE
                tvQuantity.visibility = View.GONE
                btnAddition.visibility = View.GONE
                textView7.visibility = View.GONE
            } else {
                btnOrder.visibility = View.VISIBLE
                btnAddCart.visibility = View.VISIBLE
            }
            btnShowShop.setOnClickListener {
                val intent = Intent(this@DetailServiceActivity, DetailStoreActivity::class.java)
                intent.putExtra(Common.KEY_ID_STORE, serviceExtend?.idStore?.id)
                startActivity(intent)
            }
            imgBuy.setOnClickListener {
                startActivity(Intent(this@DetailServiceActivity, HomeActivity::class.java))
            }
        }
        adapterService = AdapterService(false, false)
        views.rcvService.adapter = adapterService
        viewModel.getCart().observe(this) {
            if (it != null) {
                cart = it
            }
        }
        adapterService.setListenner(object : AdapterService.ServiceListenner {
            override fun ServiceOnClick(item: ServiceExtend, position: Int) {
                val intent = Intent(this@DetailServiceActivity, DetailServiceActivity::class.java)
                intent.putExtra(Common.KEY_ID_SERVICE, item.id)
                startActivity(intent)
                finish()
            }

            override fun EditService(serviceExtend: ServiceExtend) {}
        })
        views.tvNameService.text = serviceExtend?.name
        Log.d("DetailServiceActivity", "onCreate: $serviceExtend")
        adapterAttribute = AdapterAttribute()
        views.rcvItemAttribute.adapter = adapterAttribute

        adapterAttribute.setListenner(object : AdapterAttribute.AttributeListenner {
            override fun onClickItem(attributeModel: AttributeModel) {

            }
        })
        views.btnAddCart.setOnClickListener {
            for (index in 0 until adapterAttribute.listAttributeSelect.size) {
                Timber.tag("AAAAAAAAAAA")
                    .e("onCreate: name: " + adapterAttribute.listAttributeSelect[index].name)
            }
            if (serviceExtend != null) {
                Timber.tag("USER").d(authRepo.getUser().toString())
                if (cart.idStore != null && cart.idStore?.equals(serviceExtend!!.idStore?.id) == false) {
                    Toast.makeText(
                        this,
                        "Bạn có chắc chắn muốn đặt lại không? Nếu bạn tiếp tục, giỏ hàng của bạn sẽ bị xóa.",
                        Toast.LENGTH_SHORT
                    ).show()

                    cart.idStore = serviceExtend?.idStore?.id

                    cart.listItem.clear()
                    cart.listItem.add(
                        ItemServiceBase(
                            service = serviceExtend,
                            idService = serviceExtend?.id,
                            number = quality,
                            total = getTotalItem(),
                            attributeListExtend = adapterAttribute.listAttributeSelect,
                            attributeList = adapterAttribute.listAttributeSelect.map { attr -> attr.id }
                                .toMutableList()
                        )
                    )
                    viewModel.updateCart(cart)

                } else {
                    cart.idStore = serviceExtend!!.idStore?.id
                    cart.listItem.add(
                        ItemServiceBase(
                            service = serviceExtend,
                            idService = serviceExtend?.id,
                            number = quality,
                            total = getTotalItem(),
                            attributeListExtend = adapterAttribute.listAttributeSelect,
                            attributeList = adapterAttribute.listAttributeSelect.map { attr -> attr.id }
                                .toMutableList()
                        )
                    )
                    viewModel.updateCart(cart)
                    Toast.makeText(this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show()
                }
            }
        }
        views.btnAddition.setOnClickListener {
            quality += 1
            views.tvQuantity.text = quality.toInt().toString()
        }
        views.btnSubtraction.setOnClickListener {
            if (quality != 0.0) {
                quality -= 1
                views.tvQuantity.text = quality.toInt().toString()
            }
        }
        views.btnOrder.setOnClickListener {
            val list = mutableListOf<ItemServiceBase>()
            list.add((ItemServiceBase(
                views.tvQuantity.text.toString().toDouble(),
                getTotalItem(),
                null,
                serviceExtend?.id,
                serviceExtend,
                adapterAttribute.listAttributeSelect,
                adapterAttribute.listAttributeSelect.map { attr -> attr.id }
                    .toMutableList()

            )))
            val orderBase = OrderBase(
                authRepo.getUser()?.id,
                serviceExtend?.idStore?.id,
                getTotalItem(),
                "",
                "",
                "",
                0.0,
                0,
                "",
                false,
                list
            )
            startActivity(
                Intent(
                    this@DetailServiceActivity,
                    CheckOutActivity::class.java
                ).putExtra(Common.KEY_CART, orderBase)
            )
        }

    }

    private fun getTotalItem(): Double {
        if(serviceExtend?.idSale!=null){
            if(serviceExtend?.idSale?.unit.equals("%")){
                var priceAttr = 0.0
                adapterAttribute.listAttributeSelect.forEach { attr -> priceAttr += attr.price }
                total = ((serviceExtend!!.price!!-(serviceExtend?.price?.times(serviceExtend?.idSale?.value!!/100)!!)) + priceAttr) * quality
                return total
            }else{
                var priceAttr = 0.0
                adapterAttribute.listAttributeSelect.forEach { attr -> priceAttr += attr.price }
                total = ((serviceExtend!!.price!!- serviceExtend?.idSale?.value!!) + priceAttr) * quality
                return total
            }

        }else{
            var priceAttr = 0.0
            adapterAttribute.listAttributeSelect.forEach { attr -> priceAttr += attr.price }
            total = (serviceExtend!!.price!! + priceAttr) * quality
            return total
        }

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
        intent.getStringExtra(Common.KEY_ID_SERVICE)
            ?.let { DetailServiceViewAction.GetServiceById(it) }?.let { viewModel.handle(it) }
    }

    fun getListService(state: DetailServiceViewState) {
        when (state.stateService) {
            is Success -> {
                runBlocking {
                    launch {
                        state.stateService.invoke()?.let {
                            adapterService.setData(it)
                            Timber.tag("AAAAAAAAA")
                                .e("getListService: list service size: " + it.size)
                        }
                    }
                }
            }

            is Loading -> {
                Timber.tag("AAAAAAAAA").e("getListService: Loading")
            }

            is Fail -> {

                Timber.tag("AAAAAAAAA").e("getListService: Call Fail")
            }

            else -> {

            }
        }
    }

    private fun getService(state: DetailServiceViewState) {
        val decemDecimalFormatFormat = DecimalFormat("#")
        when (state.stateServiceByid) {
            is Loading -> {
//                Dialog_Loading.getInstance().show(this, "Loading")
                Timber.tag("AAAAAAAAAAAAA").e("getService: Loading")
            }

            is Success -> {
                runBlocking {
                    launch {
                        state.stateServiceByid.invoke()?.let {
                            serviceExtend = it
                            views.tvNameService.text = serviceExtend?.name
                            if (serviceExtend?.idSale == null) {
                                views.tvPrice.text =
                                    serviceExtend?.price?.formatCurrency(unit = serviceExtend?.unit)
                                        ?: ""
                            } else {
                                views.tvPrice.setText(
                                    Html.fromHtml(
                                        "<span style=\"text-decoration: line-through;\">${decemDecimalFormatFormat.format(serviceExtend!!.price)} VNĐ/${serviceExtend?.unit}</span> <span style=\"color: #FA0F0F;\">${
                                            if (serviceExtend!!.idSale?.unit.equals("%")) {
                                               decemDecimalFormatFormat.format( serviceExtend!!.price?.minus((serviceExtend!!.price!! * serviceExtend!!.idSale?.value!!/100)))
                                            } else {
                                               decemDecimalFormatFormat.format( (serviceExtend?.price?.minus(serviceExtend!!.idSale?.value!!)))
                                            }
                                        } VNĐ/${serviceExtend?.unit}</span>"
                                    )
                                )
                            }
                            views.tvQuantity.text = quality.toInt().toString()
                            Glide.with(this@DetailServiceActivity)
                                .load(Common.baseUrl + serviceExtend?.image)
                                .error(R.drawable.image_service).into(views.imgService)

                            var nameAttribute = ""
                            for (index in 0 until (serviceExtend?.attributeList?.size ?: 0)) {
                                if (serviceExtend?.attributeList?.size != 0) {
                                    if (index == (serviceExtend?.attributeList?.size?.minus(1))) {
                                        nameAttribute += (serviceExtend?.attributeList?.get(index)?.name
                                            ?: "")
                                    } else {
                                        nameAttribute += (serviceExtend?.attributeList?.get(index)?.name
                                            ?: "") + ">"
                                    }

                                }
                            }
                            views.tvAttribute.text = nameAttribute
                            serviceExtend?.attributeList?.let {
                                adapterAttribute.setData(it)
                            }
                        }
//                        Dialog_Loading.getInstance().dismiss()
                    }
//                    state.stateServiceByid=Uninitialized
                }
                Timber.tag("AAAAAAAAAAAAA").e("getService: Success")

            }

            is Fail -> {

                Timber.tag("AAAAAAAAAAAAA").e("getService: Fail")
            }

            else -> {}
        }
    }

    override fun getBinding(): ActivityDetailServiceBinding {
        return ActivityDetailServiceBinding.inflate(layoutInflater)
    }

    override fun create(initialState: DetailServiceViewState): DetailServiceViewModel {
        return detailServiceFactory.create(initialState)
    }
}