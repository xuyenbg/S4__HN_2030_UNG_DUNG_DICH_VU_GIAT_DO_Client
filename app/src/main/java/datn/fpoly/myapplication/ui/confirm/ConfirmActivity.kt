package datn.fpoly.myapplication.ui.confirm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import datn.fpoly.myapplication.R
import datn.fpoly.myapplication.databinding.ActivityConfirmBinding
import datn.fpoly.myapplication.ui.home.HomeActivity

class ConfirmActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConfirmBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityConfirmBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnDetail.setOnClickListener {
            val intent= Intent(this, HomeActivity::class.java)
            intent.flags= Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }

    override fun onBackPressed() {

    }
}