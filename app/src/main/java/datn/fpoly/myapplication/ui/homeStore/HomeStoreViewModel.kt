package datn.fpoly.myapplication.ui.homeStore

import com.airbnb.mvrx.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import datn.fpoly.myapplication.core.BaseViewModel
import datn.fpoly.myapplication.data.repository.CategoryRepo
import datn.fpoly.myapplication.data.repository.PostRepo
import datn.fpoly.myapplication.data.repository.StoreRepo

class HomeStoreViewModel @AssistedInject constructor(
    @Assisted state: HomeStoreState,
    private val responsePost: PostRepo,
    private val resposeCate: CategoryRepo,
    private val resposeStore: StoreRepo
) : BaseViewModel<HomeStoreState, HomeStoreViewAction, HomeStoreViewEvent>(state) {
    override fun handle(action: HomeStoreViewAction) {
        when (action) {
            is HomeStoreViewAction.PostStoreActionList -> {
                handlerGetPost()
            }
            is HomeStoreViewAction.GetListCategory->{
                getListCate()
            }
            is HomeStoreViewAction.GetStoreByIdUser->{
                getStoreByIdUser(action.idUser)
            }
        }
    }

    private fun handlerGetPost() {
        setState { copy(statePostStore = Loading()) }
        responsePost.getListPost().execute { copy(statePostStore = it) }
    }
    private fun getListCate(){
        setState { copy(stateCate = Loading()) }
        resposeCate.getDataCategory().execute { copy(stateCate= it) }
    }
    private fun getStoreByIdUser(idUser: String){
        setState { copy(stateGetStore = Loading()) }
        resposeStore.getStoreByIdUser(idUser).execute { copy(stateGetStore= it) }
    }

    @AssistedFactory
    interface Factory {
        fun create(initialState: HomeStoreState): HomeStoreViewModel
    }

    companion object : MvRxViewModelFactory<HomeStoreViewModel, HomeStoreState> {
        @JvmStatic
        override fun create(
            viewModelContext: ViewModelContext,
            state: HomeStoreState
        ): HomeStoreViewModel {
            val factory =
                when (viewModelContext) {
                    is FragmentViewModelContext -> viewModelContext.fragment as? Factory
                    is ActivityViewModelContext -> viewModelContext.activity as? Factory
                }
            return factory?.create(state)
                ?: error("You should let your activity/fragment implements Factory interface")
        }
    }
}
