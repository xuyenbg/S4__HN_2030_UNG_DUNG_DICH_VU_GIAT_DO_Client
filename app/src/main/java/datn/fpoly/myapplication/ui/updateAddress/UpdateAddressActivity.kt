package datn.fpoly.myapplication.ui.updateAddress

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.viewModel
import datn.fpoly.myapplication.AppApplication
import datn.fpoly.myapplication.R
import datn.fpoly.myapplication.core.BaseActivity
import datn.fpoly.myapplication.data.model.AddressModel
import datn.fpoly.myapplication.databinding.ActivityUpdateAddressBinding
import datn.fpoly.myapplication.databinding.DialogNotificationAllBinding
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
    lateinit var dialog: Dialog
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
        views.tvDeleteAddress.setOnClickListener {
            deleteDialog(idAddress)
        }
        views.imgBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
    private fun deleteDialog(idAddress: String?){
        dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_notification_all)
        val tvOnCancel = dialog.findViewById<TextView>(R.id.tv_onCancel)
        val tvOnConfirm = dialog.findViewById<TextView>(R.id.tv_onConfirm)
        val tvText = dialog.findViewById<TextView>(R.id.tv_text)
        tvText.text = "Bạn có chắc muốn xóa địa chỉ này ?"
        tvOnCancel.setOnClickListener {
            dialog.dismiss()
        }
        tvOnConfirm.setOnClickListener {
            idAddress?.let { it1 -> UpdateAddressViewAction.DeleteAddress(it1) }
                ?.let { it2 -> viewModel.handle(it2) }
            dialog.dismiss()
            onBackPressedDispatcher.onBackPressed()
        }
        dialog.show()
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
                Toast.makeText(this@UpdateAddressActivity, "Cập nhật thành công", Toast.LENGTH_SHORT).show()
                onBackPressedDispatcher.onBackPressed()
            }
            is Loading ->{
                Timber.tag("AAAAAAAAA").e("updateAddress: Loading")
            }
            is Fail -> {
                Timber.tag("AAAAAAAAA").e("updateAddress: Call Fail")
                Toast.makeText(this@UpdateAddressActivity, "Cập nhật thành công", Toast.LENGTH_SHORT).show()
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