package datn.fpoly.myapplication.ui.fragment.fragmentOrder.orderCompleted

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ql_ban_hang.core.BaseFragment
import datn.fpoly.myapplication.R
import datn.fpoly.myapplication.databinding.FragmentOrderCompletedBinding

class OrderCompletedFragment : BaseFragment<FragmentOrderCompletedBinding>() {

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentOrderCompletedBinding = FragmentOrderCompletedBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}