package datn.fpoly.myapplication.ui.signup

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.viewModel
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.ktx.messaging
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.orhanobut.hawk.Hawk
import datn.fpoly.myapplication.AppApplication
import datn.fpoly.myapplication.core.BaseActivity
import datn.fpoly.myapplication.data.model.account.LoginResponse
import datn.fpoly.myapplication.data.repository.AuthRepo
import datn.fpoly.myapplication.databinding.ActivityRegiterInforAccountUserBinding
import datn.fpoly.myapplication.ui.home.HomeActivity
import datn.fpoly.myapplication.utils.Dialog_Loading
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import timber.log.Timber
import java.io.File
import javax.inject.Inject

class RegisterInforActivity : BaseActivity<ActivityRegiterInforAccountUserBinding>(),
    SignUpViewModel.Factory {
    private var phonenumber: String? = null
    private var uid: String? = null
    private var favoriteStore: List<String>? = null
    private var imageUri: Uri? = null
    var dialogLoading: Dialog_Loading? = null

    @Inject
    lateinit var signupViewModelFactory: SignUpViewModel.Factory

    private val signUpViewModel: SignUpViewModel by viewModel()

    @Inject
    lateinit var authRepo: AuthRepo

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as AppApplication).appComponent.inject(this);
        super.onCreate(savedInstanceState)
        setContentView(views.root)
    }

    override fun initUiAndData() {
        super.initUiAndData()
        phonenumber = intent.getStringExtra("PHONE")
        uid = intent.getStringExtra("UID")
        dialogLoading = Dialog_Loading.getInstance()
        views.btnRegiterAccount.setOnClickListener {
            if (views.edFullname.text.toString().isEmpty()) {
                views.tvError.text = "Vui lòng nhập tên.!"
            } else {
                register()
            }
        }
        signUpViewModel.subscribe(this) {
            updateWithState(it)
        }

        views.icPickimage.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start()
        }
        views.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun register() {

        if (imageUri != null) {

            views.apply {
                val file = File(imageUri!!.path!!) // Chuyển URI thành File

                val requestFile = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                val image = MultipartBody.Part.createFormData("avatar", file.name, requestFile)

                val phone = phonenumber?.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                val passwd = uid?.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                val fullname = views.edFullname.text.toString().trim()!!
                    .toRequestBody("multipart/form-data".toMediaTypeOrNull())
                val idRole =
                    "6522667961b6e95df121642e".toRequestBody("multipart/form-data".toMediaTypeOrNull())
                signUpViewModel.handle(
                    SignUpViewAction.SignUpAction(
                        phone!!,
                        passwd!!,
                        fullname,
                        idRole,
                        null,
                        image
                    )
                )
            }
        } else {
            views.apply {
                val phone = phonenumber?.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                val passwd = uid?.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                val fullname = views.edFullname.text.toString().trim()!!
                    .toRequestBody("multipart/form-data".toMediaTypeOrNull())
                val idRole =
                    "6522667961b6e95df121642e".toRequestBody("multipart/form-data".toMediaTypeOrNull())
                signUpViewModel.handle(
                    SignUpViewAction.SignUpAction(
                        phone!!,
                        passwd!!,
                        fullname,
                        idRole,
                        null,
                        null
                    )
                )
            }
        }
    }

    private fun updateWithState(state: SignUpViewState) {
        when (state.stateSignUp) {
            is Success -> {
                runBlocking {
                    launch {
                        state.stateSignUp.invoke().let { result ->

                            if (result?.message.equals("Đăng ký thành công")) {
                                result?.user?.let {
                                    FirebaseMessaging.getInstance().subscribeToTopic(it.id!!)
                                        .addOnCompleteListener {
                                            if (it.isSuccessful) {
                                                Timber.tag("AAAAAAAAAAA")
                                                    .e("updateWithState: Đăng ký topic thàng công")
                                            } else {
                                                Timber.tag("AAAAAAAAAAA")
                                                    .e("updateWithState: Đăng ký topic thất bại")
                                            }
                                        }
                                }

                                authRepo.saveUser(accountResponse = result!!.user)

                                Log.d("Log In", "Log in successful $")

                                Toast.makeText(
                                    this@RegisterInforActivity,
                                    "Đăng ký thành công",
                                    Toast.LENGTH_SHORT
                                ).show()
                                Hawk.put("Manage", 0)
                                Hawk.put("CheckLogin", true)
                                startActivity(
                                    Intent(
                                        this@RegisterInforActivity,
                                        HomeActivity::class.java
                                    ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                )
                                dialogLoading?.dismiss()
                            } else {
                                views.tvError.text = "Số điện thoại đã được sử dụng"
                                dialogLoading?.dismiss()
                                val snack = Snackbar.make(views.root, " được sử dụng", Snackbar.LENGTH_SHORT)
                                snack.setAction("Ok") {
                                    startActivity(Intent(this@RegisterInforActivity, SignUpActivity::class.java))
                                }
                                snack.show()
                            }
                        }
                    }
                }
            }

            is Loading -> {
                //Xoay tròn indicate
                dialogLoading?.show(supportFragmentManager, "SignUpLoading")
            }

            is Fail -> {
                runBlocking {
                    launch {
                        Log.d("state", "state: ")
                    }
                }
                dialogLoading?.dismiss()
                views.tvError.text = "Số điện thoại đã được sử dụng"
            }

            else -> {}
        }
    }


    override fun getBinding() = ActivityRegiterInforAccountUserBinding.inflate(layoutInflater)

    override fun create(initialState: SignUpViewState) =
        signupViewModelFactory.create(initialState)

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            imageUri = data!!.data
            Glide.with(this).load(imageUri).into(views.ivAvt)
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }
}