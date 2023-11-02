package datn.fpoly.myapplication.ui.fragment.settingStore.setting

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ql_ban_hang.core.BaseFragment
import com.google.firebase.auth.FirebaseAuth
import com.orhanobut.hawk.Hawk
import datn.fpoly.myapplication.databinding.FragmentProfileUserBinding
import datn.fpoly.myapplication.ui.login.SignInActivity
import datn.fpoly.myapplication.ui.registerstore.RegisterStoreActivity

class FragmentSetting: BaseFragment<FragmentProfileUserBinding>() {
    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentProfileUserBinding = FragmentProfileUserBinding.inflate(layoutInflater)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun invalidate() {
        super.invalidate()
        views.btnLogOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            Hawk.delete("Account");
            Hawk.put("CheckLogin", false)
            startActivity(Intent(requireContext(),SignInActivity::class.java))
        }
        views.tvRegisterStore.setOnClickListener {
            startActivity(Intent(requireContext(),RegisterStoreActivity::class.java))
        }
    }

}