package datn.fpoly.myapplication.ui.fragment.settingStore.setting

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.ql_ban_hang.core.BaseFragment
import com.google.firebase.auth.FirebaseAuth
import com.orhanobut.hawk.Hawk
import datn.fpoly.myapplication.data.model.account.AccountModel
import datn.fpoly.myapplication.databinding.FragmentProfileUserBinding
import datn.fpoly.myapplication.ui.login.SignInActivity
import datn.fpoly.myapplication.ui.registerstore.RegisterStoreActivity

class FragmentSetting : BaseFragment<FragmentProfileUserBinding>() {

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentProfileUserBinding = FragmentProfileUserBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun invalidate() {
        super.invalidate()
        val account = Hawk.get<AccountModel>("Account", null)
        Log.d("FragmentSetting", "invalidate: $account")

        views.btnLogOut.setOnClickListener {
            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({
                FirebaseAuth.getInstance().signOut()
                Hawk.delete("Account");
                Hawk.put("CheckLogin", false)
                startActivity(Intent(requireContext(), SignInActivity::class.java))
            }, 3000)

        }
        views.tvRegisterStore.setOnClickListener {
            if (account?.idRole == "6522666361b6e95df121642d") {
                Toast.makeText(
                    requireContext(),
                    "Bạn đã đăng kí của hàng rồi !",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                startActivity(Intent(requireContext(), RegisterStoreActivity::class.java))
            }
        }
    }

}