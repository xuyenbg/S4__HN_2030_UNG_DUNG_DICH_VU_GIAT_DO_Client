package datn.fpoly.myapplication.ui.signup

import android.content.ContentValues
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import datn.fpoly.myapplication.R
import datn.fpoly.myapplication.core.BaseActivity
import datn.fpoly.myapplication.databinding.ActivitySignUpBinding
import datn.fpoly.myapplication.ui.home.HomeActivity
import datn.fpoly.myapplication.ui.otp.AuthenticationOtpActivity
import datn.fpoly.myapplication.utils.Dialog_Loading
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SignUpActivity : BaseActivity<ActivitySignUpBinding>(){
    private lateinit var number: String
    private lateinit var auth: FirebaseAuth
    override fun getBinding(): ActivitySignUpBinding {
        return ActivitySignUpBinding.inflate(layoutInflater)
    }

    override fun initUiAndData() {
        super.initUiAndData()
        views.progressPhone.visibility = View.INVISIBLE
        auth = FirebaseAuth.getInstance()


        views.btnContinue.setOnClickListener {
            number = views.phoneNumber.text.toString().trim()
            if (number.isNotEmpty()) {
                if (number.length == 10) {
                    number = "+84$number"
//                    views.progressPhone.visibility = View.VISIBLE
                    Dialog_Loading.getInstance().show(supportFragmentManager,"SignUpLoading")

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
//            views.progressPhone.visibility = View.VISIBLE
            Dialog_Loading.getInstance().show(supportFragmentManager,"SignUpLoading")
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
            val intent = Intent(this@SignUpActivity, AuthenticationOtpActivity::class.java)
            intent.putExtra("OTP", verificationId)
            intent.putExtra("resendToken", token)
            intent.putExtra("phone", views.phoneNumber.text.toString().trim())
            startActivity(intent)
        }
    }
}