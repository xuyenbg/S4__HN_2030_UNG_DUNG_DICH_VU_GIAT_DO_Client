package datn.fpoly.myapplication.ui.poststore


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.viewModel
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.orhanobut.hawk.Hawk
import datn.fpoly.myapplication.AppApplication
import datn.fpoly.myapplication.core.BaseActivity
import datn.fpoly.myapplication.data.model.StoreModel
import datn.fpoly.myapplication.data.repository.AuthRepo
import datn.fpoly.myapplication.databinding.ActivityAddPostBinding
import datn.fpoly.myapplication.utils.Common
import datn.fpoly.myapplication.utils.Dialog_Loading
import gun0912.tedimagepicker.builder.TedImagePicker
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import timber.log.Timber
import java.io.File
import javax.inject.Inject

class AddPostActivity : BaseActivity<ActivityAddPostBinding>(), AddPostViewModel.Factory {
    @Inject
    lateinit var addPosFactory: AddPostViewModel.Factory

    private val viewModel: AddPostViewModel by viewModel()

    override fun getBinding(): ActivityAddPostBinding {
        return ActivityAddPostBinding.inflate(layoutInflater)
    }

    private var imageUri: Uri? = null
    private var isValidate = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as AppApplication).appComponent.inject(this);
        super.onCreate(savedInstanceState)
        setContentView(views.root)
    }

    override fun initUiAndData() {
        super.initUiAndData()
        viewModel.subscribe(this) {
            updateWithState(it)
        }
        views.icImage.setOnClickListener {
//            TedImagePicker.with(this)
//                .start { uri ->
//                    imageUri = uri
//                    Glide.with(this).load(uri).into(views.imagePost)
//                }
//        }
            ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start()
        }

        views.btnPost.setOnClickListener {
            Dialog_Loading.getInstance().show(supportFragmentManager, "Loading add post")
            addPost()
        }
        views.toobar.icBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        views.toobar.tvTitleTooobal.text = "Add Post"
    }

    private fun validate() {
        if (views.edTitle.text.toString().isEmpty()) {
            views.edTitle.error = "Vui lòng không để trống tiêu đề !"
            isValidate++
        } else {
            views.edTitle.error = null
            isValidate = 0
        }

        if (views.edContent.text.toString().isEmpty()) {
            views.edContent.error = "Vui lòng không để trống nội dung !"
            isValidate++
        } else {
            views.edContent.error = null
            isValidate = 0
        }
    }

    private fun addPost() {
        validate()
        if (isValidate == 0) {
            val title = views.edTitle.text.toString()
            val content = views.edContent.text.toString()
            val idStore = Hawk.get<StoreModel>(Common.KEY_STORE).id


            if (imageUri != null) {

                val file = File(imageUri!!.path!!) // Chuyển URI thành File

                val requestFile = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                val image = MultipartBody.Part.createFormData("image", file.name, requestFile)

                val title = title.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                val content = content.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                val idStore = idStore!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                viewModel.handle(
                    AddPostViewAction.AddPostAction(
                        idStore,
                        title,
                        content,
                        image
                    )
                )
            } else {
                val title_1 = title.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                val content_1 = content.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                val idStore_1 = idStore!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                viewModel.handle(
                    AddPostViewAction.AddPostAction(
                        idStore_1,
                        title_1,
                        content_1,
                        null
                    )
                )
            }
        }

    }

    private fun setClear() {
        views.edContent.setText("")
        views.edTitle.setText("")
    }

    private fun updateWithState(state: AddPostViewState) {
        Timber.tag("AddPostActivity").d("chậgdhasjd: ")
        when (state.stateAddPost) {
            is Success -> {
                runBlocking {
                    launch {
                        state.stateAddPost.invoke()?.let { result ->
                            Timber.tag("updateWithState").d("updateWithState: " + result.message())
                            if (result.code() == 200) {
                                Toast.makeText(
                                    this@AddPostActivity,
                                    "Thêm thành công",
                                    Toast.LENGTH_SHORT
                                ).show()
                                onBackPressedDispatcher.onBackPressed()
                            } else {
                                Toast.makeText(this@AddPostActivity, "Thất Bại", Toast.LENGTH_SHORT)
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
            Glide.with(this).load(imageUri).into(views.imagePost)
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    override fun create(initialState: AddPostViewState) = addPosFactory.create(initialState)


}