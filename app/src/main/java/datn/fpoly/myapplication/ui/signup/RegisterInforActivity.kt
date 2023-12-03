package datn.fpoly.myapplication.ui.signup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.viewModel
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.orhanobut.hawk.Hawk
import datn.fpoly.myapplication.AppApplication
import datn.fpoly.myapplication.core.BaseActivity
import datn.fpoly.myapplication.data.model.account.LoginResponse
import datn.fpoly.myapplication.databinding.ActivityRegiterInforAccountUserBinding
import datn.fpoly.myapplication.ui.home.HomeActivity
import datn.fpoly.myapplication.utils.Dialog_Loading
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

class RegisterInforActivity : BaseActivity<ActivityRegiterInforAccountUserBinding>(),
    SignUpViewModel.Factory {
    private var phonenumber: String? = null
    private var uid: String? = null
    private var favoriteStore: List<String>? = null

    @Inject
    lateinit var signupViewModelFactory: SignUpViewModel.Factory

    private val signUpViewModel: SignUpViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as AppApplication).appComponent.inject(this);
        super.onCreate(savedInstanceState)
        setContentView(views.root)
    }

    override fun initUiAndData() {
        super.initUiAndData()
        phonenumber = intent.getStringExtra("PHONE")
        uid = intent.getStringExtra("UID")
        views.btnRegiterAccount.setOnClickListener {
            register()
        }
        signUpViewModel.subscribe(this) {
            updateWithState(it)
        }


    }

    private fun register() {
        phonenumber?.let {
            uid?.let { it1 ->
                SignUpViewAction.SignUpAction(
                    phonenumber!!,
                    uid!!,
                    views.edFullname.text.toString().trim(),
                    "6522667961b6e95df121642e",
                    favoriteStore,
                )
            }
        }?.let { signUpViewModel.handle(it) }
    }

    private fun updateWithState(state: SignUpViewState) {
        Log.d("RegisterInforActivity", "updateWithState: ${state.stateSignUp.invoke()?.body()}")
        when (state.stateSignUp) {
            is Success -> {
                runBlocking {
                    launch {
                        state.stateSignUp.invoke().let { s ->
                            Timber.tag("RegisterInforActivity")
                                .d("updateWithState đăng kí: " + s?.code())
                            val check = s?.code()
                            if (check == 200) {
                                val account = s.body()?.let { parseJsonToAccountList(it.string()) }
                                account?.user?.id?.let {
                                    Firebase.messaging.subscribeToTopic(it)
                                        .addOnCompleteListener {
                                            if (it.isSuccessful) {
                                                Timber.tag("AAAAAAAAAAAAAAAAA")
                                                    .e("updateWithState: Đăng ký topic thành công")
                                            } else {
                                                Timber.tag("AAAAAAAAAAAAAAAAA")
                                                    .e("updateWithState: Đăng ký topic thất bại")
                                            }
                                        }
                                }
                                Log.d("Log In", "Log in successful $s")

                                Toast.makeText(
                                    this@RegisterInforActivity,
                                    "Đăng ký thành công",
                                    Toast.LENGTH_SHORT
                                ).show()
                                Hawk.put("CheckLogin", true)
                                startActivity(
                                    Intent(
                                        this@RegisterInforActivity,
                                        HomeActivity::class.java
                                    ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                )
                            } else {
                                Toast.makeText(
                                    this@RegisterInforActivity,
                                    "Số điện thoại đã được sử dụng",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }

            is Loading -> {
                //Xoay tròn indicate
                Log.d("RegisterInforActivity", "loading: ")
                Dialog_Loading.getInstance().show(supportFragmentManager,"SignUpLoading")
            }

            is Fail -> {
                runBlocking {
                    launch {
                        Log.d("state", "state: ")
                    }
                }
                Log.d("Fail", "Đăng ký thất bại ")
                Log.e("signupViewModelFactory", "error")
            }

            else -> {}
        }
    }

    private fun parseJsonToAccountList(json: String): LoginResponse {
        val gson = Gson()
        val type = object : TypeToken<LoginResponse>() {}.type
        return gson.fromJson(json, type)
    }

    override fun getBinding() = ActivityRegiterInforAccountUserBinding.inflate(layoutInflater)

    override fun create(initialState: SignUpViewState) = signupViewModelFactory.create(initialState)

}