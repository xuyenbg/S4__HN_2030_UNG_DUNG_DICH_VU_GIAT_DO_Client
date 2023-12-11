package datn.fpoly.myapplication.ui.fragment.orderStore

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import com.orhanobut.hawk.Hawk
import datn.fpoly.myapplication.core.BaseFragment
import datn.fpoly.myapplication.data.model.StoreModel
import datn.fpoly.myapplication.data.model.orderList.OrderResponse

import datn.fpoly.myapplication.databinding.FragmentOrderStoreBinding
import datn.fpoly.myapplication.ui.fragment.orderStore.adapter.OrderStoreAdapter
import datn.fpoly.myapplication.ui.home.HomeViewAction
import datn.fpoly.myapplication.ui.home.HomeViewState
import datn.fpoly.myapplication.ui.homeStore.HomeStoreState
import datn.fpoly.myapplication.ui.homeStore.HomeStoreViewAction
import datn.fpoly.myapplication.ui.homeStore.HomeStoreViewModel
import datn.fpoly.myapplication.ui.order.OrderDetailStoreActivity
import datn.fpoly.myapplication.utils.Common
import datn.fpoly.myapplication.utils.ItemSpacingDecoration
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class OrderStoreFragment : BaseFragment<FragmentOrderStoreBinding>() {

    private val viewModel: HomeStoreViewModel by activityViewModel()
    private lateinit var calendar: Calendar
    private lateinit var orderStoreAdapter: OrderStoreAdapter
    private var idStore = ""
    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentOrderStoreBinding {
        return FragmentOrderStoreBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        idStore = Hawk.get<StoreModel>(Common.KEY_STORE).id.toString()
        idStore?.let { HomeStoreViewAction.GetDataOrderStore(it, "desc") }
            ?.let { viewModel.handle(it) }
        calendar = Calendar.getInstance()
        initView()
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        views.btnEndDate.isEnabled= false
        views.btnStartDate.setOnClickListener {
            DatePickerDialog(
                requireContext(), { datePicker, i, i2, i3 ->
                    views.tvDateStart.text = "$i-${i2+1}-$i3"
                    views.btnEndDate.isEnabled =true
                }, year, month, day
            ).show()
        }
        views.btnEndDate.setOnClickListener {
           val datePick= DatePickerDialog(
                requireContext(), { datePicker, i, i2, i3 ->
                    views.tvDateEnd.text = "$i-${i2+1}-$i3"
                }, year, month, day
            )
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            datePick.datePicker.minDate= simpleDateFormat.parse(views.tvDateStart.text.toString().trim()).time
            datePick.show()
        }
        views.apply {

            btnFilter.setOnClickListener {
                if (tvDateStart.text.isEmpty() || tvDateEnd.text.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        "Vui lòng không để trống ngày bắt đầu và ngày kết thúc",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    viewModel.handle(
                        HomeStoreViewAction.FilterOrder(
                            idStore,
                            tvDateStart.text.toString(),
                            tvDateEnd.text.toString(),
                            4
                        )
                    )
                }
            }

        }

        views.icClear.setOnClickListener {
            views.edSearch.setText("")
        }
        orderStoreAdapter = OrderStoreAdapter(onBtnAction = {
            val intent = Intent(requireContext(), OrderDetailStoreActivity::class.java)
            intent.putExtra(Common.KEY_ID_ORDER, it.id)
            intent.putExtra("store", true)
            requireContext().startActivity(intent)
        }, itemOnclick = {})
        views.listOrderStore.adapter = orderStoreAdapter
        views.listOrderStore.addItemDecoration(ItemSpacingDecoration(32))

        views.edSearch.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    Log.d("onTextChanged", "onTextChanged: $p0")
//                    orderStoreAdapter.filter.filter(p0.toString())
                    orderStoreAdapter.filter(p0.toString())
                }

                override fun afterTextChanged(p0: Editable?) {

                }
            }
        )


    }

    override fun invalidate(): Unit = withState(viewModel) {
        super.invalidate()
        getListOrder(it)
        getFilterOrder(it)
    }

    private fun getListOrder(it: HomeStoreState) {
        when (it.stateGetOrderStore) {
            is Success -> {
                runBlocking {
                    launch {
                        it.stateGetOrderStore.invoke()?.let {

                            val allListOrder = mutableListOf<OrderResponse>()
                            if (it.filter { it.status == 4 }.isNullOrEmpty()) {
                                views.layoutCartEmpty.root.visibility = View.VISIBLE
                                views.layoutCartEmpty.tvTitle.text = "Không có đơn hàng nào"
                            } else {
                                views.layoutCartEmpty.root.visibility = View.GONE
                            }
                            allListOrder.addAll(it.filter { it.status == 4 })
                            orderStoreAdapter.updateData(allListOrder)
                        }
                    }

                }
            }

            is Loading -> {
                Timber.tag("AAAAAAAAAAAAAAA").e("getPost: loading")
            }

            is Fail -> {
                Timber.tag("AAAAAAAAAAAAAAA").e("getPost: Fail")
            }

            else -> {

            }
        }
    }

    private fun getFilterOrder(it: HomeStoreState) {
        when (it.stateFilterOrder) {
            is Success -> {
                runBlocking {
                    launch {
                        it.stateFilterOrder.invoke()?.let {
                            if (it.isEmpty()) {
                                views.layoutCartEmpty.root.visibility = View.VISIBLE
                                views.layoutCartEmpty.tvTitle.text = "Không có đơn hàng nào"
                            } else {
                                views.layoutCartEmpty.root.visibility = View.GONE
                            }
                            orderStoreAdapter.updateData(it)
                        }
                    }

                }
            }

            is Loading -> {
                Timber.tag("AAAAAAAAAAAAAAA").e("getPost: loading")
            }

            is Fail -> {
                Timber.tag("AAAAAAAAAAAAAAA").e("getPost: Fail")
            }

            else -> {

            }
        }
    }
}