package datn.fpoly.myapplication.ui.searchService

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import com.airbnb.mvrx.*
import datn.fpoly.myapplication.AppApplication
import datn.fpoly.myapplication.R
import datn.fpoly.myapplication.core.BaseActivity
import datn.fpoly.myapplication.data.model.ServiceExtend
import datn.fpoly.myapplication.databinding.ActivitySearchServiceBinding
import datn.fpoly.myapplication.ui.adapter.AdapterService
import datn.fpoly.myapplication.ui.service.DetailServiceActivity
import datn.fpoly.myapplication.utils.Common
import datn.fpoly.myapplication.utils.Dialog_Loading
import timber.log.Timber
import javax.inject.Inject

class SearchServiceActivity : BaseActivity<ActivitySearchServiceBinding>(), SearchServiceViewModel.Factory {
    @Inject
    lateinit var factory: SearchServiceViewModel.Factory

    private val viewModel: SearchServiceViewModel by viewModel()

    private lateinit var adapterService: AdapterService
    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as AppApplication).appComponent.inject(this);
        super.onCreate(savedInstanceState)
        views.tvNoti.visibility=View.GONE
        views.imgNoti.visibility=View.GONE
        views.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        views.btnClear.setOnClickListener {
            views.edSearch.setText("")
        }
        views.edSearch.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0 != null) {
                    if(p0.isEmpty()){
                        views.btnClear.visibility= View.INVISIBLE
                    }else{
                        views.btnClear.visibility= View.VISIBLE
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
        views.edSearch.setOnFocusChangeListener { view, b ->
            if(!b){
                Common.hideKeyboard(this, views.edSearch)
            }
        }
        views.edSearch.setOnEditorActionListener { textView, i, keyEvent ->
            if(i==EditorInfo.IME_ACTION_SEARCH || keyEvent.keyCode == KeyEvent.KEYCODE_ENTER){
                Common.hideKeyboard(this@SearchServiceActivity, views.edSearch)
                viewModel.handle(SearchServiceViewAction.SearchService(views.edSearch.text.toString().trim()))
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
        adapterService = AdapterService(false, false)
        views.rcvListSearch.adapter= adapterService
        adapterService.setListenner(object : AdapterService.ServiceListenner{
            override fun ServiceOnClick(item: ServiceExtend, position: Int) {
                val intent = Intent(this@SearchServiceActivity, DetailServiceActivity::class.java)
                intent.putExtra(Common.KEY_ID_SERVICE, item.id)
                startActivity(intent)
            }

            override fun EditService(serviceExtend: ServiceExtend) {

            }
        })
        views.btnSearch.setOnClickListener {
            Common.hideKeyboard(this@SearchServiceActivity, views.edSearch)
            viewModel.handle(SearchServiceViewAction.SearchService(views.edSearch.text.toString().trim()))
        }
        viewModel.subscribe(this){
            updateStateSearch(it)
        }

    }

    private fun updateStateSearch(state: SearchServiceViewState){
        when(state.stateSearchService){
            is Loading->{
                views.shimmer.visibility=View.VISIBLE
                views.shimmer.startShimmer()
                Timber.e("loading search")

            }
            is Success->{
                Timber.e("Success search")
                state.stateSearchService.invoke()?.let {
                    views.shimmer.visibility=View.GONE
                    adapterService.setData(it)
                    if(it.size!=0){
                        views.tvNoti.visibility=View.GONE
                        views.imgNoti.visibility=View.GONE
                        views.rcvListSearch.visibility=View.VISIBLE
                    }else{
                        views.tvNoti.visibility=View.VISIBLE
                        views.imgNoti.visibility=View.VISIBLE
                        views.rcvListSearch.visibility=View.GONE
                    }
                }
                state.stateSearchService=Uninitialized


            }
            is Fail->{
                views.shimmer.visibility=View.GONE
                state.stateSearchService=Uninitialized
                Timber.e("Fail search")

            }
            else ->{}
        }
    }

    override fun getBinding(): ActivitySearchServiceBinding {
        return ActivitySearchServiceBinding.inflate(layoutInflater)
    }

    override fun create(initialState: SearchServiceViewState): SearchServiceViewModel {
       return factory.create(initialState)
    }
}