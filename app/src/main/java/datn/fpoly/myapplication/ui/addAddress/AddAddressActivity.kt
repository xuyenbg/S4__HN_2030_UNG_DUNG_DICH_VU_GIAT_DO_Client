package datn.fpoly.myapplication.ui.addAddress

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.viewModel
import com.orhanobut.hawk.Hawk
import datn.fpoly.myapplication.AppApplication
import datn.fpoly.myapplication.R
import datn.fpoly.myapplication.core.BaseActivity
import datn.fpoly.myapplication.data.model.AddressModel
import datn.fpoly.myapplication.data.model.account.AccountModel
import datn.fpoly.myapplication.databinding.ActivityAddNewAddressBinding
import datn.fpoly.myapplication.ui.map.PickPossitionInMapActivity
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

class AddAddressActivity : BaseActivity<ActivityAddNewAddressBinding>(),AddAddressViewModel.Factory {
    @Inject
    lateinit var addAddressFactory: AddAddressViewModel.Factory
    private val viewModel: AddAddressViewModel by viewModel()
    private val account = Hawk.get<AccountModel>("Account",null)
    private var idUser = account.id.toString()
    private var address = "";
    private var latitude = 0.0
    private var longitude = 0.0
    private var isDefault = false
    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as AppApplication).appComponent.inject(this);
        super.onCreate(savedInstanceState)
        setContentView(views.root)
    }
    override fun initUiAndData() {
        super.initUiAndData()
        viewModel.subscribe(this){
            addAddress(it)
        }
        views.tvAddressMap.setOnClickListener {
            startActivity(Intent(this@AddAddressActivity,PickPossitionInMapActivity::class.java))
        }
        views.btnConfirm.setOnClickListener {
            addNewAddress()
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
    private fun addNewAddress(){
        val addressModel = AddressModel()
        addressModel.address = address
        addressModel.idUser = idUser
        addressModel.latitude = latitude
        addressModel.longitude = longitude
        isDefault = views.swBtnDefault.isChecked
        addressModel.isDefault = isDefault
        viewModel.handle(AddAddressViewAction.AddAddress(addressModel))
    }
    private fun addAddress(state: AddAddressViewState){
        when(state.stateAddAddress){
            is Success->{
                runBlocking {
                    launch {
                        state.stateAddAddress.invoke()?.let {
                            state.stateAddAddress = Uninitialized

                        }
                    }
                }
                Toast.makeText(this@AddAddressActivity, "Thêm thành công", Toast.LENGTH_SHORT).show()
                onBackPressedDispatcher.onBackPressed()
            }
            is Loading ->{
                Timber.tag("AAAAAAAAA").e("addAddress: Loading")
            }
            is Fail -> {
                Timber.tag("AAAAAAAAA").e("addAddress: Call Fail")
                Toast.makeText(this@AddAddressActivity, "Thêm thành công", Toast.LENGTH_SHORT).show()
                onBackPressedDispatcher.onBackPressed()
            }
            else->{

            }
        }
    }

    override fun getBinding(): ActivityAddNewAddressBinding {
        return ActivityAddNewAddressBinding.inflate(layoutInflater)
    }

    override fun create(initialState: AddAddressViewState): AddAddressViewModel {
        return addAddressFactory.create(initialState)
    }
}