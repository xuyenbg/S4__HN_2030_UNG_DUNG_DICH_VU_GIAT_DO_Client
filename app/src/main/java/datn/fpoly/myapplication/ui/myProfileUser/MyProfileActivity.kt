package datn.fpoly.myapplication.ui.myProfileUser

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.viewModel
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.orhanobut.hawk.Hawk
import datn.fpoly.myapplication.AppApplication
import datn.fpoly.myapplication.R
import datn.fpoly.myapplication.core.BaseActivity
import datn.fpoly.myapplication.data.model.account.AccountModel
import datn.fpoly.myapplication.databinding.ActivityMyProfileBinding
import datn.fpoly.myapplication.ui.myshop.MyShopViewModel
import datn.fpoly.myapplication.utils.Common
import datn.fpoly.myapplication.utils.Dialog_Loading
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import timber.log.Timber
import java.io.File
import javax.inject.Inject

class MyProfileActivity : BaseActivity<ActivityMyProfileBinding>() ,MyProfileViewModel.Factory{
    @Inject
    lateinit var myProfileViewModelFactory: MyProfileViewModel.Factory
    private val viewModel: MyProfileViewModel by viewModel()
    private var account: AccountModel?=null
    private var imageUri: Uri?=null
    private var dialog : Dialog_Loading?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as AppApplication).appComponent.inject(this);
        super.onCreate(savedInstanceState)
        dialog = Dialog_Loading.getInstance()
        views.tvName.isEnabled = false
        views.icAvtshop.isEnabled= false
        views.tvErrorContent.visibility=View.INVISIBLE
        views.btnUpdate.visibility=View.GONE
        account = intent.getSerializableExtra("profile") as AccountModel
        views.tvPhone.setText(account?.phone)
        views.tvName.setText(account?.fullname)
        if(account?.avatar?.startsWith("/img") == false || account?.avatar==null){
            Glide.with(views.icAvtshop).load(R.drawable.avatar_profile).into(views.icAvtshop)
        }else{
            Glide.with(views.icAvtshop).load(Common.baseUrl+account?.avatar).error(ContextCompat.getDrawable(this, R.drawable.avatar_profile)).into(views.icAvtshop)
        }
        views.icAvtshop.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start()
        }
        views.imgBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        views.tvUpdate.setOnClickListener {
            if(views.tvUpdate.text.toString()=="Sửa"){
                views.tvName.isEnabled= true
                views.icAvtshop.isEnabled=true
                views.tvUpdate.setText("Hủy")
                views.btnUpdate.visibility= View.VISIBLE
            }else{
                views.tvName.isEnabled= false
                views.icAvtshop.isEnabled=false
                views.tvUpdate.setText("Sửa")
                views.btnUpdate.visibility= View.GONE
            }
        }
        views.btnUpdate.setOnClickListener {
            if(views.tvName.text?.isNotEmpty() == true){
                updateProfile()
                views.tvErrorContent.visibility=View.INVISIBLE
                views.tvErrorContent.text=""
            }else{
                views.tvErrorContent.visibility=View.VISIBLE
                views.tvErrorContent.text="Họ và tên không được để trống"
            }
        }
        viewModel.subscribe(this){
            updateStateProfile(it)
        }


    }
    private fun updateStateProfile(state: MyProfileViewState){
        when(state.stateUpdateProfile){
            is Loading-> {
                Timber.tag("AAAAAAAAAAAA").e("updateStateProfile: Loading")
                dialog?.show(supportFragmentManager,"update user")
            }
            is Success-> {
                Timber.tag("AAAAAAAAAAAA").e("updateStateProfile: Success")
                dialog?.dismiss()
                state.stateUpdateProfile.invoke()?.let {
                    onBackPressedDispatcher.onBackPressed()
                    Hawk.put("Account", it.user)
                    Toast.makeText(this@MyProfileActivity, it.message, Toast.LENGTH_SHORT).show()
                }
            }
            is Fail-> {
                Timber.tag("AAAAAAAAAAAA").e("updateStateProfile: Fail")
                dialog?.dismiss()
            }
            else->{}
        }
    }

    private fun updateProfile(){
        if(imageUri!=null){
            val file = File(imageUri!!.path!!)
            val requestFile = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            val image = MultipartBody.Part.createFormData("avatar", file.name, requestFile)

            val phone = account?.phone?.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val passwd = account?.passwd?.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val fullname = views.tvName.text.toString().trim().toRequestBody("multipart/form-data".toMediaTypeOrNull())
            account?.id?.let { MyProfileViewAction.UpdateProfile(it, phone!!, passwd!!, fullname, null, null, image) }
                ?.let { viewModel.handle(it) }
        }else {
            val phone = account?.phone?.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val passwd = account?.passwd?.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val fullname = views.tvName.text.toString().trim().toRequestBody("multipart/form-data".toMediaTypeOrNull())
            account?.id?.let { MyProfileViewAction.UpdateProfile(it, phone!!, passwd!!, fullname, null, null, null) }
                ?.let { viewModel.handle(it) }
        }
    }

    override fun getBinding(): ActivityMyProfileBinding {
        return  ActivityMyProfileBinding.inflate(layoutInflater)
    }

    override fun create(initialState: MyProfileViewState): MyProfileViewModel {
       return  myProfileViewModelFactory.create(initialState)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            imageUri = data!!.data
            Glide.with(this).load(imageUri).into(views.icAvtshop)
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
        }
    }
}