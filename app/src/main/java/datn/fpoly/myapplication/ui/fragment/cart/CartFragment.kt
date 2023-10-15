package datn.fpoly.myapplication.ui.fragment.cart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ql_ban_hang.core.BaseFragment
import datn.fpoly.myapplication.R
import datn.fpoly.myapplication.databinding.FragmentCart2Binding


class CartFragment :BaseFragment<FragmentCart2Binding>() {
    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentCart2Binding = FragmentCart2Binding.inflate(layoutInflater)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


}