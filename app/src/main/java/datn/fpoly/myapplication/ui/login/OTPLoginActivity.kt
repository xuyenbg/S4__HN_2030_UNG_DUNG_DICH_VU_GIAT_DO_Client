package datn.fpoly.myapplication.ui.login

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.viewModel
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.orhanobut.hawk.Hawk
import datn.fpoly.myapplication.AppApplication
import datn.fpoly.myapplication.R
import datn.fpoly.myapplication.core.BaseActivity
import datn.fpoly.myapplication.data.model.account.LoginResponse
import datn.fpoly.myapplication.data.repository.AuthRepo
import datn.fpoly.myapplication.data.repository.RoomDbRepo
import datn.fpoly.myapplication.databinding.ActivityOtpLoginBinding
import datn.fpoly.myapplication.ui.home.HomeActivity
import datn.fpoly.myapplication.ui.homeStore.HomeStoreActivity
import datn.fpoly.myapplication.utils.Dialog_Loading
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class OTPLoginActivity : BaseActivity<ActivityOtpLoginBinding>(), LoginViewModel.Factory {
    @Inject
    lateinit var loginViewModelFactory: LoginViewModel.Factory

    @Inject
    lateinit var dbRepo: RoomDbRepo

    @Inject
    lateinit var authRepo: AuthRepo
    private val viewModel: LoginViewModel by viewModel()
    private lateinit var auth: FirebaseAuth
    private lateinit var OTP: String
    private var check = false
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var phoneNumber: String
    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as AppApplication).appComponent.inject(this);
        super.onCreate(savedInstanceState)
        setContentView(views.root)
    }

    override fun initUiAndData() {
        super.initUiAndData()

        auth = FirebaseAuth.getInstance()

        OTP = intent.getStringExtra("OTP").toString()
        resendToken = intent.getParcelableExtra("resendToken")!!
        phoneNumber = intent.getStringExtra("phone")!!
        check = intent.getBooleanExtra("CHECKSTORE", false)

        viewModel.subscribe(this) {
            updateWithState(it)
        }
        addTextChangeListener()
        resendOTPTvVisibility()
        views.btnVeri.setOnClickListener {
            val typeOTP = views.edOtp1.text.toString() + views.edOtp2.text.toString() +
                    views.edOtp3.text.toString() + views.edOtp4.text.toString() +
                    views.edOtp5.text.toString() + views.edOtp6.text.toString()

            if (typeOTP.isNotEmpty()) {
                if (typeOTP.length == 6) {

                    val credential: PhoneAuthCredential = PhoneAuthProvider.getCredential(
                        OTP, typeOTP
                    )
//                    views.progressPhone.visibility = View.VISIBLE
                    Dialog_Loading.getInstance().show(supportFragmentManager, "LoginLoading")
                    signInWithPhoneAuthCredential(credential)
                } else {
                    Toast.makeText(this, "Vui lòng nhập lại OTP", Toast.LENGTH_SHORT).show()

                }
            } else {
                Toast.makeText(this, "Vui lòng nhập OTP", Toast.LENGTH_SHORT).show()
            }
        }
        views.tvSendTo.setOnClickListener {
            resendVerificationCode()
            resendOTPTvVisibility()
        }
    }

    private fun login(uID: String) {
        viewModel.handle(LoginViewAction.LoginAction(phoneNumber, uID))
    }

    private fun updateWithState(state: LoginViewState) {
        Timber.tag("LogIn").d("Log in successful ${state.stateLogin}")
        when (state.stateLogin) {
            is Success -> {
                runBlocking {
                    launch {
                        state.stateLogin.invoke()?.let { result ->
                            Timber.tag("LogIn").d("Log in successful ${result.body()}}")

                            // account chứa cả đối tượng và message
                            val account = result.body()?.let { parseJsonToAccountList(it.string()) }

                            if (account?.message == "Đăng nhập thành công") {
                                authRepo.saveUser(accountResponse = account.user)
                                authRepo.setLogin(isLogin = true)
                                dbRepo.getCart()
//                                Toast.makeText(
//                                    this@OTPLoginActivity,
//                                    "Đăng nhập thành công",
//                                    Toast.LENGTH_SHORT
//                                ).show()
                                if (!check) {
                                    Hawk.put("Manage", 0)
                                    startActivity(
                                        Intent(
                                            this@OTPLoginActivity,
                                            HomeActivity::class.java
                                        ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                    )
                                } else {
                                    if (account.user.idRole != "6522666361b6e95df121642d") {
                                        Hawk.put("Manage", 3)
                                        Log.d("OTPLoginActivity", "chưa đăng kí: ")
                                        Toast.makeText(
                                            this@OTPLoginActivity,
                                            "Bạn chưa đăng kí của hàng",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        startActivity(
                                            Intent(
                                                this@OTPLoginActivity,
                                                SignInActivity::class.java
                                            )
                                        )
                                    } else if (account.user.idRole == "6522666361b6e95df121642d") {
                                        Log.d("OTPLoginActivity", "dã  đăng kí: ")

                                        Hawk.put("Manage", 1)
                                        startActivity(
                                            Intent(
                                                this@OTPLoginActivity,
                                                HomeStoreActivity::class.java
                                            ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                        )
                                    }
                                }

                            } else {
                                Log.d("Log In", "Sai tài khoản hoặc mật khẩu")

                                Toast.makeText(
                                    this@OTPLoginActivity,
                                    "Sai tài khoản hoặc mật khẩu",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                    }
                }
            }

            is Loading -> {
                //Xoay tròn indicate

            }

            is Fail -> {
                Timber.tag("OTPLogin").e("updateWithState: ")
            }

            else -> {}
        }
    }

    private fun parseJsonToAccountList(json: String): LoginResponse {
        val gson = Gson()
        val type = object : TypeToken<LoginResponse>() {}.type
        return gson.fromJson(json, type)
    }

    override fun getBinding() = ActivityOtpLoginBinding.inflate(layoutInflater)

    override fun create(initialState: LoginViewState) = loginViewModelFactory.create(initialState)

    private fun resendOTPTvVisibility() {
        views.edOtp1.setText("")
        views.edOtp2.setText("")
        views.edOtp3.setText("")
        views.edOtp4.setText("")
        views.edOtp5.setText("")
        views.edOtp6.setText("")
        views.tvSendTo.visibility = View.INVISIBLE
        views.tvSendTo.isEnabled = false

        val countdownTimer = object : CountDownTimer(60000, 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                views.tvTimeCount.text = "Còn lại : ${(millisUntilFinished / 1000)}"
            }

            override fun onFinish() {
                views.tvSendTo.visibility = View.VISIBLE
                views.tvSendTo.isEnabled = true
            }
        }
        countdownTimer.start()
//        Handler(Looper.myLooper()!!).postDelayed(Runnable {
//            views.tvSendTo.visibility = View.VISIBLE
//            views.tvSendTo.isEnabled = true
//        }, 60000)
    }

    private fun addTextChangeListener() {
        views.edOtp1.addTextChangedListener(EditTextWatcher(views.edOtp1))
        views.edOtp2.addTextChangedListener(EditTextWatcher(views.edOtp2))
        views.edOtp3.addTextChangedListener(EditTextWatcher(views.edOtp3))
        views.edOtp4.addTextChangedListener(EditTextWatcher(views.edOtp4))
        views.edOtp5.addTextChangedListener(EditTextWatcher(views.edOtp5))
        views.edOtp6.addTextChangedListener(EditTextWatcher(views.edOtp6))

    }

    inner class EditTextWatcher(private val view: View) : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            val text = s.toString()
            when (view.id) {
                R.id.ed_otp_1 -> if (text.length == 1) views.edOtp2.requestFocus()
                R.id.ed_otp_2 -> if (text.length == 1) views.edOtp3.requestFocus() else if (text.isEmpty()) views.edOtp1.requestFocus()
                R.id.ed_otp_3 -> if (text.length == 1) views.edOtp4.requestFocus() else if (text.isEmpty()) views.edOtp2.requestFocus()
                R.id.ed_otp_4 -> if (text.length == 1) views.edOtp5.requestFocus() else if (text.isEmpty()) views.edOtp3.requestFocus()
                R.id.ed_otp_5 -> if (text.length == 1) views.edOtp6.requestFocus() else if (text.isEmpty()) views.edOtp4.requestFocus()
                R.id.ed_otp_6 -> if (text.isEmpty()) views.edOtp5.requestFocus()

            }
        }

    }

    private fun resendVerificationCode() {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(callbacks)
            .setForceResendingToken(resendToken)// OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {

            signInWithPhoneAuthCredential(credential)
        }


        override fun onVerificationFailed(e: FirebaseException) {
            if (e is FirebaseAuthInvalidCredentialsException) {
                // Invalid request
                Log.d("TAG", "onVerificationFailed: ${e.toString()}")

            } else if (e is FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                Log.d("TAG", "onVerificationFailed: ${e.toString()}")
            }
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            OTP = verificationId
            resendToken = token
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = task.result?.user
                    Timber.tag("OTPLoginActivity").d("initUiAndData: ${phoneNumber}")
                    Timber.tag("OTPLoginActivity").d("initUiAndData:uid ${user?.uid}")
                    login(user!!.uid)
//                    Dialog_Loading.getInstance().show(supportFragmentManager,"LoginLoading")
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w(ContentValues.TAG, "signInWithCredential:failure", task.exception)

                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                    // Update UI
                }
            }
    }
}