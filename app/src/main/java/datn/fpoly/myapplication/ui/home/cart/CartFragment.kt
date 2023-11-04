package datn.fpoly.myapplication.ui.home.cart

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import com.example.ql_ban_hang.core.BaseFragment
import datn.fpoly.myapplication.databinding.FragmentCart2Binding
import datn.fpoly.myapplication.ui.check_out.AdapterItemCart2
import datn.fpoly.myapplication.ui.check_out.CheckOutActivity
import datn.fpoly.myapplication.ui.home.HomeUserViewModel
import datn.fpoly.myapplication.utils.Common.formatCurrency
import timber.log.Timber


class CartFragment :BaseFragment<FragmentCart2Binding>() {
    private val viewModel: HomeUserViewModel by activityViewModel()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getCart().observe(viewLifecycleOwner) {
            Timber.tag("CART").d("observe")
            if (it != null) {
                views.recycleView.adapter = AdapterItemCart2(requireContext(),it.listItem, eventClick = {})
                views.tvQuantity.text = it.listItem.size.toString()
                views.tvPrice.text = it.total?.formatCurrency(null) ?: "-"
            }
        }

        views.btnOrder.setOnClickListener {
            startActivity(Intent(activity,CheckOutActivity::class.java))
        }

        views.toolbar.btnBack.visibility = View.INVISIBLE

        views.recycleView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                // Xử lý sự kiện thay đổi trạng thái scroll (lên, xuống, dừng)
                // newState: RecyclerView.SCROLL_STATE_IDLE (dừng), RecyclerView.SCROLL_STATE_DRAGGING (scroll lên/xuống), RecyclerView.SCROLL_STATE_SETTLING (scroll tự động)
                if(newState == RecyclerView.SCROLL_STATE_IDLE){
                    views.layoutBottomSheet.visibility = View.VISIBLE
                }else{
                    views.layoutBottomSheet.visibility = View.GONE
                }
            }
        })
    }

    override fun invalidate(): Unit = withState(viewModel) {

    }
    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentCart2Binding = FragmentCart2Binding.inflate(layoutInflater)


}