package datn.fpoly.myapplication.ui.seeMore

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.orhanobut.hawk.Hawk
import datn.fpoly.myapplication.core.BaseActivity
import datn.fpoly.myapplication.data.model.CategoryModel
import datn.fpoly.myapplication.data.model.StoreModel
import datn.fpoly.myapplication.databinding.ActivitySeeMoreBinding
import datn.fpoly.myapplication.ui.adapter.AdapterCategory
import datn.fpoly.myapplication.ui.adapter.AdapterStore
import datn.fpoly.myapplication.ui.detailstore.DetailStoreActivity
import datn.fpoly.myapplication.utils.Common
import datn.fpoly.myapplication.utils.DataRaw
import datn.fpoly.myapplication.utils.ItemSpacingDecoration

class SeeMoreActivity : BaseActivity<ActivitySeeMoreBinding>() {
    private lateinit var adapterCate: AdapterCategory
    private lateinit var adapterStore: AdapterStore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        views.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        when(intent.getIntExtra(Common.KEY_SEE_MORE, 0)){
            1->{
                setUpViewListCategory()

            }
            2->{
                setUpViewListStore()

            }
            else->{}

        }
    }
    private fun setUpViewListCategory(){
        views.tvTitle.setText("Danh sách Loại dịch vụ")
        adapterCate = AdapterCategory(0, true)
        views.rcvList.adapter = adapterCate
        adapterCate.updateData(DataRaw.getDataCategory())
        adapterCate.setListener(object : AdapterCategory.CategoryListener{
            override fun onClickCate(categoryModel: CategoryModel) {

            }
        })
    }
    private fun setUpViewListStore(){
        views.tvTitle.setText("Danh sách Cửa Hàng")
        val layoutManager =GridLayoutManager(this, 2)
        views.rcvList.layoutManager = layoutManager
        adapterStore = AdapterStore(0)
        views.rcvList.adapter = adapterStore
        views.rcvList.addItemDecoration(ItemSpacingDecoration(32))
        adapterStore.setData(DataRaw.getDataStore())
        adapterStore.setListener(object : AdapterStore.StoreListener{
            override fun onClickStoreListener(storeModel: StoreModel) {
                Hawk.put(Common.KEY_STORE_DETAIL, storeModel)
                startActivity(Intent(this@SeeMoreActivity, DetailStoreActivity::class.java))
            }
        })
    }

    override fun getBinding(): ActivitySeeMoreBinding {
        return ActivitySeeMoreBinding.inflate(layoutInflater)
    }
}