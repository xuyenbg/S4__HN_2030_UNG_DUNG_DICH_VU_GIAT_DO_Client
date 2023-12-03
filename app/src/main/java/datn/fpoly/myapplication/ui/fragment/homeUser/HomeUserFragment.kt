package datn.fpoly.myapplication.ui.fragment.homeUser


import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationRequest
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import com.airbnb.mvrx.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import datn.fpoly.myapplication.core.BaseFragment
import com.orhanobut.hawk.Hawk
import datn.fpoly.myapplication.data.model.CategoryModel
import datn.fpoly.myapplication.data.model.StoreModel
import datn.fpoly.myapplication.data.model.StoreNearplaceModel
import datn.fpoly.myapplication.databinding.FragmentHomeUserBinding
import datn.fpoly.myapplication.ui.adapter.AdapterCategory
import datn.fpoly.myapplication.ui.adapter.AdapterStore
import datn.fpoly.myapplication.ui.adapter.SlideImageAdapter
import datn.fpoly.myapplication.ui.detailstore.DetailStoreActivity
import datn.fpoly.myapplication.ui.home.HomeUserViewModel
import datn.fpoly.myapplication.ui.home.HomeViewAction
import datn.fpoly.myapplication.ui.home.HomeViewState
import datn.fpoly.myapplication.ui.listService.ListServiceActivity
import datn.fpoly.myapplication.ui.map.PickPossitionInMapActivity
import datn.fpoly.myapplication.ui.searchService.SearchServiceActivity
import datn.fpoly.myapplication.ui.seeMore.SeeMoreActivity
import datn.fpoly.myapplication.utils.Common
import datn.fpoly.myapplication.utils.DataRaw
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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
    private lateinit var fusedLoaction: FusedLocationProviderClient

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeUserBinding = FragmentHomeUserBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fusedLoaction = LocationServices.getFusedLocationProviderClient(requireActivity())
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
            override fun onClickStoreListener(storeModel: StoreNearplaceModel) {
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
        views.imgSearch.setOnClickListener {
            val intent = Intent(requireContext(), SearchServiceActivity::class.java)
            requireContext().startActivity(intent)
        }

        initSlide()
        views.refLayout.setOnRefreshListener {
            if (!views.refLayout.isRefreshing) {
                viewModel.handle(HomeViewAction.HomeActionCategory)
                viewModel.handle(
                    HomeViewAction.HomeActionGetListStore(
                        Common.getMyLocationLatitude(requireContext()),
                        Common.getMyLocationLongitude(requireContext())
                    )
                )
            }

        }
        if (Common.getMyLocationLatitude(requireContext()) == 0f && Common.getMyLocationLongitude(
                requireContext()
            ) == 0f
        ) {
            dialogGPS(requireContext())
        }else{
            if(!Common.checkPermission(requireContext())){
                getCurrentLocation()
            }
        }

    }


    override fun onResume() {
        super.onResume()
        viewModel.handle(HomeViewAction.HomeActionCategory)
        if (Common.getMyLocationLatitude(requireContext()) != 0f && Common.getMyLocationLongitude(
                requireContext()
            ) != 0f
        ) {
            viewModel.handle(
                HomeViewAction.HomeActionGetListStore(
                    Common.getMyLocationLatitude(requireContext()),
                    Common.getMyLocationLongitude(requireContext())
                )
            )
        }

    }
    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLoaction.getCurrentLocation(
                LocationRequest.QUALITY_HIGH_ACCURACY,
                object : CancellationToken() {
                    override fun onCanceledRequested(p0: OnTokenCanceledListener): CancellationToken {

                        return CancellationTokenSource().token
                    }

                    override fun isCancellationRequested(): Boolean {
                        return false
                    }

                }).addOnSuccessListener() {
                if (it != null) {
                   Common.setMyLocation(requireContext(), LatLng(it.latitude, it.longitude))
                    viewModel.handle(HomeViewAction.HomeActionGetListStore(it.latitude.toFloat(),it.longitude.toFloat()))
                }
            }
        }

    }
    private fun dialogGPS(context: Context){
        val build = AlertDialog.Builder(context)
        build.setTitle("Thông báo")
        build.setMessage("Lấy thông tin vị trí")
        build.setPositiveButton("Lấy vị trí", DialogInterface.OnClickListener { dialogInterface, i ->
            val intent = Intent(context, PickPossitionInMapActivity::class.java)
            requireActivity().startActivity(intent)
        })
        build.setNegativeButton("Đóng", DialogInterface.OnClickListener { dialogInterface, i ->
            dialogInterface.dismiss()
            viewModel.handle(
                HomeViewAction.HomeActionGetListStore(
                    Common.getMyLocationLatitude(requireContext()),
                    Common.getMyLocationLongitude(requireContext())
                )
            )
        })
        val dialog = build.create()
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
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
                            views.shimmerCate.visibility= View.GONE
                            views.rcvListCategory.visibility=View.VISIBLE
                            views.refLayout.isRefreshing= false
                            adapterCate.updateData(it)
                            Timber.tag("AAAAAAAAAAAAAA").e("invalidate: " + it.size)
                        }
                    }

                }
            }

            is Loading -> {
                views.shimmerCate.visibility= View.VISIBLE
                views.shimmerCate.startShimmer()
                views.rcvListCategory.visibility=View.INVISIBLE
                Timber.tag("AAAAAAAAAAAAAAA").e("getListCategory: loading")
            }

            is Fail -> {
                views.shimmerCate.visibility= View.GONE
                views.rcvListCategory.visibility=View.VISIBLE
                Timber.tag("AAAAAAAAAAAAAAA").e("getListCategory: Fail")
            }

            else -> {

            }
        }
    }

    fun getListStore(state: HomeViewState) {
        when (state.stateStore) {
            is Loading -> {
                views.shimmerStore.visibility=View.VISIBLE
                views.rcvListStore.visibility=View.INVISIBLE
                views.shimmerStore.startShimmer()
                Timber.tag("AAAAAAAAAAAAAAA").e("getListStore: loading")
            }

            is Success -> {
                Timber.tag("AAAAAAAAAAAAAAA").e("getListStore: Success")
                runBlocking {
                    Timber.tag("AAAAAAAAAAAAAAA").e("getListStore: Success2")
                    launch {
                        Timber.tag("AAAAAAAAAAAAAAA").e("getListStore: Success3")
                        state.stateStore.invoke()?.let {
                            views.shimmerStore.visibility=View.GONE
                            views.rcvListStore.visibility=View.VISIBLE
                            views.refLayout.isRefreshing= false
                            adapterStore.setData(it)
                            Timber.tag("AAAAAAAAAAAAAAA").e("getListStore: size" + it.size)
                        }
                    }
                }
            }

            is Fail -> {
                views.shimmerStore.visibility=View.GONE
                views.rcvListStore.visibility=View.VISIBLE
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


