package datn.fpoly.myapplication.ui.fragment.fragmentOrder.orderCompleted

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import com.example.ql_ban_hang.core.BaseFragment
import datn.fpoly.myapplication.R
import datn.fpoly.myapplication.databinding.FragmentOrderCompletedBinding
import datn.fpoly.myapplication.ui.home.HomeUserViewModel

class OrderCompletedFragment : BaseFragment<FragmentOrderCompletedBinding>() {
    private val viewModel: HomeUserViewModel by activityViewModel()
    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentOrderCompletedBinding = FragmentOrderCompletedBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun invalidate() : Unit = withState(viewModel){

    }
}