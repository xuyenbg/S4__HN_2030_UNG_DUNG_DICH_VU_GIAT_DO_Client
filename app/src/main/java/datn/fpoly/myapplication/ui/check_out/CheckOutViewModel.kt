package datn.fpoly.myapplication.ui.check_out

import com.airbnb.mvrx.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import datn.fpoly.myapplication.core.BaseViewModel
import datn.fpoly.myapplication.data.repository.CategoryRepo
import datn.fpoly.myapplication.data.repository.PostRepo
import datn.fpoly.myapplication.data.repository.RoomDbRepo
import datn.fpoly.myapplication.data.repository.StoreRepo

class CheckOutViewModel @AssistedInject constructor(
    @Assisted state: CheckOutViewState,
    private val responsePost: PostRepo,
    private val responseCategory: CategoryRepo,
    private val responseStore: StoreRepo,
    private val dbRepo: RoomDbRepo
) : BaseViewModel<CheckOutViewState, CheckOutViewAction, CheckOutViewEvent>(state) {
    override fun handle(action: CheckOutViewAction) {

    }

    @AssistedFactory
    interface Factory {
        fun create(initialState: CheckOutViewState): CheckOutViewModel
    }

    companion object : MvRxViewModelFactory<CheckOutViewModel, CheckOutViewState> {
        @JvmStatic
        override fun create(
            viewModelContext: ViewModelContext,
            state: CheckOutViewState
        ): CheckOutViewModel {
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

