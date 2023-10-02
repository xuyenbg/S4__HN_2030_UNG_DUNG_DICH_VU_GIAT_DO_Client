package datn.fpoly.myapplication.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.viewModel
import datn.fpoly.myapplication.AppApplication
import datn.fpoly.myapplication.R
import datn.fpoly.myapplication.core.BaseActivity
import datn.fpoly.myapplication.databinding.ActivitySignInBinding
import datn.fpoly.myapplication.ui.dashboard.DashboardActivity
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

class SignInActivity : BaseActivity<ActivitySignInBinding>(), LoginViewModel.Factory {
    @Inject
    lateinit var loginViewModelFactory: LoginViewModel.Factory
    private val viewModel:LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as AppApplication).appComponent.inject(this);
        super.onCreate(savedInstanceState)
        setContentView(views.root)
    }

    override fun initUiAndData() {
        super.initUiAndData()
        views.loginSubmit.setOnClickListener { login() }
        viewModel.subscribe(this){
            updateWithState(it)
        }
    }

    private fun login() {
        val username = views.username.text.toString();
        val password = views.password.text.toString();
        if(username.isBlank() || password.isBlank()){
            Toast.makeText(this, "Vui lòng không để trống", Toast.LENGTH_SHORT).show()
            return
        }
        viewModel.handle(LoginViewAction.LoginAction(username, password))
    }

    private fun updateWithState(state: LoginViewState) {
        when (state.stateLogin) {
            is Success -> {
                runBlocking {
                    Timber.tag("Log In: ").d("LogIn...")
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

            else -> {}
        }
    }

    override fun getBinding() = ActivitySignInBinding.inflate(layoutInflater)

    override fun create(initialState: LoginViewState) = loginViewModelFactory.create(initialState)

}