package datn.fpoly.myapplication.ui.login

import android.content.ContentValues
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.orhanobut.hawk.Hawk
import datn.fpoly.myapplication.R
import datn.fpoly.myapplication.core.BaseActivity
import datn.fpoly.myapplication.databinding.ActivitySignIn2Binding
import datn.fpoly.myapplication.ui.home.HomeActivity
import datn.fpoly.myapplication.ui.homeStore.HomeStoreActivity
import datn.fpoly.myapplication.ui.signup.SignUpActivity
import datn.fpoly.myapplication.utils.Dialog_Loading
import timber.log.Timber
import java.util.concurrent.TimeUnit

class SignInActivity : BaseActivity<ActivitySignIn2Binding>() {

    private lateinit var number: String
    private lateinit var auth: FirebaseAuth
    private var checkStrore = false

    override fun getBinding(): ActivitySignIn2Binding {
        return ActivitySignIn2Binding.inflate(layoutInflater)
    }

    override fun initUiAndData() {
        super.initUiAndData()
        auth = FirebaseAuth.getInstance()

        views.cbRule.setOnCheckedChangeListener { _, isChecked ->
            // Cập nhật trạng thái của button dựa trên checkbox
            views.btnContinue.isEnabled = isChecked
            if (!isChecked) {
                views.btnContinue.setTextColor(Color.GRAY)
            } else {
                views.btnContinue.setTextColor(getColor(R.color.white))
            }
        }
        views.cbShop.setOnCheckedChangeListener { _, isChecked ->
            // Cập nhật trạng thái của button dựa trên checkbox
           checkStrore = isChecked
        }
        Log.d("SignInActivity", "checkstore: $checkStrore")
        views.btnSignUp.setOnClickListener {
            startActivity(Intent(this,SignUpActivity::class.java))
        }
        views.btnContinue.setOnClickListener {
            number = views.phoneNumber.text.toString().trim()
            if (number.isNotEmpty()) {
                if (number.length == 10) {
                    number = "+84$number"
//                    views.progressPhone.visibility = View.VISIBLE
                    Dialog_Loading.getInstance().show(supportFragmentManager,"LoginLoading")
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
//                views.progressPhone.visibility = View.INVISIBLE

            }
    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            Log.d(ContentValues.TAG, "onVerificationCompleted:$credential")
            signInWithPhoneAuthCredential(credential)
        }
        override fun onVerificationFailed(e: FirebaseException) {
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
//            views.progressPhone.visibility = View.VISIBLE
            Dialog_Loading.getInstance().show(supportFragmentManager,"LoginLoading")
            // Show a message and update the UI
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken,
        ) {
            checkStrore = views.cbShop.isChecked
            Timber.d("OTP", verificationId)
            val intent = Intent(this@SignInActivity, OTPLoginActivity::class.java)
            intent.putExtra("OTP", verificationId)
            intent.putExtra("resendToken", token)
            intent.putExtra("phone", number)
            intent.putExtra("CHECKSTORE", checkStrore)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        val user : Boolean = Hawk.get("CheckLogin",false)
        val manege : Int = Hawk.get("Manage",0)
        if (auth.currentUser != null && user) {
            if (manege ==0) {
                startActivity(Intent(this, HomeActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
            } else if (manege ==1) {
                startActivity(Intent(this, HomeStoreActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
            } else {
                return
            }

        }
    }
}