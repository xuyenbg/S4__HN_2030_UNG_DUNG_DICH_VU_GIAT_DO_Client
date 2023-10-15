package datn.fpoly.myapplication.ui.fragment.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ql_ban_hang.core.BaseFragment
import datn.fpoly.myapplication.databinding.FragmentProfileUserBinding

class FragmentSetting: BaseFragment<FragmentProfileUserBinding>() {
    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentProfileUserBinding = FragmentProfileUserBinding.inflate(layoutInflater)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}