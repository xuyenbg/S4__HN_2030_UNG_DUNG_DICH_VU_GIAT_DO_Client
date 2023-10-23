package datn.fpoly.myapplication.ui.fragment.fragmentOrder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ql_ban_hang.core.BaseFragment
import datn.fpoly.myapplication.databinding.FragmentOrderBinding

class FragmentOrder: BaseFragment<FragmentOrderBinding>() {
    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentOrderBinding = FragmentOrderBinding.inflate(layoutInflater)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}