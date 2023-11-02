package datn.fpoly.myapplication.ui.check_out


import android.os.Bundle
import com.airbnb.mvrx.viewModel
import datn.fpoly.myapplication.AppApplication
import datn.fpoly.myapplication.core.BaseActivity
import datn.fpoly.myapplication.databinding.ActivityCheckOutBinding
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
    }

    private fun updateWithState(state: CheckOutViewState) {

    }

    override fun create(initialState: CheckOutViewState) = addPosFactory.create(initialState)


}