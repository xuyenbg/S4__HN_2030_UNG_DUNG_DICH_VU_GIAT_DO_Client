package datn.fpoly.myapplication.ui.dashboard

import com.airbnb.mvrx.ActivityViewModelContext
import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import datn.fpoly.myapplication.core.BaseViewModel
import datn.fpoly.myapplication.data.repository.AuthRepo

class DashbroardViewModel @AssistedInject constructor(
    @Assisted state: DashboardViewState,
    private val authRepo: AuthRepo
): BaseViewModel<DashboardViewState, DashboardViewAction, DashbroardViewEvent>(state) {
    override fun handle(action: DashboardViewAction) {
        when (action){
            else -> {

            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(initialState: DashboardViewState): DashbroardViewModel
    }

    companion object : MvRxViewModelFactory<DashbroardViewModel, DashboardViewState> {
        @JvmStatic
        override fun create(
            viewModelContext: ViewModelContext,
            state: DashboardViewState
        ): DashbroardViewModel {
            val factory =
                when (viewModelContext) {
                    is FragmentViewModelContext -> viewModelContext.fragment as? DashbroardViewModel.Factory
                    is ActivityViewModelContext -> viewModelContext.activity as? DashbroardViewModel.Factory
                }
            return factory?.create(state)
                ?: error("You should let your activity/fragment implements Factory interface")
        }
    }


}