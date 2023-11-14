package datn.fpoly.myapplication.ui.home.cart

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import com.example.ql_ban_hang.core.BaseFragment
import datn.fpoly.myapplication.data.model.Order
import datn.fpoly.myapplication.databinding.FragmentCartBinding
import datn.fpoly.myapplication.ui.check_out.AdapterItemCart2
import datn.fpoly.myapplication.ui.check_out.CheckOutActivity
import datn.fpoly.myapplication.ui.home.HomeUserViewModel
import datn.fpoly.myapplication.utils.Common.formatCurrency
import timber.log.Timber


class CartFragment :BaseFragment<FragmentCartBinding>() {
    private val viewModel: HomeUserViewModel by activityViewModel()
    var cart: Order? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getCart().observe(viewLifecycleOwner) {
            Timber.tag("CART").d("observe ${it.toString()}")
            if (it != null && it.listItem.isNotEmpty()) {
                cart = it
                views.layoutCartEmpty.root.visibility = View.GONE
                views.layoutBottomSheet.visibility = View.VISIBLE
                views.recycleView.visibility = View.VISIBLE
                views.recycleView.adapter = AdapterItemCart2(requireContext(),it.listItem, eventClick = {})
                views.tvQuantity.text = it.listItem.size.toString()
                views.tvPrice.text = it.total?.formatCurrency(null) ?: "-"
            }else{
                views.layoutCartEmpty.root.visibility = View.VISIBLE
                views.layoutBottomSheet.visibility = View.GONE
                views.recycleView.visibility = View.GONE
            }
        }

        views.btnOrder.setOnClickListener {
            startActivity(Intent(activity,CheckOutActivity::class.java))
        }

        views.toolbar.btnBack.visibility = View.INVISIBLE

        views.toolbar.title.text = "GIỎ HÀNG"

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

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean { return false }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                cart!!.listItem.removeAt(viewHolder.layoutPosition)
                viewModel.updateCart(cart!!)
            }
        }
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(views.recycleView)
    }

    override fun invalidate(): Unit = withState(viewModel) {

    }
    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentCartBinding = FragmentCartBinding.inflate(layoutInflater)


}