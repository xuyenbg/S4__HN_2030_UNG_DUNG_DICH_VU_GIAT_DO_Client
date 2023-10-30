package datn.fpoly.myapplication.utils

import datn.fpoly.myapplication.data.model.CategoryModel
import datn.fpoly.myapplication.data.model.StoreModel

object GetListRaw {
    private val listStore: MutableList<StoreModel> = mutableListOf()
    private val listCategory: MutableList<CategoryModel> = mutableListOf()
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


}