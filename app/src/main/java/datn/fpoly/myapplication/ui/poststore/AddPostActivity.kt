package datn.fpoly.myapplication.ui.poststore


import android.os.Bundle
import com.airbnb.mvrx.viewModel
import datn.fpoly.myapplication.AppApplication
import datn.fpoly.myapplication.core.BaseActivity
import datn.fpoly.myapplication.databinding.ActivityAddPostBinding
import javax.inject.Inject

class AddPostActivity : BaseActivity<ActivityAddPostBinding>(), AddPostViewModel.Factory {
    @Inject
    lateinit var addPosFactory: AddPostViewModel.Factory

    private val viewModel: AddPostViewModel by viewModel()
    override fun getBinding(): ActivityAddPostBinding {
        return ActivityAddPostBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as AppApplication).appComponent.inject(this);
        super.onCreate(savedInstanceState)
        setContentView(views.root)
    }

    override fun initUiAndData() {
        super.initUiAndData()
        views.btnPost.setOnClickListener {
            addPost()
        }
    }

    private fun addPost() {
//        addPostViewModel.handle(AddPostViewAction.AddPostAction())
    }

    override fun create(initialState: AddPostViewState) = addPosFactory.create(initialState)


}