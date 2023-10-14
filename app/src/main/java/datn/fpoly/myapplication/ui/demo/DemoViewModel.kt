package datn.fpoly.myapplication.ui.demo

import com.airbnb.mvrx.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import datn.fpoly.myapplication.core.BaseViewModel
import datn.fpoly.myapplication.data.repository.AuthRepo


class DemoViewModel @AssistedInject constructor(
    @Assisted state: DemoViewState,
    private val repository: AuthRepo,
): BaseViewModel<DemoViewState,DemoViewAction, DemoViewEvent>(state) {

    override fun handle(action: DemoViewAction) {
        when (action){
            is DemoViewAction.GetListPersonAction -> {
                handListPerson()
            }

            else -> {}
        }
    }

    private fun handListPerson() {
        setState { copy(stateGetListPerson = Loading()) }
        repository.getListPerson().execute { copy(stateGetListPerson = it) }
    }

    @AssistedFactory
    interface Factory {
        fun create(initialState: DemoViewState): DemoViewModel
    }

    companion object : MvRxViewModelFactory<DemoViewModel, DemoViewState> {
        @JvmStatic
        override fun create(
            viewModelContext: ViewModelContext,
            state: DemoViewState
        ): DemoViewModel {
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