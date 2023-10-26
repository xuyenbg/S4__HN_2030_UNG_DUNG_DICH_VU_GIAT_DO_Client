package datn.fpoly.myapplication.ui.fragment.cart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import com.example.ql_ban_hang.core.BaseFragment
import com.orhanobut.hawk.Hawk
import datn.fpoly.myapplication.R
import datn.fpoly.myapplication.data.model.Order
import datn.fpoly.myapplication.data.model.account.AccountResponse
import datn.fpoly.myapplication.data.repository.AuthRepo
import datn.fpoly.myapplication.data.repository.RoomDbRepo
import datn.fpoly.myapplication.databinding.FragmentCart2Binding
import datn.fpoly.myapplication.ui.home.HomeUserViewModel
import javax.inject.Inject


class CartFragment :BaseFragment<FragmentCart2Binding>() {
    @Inject
    lateinit var dbRepo: RoomDbRepo
    @Inject
    lateinit var authRepo: AuthRepo
    private var cart: Order? = null
    private val viewModel: HomeUserViewModel by activityViewModel()
    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentCart2Binding = FragmentCart2Binding.inflate(layoutInflater)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cart = dbRepo.getCart(key = authRepo.getUser()!!._id!!)


    }

    override fun invalidate(): Unit = withState(viewModel) {

    }


}