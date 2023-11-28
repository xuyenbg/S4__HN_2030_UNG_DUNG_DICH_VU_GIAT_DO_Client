package datn.fpoly.myapplication.ui.updateAddress

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.viewModel
import datn.fpoly.myapplication.AppApplication
import datn.fpoly.myapplication.core.BaseActivity
import datn.fpoly.myapplication.data.model.AddressModel
import datn.fpoly.myapplication.databinding.ActivityUpdateAddressBinding
import datn.fpoly.myapplication.ui.addAddress.AddAddressViewAction
import datn.fpoly.myapplication.ui.map.PickPossitionInMapActivity
import datn.fpoly.myapplication.utils.Common
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

class UpdateAddressActivity : BaseActivity<ActivityUpdateAddressBinding>(),UpdateAddressViewModel.Factory {
    @Inject
    lateinit var updateAddressFactory: UpdateAddressViewModel.Factory
    private val viewModel: UpdateAddressViewModel by viewModel()
    private var address = "";
    private var latitude = 0.0
    private var longitude = 0.0
    private var isDefault = false

    val addressModel = AddressModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as AppApplication).appComponent.inject(this);
        super.onCreate(savedInstanceState)
        setContentView(views.root)
    }
    override fun initUiAndData() {
        super.initUiAndData()
        val idAddress = intent.getStringExtra("idAddress")
        idAddress?.let { UpdateAddressViewAction.GetDetailAddress(it) }
            ?.let { viewModel.handle(it) }
        viewModel.subscribe(this){
            updateAddress(it)
        }
        views.tvAddressMap.setOnClickListener {
            startActivity(Intent(this@UpdateAddressActivity, PickPossitionInMapActivity::class.java))
        }
        views.btnConfirm.setOnClickListener {
            _updateAddress()
        }
        views.imgBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
    override fun onResume() {
        super.onResume()
        address = PickPossitionInMapActivity.dataAdress.pick_address
        latitude = PickPossitionInMapActivity.dataAdress.latitude
        longitude = PickPossitionInMapActivity.dataAdress.longitude
        views.tvAddress.text = address
    }
    private fun _updateAddress(){
        val idAddress = intent.getStringExtra("idAddress")
        addressModel.address = address
        addressModel.latitude = latitude
        addressModel.longitude = longitude
        isDefault = views.swBtnDefault.isChecked
        addressModel.isDefault = isDefault
        idAddress?.let { UpdateAddressViewAction.PutAddress(it,addressModel) }
            ?.let { viewModel.handle(it) }
    }
    private fun updateAddress(state: UpdateAddressViewState){
        when(state.stateDetailAddress){
            is Success->{
                runBlocking {
                    launch {
                        val addressDetail = state.stateDetailAddress.invoke()
                        views.tvAddress.text = addressDetail?.address
                        views.swBtnDefault.isChecked = addressDetail?.isDefault ==true

                        address = addressDetail?.address.toString()
                        latitude = addressDetail?.latitude!!
                        longitude = addressDetail?.longitude!!
                        isDefault = addressDetail?.isDefault == true

                    }
                }
            }
            is Loading ->{
                Timber.tag("AAAAAAAAA").e("detailAddress: Loading")
            }
            is Fail -> {
                Timber.tag("AAAAAAAAA").e("detailAddress: Call Fail")
            }
            else->{

            }
        }
        when(state.stateAddress){
            is Success->{
                runBlocking {
                    launch {
                        state.stateAddress.invoke()?.let {
                            state.stateAddress = Uninitialized
                        }
                    }
                }
                Toast.makeText(this@UpdateAddressActivity, "Thêm thành công", Toast.LENGTH_SHORT).show()
                onBackPressedDispatcher.onBackPressed()
            }
            is Loading ->{
                Timber.tag("AAAAAAAAA").e("updateAddress: Loading")
            }
            is Fail -> {
                Timber.tag("AAAAAAAAA").e("updateAddress: Call Fail")
                Toast.makeText(this@UpdateAddressActivity, "Thêm thành công", Toast.LENGTH_SHORT).show()
                onBackPressedDispatcher.onBackPressed()
            }
            else->{

            }
        }

    }
    override fun getBinding(): ActivityUpdateAddressBinding {
        return ActivityUpdateAddressBinding.inflate(layoutInflater)
    }

    override fun create(initialState: UpdateAddressViewState): UpdateAddressViewModel {
        return updateAddressFactory.create(initialState)
    }
}