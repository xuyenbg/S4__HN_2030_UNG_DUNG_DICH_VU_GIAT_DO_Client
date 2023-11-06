package datn.fpoly.myapplication.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import datn.fpoly.myapplication.R
import datn.fpoly.myapplication.data.model.CategoryModel

class AdapterSpinnerCate: BaseAdapter() {
    private val listCate = mutableListOf<CategoryModel>()
    fun setData(list: MutableList<CategoryModel>){
        this.listCate.clear()
        this.listCate.addAll(list)
        notifyDataSetChanged()
    }
    override fun getCount(): Int = listCate.size

    override fun getItem(p0: Int): Any = listCate[p0]

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val layoutInflater = LayoutInflater.from(p2?.context)
        val rowView=layoutInflater.inflate(R.layout.item_spinner_category, null)
        val tv_name_cate = rowView.findViewById<TextView>(R.id.tv_name_cate)
        tv_name_cate.text = listCate[p0].name
        return rowView
    }
}