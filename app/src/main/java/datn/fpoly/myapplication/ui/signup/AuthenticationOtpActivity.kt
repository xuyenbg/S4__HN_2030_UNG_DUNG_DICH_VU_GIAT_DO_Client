package datn.fpoly.myapplication.ui.signup

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import datn.fpoly.myapplication.R
import datn.fpoly.myapplication.core.BaseActivity
import datn.fpoly.myapplication.databinding.ActivityAuthenticationOtpBinding
import datn.fpoly.myapplication.utils.Dialog_Loading
import java.util.concurrent.TimeUnit

class AuthenticationOtpActivity : BaseActivity<ActivityAuthenticationOtpBinding>() {
    private lateinit var auth: FirebaseAuth
    private lateinit var OTP: String
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var phoneNumber: String
    private var dialog: Dialog_Loading? = null
    override fun getBinding(): ActivityAuthenticationOtpBinding {
        return ActivityAuthenticationOtpBinding.inflate(layoutInflater)
    }

    override fun initUiAndData() {
        super.initUiAndData()
        auth = FirebaseAuth.getInstance()
        OTP = intent.getStringExtra("OTP").toString()
        resendToken = intent.getParcelableExtra("resendToken")!!
        phoneNumber = intent.getStringExtra("phone")!!
        dialog = Dialog_Loading.getInstance()
        addTextChangeListener()
        resendOTPTvVisibility()
        views.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
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
                    dialog?.show(supportFragmentManager, "SignUpLoading")
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
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            signInWithPhoneAuthCredential(credential)
        }


        override fun onVerificationFailed(e: FirebaseException) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.

            if (e is FirebaseAuthInvalidCredentialsException) {
                // Invalid request
                Log.d("TAG", "onVerificationFailed: ${e.toString()}")

            } else if (e is FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                Log.d("TAG", "onVerificationFailed: ${e.toString()}")
            }
//            views.progressPhone.visibility = View.VISIBLE
//            dialogLoading.show(supportFragmentManager, "LoadingAccount")
            // Show a message and update the UI
            dialog?.dismiss()
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            // Save verification ID and resending token so we can use them later
            OTP = verificationId
            resendToken = token
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
//                    views.progressPhone.visibility = View.
                    dialog?.dismiss()
                    val user = task.result?.user
                    Log.d("signInWithCredential", "signInWithPhoneAuthCredential: ${user?.uid}")
                    val intent = Intent(this, RegisterInforActivity::class.java)
                    intent.putExtra("PHONE", phoneNumber)
                    intent.putExtra("UID", user?.uid)
                    startActivity(intent)
                    finish()
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w(ContentValues.TAG, "signInWithCredential:failure", task.exception)

                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                    dialog?.dismiss()
                    views.tvError.text = "OTP không đúng.!"
                    // Update UI
                }
            }
    }
}