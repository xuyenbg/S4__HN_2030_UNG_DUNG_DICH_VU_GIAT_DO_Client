package datn.fpoly.myapplication.ui.fragment.fragmentOrder.orderCancelled

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import com.example.ql_ban_hang.core.BaseFragment
import datn.fpoly.myapplication.databinding.FragmentOrderCancelledBinding
import datn.fpoly.myapplication.ui.home.HomeUserViewModel

class OrderCancelledFragment : BaseFragment<FragmentOrderCancelledBinding>() {
    private val viewModel: HomeUserViewModel by activityViewModel()
    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentOrderCancelledBinding = FragmentOrderCancelledBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun invalidate() : Unit = withState(viewModel){

    }



}