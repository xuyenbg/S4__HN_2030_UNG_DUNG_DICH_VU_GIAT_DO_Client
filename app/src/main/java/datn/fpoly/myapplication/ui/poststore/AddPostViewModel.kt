package datn.fpoly.myapplication.ui.poststore

import com.airbnb.mvrx.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import datn.fpoly.myapplication.core.BaseViewModel
import datn.fpoly.myapplication.data.repository.PostRepo
import okhttp3.MultipartBody
import okhttp3.RequestBody


class AddPostViewModel @AssistedInject constructor(
    @Assisted state: AddPostViewState,
    private val repository: PostRepo,
) : BaseViewModel<AddPostViewState, AddPostViewAction, AddPostViewEvent>(state) {

    override fun handle(action: AddPostViewAction) {
        when (action) {
            is AddPostViewAction.AddPostAction -> {
                handleAddPost(action.idStore, action.title, action.content, action.image)
            }
            else -> {}
        }
    }

    private fun handleAddPost(
        idStore: RequestBody,
        title: RequestBody,
        content: RequestBody,
        image: MultipartBody.Part?
    ) {
        setState { copy(stateAddPost = Loading()) }
            repository.addListPost(idStore, title, content,image
            ).execute { copy(stateAddPost = it) }
    }

    @AssistedFactory
    interface Factory {
        fun create(initialState: AddPostViewState): AddPostViewModel
    }

    companion object : MvRxViewModelFactory<AddPostViewModel, AddPostViewState> {
        @JvmStatic
        override fun create(
            viewModelContext: ViewModelContext,
            state: AddPostViewState
        ): AddPostViewModel {
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