package datn.fpoly.myapplication.ui.fragment.settingStore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ql_ban_hang.core.BaseFragment
import datn.fpoly.myapplication.databinding.FragmentProfileStoreBinding

class FragmentSettingStore: BaseFragment<FragmentProfileStoreBinding>() {
    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentProfileStoreBinding = FragmentProfileStoreBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}