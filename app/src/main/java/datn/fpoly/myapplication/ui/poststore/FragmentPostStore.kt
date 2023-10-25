package datn.fpoly.myapplication.ui.poststore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ql_ban_hang.core.BaseFragment
import datn.fpoly.myapplication.R
import datn.fpoly.myapplication.databinding.ActivityPostStoreBinding

class FragmentPostStore : BaseFragment<ActivityPostStoreBinding>() {
    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): ActivityPostStoreBinding {
        return ActivityPostStoreBinding.inflate(layoutInflater)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}