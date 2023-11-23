package datn.fpoly.myapplication.ui.splashScreen


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import datn.fpoly.myapplication.R
import datn.fpoly.myapplication.ui.login.SignInActivity

class SplashsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val windowInsetsController =
            WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        window.decorView.setOnApplyWindowInsetsListener { view, windowInsets ->
            view.onApplyWindowInsets(windowInsets)
        }

        setContentView(R.layout.activity_splash)

        val handler = Handler()
        handler.postDelayed({
            startActivity(Intent(this@SplashsActivity, SignInActivity::class.java))
            finish()
        }, 3000)

    }
}