package datn.fpoly.myapplication.ui.fragment.fragmentOrder.orderUncomping

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import com.example.ql_ban_hang.core.BaseFragment
import datn.fpoly.myapplication.R
import datn.fpoly.myapplication.databinding.FragmentOrderUncompingBinding
import datn.fpoly.myapplication.ui.home.HomeUserViewModel


class OrderUncompingFragment : BaseFragment<FragmentOrderUncompingBinding>() {
    private val viewModel: HomeUserViewModel by activityViewModel()
    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentOrderUncompingBinding = FragmentOrderUncompingBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun invalidate() : Unit = withState(viewModel){

    }


}