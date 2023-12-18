package datn.fpoly.myapplication.ui.myshop

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.viewModel
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.orhanobut.hawk.Hawk
import datn.fpoly.myapplication.AppApplication
import datn.fpoly.myapplication.R
import datn.fpoly.myapplication.core.BaseActivity
import datn.fpoly.myapplication.data.model.StoreModel
import datn.fpoly.myapplication.databinding.ActivityMyShopBinding
import datn.fpoly.myapplication.ui.detailstore.DetailStoreViewAction
import datn.fpoly.myapplication.ui.detailstore.DetailStoreViewModel
import datn.fpoly.myapplication.ui.detailstore.DetailStoreViewState
import datn.fpoly.myapplication.ui.homeStore.HomeStoreViewModel
import datn.fpoly.myapplication.ui.map.PickPossitionInMapActivity
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
import kotlin.math.log

class MyShopActivity : BaseActivity<ActivityMyShopBinding>(), MyShopViewModel.Factory {
    @Inject
    lateinit var myShopViewModelFactory: MyShopViewModel.Factory
    private val viewModel: MyShopViewModel by viewModel()
    private var address: String? = null
    private var latiu = 0.0
    private var longtiu = 0.0
    private var imageUri: Uri? = null
    private var idStore: String? = null
    override fun getBinding(): ActivityMyShopBinding = ActivityMyShopBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as AppApplication).appComponent.inject(this);
        super.onCreate(savedInstanceState)
        idStore = Hawk.get<StoreModel>(Common.KEY_STORE).id

        viewModel.handle(MyShopViewAction.GetStoreById(idStore!!))
        views.icAvtshop.isEnabled = false
        views.tvUpdate.setOnClickListener {
            if (views.tvUpdate.text.toString() == "Hủy") {
                views.apply {
                    tvUpdate.text = "Sửa"
                    tvAddressShop.isEnabled = false
                    tvNameShop.isEnabled = false
                    btnUpdate.visibility = View.GONE
                    icAvtshop.isEnabled = false
                }
            } else {
                views.apply {
                    tvUpdate.text = "Hủy"
                    tvAddressShop.isEnabled = true
                    tvNameShop.isEnabled = true
                    btnUpdate.visibility = View.VISIBLE
                    icAvtshop.isEnabled = true
                }
            }
        }

        viewModel.subscribe(this) {
            getStore(it)
            getStoreUpdate(it)
        }
        views.icAvtshop.setOnClickListener {

        }
        views.imgBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        views.tvAddressShop.setOnClickListener {
            startActivity(Intent(this, PickPossitionInMapActivity::class.java))
        }
        views.btnUpdate.setOnClickListener {
            validate()
        }
        views.icAvtshop.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start()
        }
    }

    override fun onResume() {
        super.onResume()
        address = PickPossitionInMapActivity.dataAdress.pick_address
//        latiu = PickPossitionInMapActivity.dataAdress.latitude
//        longtiu = PickPossitionInMapActivity.dataAdress.longitude

        latiu = Common.getMyLocationLatitude(this).toDouble()
        longtiu = Common.getMyLocationLongitude(this).toDouble()
        Log.e("AAAAAAAAAAAA", "onResume: "+latiu+":"+longtiu )
        views.tvAddressShop.setText(address)
    }

    private fun validate() {
        if (views.tvNameShop.text.toString().trim()
                .isNotEmpty()
        ) {
            Dialog_Loading.getInstance().show(supportFragmentManager, "Loading add post")
            insert()
            views.tvErrorContent.text = ""
            views.tvErrorContent.text = ""

        } else {
            if (views.tvNameShop.text.toString().trim().isEmpty()) {
                views.tvErrorContent.visibility = View.VISIBLE
                views.tvErrorContent.text = "Tiêu đề không được để trống"
            } else {
                views.tvErrorContent.text = ""
            }
        }
    }

    private fun insert() {
        val name = views.tvNameShop.text.toString()
        val idUser = Hawk.get<StoreModel>(Common.KEY_STORE).iduser?.id

        val nameRes = name.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val idUserRes = idUser!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val iDefaultRes =
            true.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())

        if (imageUri != null) {
            val file = File(imageUri!!.path!!)
            val requestFile = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            val image = MultipartBody.Part.createFormData("image", file.name, requestFile)
            if (address != "") {
                val addressRes = address?.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                val latRes =
                    latiu.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
                val longRes =
                    longtiu.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
                idStore?.let {
                    addressRes?.let { it1 ->
                        MyShopViewAction.UpdateStore(
                            it, nameRes,
                            it1, latRes, longRes, iDefaultRes, idUserRes, image
                        )
                    }
                }
                    ?.let { viewModel.handle(it) }
            } else {
                idStore?.let { MyShopViewAction.UpdateStoreOne(it, nameRes, image) }
                    ?.let { viewModel.handle(it) }
            }
        } else {
            if (address != "") {
                val addressRes = address?.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                val latRes =
                    latiu.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
                val longRes =
                    longtiu.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
                idStore?.let {
                    addressRes?.let { it1 ->
                        MyShopViewAction.UpdateStore(
                            it, nameRes,
                            it1, latRes, longRes, iDefaultRes, idUserRes, null
                        )
                    }
                }
                    ?.let { viewModel.handle(it) }
            } else {
                idStore?.let { MyShopViewAction.UpdateStoreOne(it, nameRes, null) }
                    ?.let { viewModel.handle(it) }
            }


        }

    }

    fun getStore(state: MyShopViewState) {
        when (state.statStoreDetail) {
            is Success -> {
                runBlocking {
                    launch {
                        state.statStoreDetail.invoke()?.let {
                            initView(it)
                        }
                    }
                }
            }

            is Loading -> {
                Timber.tag("AAAAAAAAA").e("getListService: Loading")
            }

            is Fail -> {
                Timber.tag("AAAAAAAAA").e("getListService: Call Fail")
            }

            else -> {

            }
        }
    }

    fun getStoreUpdate(state: MyShopViewState) {
        when (state.statUpdateStore) {
            is Success -> {
                runBlocking {
                    launch {
                        state.statUpdateStore.invoke()?.let {

                            Hawk.put(Common.KEY_STORE, it)
                            setResult(Common.CODE_LOAD_DATA)
                            onBackPressedDispatcher.onBackPressed()
                            finish()
                            Toast.makeText(
                                this@MyShopActivity,
                                "Cập Nhật Thành Công ",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }

            is Loading -> {
                Timber.tag("AAAAAAAAA").e("getListService: Loading")
            }

            is Fail -> {
                Timber.tag("AAAAAAAAA").e("getListService: Call Fail")
            }

            else -> {

            }
        }
    }

    private fun initView(storeModel: StoreModel) {
        views.apply {
            tvNameShop.setText(storeModel.name)
            tvAddressShop.setText(storeModel.idAddress!!.address)
            tvPhoneShop.setText(storeModel.iduser!!.phone)
            tvRateShop.text = storeModel.rate.toString()
            Log.d("MyShopActivity", "initView: ${storeModel.imageQACode}")
            if (storeModel.imageQACode == null) {
                icAvtshop.setImageResource(R.drawable.avatar_profile)
            } else {
                Glide.with(this@MyShopActivity).load(Common.baseUrl + storeModel.imageQACode)
                    .placeholder(R.drawable.avatar_profile)
                    .into(icAvtshop)
            }
        }
    }

    override fun create(initialState: MyShopViewState): MyShopViewModel {
        return myShopViewModelFactory.create(initialState)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            imageUri = data!!.data
            Glide.with(this).load(imageUri).into(views.icAvtshop)
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

}