package datn.fpoly.myapplication.ui.homeStore

import com.airbnb.mvrx.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import datn.fpoly.myapplication.core.BaseViewModel
import datn.fpoly.myapplication.data.repository.PostRepo

class HomeStoreViewModel @AssistedInject constructor(
    @Assisted state: HomeStoreState,
    private val responsePost: PostRepo,
) : BaseViewModel<HomeStoreState, HomeStoreViewAction, HomeStoreViewEvent>(state) {
    override fun handle(action: HomeStoreViewAction) {
        when (action) {
            is HomeStoreViewAction.PostStoreActionList -> {
                handlerGetPost()
            }
        }
    }

    private fun handlerGetPost() {
        setState { copy(statePostStore = Loading()) }
        responsePost.getListPost().execute { copy(statePostStore = it) }
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

