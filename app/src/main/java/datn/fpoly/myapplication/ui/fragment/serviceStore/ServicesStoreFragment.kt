package datn.fpoly.myapplication.ui.fragment.serviceStore

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.mvrx.activityViewModel
import com.example.ql_ban_hang.core.BaseFragment
import datn.fpoly.myapplication.R
import datn.fpoly.myapplication.databinding.FragmentServicesStoreBinding
import datn.fpoly.myapplication.ui.homeStore.HomeStoreViewModel
import datn.fpoly.myapplication.ui.postService.AddServiceActivity


class ServicesStoreFragment : BaseFragment<FragmentServicesStoreBinding>() {
    private val viewModel: HomeStoreViewModel by activityViewModel()
    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentServicesStoreBinding {
        return FragmentServicesStoreBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        views.btnAddService.setOnClickListener {
            requireContext().startActivity(Intent(requireContext(), AddServiceActivity::class.java))
        }
    }

}