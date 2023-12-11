package datn.fpoly.myapplication.ui.postService

import android.R
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.airbnb.mvrx.*
import com.bumptech.glide.Glide
import com.orhanobut.hawk.Hawk
import datn.fpoly.myapplication.AppApplication
import datn.fpoly.myapplication.core.BaseActivity
import datn.fpoly.myapplication.data.model.CategoryModel
import datn.fpoly.myapplication.data.model.post.PostService
import datn.fpoly.myapplication.databinding.ActivityAddSeviceStoreBinding
import datn.fpoly.myapplication.ui.adapter.AdapterPostAttribute
import datn.fpoly.myapplication.ui.adapter.AdapterSpinnerCate
import datn.fpoly.myapplication.utils.Common
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import com.github.dhaval2404.imagepicker.ImagePicker
import datn.fpoly.myapplication.data.model.StoreModel
import datn.fpoly.myapplication.utils.DataRaw
import datn.fpoly.myapplication.utils.DialogLoading
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

class AddServiceActivity : BaseActivity<ActivityAddSeviceStoreBinding>(),
    AddServiceViewModel.Factory {
    @Inject
    lateinit var factory: AddServiceViewModel.Factory
    private val viewModel: AddServiceViewModel by viewModel()
    private var imageUri: Uri? = null
    private val listAttributePost = mutableListOf<PostService.PostAttribute>()
    lateinit var adapterAttribute: AdapterPostAttribute
    private var unit = ""
    private var limitPriceSale: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as AppApplication).appComponent.inject(this);
        super.onCreate(savedInstanceState)
        Timber.tag("AAAAAAAAAAAA").e("onCreate: " + Hawk.get<StoreModel>(Common.KEY_STORE).id)
        views.toobar.icBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        views.toobar.icSearch.visibility = View.GONE
        viewModel.subscribe(this) {
            if (intent.getBooleanExtra(Common.KEY_UPDATE_SERVICE, false)) {
                updateStateUpdate(it)
            } else {
                updateStatePost(it)
            }
        }
        val listCate: MutableList<CategoryModel> = Hawk.get(Common.KEY_LIST_CATE)
        views.toobar.tvTitleTooobal.text = "Thêm dịch vụ"
        val adapterSpinner = AdapterSpinnerCate()
        views.spinnerCate.adapter = adapterSpinner
        adapterSpinner.setData(listCate)
        views.imgSelectImage.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start()
        }
        views.toobar.icSearch.visibility = View.GONE
        adapterAttribute =
            AdapterPostAttribute(intent.getBooleanExtra(Common.KEY_UPDATE_SERVICE, false))
        views.rcvListAttribute.adapter = adapterAttribute
        adapterAttribute.setListener(object : AdapterPostAttribute.PostAttributeListener {
            override fun onDelete(item: PostService.PostAttribute, index: Int) {
                adapterAttribute.getList().remove(item)
                adapterAttribute.notifyItemRemoved(index)
                adapterAttribute.notifyItemRangeRemoved(index, listAttributePost.size - 1)

            }
        })
        views.btnAddAttribute.setOnClickListener {
            if (views.edNameAttribute.text.toString().trim()
                    .isNotEmpty() && views.edPriceAttribute.text.toString().trim()
                    .isNotEmpty() && views.edPriceAttribute.text.toString().trim().toDouble() >= 0
            ) {
                val itemInsert = PostService.PostAttribute(
                    views.edNameAttribute.text.toString().trim(),
                    views.edPriceAttribute.text.toString().trim().toDouble()
                )
                adapterAttribute.insertItem(itemInsert)
                views.tvErrorAttribute.text = ""
                views.edNameAttribute.setText("")
                views.edPriceAttribute.setText("")

            } else {
                if (views.edNameAttribute.text.toString().trim().isEmpty()) {
                    views.tvErrorAttribute.text = "Tên dịch vụ thêm không được để trống"
                } else if (views.edPriceAttribute.text.toString().trim().isEmpty()) {
                    views.tvErrorAttribute.text = "Giá dịch vụ thêm không được để trống"
                } else if (views.edPriceAttribute.text.toString().trim().toDouble() < 0) {
                    views.tvErrorAttribute.text = "Giá dịch vụ thêm không được nhỏ hơn 0"
                }
            }

        }
        val items = mutableListOf<String>()
        items.add("Chọn đơn vị")
        items.add("%")
        items.add("VNĐ")
        val adapter = ArrayAdapter(this, R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        views.spinnerUnitSale.adapter = adapter
        views.spinnerUnitSale.setSelection(0)
        views.spinnerUnitSale.isSelected = true
        views.spinnerUnitSale.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                when (p2) {
                    0->{
                        views.edPriceSale.setText("")
                    }
                    1 -> {
                        limitPriceSale = 100
                    }

                    2 -> {
                        limitPriceSale = -1
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        views.btnUnit1.setOnClickListener {
            unit = views.btnUnit1.text.toString()
            views.btnUnit1.isChecked = true
            views.btnUnit2.isChecked = false
            views.btnUnit3.isChecked = false
            views.btnUnit4.isChecked = false
        }
        views.btnUnit2.setOnClickListener {
            unit = views.btnUnit2.text.toString()
            views.btnUnit1.isChecked = false
            views.btnUnit2.isChecked = true
            views.btnUnit3.isChecked = false
            views.btnUnit4.isChecked = false
        }
        views.btnUnit3.setOnClickListener {
            unit = views.btnUnit3.text.toString()
            views.btnUnit1.isChecked = false
            views.btnUnit2.isChecked = false
            views.btnUnit3.isChecked = true
            views.btnUnit4.isChecked = false
        }
        views.btnUnit4.setOnClickListener {
            unit = views.btnUnit4.text.toString()
            views.btnUnit1.isChecked = false
            views.btnUnit2.isChecked = false
            views.btnUnit3.isChecked = false
            views.btnUnit4.isChecked = true
        }
        views.btnInsertService.setOnClickListener {

            if (intent.getBooleanExtra(Common.KEY_UPDATE_SERVICE, false)) {
                val modelUpdate = DataRaw.getModelUpdateService()
                if (validateUpdate()) {
                    modelUpdate?.id?.let { it1 -> updateService(it1) }
                }
            } else {
                if (validate()) {
                    postService()
                }

            }


        }
        if (intent.getBooleanExtra(Common.KEY_UPDATE_SERVICE, false)) {
            val modelUpdate = DataRaw.getModelUpdateService()
            for (index in 0 until listCate.size) {
                if (modelUpdate?.idCategory?.id == listCate[index].id) {
                    views.spinnerCate.setSelection(index)
                    views.spinnerCate.isSelected = true
                    break
                }
            }
            if (modelUpdate?.idSale != null) {
                for (index in 0 until items.size) {
                    if (items[index] == modelUpdate.idSale?.unit) {
                        views.spinnerUnitSale.setSelection(index)
                        views.spinnerUnitSale.isSelected = true
                        break;
                    }
                }
                views.edPriceSale.setText(modelUpdate.idSale?.value.toString())
            }
            val listAttribute = modelUpdate?.attributeList
            val listAttributePost = mutableListOf<PostService.PostAttribute>()
            for (index in 0 until listAttribute?.size!!) {
                listAttributePost.add(PostService.PostAttribute(listAttribute[index].name, listAttribute[index].price))
            }
            adapterAttribute.initAttribute(listAttributePost)
            when (modelUpdate.unit) {
                views.btnUnit1.text -> {
                    unit = views.btnUnit1.text.toString()
                    views.btnUnit1.isChecked = true
                    views.btnUnit2.isChecked = false
                    views.btnUnit3.isChecked = false
                    views.btnUnit4.isChecked = false
                }

                views.btnUnit2.text -> {
                    unit = views.btnUnit2.text.toString()
                    views.btnUnit1.isChecked = false
                    views.btnUnit2.isChecked = true
                    views.btnUnit3.isChecked = false
                    views.btnUnit4.isChecked = false
                }

                views.btnUnit3.text -> {
                    unit = views.btnUnit3.text.toString()
                    views.btnUnit1.isChecked = false
                    views.btnUnit2.isChecked = false
                    views.btnUnit3.isChecked = true
                    views.btnUnit4.isChecked = false
                }

                views.btnUnit4.text -> {
                    unit = views.btnUnit4.text.toString()
                    views.btnUnit1.isChecked = false
                    views.btnUnit2.isChecked = false
                    views.btnUnit3.isChecked = false
                    views.btnUnit4.isChecked = true
                }
            }
            modelUpdate.isActive?.let {
                views.swIsActive.isSelected = it
            }
            views.edNameService.setText(modelUpdate.name)
            views.edPriceService.setText(modelUpdate.price.toString())
            Glide.with(views.imgSelectImage).load(Common.baseUrl + "" + modelUpdate.image)
                .into(views.imgSelectImage)
            views.btnInsertService.setText("Lưu")
            views.swIsActive.isChecked= modelUpdate.isActive!!
            views.swIsActive.visibility = View.VISIBLE
            views.tvIsActive.visibility = View.VISIBLE
        } else {
            unit = views.btnUnit1.text.toString()
            views.swIsActive.visibility = View.GONE
            views.tvIsActive.visibility = View.GONE
            views.btnInsertService.setText("Thêm")
        }

    }

    private fun validate(): Boolean {
        var isValidate = false
        if (views.edNameService.text.toString().isNotEmpty() && views.edPriceService.text.toString()
                .isNotEmpty()
        ) {
            if (views.edPriceSale.text.toString().isEmpty()) {
                isValidate = true
                views.tvErrorPriceSale.text = ""
            } else {
                if (views.spinnerUnitSale.selectedItem.toString() == "%") {
                    if (views.edPriceSale.text.toString().toDouble() < 0) {
                        isValidate = false
                        views.tvErrorPriceSale.text = "Giá trị không được nhỏ hơn 0"
                    } else if (views.edPriceSale.text.toString().toDouble() > 100) {
                        isValidate = false
                        views.tvErrorPriceSale.text = "Giá trị không được vượt quá 100%"
                    } else {
                        views.tvErrorPriceSale.text = ""
                        isValidate = true
                    }
                } else {
                    if (views.edPriceSale.text.toString().toDouble() < 0) {
                        isValidate = false
                        views.tvErrorPriceSale.text = "Giá trị không được nhỏ hơn 0"
                    } else if (views.edPriceSale.text.toString()
                            .toDouble() > views.edPriceService.text.toString().toDouble()
                    ) {
                        isValidate = false
                        views.tvErrorPriceSale.text = "Giá sale không được lớn hơn giá của dịch vụ"
                    } else {
                        views.tvErrorPriceSale.text = ""
                        isValidate = true
                    }
                }
            }
            views.tvErrorPriceService.text = ""
            views.tvErrorNameService.text = ""
        } else {
            if (views.edNameService.text.toString().trim().isEmpty()) {
                views.tvErrorNameService.text = "Tên dịch vụ không được để trống"
            } else {
                views.tvErrorNameService.text = ""
            }
            if (views.edPriceService.text.toString().isEmpty()) {
                views.tvErrorPriceService.text = "Giá dịch vụ không được để trống"
            } else if (views.edPriceService.text.toString().toDouble() < 0) {
                views.tvErrorPriceService.text = "Giá dịch vụ không được nhỏ hơn 0"
            } else {
                views.tvErrorPriceService.text = ""
            }
            isValidate = false

        }
        return isValidate
    }

    private fun validateUpdate(): Boolean {
        var isValidate = false
        if (views.edNameService.text.toString().isNotEmpty() && views.edPriceService.text.toString()
                .isNotEmpty()
        ) {
            if (views.edPriceSale.text.toString().isEmpty()) {
                isValidate = true
                views.tvErrorPriceSale.text = ""
            } else {
                if (views.spinnerUnitSale.selectedItem.toString() == "%") {
                    if (views.edPriceSale.text.toString().toDouble() < 0) {
                        isValidate = false
                        views.tvErrorPriceSale.text = "Giá trị không được nhỏ hơn 0"
                    } else if (views.edPriceSale.text.toString().toDouble() > 100) {
                        isValidate = false
                        views.tvErrorPriceSale.text = "Giá trị không được vượt quá 100%"
                    } else {
                        views.tvErrorPriceSale.text = ""
                        isValidate = true
                    }
                } else {
                    if (views.edPriceSale.text.toString().toDouble() < 0) {
                        isValidate = false
                        views.tvErrorPriceSale.text = "Giá trị không được nhỏ hơn 0"
                    } else if (views.edPriceSale.text.toString()
                            .toDouble() > views.edPriceService.text.toString().toDouble()
                    ) {
                        isValidate = false
                        views.tvErrorPriceSale.text = "Giá sale không được lớn hơn giá của dịch vụ"
                    } else {
                        views.tvErrorPriceSale.text = ""
                        isValidate = true
                    }
                }
            }
            views.tvErrorPriceService.text = ""
            views.tvErrorNameService.text = ""
        } else {
            if (views.edNameService.text.toString().trim().isEmpty()) {
                views.tvErrorNameService.text = "Tên dịch vụ không được để trống"
            } else {
                views.tvErrorNameService.text = ""
            }
            if (views.edPriceService.text.toString().isEmpty()) {

                views.tvErrorPriceService.text = "Giá dịch vụ không được để trống"
            } else if (views.edPriceService.text.toString().toDouble() < 0) {
                views.tvErrorPriceService.text = "Giá dịch vụ không được nhỏ hơn 0"
            } else {
                views.tvErrorPriceService.text = ""
            }
            isValidate = false
        }
        return isValidate
    }

    private fun postService() {
        if (imageUri != null) {
            val file = File(imageUri!!.path!!) // Chuyển URI thành File
            val requestFile = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            val image = MultipartBody.Part.createFormData("image", file.name, requestFile)
            val name = views.edNameService.text.toString().trim()
                .toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val cate = views.spinnerCate.selectedItem as CategoryModel
            val idCate =
                cate.id.toString().trim().toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val idStore =
                Hawk.get<StoreModel>(Common.KEY_STORE).id.toString()
                    .toRequestBody("multipart/form-data".toMediaTypeOrNull())
//        val idStore ="65475330501201abab1bd500".toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val listAttribute = adapterAttribute.getList()
            val unitSale = views.spinnerUnitSale.selectedItem.toString()
                .toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val valueSale = views.edPriceSale.text.toString().trim()
                .toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val priceServie = views.edPriceService.text.toString().trim()
                .toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val unit = unit.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val isActive = views.swIsActive.isSelected.toString()
                .toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val stringMap = HashMap<String, PostService.PostAttribute>()
            listAttribute.forEachIndexed { index, item ->
                stringMap["attributeList[$index]"] = item
            }
            if (views.spinnerUnitSale.selectedItemPosition==0
            ) {
                idStore?.let {
                    AddServiceViewAction.AddService(
                        image,
                        name,
                        priceServie,
                        stringMap,
                        isActive,
                        unit,
                        idCate,
                        it,
                        null,
                        null
                    )
                }?.let {
                    viewModel.handle(
                        it
                    )
                }
            } else {
                idStore?.let {
                    AddServiceViewAction.AddService(
                        image,
                        name,
                        priceServie,
                        stringMap,
                        isActive,
                        unit,
                        idCate,
                        it,
                        unitSale,
                        valueSale
                    )
                }?.let {
                    viewModel.handle(
                        it
                    )
                }
            }

        } else {
            val name = views.edNameService.text.toString().trim()
                .toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val cate = views.spinnerCate.selectedItem as CategoryModel
            val idCate =
                cate.id.toString().trim().toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val idStore =
                Hawk.get<StoreModel>(Common.KEY_STORE).id.toString()
                    .toRequestBody("multipart/form-data".toMediaTypeOrNull())
//        val idStore ="65475330501201abab1bd500".toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val listAttribute = adapterAttribute.getList()
            val unitSale = views.spinnerUnitSale.selectedItem.toString()
                .toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val valueSale = views.edPriceSale.text.toString().trim()
                .toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val priceServie = views.edPriceService.text.toString().trim()
                .toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val unit = unit.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val isActive = true.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val stringMap = HashMap<String, PostService.PostAttribute>()
            listAttribute.forEachIndexed { index, item ->
                stringMap["attributeList[$index]"] = item
            }
            Log.e("AAAAAAAAAA", "postService:unitSale " + unitSale)
            Log.e("AAAAAAAAAA", "postService:valueSale " + valueSale)
            if (views.spinnerUnitSale.selectedItemPosition==0) {
                Log.e("AAAAAAAAAAAAAAAA", "postService: sale null" )
                idStore?.let {
                    AddServiceViewAction.AddService(
                        null,
                        name,
                        priceServie,
                        stringMap,
                        isActive,
                        unit,
                        idCate,
                        it,
                        null,
                        null
                    )
                }?.let {
                    viewModel.handle(
                        it
                    )
                }
            } else {
                Log.e("AAAAAAAAAAAAAAAA", "postService: sale not null" )
                idStore?.let {
                    AddServiceViewAction.AddService(
                        null,
                        name,
                        priceServie,
                        stringMap,
                        isActive,
                        unit,
                        idCate,
                        it,
                        unitSale,
                        valueSale
                    )
                }?.let {
                    viewModel.handle(
                        it
                    )
                }
            }

        }


    }

    private fun updateService(idService: String) {
        if (imageUri != null) {
            val file = File(imageUri!!.path!!) // Chuyển URI thành File
            val requestFile = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            val image = MultipartBody.Part.createFormData("image", file.name, requestFile)
            val name = views.edNameService.text.toString().trim().toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val cate = views.spinnerCate.selectedItem as CategoryModel
            val idCate =
                cate.id.toString().trim().toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val idStore =
                Hawk.get<StoreModel>(Common.KEY_STORE).id.toString()
                    .toRequestBody("multipart/form-data".toMediaTypeOrNull())
//        val idStore ="65475330501201abab1bd500".toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val listAttribute = adapterAttribute.getList()
            val unitSale = views.spinnerUnitSale.selectedItem.toString()
                .toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val valueSale = views.edPriceSale.text.toString().trim()
                .toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val priceServie = views.edPriceService.text.toString().trim()
                .toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val unit = unit.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val isActive = views.swIsActive.isSelected.toString()
                .toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val stringMap = HashMap<String, PostService.PostAttribute>()
            listAttribute.forEachIndexed { index, item ->
                stringMap["attributeList[$index]"] = item
            }
            if (views.spinnerUnitSale.selectedItemPosition==0) {
                idStore.let {
                    AddServiceViewAction.UpdateService(
                        idService,
                        image,
                        name,
                        priceServie,
                        stringMap,
                        isActive,
                        unit,
                        idCate,
                        it,
                        null,
                        null
                    )
                }?.let {
                    viewModel.handle(
                        it
                    )
                }
            } else {
                idStore.let {
                    AddServiceViewAction.UpdateService(
                        idService,
                        image,
                        name,
                        priceServie,
                        stringMap,
                        isActive,
                        unit,
                        idCate,
                        it,
                        unitSale,
                        valueSale
                    )
                }?.let {
                    viewModel.handle(
                        it
                    )
                }
            }

        } else {
            val name = views.edNameService.text.toString().trim().toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val cate = views.spinnerCate.selectedItem as CategoryModel
            val idCate =
                cate.id.toString().trim().toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val idStore =
                Hawk.get<StoreModel>(Common.KEY_STORE).id.toString()
                    .toRequestBody("multipart/form-data".toMediaTypeOrNull())
//        val idStore ="65475330501201abab1bd500".toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val listAttribute = adapterAttribute.getList()
            val unitSale = views.spinnerUnitSale.selectedItem.toString()
                .toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val valueSale = views.edPriceSale.text.toString().trim()
                .toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val priceServie = views.edPriceService.text.toString().trim()
                .toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val unit = unit.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val isActive = views.swIsActive.isSelected.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val stringMap = HashMap<String, PostService.PostAttribute>()
            listAttribute.forEachIndexed { index, item ->
                stringMap["attributeList[$index]"] = item
            }
            if (views.spinnerUnitSale.selectedItemPosition==0) {
                idStore.let {
                    AddServiceViewAction.UpdateService(
                        idService,
                        null,
                        name,
                        priceServie,
                        stringMap,
                        isActive,
                        unit,
                        idCate,
                        it,
                        null,
                        null
                    )
                }?.let {
                    viewModel.handle(
                        it
                    )
                }
            } else {
                idStore.let {
                    AddServiceViewAction.UpdateService(
                        idService,
                        null,
                        name,
                        priceServie,
                        stringMap,
                        isActive,
                        unit,
                        idCate,
                        it,
                        unitSale,
                        valueSale
                    )
                }?.let {
                    viewModel.handle(
                        it
                    )
                }
            }

        }


    }

    fun updateStatePost(state: AddServiceViewState) {

        when (state.stateService) {
            is Loading -> {
                DialogLoading.showDialog(this)
                Timber.tag("AAAAAAAAAAAAAA").e("updateStatePost: Loading")
            }

            is Success -> {
                runBlocking {
                    launch {
                        DialogLoading.hideDialog()
                        state.stateService.invoke()?.let {
                            Toast.makeText(
                                this@AddServiceActivity,
                                "Thêm dịch vụ thành công",
                                Toast.LENGTH_SHORT
                            ).show()
                            setResult(Common.CODE_LOAD_DATA)
                            onBackPressedDispatcher.onBackPressed()
                        }


                    }
                }

            }

            is Fail -> {
                DialogLoading.hideDialog()
                Timber.tag("AAAAAAAAAAAAAA").e("updateStatePost: Fail")
            }
            else -> {}
        }

    }

    fun updateStateUpdate(state: AddServiceViewState) {
        when (state.stateServiceUpdate) {
            is Loading -> {
                DialogLoading.showDialog(this)
                Timber.tag("AAAAAAAAAAAAAA").e("updateStatePost: Loading")
            }

            is Success -> {
                runBlocking {
                    launch {
                        DialogLoading.hideDialog()
                        state.stateServiceUpdate.invoke()?.let {
                            Toast.makeText(this@AddServiceActivity, "Cập nhật dịch vụ thành công", Toast.LENGTH_SHORT).show()
                            setResult(Common.CODE_LOAD_DATA)
                            onBackPressedDispatcher.onBackPressed()
                        }
                    }
                }

            }

            is Fail -> {
                DialogLoading.hideDialog()
                Timber.tag("AAAAAAAAAAAAAA").e("updateStatePost: Fail")
            }

            else -> {}
        }

    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            imageUri = data!!.data
            Glide.with(this).load(imageUri).into(views.imgSelectImage)
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getBinding(): ActivityAddSeviceStoreBinding {
        return ActivityAddSeviceStoreBinding.inflate(layoutInflater)

    }

    override fun create(initialState: AddServiceViewState): AddServiceViewModel {
        return factory.create(initialState)
    }
}