package datn.fpoly.myapplication.ui.check_out


import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.airbnb.mvrx.viewModel
import datn.fpoly.myapplication.AppApplication
import datn.fpoly.myapplication.core.BaseActivity
import datn.fpoly.myapplication.databinding.ActivityCheckOutBinding
import datn.fpoly.myapplication.utils.Common.formatCurrency
import javax.inject.Inject

class CheckOutActivity : BaseActivity<ActivityCheckOutBinding>(), CheckOutViewModel.Factory {
    @Inject
    lateinit var addPosFactory: CheckOutViewModel.Factory

    private val viewModel: CheckOutViewModel by viewModel()

    override fun getBinding(): ActivityCheckOutBinding {
        return ActivityCheckOutBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as AppApplication).appComponent.inject(this);
        super.onCreate(savedInstanceState)
        setContentView(views.root)
    }

    override fun initUiAndData() {
        super.initUiAndData()
        viewModel.subscribe(this) {
            updateWithState(it)
        }
        viewModel.getCart().observe(this, Observer {
            if (it != null && it.listItem.isNotEmpty()) {
                views.total.text = it.total?.formatCurrency(null) ?: "- đ"
                views.recyclerView.adapter = AdapterItemCart2(this, it.listItem, eventClick = {})
            }
        })
        views.toolbar.title.text = "ĐẶT ĐƠN"
        views.toolbar.btnBack.setOnClickListener {
            this.finish()
        }
        views.toolbar.btnNotification.visibility = View.INVISIBLE
        views.btnAction.text = "Xác nhận"
        views.btnAction.setOnClickListener {

        }
    }

    private fun updateWithState(state: CheckOutViewState) {

    }

    override fun create(initialState: CheckOutViewState) = addPosFactory.create(initialState)


}