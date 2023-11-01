package datn.fpoly.myapplication.utils

import datn.fpoly.myapplication.data.model.CategoryModel
import datn.fpoly.myapplication.data.model.StoreModel

object DataRaw {
    private val listStore: MutableList<StoreModel> = mutableListOf()
    private val listCategory: MutableList<CategoryModel> = mutableListOf()
    private var idCate: String =""
    fun setDataStore(list: MutableList<StoreModel>) {
        listStore.clear()
        listStore.addAll(list)
    }
    fun getDataStore(): MutableList<StoreModel> = listStore

    fun setDataCategory(list: MutableList<CategoryModel>) {
        listCategory.clear()
        listCategory.addAll(list)
    }
    fun getDataCategory(): MutableList<CategoryModel> = listCategory
    fun setDataIdCategory(id: String){
        idCate = id
    }
    fun getDataIdCategory(): String = idCate

}