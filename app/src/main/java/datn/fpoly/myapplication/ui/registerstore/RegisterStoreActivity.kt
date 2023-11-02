package datn.fpoly.myapplication.ui.registerstore

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.viewModel
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import datn.fpoly.myapplication.AppApplication
import datn.fpoly.myapplication.core.BaseActivity
import datn.fpoly.myapplication.data.repository.AuthRepo
import datn.fpoly.myapplication.databinding.ActivityRegiterInforAccountStoreBinding
import datn.fpoly.myapplication.ui.map.PickPossitionInMapActivity
import datn.fpoly.myapplication.utils.Common
import datn.fpoly.myapplication.utils.Common.registerStartForActivityResult
import datn.fpoly.myapplication.utils.Dialog_Loading
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import timber.log.Timber
import java.io.File
import javax.inject.Inject

const val TAG = "RegisterStoreActivity"

class RegisterStoreActivity : BaseActivity<ActivityRegiterInforAccountStoreBinding>(),
    RegisterStoreViewModel.Factory {
    @Inject
    lateinit var registerStoreFactory: RegisterStoreViewModel.Factory

    @Inject
    lateinit var authRepo: AuthRepo
    private val viewModel: RegisterStoreViewModel by viewModel()
    private var imageUri: Uri? = null
    private var address = ""
    private var latiu = 0.0
    private var longtiu = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as AppApplication).appComponent.inject(this);
        super.onCreate(savedInstanceState)
        setContentView(views.root)
    }

    override fun initUiAndData() {
        super.initUiAndData()
        viewModel.subscribe(this) {
            updateWithState(it)
        }
        views.btnPickImage.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start()
        }
        views.btnRegiterAccount.setOnClickListener {
            registerStore()
        }

        views.tvPickPossitionInMap.setOnClickListener {
            startActivity(Intent(this, PickPossitionInMapActivity::class.java))
        }

    }

    override fun onResume() {
        super.onResume()
        address = PickPossitionInMapActivity.dataAdress.pick_address
        latiu = PickPossitionInMapActivity.dataAdress.latitude
        longtiu = PickPossitionInMapActivity.dataAdress.longitude
        views.tvAdress.text = address
    }

    private fun registerStore() {
        val name = views.edName.text.toString()
        val transportType = listOf<String>("Ship COD", "Lấy Hàng trực tiếp")
        val idUser = authRepo.getUser()?._id
        val isDefault = true
        val status = 1
        val rate = 0.0

        val file = File(imageUri!!.path!!)
        val requestFile = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val image = MultipartBody.Part.createFormData("imageQRCode", file.name, requestFile)

        val namePart = name.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val transportTypeRequestBody =
            transportType.joinToString(",").toRequestBody("multipart/form-data".toMediaTypeOrNull())

        val idUserPart = idUser.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val isDefaultPart =
            isDefault.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val statusPart = status.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val ratePart = rate.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val addressPart = address.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val lat = latiu.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val long = longtiu.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())

        viewModel.handle(
            RegisterStoreViewAction.AddStore(
                namePart,
                ratePart,
                idUserPart,
                statusPart,
                transportTypeRequestBody,
                lat,
                long,
                addressPart,
                isDefaultPart,
                image,
            )
        )
    }

    private fun updateWithState(state: RegisterStoreViewState) {
        Timber.tag("RegisterStoreActivity").d("chậgdhasjd: ")
        when (state.stateRegisterStore) {
            is Success -> {
                runBlocking {
                    launch {
                        state.stateRegisterStore.invoke()?.let { result ->
                            Timber.tag("updateWithState").d("updateWithState: " + result.message())
                            if (result.code() == 200) {
                                Toast.makeText(
                                    this@RegisterStoreActivity,
                                    "Thêm thành công",
                                    Toast.LENGTH_SHORT
                                ).show()
                                onBackPressedDispatcher.onBackPressed()
                            } else {
                                Toast.makeText(
                                    this@RegisterStoreActivity,
                                    "Thất Bại",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        }
                    }
                }
            }

            is Loading -> {
                //Xoay tròn indicate
                Timber.tag("AddPostActivity").d("loadiing: ")
                Dialog_Loading.getInstance().show(supportFragmentManager, "Loading_store")
            }

            is Fail -> {
                Timber.tag("AddPostActivity").e("Error: ")
            }

            else -> {}
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            RESULT_OK -> {
                imageUri = data!!.data
                Glide.with(this).load(imageUri).into(views.imageView)
            }

            ImagePicker.RESULT_ERROR -> {
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            }

            else -> {
                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    }

    val startForResult = registerStartForActivityResult { result: ActivityResult ->
        when (result.resultCode) {
            1 -> {
                address = result.data?.getStringExtra(Common.ADDRESS)!!

                longtiu = result.data?.getDoubleExtra(Common.LONGTIU, 0.0)!!
                latiu = result.data?.getDoubleExtra(Common.LATIU, 0.0)!!

            }
        }
    }

    override fun getBinding() = ActivityRegiterInforAccountStoreBinding.inflate(layoutInflater)
    override fun create(initialState: RegisterStoreViewState) =
        registerStoreFactory.create(initialState)

}