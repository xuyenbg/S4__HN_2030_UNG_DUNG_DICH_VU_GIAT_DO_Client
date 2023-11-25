package datn.fpoly.myapplication.ui.fragment.orderStore

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ql_ban_hang.core.BaseFragment
import datn.fpoly.myapplication.R
import datn.fpoly.myapplication.databinding.FragmentOrderStoreBinding


class OrderStoreFragment : BaseFragment<FragmentOrderStoreBinding>() {
    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentOrderStoreBinding.inflate(layoutInflater)

}