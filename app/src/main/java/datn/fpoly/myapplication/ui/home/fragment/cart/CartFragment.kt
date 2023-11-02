package datn.fpoly.myapplication.ui.home.fragment.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import com.example.ql_ban_hang.core.BaseFragment
import datn.fpoly.myapplication.databinding.FragmentCart2Binding
import datn.fpoly.myapplication.ui.home.fragment.cart.adapter.AdapterItemCart
import datn.fpoly.myapplication.ui.home.HomeUserViewModel
import datn.fpoly.myapplication.utils.Common.formatCurrency
import timber.log.Timber


class CartFragment :BaseFragment<FragmentCart2Binding>() {
    lateinit var adapter: AdapterItemCart
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
                views.tvQuantity.text = it.listItem.size.toString()
                views.tvPrice.text = it.total?.formatCurrency(null) ?: "0"
            }
        }

    }

    override fun invalidate(): Unit = withState(viewModel) {

    }
    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentCart2Binding = FragmentCart2Binding.inflate(layoutInflater)


}