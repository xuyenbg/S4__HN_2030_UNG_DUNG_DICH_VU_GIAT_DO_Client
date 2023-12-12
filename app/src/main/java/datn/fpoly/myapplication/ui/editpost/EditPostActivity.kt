package datn.fpoly.myapplication.ui.editpost

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.viewModel
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.orhanobut.hawk.Hawk
import datn.fpoly.myapplication.AppApplication
import datn.fpoly.myapplication.R
import datn.fpoly.myapplication.core.BaseActivity
import datn.fpoly.myapplication.data.model.StoreModel
import datn.fpoly.myapplication.data.model.post.PostModel
import datn.fpoly.myapplication.data.model.post.PostService
import datn.fpoly.myapplication.databinding.ActivityEditPostBinding
import datn.fpoly.myapplication.utils.Common
import datn.fpoly.myapplication.utils.Dialog_Loading
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import timber.log.Timber
import java.io.File
import javax.inject.Inject


@Suppress("DEPRECATION")
class EditPostActivity : BaseActivity<ActivityEditPostBinding>(), EditPostViewModel.Factory {

    @Inject
    lateinit var editPostFactory: EditPostViewModel.Factory

    private val viewModel: EditPostViewModel by viewModel()
    private var postModel: PostModel? = null
    override fun getBinding(): ActivityEditPostBinding {
        return ActivityEditPostBinding.inflate(layoutInflater)
    }

    private var imageUri: Uri? = null
    private var isValidate = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as AppApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(views.root)
    }

    override fun initUiAndData() {
        super.initUiAndData()
        viewModel.subscribe(this) {
            updateWithState(it)
        }
        if (imageUri != null) {
            views.imagePost.visibility = View.VISIBLE
        }
        views.icImage.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start()
        }
        postModel = intent.getSerializableExtra(Common.KEY_POST) as PostModel
        views.apply {
            edTitleEdit.setText(postModel?.title.toString())
            edContentEdit.setText(postModel?.content.toString())
            if (postModel?.image == null) {
                imagePost.visibility = View.GONE
            } else {
                imagePost.visibility = View.VISIBLE
            }
            Glide.with(this@EditPostActivity).load(Common.baseUrl + postModel?.image)
                .placeholder(R.drawable.loading_error).into(imagePost)
            toobar.icSearch.visibility = View.GONE

        }
        views.btnPost.setOnClickListener {
            validate()
            Log.d("EditPostActivity", "initUiAndData: ${views.edTitleEdit.text.toString()}")
            if (isValidate == 0) {
                validate()
            }

        }
        views.toobar.icBack.setOnClickListener {
            setResult(Common.CODE_LOAD_DATA)
            onBackPressedDispatcher.onBackPressed()
        }
        views.toobar.tvTitleTooobal.text = "Sửa Bài Viết"
    }

    private fun validate() {
        if (views.edTitleEdit.text.toString().trim()
                .isNotEmpty() && views.edContentEdit.text.toString().trim()
                .isNotEmpty()
        ) {
            Dialog_Loading.getInstance().show(supportFragmentManager, "Loading add post")
            editPost()
            views.tvErrorTitle.text = ""
            views.tvErrorContent.text = ""

        } else {
            if (views.edTitleEdit.text.toString().trim().isEmpty()) {
                views.tvErrorTitle.visibility = View.VISIBLE
                views.tvErrorTitle.text = "Tiêu đề không được để trống"
            } else {
                views.tvErrorTitle.text = ""
            }

            if (views.edContentEdit.text.toString().trim().isEmpty()) {
                views.tvErrorContent.text = "Nội dung không được để trống"
                views.tvErrorContent.visibility = View.VISIBLE
            } else {
                views.tvErrorContent.text = ""
            }
        }
    }

    private fun editPost() {
        Log.d("EditPostActivity", "editPost: $isValidate")
        val title = views.edTitleEdit.text.toString()
        val content = views.edContentEdit.text.toString()

        if (imageUri != null) {
            Dialog_Loading.getInstance().show(supportFragmentManager, "Loading edit post")

            val file = File(imageUri!!.path!!) // Chuyển URI thành File

            val requestFile = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            val image = MultipartBody.Part.createFormData("image", file.name, requestFile)

            val title = title.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val content = content.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            viewModel.handle(
                EditPostViewAction.EditPostAction(
                    postModel!!.id,
                    title,
                    content,
                    image
                )
            )
        } else {
            Dialog_Loading.getInstance().show(supportFragmentManager, "Loading edit post")

            val title_1 = title.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val content_1 = content.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            viewModel.handle(
                EditPostViewAction.EditPostAction(
                    postModel!!.id,
                    title_1,
                    content_1,
                    null
                )
            )
        }

    }

    private fun updateWithState(state: EditPostViewState) {
        Timber.tag("AddPostActivity").d("chậgdhasjd: ")
        when (state.stateEditPost) {
            is Success -> {
                runBlocking {
                    launch {
                        state.stateEditPost.invoke()?.let { result ->
                            Timber.tag("updateWithState").d("updateWithState: " + result.message())
                            if (result.code() == 200) {
                                Toast.makeText(
                                    this@EditPostActivity,
                                    "Sửa thành công",
                                    Toast.LENGTH_SHORT
                                ).show()
                                setResult(Common.CODE_LOAD_DATA)
                                onBackPressedDispatcher.onBackPressed()
                            } else {
                                Toast.makeText(
                                    this@EditPostActivity,
                                    "Thất Bại",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        }
                    }
                }
            }

            is Loading -> {
                //Xoay tròn indicate
                Timber.tag("AddPostActivity").d("loadiing: ")
            }

            is Fail -> {
                Timber.tag("AddPostActivity").e("Error: ")
            }

            else -> {}
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            imageUri = data!!.data
            views.imagePost.visibility = View.VISIBLE
            Glide.with(this).load(imageUri).into(views.imagePost)
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    override fun create(initialState: EditPostViewState) = editPostFactory.create(initialState)


}