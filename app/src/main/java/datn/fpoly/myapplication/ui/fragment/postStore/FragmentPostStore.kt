package datn.fpoly.myapplication.ui.fragment.postStore

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import com.example.ql_ban_hang.core.BaseFragment
import com.orhanobut.hawk.Hawk
import datn.fpoly.myapplication.data.model.StoreModel
import datn.fpoly.myapplication.data.model.post.PostModel
import datn.fpoly.myapplication.databinding.ActivityPostStoreBinding
import datn.fpoly.myapplication.ui.detailstore.DetailStoreActivity
import datn.fpoly.myapplication.ui.fragment.postclient.adapter.PostClientAdapter
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
    private lateinit var postClientAdapter: PostClientAdapter
    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): ActivityPostStoreBinding = ActivityPostStoreBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val idStore = Hawk.get<StoreModel>(Common.KEY_STORE).id
        viewModel.handle(HomeStoreViewAction.PostStoreActionList(idStore!!))

        views.toobar.tvTitleTooobal.text = "Tạo Bài Viết"
        views.btnAddPost.setOnClickListener {
            startActivity(Intent(requireContext(), AddPostActivity::class.java))
        }
        postClientAdapter = PostClientAdapter()
        val itemDecoration = ItemSpacingDecoration(32)
        views.recyclerViewPostStore.addItemDecoration(itemDecoration)
        postClientAdapter.setListener(object : PostClientAdapter.PostListener {
            override fun onClickPost(postModel: PostModel) {
                val intent = Intent(requireContext(), DetailStoreActivity::class.java)
                startActivity(intent)
            }
        })

        views.swipeToRefresh.setOnRefreshListener {

            viewModel.handle(HomeStoreViewAction.PostStoreActionList(idStore))
            lifecycleScope.launch {
            }

        }
    }

    override fun invalidate(): Unit = withState(viewModel) {
        getListPost(it)
    }

    private fun getListPost(it: HomeStoreState) {
        when (it.statePostStore) {
            is Success -> {
                views.swipeToRefresh.isRefreshing = false
                runBlocking {
                    launch {
                        it.statePostStore.invoke()?.let {
                            Timber.tag("PostClientFragment").d("postinvalidate: ${it.size}")
                            postClientAdapter.updateData(it)
                            views.recyclerViewPostStore.adapter = postClientAdapter

                            postClientAdapter.notifyDataSetChanged()

                            Log.d("PostClientFragment", "getListPost: ${it.size}")
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

    override fun onResume() {
        super.onResume()

    }
}

class ItemSpacingDecoration(private val spacing: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.bottom = spacing // Đặt khoảng dưới theo giá trị spacing bạn muốn
        outRect.right = spacing
        outRect.left = spacing
    }
}