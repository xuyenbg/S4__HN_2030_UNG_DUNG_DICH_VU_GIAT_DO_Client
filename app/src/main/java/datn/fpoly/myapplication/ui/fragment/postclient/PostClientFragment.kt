package datn.fpoly.myapplication.ui.fragment.postclient

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import datn.fpoly.myapplication.core.BaseFragment
import datn.fpoly.myapplication.data.model.post.PostModel
import datn.fpoly.myapplication.databinding.FragmentPostClientBinding
import datn.fpoly.myapplication.ui.detailstore.DetailStoreActivity
import datn.fpoly.myapplication.ui.fragment.postclient.adapter.PostClientAdapter
import datn.fpoly.myapplication.ui.home.HomeUserViewModel
import datn.fpoly.myapplication.ui.home.HomeViewAction
import datn.fpoly.myapplication.ui.home.HomeViewState
import datn.fpoly.myapplication.utils.Common
import datn.fpoly.myapplication.utils.ItemSpacingDecoration
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

class PostClientFragment : BaseFragment<FragmentPostClientBinding>() {
    private val viewModel: HomeUserViewModel by activityViewModel()

    private lateinit var postClientAdapter: PostClientAdapter
    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPostClientBinding = FragmentPostClientBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.handle(HomeViewAction.PostClientActionList)
        postClientAdapter = PostClientAdapter()
        val itemDecoration = ItemSpacingDecoration(42)
        views.recycleviewPost.addItemDecoration(itemDecoration)
        postClientAdapter.setListener(object : PostClientAdapter.PostListener {
            override fun onClickPost(postModel: PostModel) {
                val intent = Intent(requireContext(), DetailStoreActivity::class.java)
                intent.putExtra(Common.KEY_ID_STORE,postModel.idStore.id)
                startActivity(intent)
            }
        })
        views.swipeToRefresh.setOnRefreshListener {
            if(views.swipeToRefresh.isRefreshing){
                viewModel.handle(HomeViewAction.PostClientActionList)
            }
        }
    }

    override fun invalidate(): Unit = withState(viewModel) {
        super.invalidate()
        getListPost(it)
    }

    private fun getListPost(it: HomeViewState) {
        when (it.statePost) {
            is Success -> {
                views.swipeToRefresh.isRefreshing = false
                runBlocking {
                    launch {
                        it.statePost.invoke()?.let {
                            views.shimmer.visibility=View.GONE
                            views.swipeToRefresh.visibility=View.VISIBLE
                            Timber.tag("PostClientFragment").d("postinvalidate: ${it.size}")
                            postClientAdapter.updateData(it)
                            views.recycleviewPost.adapter = postClientAdapter

                            postClientAdapter.notifyDataSetChanged()

                            Log.d("PostClientFragment", "getListPost: ${it.size}")
                        }
                    }

                }
            }

            is Loading -> {
                views.shimmer.visibility=View.VISIBLE
                views.swipeToRefresh.visibility=View.GONE
                views.shimmer.startShimmer()
                Timber.tag("AAAAAAAAAAAAAAA").e("getPost: loading")
            }

            is Fail -> {
                views.shimmer.visibility=View.GONE
                views.swipeToRefresh.visibility=View.VISIBLE
                Timber.tag("AAAAAAAAAAAAAAA").e("getPost: Fail")
            }

            else -> {

            }
        }
    }
}
