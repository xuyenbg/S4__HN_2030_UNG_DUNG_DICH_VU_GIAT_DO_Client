package datn.fpoly.myapplication.ui.fragment.fragmentOrder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ql_ban_hang.core.BaseFragment
import datn.fpoly.myapplication.databinding.FragmentOrderBinding
import datn.fpoly.myapplication.ui.adapter.AdapterViewPage
import datn.fpoly.myapplication.ui.fragment.fragmentOrder.orderCancelled.OrderCancelledFragment
import datn.fpoly.myapplication.ui.fragment.fragmentOrder.orderCompleted.OrderCompletedFragment
import datn.fpoly.myapplication.ui.fragment.fragmentOrder.orderUncomping.OrderUncompingFragment

class FragmentOrder: BaseFragment<FragmentOrderBinding>() {
    private lateinit var adapterVp: AdapterViewPage
    private val listFragment = mutableListOf<Fragment>()
    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentOrderBinding = FragmentOrderBinding.inflate(layoutInflater)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpTabLayoutCustom()

    }
    fun setUpTabLayoutCustom(){
        listFragment.add(0, OrderUncompingFragment())
        listFragment.add(1, OrderCompletedFragment())
        listFragment.add(2, OrderCancelledFragment())
        adapterVp = AdapterViewPage(listFragment, requireActivity())
        views.vp2Order.isUserInputEnabled = false
        views.vp2Order.setCurrentItem(0, true)
        views.viewBgItem1.visibility = View.VISIBLE
        views.viewBgItem2.visibility = View.INVISIBLE
        views.viewBgItem3.visibility = View.INVISIBLE
        views.vp2Order.adapter = adapterVp
        views.itemUpcoming.setOnClickListener {
            views.vp2Order.setCurrentItem(0, true)
            views.viewBgItem1.visibility = View.VISIBLE
            views.viewBgItem2.visibility = View.INVISIBLE
            views.viewBgItem3.visibility = View.INVISIBLE
        }
        views.itemCompleted.setOnClickListener {
            views.vp2Order.setCurrentItem(1, true)
            views.viewBgItem2.visibility = View.VISIBLE
            views.viewBgItem1.visibility = View.INVISIBLE
            views.viewBgItem3.visibility = View.INVISIBLE
        }
        views.itemCancelled.setOnClickListener {
            views.vp2Order.setCurrentItem(2, true)
            views.viewBgItem3.visibility = View.VISIBLE
            views.viewBgItem2.visibility = View.INVISIBLE
            views.viewBgItem1.visibility = View.INVISIBLE
        }
    }
}