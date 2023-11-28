package datn.fpoly.myapplication.ui.fragment.homeUser


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.mvrx.*
import datn.fpoly.myapplication.core.BaseFragment
import com.orhanobut.hawk.Hawk
import datn.fpoly.myapplication.data.model.CategoryModel
import datn.fpoly.myapplication.data.model.StoreModel
import datn.fpoly.myapplication.databinding.FragmentHomeUserBinding
import datn.fpoly.myapplication.ui.adapter.AdapterCategory
import datn.fpoly.myapplication.ui.adapter.AdapterStore
import datn.fpoly.myapplication.ui.adapter.SlideImageAdapter
import datn.fpoly.myapplication.ui.detailstore.DetailStoreActivity
import datn.fpoly.myapplication.ui.home.HomeUserViewModel
import datn.fpoly.myapplication.ui.home.HomeViewAction
import datn.fpoly.myapplication.ui.home.HomeViewState
import datn.fpoly.myapplication.ui.listService.ListServiceActivity
import datn.fpoly.myapplication.ui.seeMore.SeeMoreActivity
import datn.fpoly.myapplication.utils.Common
import datn.fpoly.myapplication.utils.DataRaw
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber


class HomeUserFragment : BaseFragment<FragmentHomeUserBinding>() {

    private val viewModel: HomeUserViewModel by activityViewModel()
    private lateinit var adapterCate: AdapterCategory
    private lateinit var adapterStore: AdapterStore
    private lateinit var handler: Handler
    private var imageList: MutableList<Int> = mutableListOf()
    private lateinit var adapter: SlideImageAdapter

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeUserBinding = FragmentHomeUserBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = SlideImageAdapter(views.vpSlideShow)

        adapterCate = AdapterCategory(6, false)
        views.rcvListCategory.adapter = adapterCate
        adapterCate.setListener(object : AdapterCategory.CategoryListener {
            override fun onClickCate(categoryModel: CategoryModel) {
                categoryModel.id?.let { DataRaw.setDataIdCategory(it) }
                requireContext().startActivity(
                    Intent(
                        requireContext(),
                        ListServiceActivity::class.java
                    )
                )
            }
        })
        adapterStore = AdapterStore(6)
        views.rcvListStore.adapter = adapterStore
        adapterStore.setListener(object : AdapterStore.StoreListener {
            override fun onClickStoreListener(storeModel: StoreModel) {
                Hawk.put(Common.KEY_STORE_DETAIL, storeModel)
                requireContext().startActivity(
                    Intent(
                        requireContext(),
                        DetailStoreActivity::class.java
                    ).putExtra(Common.KEY_ID_STORE, storeModel.id)
                )
            }
        })
        views.tvSeeMore.setOnClickListener {
            DataRaw.setDataCategory(adapterCate.getListCate)
            val intent = Intent(requireContext(), SeeMoreActivity::class.java)
            intent.putExtra(Common.KEY_SEE_MORE, 1)
            requireContext().startActivity(intent)

        }
        views.tvSeeMoreStore.setOnClickListener {
            DataRaw.setDataStore(adapterStore.getListStore)
            val intent = Intent(requireContext(), SeeMoreActivity::class.java)
            intent.putExtra(Common.KEY_SEE_MORE, 2)
            requireContext().startActivity(intent)
        }

        initSlide()


    }

    override fun onResume() {
        super.onResume()
        viewModel.handle(HomeViewAction.HomeActionCategory)
        viewModel.handle(HomeViewAction.HomeActionGetListStore)
//        handler.postDelayed(runnable, 2000)
    }

    override fun invalidate(): Unit = withState(viewModel) {
        getListCate(it)
        getListStore(it)
    }

    fun getListCate(it: HomeViewState) {
        when (it.stateCategory) {
            is Success -> {
                Timber.tag("AAAAAAAAAAAAAAA").e("getListCategory: Success")
                runBlocking {
                    Timber.tag("AAAAAAAAAAAAAAA").e("getListCategory: Success2")
                    launch {
                        Timber.tag("AAAAAAAAAAAAAAA").e("getListCategory: Success3")
                        it.stateCategory.invoke()?.let {
                            adapterCate.updateData(it)
                            Timber.tag("AAAAAAAAAAAAAA").e("invalidate: " + it.size)
                        }
                    }

                }
            }

            is Loading -> {
                Timber.tag("AAAAAAAAAAAAAAA").e("getListCategory: loading")
            }

            is Fail -> {
                Timber.tag("AAAAAAAAAAAAAAA").e("getListCategory: Fail")
            }

            else -> {

            }
        }
    }

    fun getListStore(state: HomeViewState) {
        when (state.stateStore) {
            is Loading -> {
                Timber.tag("AAAAAAAAAAAAAAA").e("getListStore: loading")
            }

            is Success -> {
                Timber.tag("AAAAAAAAAAAAAAA").e("getListStore: Success")
                runBlocking {
                    Timber.tag("AAAAAAAAAAAAAAA").e("getListStore: Success2")
                    launch {
                        Timber.tag("AAAAAAAAAAAAAAA").e("getListStore: Success3")
                        state.stateStore.invoke()?.let {
                            adapterStore.setData(it)
                            Timber.tag("AAAAAAAAAAAAAAA").e("getListStore: size" + it.size)
                        }
                    }
                }
            }

            is Fail -> {
                Timber.tag("AAAAAAAAAAAAAAA").e("getListStore: fail")
            }

            else -> {

            }
        }

    }

    private val runnable = Runnable {
        if (views.vpSlideShow.currentItem == imageList.size - 1) {
            views.vpSlideShow.currentItem = 0
        } else {
            views.vpSlideShow.currentItem = views.vpSlideShow.currentItem + 1
        }
    }

    private fun initSlide() {
        handler = Handler(Looper.getMainLooper())
        imageList.add(datn.fpoly.myapplication.R.drawable.imageslide_1)
        imageList.add(datn.fpoly.myapplication.R.drawable.imageslide_2)
        imageList.add(datn.fpoly.myapplication.R.drawable.imageslide3)
        imageList.add(datn.fpoly.myapplication.R.drawable.imageslide_4)
//        adapter = SlideImageAdapter(views.vpSlideShow)
        adapter.updateData(imageList)
        views.vpSlideShow.adapter = adapter


       // views.circle3.setViewPager(views.vpSlideShow)
//        views.vpSlideShow.registerOnPageChangeCallback(object : OnPageChangeCallback() {
//            override fun onPageSelected(position: Int) {
//                super.onPageSelected(position)
//                handler.removeCallbacks(runnable)
//                handler.postDelayed(runnable, 2000)
//            }
//        })
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
    }
}


