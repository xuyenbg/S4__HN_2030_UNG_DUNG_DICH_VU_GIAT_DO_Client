package datn.fpoly.myapplication.ui.fragment.cart

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import com.example.ql_ban_hang.core.BaseFragment
import com.orhanobut.hawk.Hawk
import datn.fpoly.myapplication.AppApplication
import datn.fpoly.myapplication.R
import datn.fpoly.myapplication.data.model.Order
import datn.fpoly.myapplication.data.model.account.AccountResponse
import datn.fpoly.myapplication.data.repository.AuthRepo
import datn.fpoly.myapplication.data.repository.RoomDbRepo
import datn.fpoly.myapplication.databinding.FragmentCart2Binding
import datn.fpoly.myapplication.di.AppComponent
import datn.fpoly.myapplication.ui.fragment.cart.adapter.AdapterItemCart
import datn.fpoly.myapplication.ui.home.HomeUserViewModel
import timber.log.Timber
import javax.inject.Inject


class CartFragment :BaseFragment<FragmentCart2Binding>() {
    lateinit var adapter:AdapterItemCart
    private val viewModel: HomeUserViewModel by activityViewModel()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = AdapterItemCart(context = requireContext())
        views.rcvListOrderCart.adapter = adapter
        viewModel.getCart().observe(viewLifecycleOwner) {
            Timber.tag("CART").d("observe")
            if (it != null) {
                Timber.tag("CART").d(it.toString())
                adapter.setData(it.listItem)
            }
        }

    }

    override fun invalidate(): Unit = withState(viewModel) {

    }
    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentCart2Binding = FragmentCart2Binding.inflate(layoutInflater)


}