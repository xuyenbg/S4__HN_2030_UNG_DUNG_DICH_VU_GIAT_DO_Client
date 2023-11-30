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
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.orhanobut.hawk.Hawk
import datn.fpoly.myapplication.R
import datn.fpoly.myapplication.core.BaseFragment
import datn.fpoly.myapplication.data.model.account.AccountModel
import datn.fpoly.myapplication.databinding.FragmentProfileUserBinding
import datn.fpoly.myapplication.ui.favoriteStore.FavoriteStoreActivity
import datn.fpoly.myapplication.ui.login.SignInActivity
import datn.fpoly.myapplication.ui.myProfileUser.MyProfileActivity
import datn.fpoly.myapplication.ui.registerstore.RegisterStoreActivity
import datn.fpoly.myapplication.utils.Common
import datn.fpoly.myapplication.utils.Dialog_Loading

class FragmentSetting : BaseFragment<FragmentProfileUserBinding>() {

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentProfileUserBinding = FragmentProfileUserBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private var account: AccountModel? = null

    override fun invalidate() {
        super.invalidate()
        Log.d("FragmentSetting", "invalidate: $account")

        views.btnLogOut.setOnClickListener {
            Dialog_Loading.getInstance().show(childFragmentManager, "Loading_logour")
            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({
                FirebaseAuth.getInstance().signOut()
                Hawk.delete("Account");
                Hawk.put("CheckLogin", false)
                startActivity(Intent(requireContext(), SignInActivity::class.java))
            }, 3000)

        }
        views.tvFullname.text = account?.fullname
        Glide.with(views.imgAvatar).load(Common.baseUrl + account?.avatar)
            .error(ContextCompat.getDrawable(requireContext(), R.drawable.avatar_profile))
            .into(views.imgAvatar)
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
        views.tvFavouriteStore.setOnClickListener {
            startActivity(Intent(requireContext(), FavoriteStoreActivity::class.java))
        }
        views.tvEditProfile.setOnClickListener {
            startActivity(
                Intent(
                    requireContext(),
                    MyProfileActivity::class.java
                ).putExtra("profile", account)
            )
        }
    }

    override fun onResume() {
        super.onResume()
        account = Hawk.get<AccountModel>("Account", null)
    }

}