package datn.fpoly.myapplication.ui.demo

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.viewModel
import datn.fpoly.myapplication.AppApplication
import datn.fpoly.myapplication.R
import datn.fpoly.myapplication.core.BaseActivity
import datn.fpoly.myapplication.databinding.ActivityDemoBinding
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class DemoActivity : BaseActivity<ActivityDemoBinding>(), DemoViewModel.Factory {
    @Inject
    lateinit var demoViewModelFactory: DemoViewModel.Factory
    private val viewModel: DemoViewModel by viewModel()
    private lateinit var adapter:ArrayAdapter<String>;
    private var listString:List<String> = arrayListOf();
    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as AppApplication).appComponent.inject(this);
        super.onCreate(savedInstanceState)
        setContentView(views.root)
    }

    override fun initUiAndData() {
        super.initUiAndData()
        views.progressCircular.visibility = View.GONE
        adapter = ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, listString);
        views.recyclerView.adapter = adapter
        views.button.setOnClickListener {
            viewModel.handle(DemoViewAction.GetListPersonAction)
        }
        viewModel.subscribe(this){
            updateWithState(it)
        }

    }

    private fun updateWithState(state: DemoViewState) {
        when (state.stateGetListPerson) {
            is Success -> {
                views.progressCircular.visibility = View.GONE
                runBlocking {
                    launch {
                        state.stateGetListPerson.invoke()?.let{ listPerson ->
                            listString = listPerson.map { person ->  person.toString() }.toList()
                            adapter.notifyDataSetChanged()
                        }
                    }
                }
            }
            is Loading ->{
                //Xoay tròn indicate
                views.progressCircular.visibility = View.VISIBLE
            }

            is Fail -> {
                views.progressCircular.visibility = View.GONE
                Toast.makeText(this, "Lỗi", Toast.LENGTH_SHORT).show()
            }

            else -> {

            }
        }
    }

    override fun getBinding() = ActivityDemoBinding.inflate(layoutInflater)

    override fun create(initialState: DemoViewState) = demoViewModelFactory.create(initialState)

}