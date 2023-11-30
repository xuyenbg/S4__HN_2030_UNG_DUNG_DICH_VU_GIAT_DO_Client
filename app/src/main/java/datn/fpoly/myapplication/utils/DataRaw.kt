package datn.fpoly.myapplication.utils

import android.content.Context
import android.view.View
import android.view.animation.AnimationUtils
import datn.fpoly.myapplication.R
import datn.fpoly.myapplication.data.model.CategoryModel
import datn.fpoly.myapplication.data.model.ServiceExtend
import datn.fpoly.myapplication.data.model.StoreModel
import datn.fpoly.myapplication.data.model.StoreNearplaceModel

object DataRaw {
    private val listStore: MutableList<StoreNearplaceModel> = mutableListOf()
    private val listCategory: MutableList<CategoryModel> = mutableListOf()
    private var idCate: String =""
    private var modelUpdateService: ServiceExtend?= null
    fun setDataStore(list: MutableList<StoreNearplaceModel>) {
        listStore.clear()
        listStore.addAll(list)
    }
    fun getDataStore(): MutableList<StoreNearplaceModel> = listStore

    fun setDataCategory(list: MutableList<CategoryModel>) {
        listCategory.clear()
        listCategory.addAll(list)
    }
    fun getDataCategory(): MutableList<CategoryModel> = listCategory
    fun setDataIdCategory(id: String){
        idCate = id
    }
    fun getDataIdCategory(): String = idCate
    fun animStart(view: View, context: Context){
        view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.anim_item_bot_na_custom))
    }
    fun setModelUpdateService(serviceExtend: ServiceExtend){
        modelUpdateService = serviceExtend
    }
    fun getModelUpdateService(): ServiceExtend? = modelUpdateService

}