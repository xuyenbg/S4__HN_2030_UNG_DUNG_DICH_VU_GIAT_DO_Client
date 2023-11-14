package datn.fpoly.myapplication.ui.fragment.settingStore

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import com.example.ql_ban_hang.core.BaseFragment
import com.google.firebase.auth.FirebaseAuth
import com.orhanobut.hawk.Hawk
import datn.fpoly.myapplication.data.model.StoreModel
import datn.fpoly.myapplication.data.model.orderList.OrderResponse
import datn.fpoly.myapplication.databinding.FragmentProfileStoreBinding
import datn.fpoly.myapplication.ui.historystore.HistoryStoreActivity
import datn.fpoly.myapplication.ui.home.HomeViewState
import datn.fpoly.myapplication.ui.homeStore.HomeStoreState
import datn.fpoly.myapplication.ui.homeStore.HomeStoreViewAction
import datn.fpoly.myapplication.ui.homeStore.HomeStoreViewModel
import datn.fpoly.myapplication.ui.login.SignInActivity
import datn.fpoly.myapplication.utils.Common
import datn.fpoly.myapplication.utils.DataRaw
import datn.fpoly.myapplication.utils.Dialog_Loading
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber

class FragmentSettingStore : BaseFragment<FragmentProfileStoreBinding>() {
    private val viewModel: HomeStoreViewModel by activityViewModel()
    private val listOrderStore = arrayListOf<OrderResponse>()
    private var countUnconfimred = 0
    private var countConfirmed = 0
    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentProfileStoreBinding = FragmentProfileStoreBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val idStore = Hawk.get<StoreModel>(Common.KEY_STORE).id

        viewModel.handle(HomeStoreViewAction.GetDataOrderStore(idStore!!, "desc"))
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
        views.tvOrderHistory.setOnClickListener {
            val intent = Intent(requireContext(), HistoryStoreActivity::class.java)
            intent.putExtra("KEY_HIS", listOrderStore)
            Log.d("FragmentSettingStore", "onViewCreated: $listOrderStore")

            startActivity(intent)
//            DataRaw.animStart(views.tvOrderHistory, requireContext())
        }
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
        countConfirmed = listOrder.size-countUnconfimred
        views.apply {
            iconOrderConfim.text = countConfirmed.toString()
            iconOrderNotConfim.text = countUnconfimred.toString()
        }
    }
}