package datn.fpoly.myapplication.ui.fragment.settingStore

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import com.bumptech.glide.Glide
import datn.fpoly.myapplication.core.BaseFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import com.orhanobut.hawk.Hawk
import datn.fpoly.myapplication.R
import datn.fpoly.myapplication.data.model.StoreModel
import datn.fpoly.myapplication.data.model.orderList.OrderResponse
import datn.fpoly.myapplication.databinding.FragmentProfileStoreBinding
import datn.fpoly.myapplication.ui.historystore.HistoryStoreActivity
import datn.fpoly.myapplication.ui.homeStore.HomeStoreState
import datn.fpoly.myapplication.ui.homeStore.HomeStoreViewAction
import datn.fpoly.myapplication.ui.homeStore.HomeStoreViewModel
import datn.fpoly.myapplication.ui.login.SignInActivity
import datn.fpoly.myapplication.ui.myshop.MyShopActivity
import datn.fpoly.myapplication.ui.notification.NotificationActivity
import datn.fpoly.myapplication.utils.Common
import datn.fpoly.myapplication.utils.Dialog_Loading
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber

class FragmentSettingStore : BaseFragment<FragmentProfileStoreBinding>() {
    private val viewModel: HomeStoreViewModel by activityViewModel()
    private val listOrderStore = arrayListOf<OrderResponse>()
    private var countUnconfimred = 0
    private var countConfirmed = 0
    private var storeModel: StoreModel? = null
    var idStore=""
    private val startActivityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Common.CODE_LOAD_DATA) {
                storeModel = Hawk.get(Common.KEY_STORE)
                views.tvFullname.text = storeModel?.name
                Glide.with(views.imgAvatar).load(Common.baseUrl + storeModel?.imageQACode)
                    .error(R.drawable.avatar_profile)
                    .into(views.imgAvatar)
                idStore?.let { HomeStoreViewAction.GetDataOrderStore(it, "desc") }
                    ?.let { viewModel.handle(it) }
            }
        }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentProfileStoreBinding = FragmentProfileStoreBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        idStore = Hawk.get<StoreModel>(Common.KEY_STORE).id.toString()
        idStore?.let { HomeStoreViewAction.GetDataOrderStore(it, "desc") }
            ?.let { viewModel.handle(it) }
        storeModel = Hawk.get(Common.KEY_STORE)
        views.tvFullname.text = storeModel?.name
        Glide.with(views.imgAvatar).load(Common.baseUrl + storeModel?.imageQACode)
            .error(R.drawable.avatar_profile)
            .into(views.imgAvatar)
        views.btnLogOut.setOnClickListener {
            Dialog_Loading.getInstance().show(childFragmentManager, "Loading_logour")
            val handler = Handler(Looper.getMainLooper())
            storeModel?.iduser?.id?.let { it1 ->
                FirebaseMessaging.getInstance().unsubscribeFromTopic(
                    it1
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        handler.postDelayed({
                            FirebaseAuth.getInstance().signOut()
                            Hawk.delete("Account");
                            Hawk.put("CheckLogin", false)
                            Hawk.delete("Manage")
                            startActivity(Intent(requireContext(), SignInActivity::class.java))
                        }, 1500)
                    }

                }
            }

        }
        views.tvOrderHistory.setOnClickListener {
            val intent = Intent(requireContext(), HistoryStoreActivity::class.java)
            intent.putExtra("KEY_HIS", listOrderStore)
            Log.d("FragmentSettingStore", "onViewCreated: $listOrderStore")

            startActivityResult.launch(intent)

        }
        views.tvTermsAndPolicies.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://www.freeprivacypolicy.com/live/aa3aeb2d-c7c4-429d-8ffe-cbf8bc59c16e")
            )
            startActivityResult.launch(intent)
        }
        views.tvStore.setOnClickListener {
            val intent = Intent(requireContext(), MyShopActivity::class.java)
            startActivityResult.launch(intent)
        }
        views.tvNotifications.setOnClickListener {
            val intent = Intent(requireContext(), NotificationActivity::class.java)
            intent.putExtra(Common.KEY_ID_USER, storeModel?.iduser?.id)
            startActivityResult.launch(intent)
        }
    }

    override fun onResume() {
        super.onResume()

    }

    override fun invalidate(): Unit = withState(viewModel) {
        super.invalidate()
        getListPost(it)
    }

    private fun getListPost(it: HomeStoreState) {
        when (it.stateGetOrderStore) {
            is Success -> {
                runBlocking {
                    launch {
                        it.stateGetOrderStore.invoke()?.let {
                            Timber.tag("FragmentSettingStore").d("postinvalidate: ${it.size}")
                            listOrderStore.clear()
                            listOrderStore.addAll(it)
                            countOrder(it)
                            Log.d("FragmentSettingStore", "getListPost: ${it.size}")
                        }
                    }

                }
            }

            is Loading -> {
                Timber.tag("FragmentSettingStore").e("getPost: loading")
            }

            is Fail -> {
                Timber.tag("FragmentSettingStore").e("getPost: Fail")
            }

            else -> {

            }
        }
    }

    private fun countOrder(listOrder: MutableList<OrderResponse>) {
        countUnconfimred = listOrder.count { it.status == 1 }
        countConfirmed = listOrder.size - countUnconfimred
        views.apply {
            iconOrderConfim.text = countConfirmed.toString()
            iconOrderNotConfim.text = countUnconfimred.toString()
        }
    }
}