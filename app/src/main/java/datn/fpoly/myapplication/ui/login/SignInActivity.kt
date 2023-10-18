package datn.fpoly.myapplication.ui.login

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.viewModel
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import datn.fpoly.myapplication.AppApplication
import datn.fpoly.myapplication.R
import datn.fpoly.myapplication.core.BaseActivity
import datn.fpoly.myapplication.data.model.User
import datn.fpoly.myapplication.databinding.ActivitySignInBinding
import datn.fpoly.myapplication.ui.dashboard.DashboardActivity
import datn.fpoly.myapplication.ui.home.HomeActivity
import datn.fpoly.myapplication.ui.otp.AuthenticationOtpActivity
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SignInActivity : BaseActivity<ActivitySignInBinding>(), LoginViewModel.Factory {
    @Inject
    lateinit var loginViewModelFactory: LoginViewModel.Factory
    private val viewModel:LoginViewModel by viewModel()
    private lateinit var number: String
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as AppApplication).appComponent.inject(this);
        super.onCreate(savedInstanceState)
        setContentView(views.root)
    }

    override fun initUiAndData() {
        super.initUiAndData()
        views.btnContinue.setOnClickListener { login() }
        viewModel.subscribe(this){
            updateWithState(it)
        }
        views.btnContinue.setOnClickListener {
            number = views.phoneNumber.text.toString().trim()
            if (number.isNotEmpty()) {
                if (number.length == 10) {
                    number = "+84$number"
                    views.progressPhone.visibility = View.VISIBLE
                    val options = PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(number) // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this) // Activity (for callback binding)
                        .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
                        .build()
                    PhoneAuthProvider.verifyPhoneNumber(options)
                } else {
                    Toast.makeText(this, "Vui lòng nhập lại phone", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Vui lòng nhập phone", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun login() {
//        val username = views.username.text.toString();
//        val password = views.password.text.toString();
//        if(username.isBlank() || password.isBlank()){
//            Toast.makeText(this, "Vui lòng không để trống", Toast.LENGTH_SHORT).show()
//            return
//        }
        viewModel.handle(LoginViewAction.LoginAction("admin", "12345"))
    }

    private fun updateWithState(state: LoginViewState) {
        when (state.stateLogin) {
            is Success -> {
                runBlocking {
                    launch {
                        state.stateLogin.invoke()?.let{ listUser ->
                            if (listUser != null && listUser.isNotEmpty()) {
                                Log.d("ListData", listUser.toString())
                                Log.d("Log In", "Log in successful")
                                //snackbar("Đăng nhập thành công")
                                Toast.makeText(this@SignInActivity, "Đăng nhập thành công", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this@SignInActivity, DashboardActivity::class.java))
                            }else{
                                Log.d("Log In", "Sai tài khoản hoặc mật khẩu")
                                Toast.makeText(this@SignInActivity, "Sai tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
            is Loading ->{
                //Xoay tròn indicate
            }

            else -> {}
        }
    }

    override fun getBinding() = ActivitySignInBinding.inflate(layoutInflater)

    override fun create(initialState: LoginViewState) = loginViewModelFactory.create(initialState)
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(ContentValues.TAG, "signInWithCredential:success")
                    Toast.makeText(this, "Thành Công", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, HomeActivity::class.java))
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w(ContentValues.TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                    // Update UI
                }
                views.progressPhone.visibility = View.INVISIBLE
            }
    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            Log.d(ContentValues.TAG, "onVerificationCompleted:$credential")
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            Log.w("TAG", "onVerificationFailed", e)

            if (e is FirebaseAuthInvalidCredentialsException) {
                // Invalid request
                Log.w("TAG", "onVerificationFailed", e)
            } else if (e is FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                Log.w("TAG", "onVerificationFailed", e)
            } else if (e is FirebaseAuthMissingActivityForRecaptchaException) {
                // reCAPTCHA verification attempted with null Activity
            }
            views.progressPhone.visibility = View.VISIBLE
            // Show a message and update the UI
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken,
        ) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID
            // Save verification ID and resending token so we can use them later
            Timber.d("OTP", verificationId)
            val intent = Intent(this@SignInActivity, AuthenticationOtpActivity::class.java)
            intent.putExtra("OTP", verificationId)
            intent.putExtra("resendToken", token)
            intent.putExtra("phone", number)
            startActivity(intent)
            views.progressPhone.visibility = View.INVISIBLE
        }
    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser != null) {
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }
}