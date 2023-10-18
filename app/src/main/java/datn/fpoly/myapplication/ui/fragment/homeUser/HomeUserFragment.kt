package datn.fpoly.myapplication.ui.fragment.homeUser


import android.os.Bundle
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
import datn.fpoly.myapplication.ui.home.HomeUserViewModel
import datn.fpoly.myapplication.ui.home.HomeViewState


class HomeUserFragment : BaseFragment<FragmentHomeUserBinding>() {

    private  var viewModel: HomeUserViewModel by activityViewModel()
    private lateinit var state: HomeViewState
    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeUserBinding = FragmentHomeUserBinding.inflate(layoutInflater)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.subscribe(this){
            runBlocking {
               launch {
                   state  = it
               }
            }
        }
        val list = getListCategory(state)
    }
    fun getListCategory(state: HomeViewState) : MutableList<CategoryModel>{
        val listCast = mutableListOf<CategoryModel>()
        when(state.stateCategory){
            is Success->{
                runBlocking {
                    state.stateCategory.invoke()?.let{
                        listCast.clear()
                        listCast.addAll(it)
                    }
                }
            }
            is Loading->{

            }
            is Fail->{

            }
            else -> {

            }
        }
        return listCast
    }


}


