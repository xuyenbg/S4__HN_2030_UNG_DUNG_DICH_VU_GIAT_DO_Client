package datn.fpoly.myapplication.ui.editpost

import com.airbnb.mvrx.ActivityViewModelContext
import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import datn.fpoly.myapplication.core.BaseViewModel
import datn.fpoly.myapplication.data.repository.PostRepo
import okhttp3.MultipartBody
import okhttp3.RequestBody

class EditPostViewModel @AssistedInject constructor(
    @Assisted state: EditPostViewState,
    private val repo: PostRepo
) : BaseViewModel<EditPostViewState, EditPostViewAction, EditPostViewEvent>(state) {
    override fun handle(action: EditPostViewAction) {
        when (action) {
            is EditPostViewAction.EditPostAction -> {
                handleEditPost(
                    action.idPost,
                    action.title,
                    action.content,
                    action.image
                )
            }

            else -> {}
        }
    }

    private fun handleEditPost(
        idPost: String,
        title: RequestBody,
        content: RequestBody,
        image: MultipartBody.Part?
    ) {
        setState { copy(stateEditPost = Loading()) }
        repo.editPost(
            idPost,
            title, content, image
        ).execute { copy(stateEditPost = it) }
    }

    @AssistedFactory
    interface Factory {
        fun create(initialState: EditPostViewState): EditPostViewModel
    }

    companion object : MvRxViewModelFactory<EditPostViewModel, EditPostViewState> {
        @JvmStatic
        override fun create(
            viewModelContext: ViewModelContext,
            state: EditPostViewState
        ): EditPostViewModel {
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