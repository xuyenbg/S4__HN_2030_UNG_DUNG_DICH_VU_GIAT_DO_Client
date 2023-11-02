package datn.fpoly.myapplication.ui.fragment.settingStore

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ql_ban_hang.core.BaseFragment
import com.google.firebase.auth.FirebaseAuth
import com.orhanobut.hawk.Hawk
import datn.fpoly.myapplication.databinding.FragmentProfileStoreBinding
import datn.fpoly.myapplication.ui.login.SignInActivity

class FragmentSettingStore: BaseFragment<FragmentProfileStoreBinding>() {
    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentProfileStoreBinding = FragmentProfileStoreBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        views.btnLogOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            Hawk.delete("Account");
            Hawk.put("CheckLogin", false)
            startActivity(Intent(requireContext(), SignInActivity::class.java))
        }
    }

}