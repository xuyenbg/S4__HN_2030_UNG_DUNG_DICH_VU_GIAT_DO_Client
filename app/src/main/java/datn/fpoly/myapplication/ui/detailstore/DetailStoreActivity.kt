package datn.fpoly.myapplication.ui.detailstore

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.viewModel
import com.bumptech.glide.Glide
import com.orhanobut.hawk.Hawk
import datn.fpoly.myapplication.AppApplication
import datn.fpoly.myapplication.R
import datn.fpoly.myapplication.core.BaseActivity
import datn.fpoly.myapplication.data.model.ServiceExtend
import datn.fpoly.myapplication.data.model.StoreModel
import datn.fpoly.myapplication.data.model.account.AccountModel
import datn.fpoly.myapplication.databinding.ActivityDetailStoreBinding
import datn.fpoly.myapplication.ui.adapter.AdapterRate
import datn.fpoly.myapplication.ui.adapter.AdapterService
import datn.fpoly.myapplication.ui.service.DetailServiceActivity
import datn.fpoly.myapplication.utils.Common
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

class DetailStoreActivity : BaseActivity<ActivityDetailStoreBinding>(),
    DetailStoreViewModel.Factory {
    @Inject
    lateinit var detailStoreFactory: DetailStoreViewModel.Factory
    private val viewModel: DetailStoreViewModel by viewModel()
    private lateinit var itemStoreDetail: StoreModel
    private lateinit var adapterService: AdapterService
    private lateinit var adapterRate: AdapterRate
    var isStoreFavorited: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as AppApplication).appComponent.inject(this);
        super.onCreate(savedInstanceState)
        adapterService = AdapterService(false, false)
        val account = Hawk.get<AccountModel>("Account", null)
        var store = intent.getStringExtra(Common.KEY_ID_STORE)
        var idUser = account.id.toString()
        val accountModel = AccountModel()
        intent.getStringExtra(Common.KEY_ID_STORE)
            ?.let { viewModel.handle(DetailStoreViewAction.GetListServiceByStore(it)) }
        intent.getStringExtra(Common.KEY_ID_STORE)
            ?.let { viewModel.handle(DetailStoreViewAction.GetStoreById(it)) }
        intent.getStringExtra(Common.KEY_ID_STORE)
            ?.let { viewModel.handle(DetailStoreViewAction.GetListRateByStore(it)) }
        views.imgBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        viewModel.subscribe(this) {
            getListService(it)
            getStore(it)
            getListRate(it)
            getDetailUser(it)
        }

        views.rcvDetailStore.adapter = adapterService
        adapterService.setListenner(object : AdapterService.ServiceListenner {
            override fun serviceOnClick(item: ServiceExtend, position: Int) {
//                Hawk.put(Common.KEY_SERVICE_DETAIL, item)
                val intent = Intent(this@DetailStoreActivity, DetailServiceActivity::class.java)
                intent.putExtra(Common.KEY_ID_SERVICE, item.id)
                startActivity(intent)
            }

            override fun editService(serviceExtend: ServiceExtend) {

            }
        })

        accountModel.favoriteStores = ArrayList()
        (accountModel.favoriteStores as ArrayList<String>)?.add(store.toString())
        views.imgAddFavoriteStore.setOnClickListener {

//            Toast.makeText(this, "ddđ", Toast.LENGTH_SHORT).show()
            val updatedImgResource = views.imgAddFavoriteStore.drawable

            if (!isStoreFavorited) {
                Toast.makeText(this, "Đã thêm cửa hàng vào danh sách yêu thích", Toast.LENGTH_SHORT)
                    .show()
                views.imgAddFavoriteStore2.visibility = View.VISIBLE
                views.imgAddFavoriteStore.visibility = View.INVISIBLE
                viewModel.addFavoriteStore(idUser, accountModel)
            } else {
                Toast.makeText(
                    this,
                    "Cửa hàng này đã có trong danh sách yêu thích",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        viewModel.handle(DetailStoreViewAction.GetDetailUser(idUser))

        adapterRate = AdapterRate()
        views.rcvRates.adapter = adapterRate


    }

    fun getListService(state: DetailStoreViewState) {
        when (state.stateService) {
            is Success -> {
                runBlocking {
                    launch {
                        state.stateService.invoke()?.let {
                            views.shimmer.visibility = View.GONE
                            views.rcvDetailStore.visibility = View.VISIBLE
                            adapterService.setData(it)

                            Timber.tag("AAAAAAAAA")
                                .e("getListService: list service size: " + it.size)
                        }
                    }
                }
            }

            is Loading -> {
                views.shimmer.visibility = View.VISIBLE
                views.rcvDetailStore.visibility = View.GONE
                views.shimmer.startShimmer()
                Timber.tag("AAAAAAAAA").e("getListService: Loading")
            }

            is Fail -> {
                views.shimmer.visibility = View.GONE
                views.rcvDetailStore.visibility = View.VISIBLE

                Timber.tag("AAAAAAAAA").e("getListService: Call Fail")
            }

            else -> {

            }
        }
    }

    fun getStore(state: DetailStoreViewState) {
        when (state.stateStore) {
            is Success -> {
                runBlocking {
                    launch {
                        state.stateStore.invoke()?.let { itemStoreDetail ->
                            views.tvNameStore.text = itemStoreDetail.name
                            views.tvAddress.text = itemStoreDetail.idAddress?.address

                            Glide.with(this@DetailStoreActivity)
                                .load(Common.baseUrl + itemStoreDetail.imageQACode)
                                .placeholder(
                                    R.drawable.avatar_profile
                                ).error(R.drawable.avatar_profile).into(views.imgAvatar)

                            views.tvRate.text = itemStoreDetail.rate.toString()
                            views.tvPhone.text = itemStoreDetail.iduser?.phone
                            state.stateStore = Uninitialized
                        }
                    }
                }

            }

            is Loading -> {

                Timber.tag("AAAAAAAAA").e("getListService: Loading")
            }

            is Fail -> {
                state.stateStore = Uninitialized
                Timber.tag("AAAAAAAAA").e("getListService: Call Fail")
            }

            else -> {

            }
        }
    }

    fun getListRate(state: DetailStoreViewState) {
        when (state.stateListRateStore) {
            is Loading -> {
                views.shimmerRate.visibility = View.VISIBLE
                views.rcvRates.visibility = View.GONE
                views.shimmerRate.startShimmer()
                Timber.tag("AAAAAAAAAAAA").e("getListRate: loading ")
            }

            is Success -> {
                Timber.tag("AAAAAAAAAAAA").e("getListRate: Success ")
                state.stateListRateStore.invoke()?.let {
                    views.shimmerRate.visibility = View.GONE
                    views.rcvRates.visibility = View.VISIBLE
                    adapterRate.initData(it)
                    state.stateListRateStore = Uninitialized
                }
            }

            is Fail -> {
                views.shimmerRate.visibility = View.GONE
                views.rcvRates.visibility = View.VISIBLE
                state.stateListRateStore = Uninitialized
                Timber.tag("AAAAAAAAAAAA").e("getListRate: Fail ")
            }

            else -> {}
        }
    }

    fun getDetailUser(state: DetailStoreViewState) {
        when (state.stateDetailAccount) {
            is Success -> {
                runBlocking {
                    launch {
                        state.stateDetailAccount.invoke()?.let { account ->
                            val favoriteStores = account.favoriteStores
                            var store = intent.getStringExtra(Common.KEY_ID_STORE)
                            isStoreFavorited =
                                favoriteStores?.any { storeExtend -> storeExtend.id == store.toString() }
                                    ?: false
                            if (isStoreFavorited) {
                                views.imgAddFavoriteStore.setImageResource(R.drawable.ic_favorite_2)
                            } else {
                                views.imgAddFavoriteStore.setImageResource(R.drawable.ic_favorite_1)
                            }
                        }
                    }
                }
            }

            is Loading -> {
                Timber.tag("AAAAAAAAA").e("getDetailUser: Loading")
            }

            is Fail -> {
                Timber.tag("AAAAAAAAA").e("getDetailUser: Call Fail")
            }

            else -> {

            }
        }

    }

    override fun getBinding(): ActivityDetailStoreBinding =
        ActivityDetailStoreBinding.inflate(layoutInflater)

    override fun create(initialState: DetailStoreViewState): DetailStoreViewModel {
        return detailStoreFactory.create(initialState)
    }

}