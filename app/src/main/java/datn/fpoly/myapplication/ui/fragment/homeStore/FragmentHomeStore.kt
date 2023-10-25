package datn.fpoly.myapplication.ui.fragment.homeStore

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.ql_ban_hang.core.BaseFragment
import datn.fpoly.myapplication.databinding.FragmentHomeLaundryBinding

class FragmentHomeStore: BaseFragment<FragmentHomeLaundryBinding>() {
    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeLaundryBinding {
        return FragmentHomeLaundryBinding.inflate(layoutInflater)
    }

}