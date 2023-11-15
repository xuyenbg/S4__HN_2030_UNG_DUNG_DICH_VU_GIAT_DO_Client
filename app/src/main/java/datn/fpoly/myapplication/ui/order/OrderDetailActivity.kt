package datn.fpoly.myapplication.ui.order


import android.os.Bundle
import com.airbnb.mvrx.viewModel
import datn.fpoly.myapplication.AppApplication
import datn.fpoly.myapplication.core.BaseActivity
import datn.fpoly.myapplication.databinding.ActivityOrderDetailBinding
import javax.inject.Inject

class OrderDetailActivity : BaseActivity<ActivityOrderDetailBinding>(), OrderViewModel.Factory {
    @Inject
    lateinit var addPosFactory: OrderViewModel.Factory

    private val viewModel: OrderViewModel by viewModel()

    override fun getBinding(): ActivityOrderDetailBinding {
        return ActivityOrderDetailBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as AppApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(views.root)
    }

    override fun initUiAndData() {
        super.initUiAndData()

    }

    private fun updateWithState(state: OrderViewState) {

    }

    override fun create(initialState: OrderViewState) = addPosFactory.create(initialState)


}