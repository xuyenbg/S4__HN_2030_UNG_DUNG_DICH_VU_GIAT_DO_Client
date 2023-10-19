package datn.fpoly.myapplication.ui.fragment.homeUser


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.mvrx.*
import com.example.ql_ban_hang.core.BaseFragment
import datn.fpoly.myapplication.data.model.CategoryModel
import datn.fpoly.myapplication.databinding.FragmentHomeUserBinding
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import com.airbnb.mvrx.activityViewModel
import datn.fpoly.myapplication.data.model.StoreModel
import datn.fpoly.myapplication.ui.adapter.AdapterCategory
import datn.fpoly.myapplication.ui.adapter.AdapterStore
import datn.fpoly.myapplication.ui.home.HomeUserViewModel
import datn.fpoly.myapplication.ui.home.HomeViewAction
import datn.fpoly.myapplication.ui.home.HomeViewState


class HomeUserFragment : BaseFragment<FragmentHomeUserBinding>() {

    private  val viewModel: HomeUserViewModel by activityViewModel()
    private lateinit var adapterCate: AdapterCategory
    private lateinit var adapterStore: AdapterStore
    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeUserBinding = FragmentHomeUserBinding.inflate(layoutInflater)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.handle(HomeViewAction.HomeActionCategory)
        adapterCate = AdapterCategory(6)
        views.rcvListCategory.adapter = adapterCate
        adapterCate.setListener(object : AdapterCategory.CategoryListener {
            override fun onClickCate(categoryModel: CategoryModel) {

            }
        })
        adapterStore = AdapterStore(6)
        views.rcvListStore.adapter = adapterStore
        adapterStore.setListener(object : AdapterStore.StoreListener{
            override fun onClickStoreListener(storeModel: StoreModel) {

            }
        })


    }

    override fun invalidate() : Unit = withState(viewModel){
       getListCate(it)
        getListStore(it)
    }
    fun getListCate(it: HomeViewState){
        when(it.stateCategory){
            is Success->{
                runBlocking {
                    launch {
                        it.stateCategory.invoke()?.let{
                            adapterCate.updateData(it)
                            Log.e("AAAAAAAAAAAAAA", "invalidate: "+it.size )
                        }
                    }

                }
            }
            is Loading->{
                Log.e("AAAAAAAAAAAAAAA", "getListCategory: loading" )
            }
            is Fail->{
                Log.e("AAAAAAAAAAAAAAA", "getListCategory: Fail" )
            }
            else -> {

            }
        }
    }
    fun getListStore(state: HomeViewState){
        when(state.stateStore){
            is Loading->{

            }
            is Success->{
                    runBlocking {
                        launch {
                            state.stateStore.invoke()?.let {
                                adapterStore.setData(it)
                            }
                        }
                    }
            }
            is Fail->{

            }
            else->{

            }
        }

    }





}


