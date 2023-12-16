package datn.fpoly.myapplication.ui.fragment.postStore

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.mvrx.*
import com.bumptech.glide.Glide
import datn.fpoly.myapplication.core.BaseFragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.orhanobut.hawk.Hawk
import datn.fpoly.myapplication.R
import datn.fpoly.myapplication.data.model.StoreModel
import datn.fpoly.myapplication.data.model.post.PostModel
import datn.fpoly.myapplication.databinding.ActivityPostStoreBinding
import datn.fpoly.myapplication.ui.detailstore.DetailStoreActivity
import datn.fpoly.myapplication.ui.editpost.EditPostActivity
import datn.fpoly.myapplication.ui.fragment.postclient.adapter.PostStoreAdapter
import datn.fpoly.myapplication.ui.homeStore.HomeStoreState
import datn.fpoly.myapplication.ui.homeStore.HomeStoreViewAction
import datn.fpoly.myapplication.ui.homeStore.HomeStoreViewModel
import datn.fpoly.myapplication.ui.poststore.AddPostActivity
import datn.fpoly.myapplication.utils.Common
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber


class FragmentPostStore : BaseFragment<ActivityPostStoreBinding>() {
    private val viewModel: HomeStoreViewModel by activityViewModel()
    private lateinit var postClientAdapter: PostStoreAdapter
    private var idStore: String? = null
    private val startActivityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Common.CODE_LOAD_DATA) {
                viewModel.handle(HomeStoreViewAction.PostStoreActionList(idStore!!))
            }
        }
    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): ActivityPostStoreBinding = ActivityPostStoreBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        idStore = Hawk.get<StoreModel>(Common.KEY_STORE).id
        viewModel.handle(HomeStoreViewAction.PostStoreActionList(idStore!!))
        views.btnAddPost.setOnClickListener {
            startActivityResult.launch(Intent(requireContext(), AddPostActivity::class.java))
        }
        postClientAdapter = PostStoreAdapter()
        val itemDecoration = ItemSpacingDecoration(32)
        views.recyclerViewPostStore.addItemDecoration(itemDecoration)
        postClientAdapter.setListener(object : PostStoreAdapter.PostListener {
            override fun onClickPost(postModel: PostModel) {
            }

            override fun onClickEdit(postModel: PostModel) {
                bottomSheetDialog(postModel)
//                deletePost(postModel)
            }
        })

        views.swipeToRefresh.setOnRefreshListener {

            viewModel.handle(HomeStoreViewAction.PostStoreActionList(idStore!!))
            lifecycleScope.launch {
            }

        }

    }

    override fun invalidate(): Unit = withState(viewModel) {
        getListPost(it)
        deletePostUi(it)
    }

    private fun getListPost(it: HomeStoreState) {
        when (it.statePostStore) {
            is Success -> {
                views.swipeToRefresh.isRefreshing = false
                runBlocking {
                    launch {
                        it.statePostStore.invoke()?.let {
                            postClientAdapter.updateData(it)
                            views.recyclerViewPostStore.adapter = postClientAdapter

                            postClientAdapter.notifyDataSetChanged()

                        }
                    }

                }
            }

            is Loading -> {
                Timber.tag("AAAAAAAAAAAAAAA").e("getPost: loading")
            }

            is Fail -> {
                Timber.tag("AAAAAAAAAAAAAAA").e("getPost: Fail")
            }

            else -> {

            }
        }
    }

    private fun deletePostUi(it: HomeStoreState) {
        when (it.stateDelete) {
            is Success -> {
                runBlocking {
                    launch {
                        it.stateDelete.invoke()?.let { result ->
                            if (result.code() == 200) {
                                Toast.makeText(requireContext(), "Đã xóa", Toast.LENGTH_SHORT)
                                    .show()
                            } else {
                                Toast.makeText(requireContext(), "Thất Bại", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                        it.stateDelete= Uninitialized
                    }

                }
            }

            is Loading -> {
                Timber.tag("AAAAAAAAAAAAAAA").e("getPost: loading")
            }

            is Fail -> {
                Timber.tag("AAAAAAAAAAAAAAA").e("getPost: Fail")
            }

            else -> {

            }
        }
    }

    private fun bottomSheetDialog(postModel: PostModel) {
        val view: View = layoutInflater.inflate(R.layout.layout_opption_edit_delete_post, null)
        val dialog = BottomSheetDialog(requireContext())
        dialog.setContentView(view)

        view.findViewById<LinearLayout>(R.id.liner_edit).setOnClickListener {
            val intent = Intent(requireContext(), EditPostActivity::class.java)
            intent.putExtra(Common.KEY_POST, postModel)
            startActivityResult.launch(intent)
            dialog.dismiss()
        }

        view.findViewById<LinearLayout>(R.id.liner_delete).setOnClickListener {
            deletePost(postModel)
            dialog.dismiss()
        }

        view.findViewById<AppCompatImageView>(R.id.ic_cancle).setOnClickListener {
            dialog.dismiss()

        }
        dialog.show()

    }

    @SuppressLint("MissingInflatedId")
    private fun deletePost(postModel: PostModel) {
        val dialogbinding = layoutInflater.inflate(R.layout.dialog_notification_all, null)
        val myDialog = Dialog(requireActivity())
        myDialog.setContentView(dialogbinding)

        myDialog.setCancelable(true)
        myDialog.setCanceledOnTouchOutside(false)
        myDialog.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        myDialog.show()
        dialogbinding.findViewById<TextView>(R.id.tv_text)
            .setText("Bạn có chắc chắn muốn xóa bài viết ?")

        dialogbinding.findViewById<TextView>(R.id.tv_onConfirm).setOnClickListener {
            viewModel.handle(HomeStoreViewAction.deletePost(postModel.id))
            viewModel.handle(HomeStoreViewAction.PostStoreActionList(idStore!!))
            postClientAdapter.notifyDataSetChanged()
            myDialog.dismiss()
        }

        dialogbinding.findViewById<TextView>(R.id.tv_onCancel).setOnClickListener {
            myDialog.dismiss()
        }
    }

//    override fun onResume() {
//        super.onResume()
//        viewModel.handle(HomeStoreViewAction.PostStoreActionList(idStore!!))
//        postClientAdapter.notifyDataSetChanged()
//    }
}

class ItemSpacingDecoration(private val spacing: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.top = spacing/2
        outRect.bottom = spacing // Đặt khoảng dưới theo giá trị spacing bạn muốn
        outRect.right = spacing
        outRect.left = spacing
    }
}