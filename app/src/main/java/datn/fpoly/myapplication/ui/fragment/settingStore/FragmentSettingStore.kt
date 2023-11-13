package datn.fpoly.myapplication.ui.fragment.settingStore

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ql_ban_hang.core.BaseFragment
import com.google.firebase.auth.FirebaseAuth
import com.orhanobut.hawk.Hawk
import datn.fpoly.myapplication.databinding.FragmentProfileStoreBinding
import datn.fpoly.myapplication.ui.login.SignInActivity
import datn.fpoly.myapplication.utils.Dialog_Loading
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

class FragmentSettingStore : BaseFragment<FragmentProfileStoreBinding>() {
    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentProfileStoreBinding = FragmentProfileStoreBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        views.btnLogOut.setOnClickListener {
            Dialog_Loading.getInstance().show(childFragmentManager,"Loading_logour")
            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({
                FirebaseAuth.getInstance().signOut()
                Hawk.delete("Account");
                Hawk.put("CheckLogin", false)
                startActivity(Intent(requireContext(), SignInActivity::class.java))
            }, 3000)

        }
    }

}