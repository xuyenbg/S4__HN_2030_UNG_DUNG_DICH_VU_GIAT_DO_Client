package datn.fpoly.myapplication.ui.homeStore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import datn.fpoly.myapplication.AppApplication
import datn.fpoly.myapplication.R
import datn.fpoly.myapplication.core.BaseActivity
import datn.fpoly.myapplication.databinding.ActivityHomeStoreBinding
import datn.fpoly.myapplication.ui.adapter.AdapterViewPage
import datn.fpoly.myapplication.ui.fragment.cart.CartFragment
import datn.fpoly.myapplication.ui.fragment.fragmentOrder.FragmentOrder
import datn.fpoly.myapplication.ui.fragment.homeStore.FragmentHomeStore
import datn.fpoly.myapplication.ui.fragment.homeUser.HomeUserFragment
import datn.fpoly.myapplication.ui.fragment.postclient.PostClientFragment
import datn.fpoly.myapplication.ui.fragment.settingStore.FragmentSettingStore
import datn.fpoly.myapplication.ui.fragment.settingStore.setting.FragmentSetting
import datn.fpoly.myapplication.ui.poststore.FragmentPostStore

class HomeStoreActivity : BaseActivity<ActivityHomeStoreBinding>() {
    private lateinit var adapterVp: AdapterViewPage
    private val listFragment= mutableListOf<Fragment>()
    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as AppApplication).appComponent.inject(this);
        super.onCreate(savedInstanceState)
        setContentView(views.root)
        setViewNavigation()


    }
    fun setViewNavigation(){
        listFragment.add(0, FragmentHomeStore())
//        listFragment.add(1, CartFragment())
        listFragment.add(2, FragmentPostStore())
        listFragment.add(3,FragmentSettingStore())
        adapterVp = AdapterViewPage(listFragment, this)
        views.vp2Home.adapter = adapterVp
        views.vp2Home.isUserInputEnabled= false
        views.vp2Home.setCurrentItem(0, true)
        views.viewBgItem.visibility = View.VISIBLE
        views.viewBgItem2.visibility = View.INVISIBLE
        views.viewBgItem3.visibility = View.INVISIBLE
        views.viewBgItem4.visibility = View.INVISIBLE
        views.tvHome.setTextAppearance(R.style.item_bottom_avigation_custom_selected)
        views.tvOrder.setTextAppearance(R.style.item_bottom_avigation_custom)
        views.tvPost.setTextAppearance(R.style.item_bottom_avigation_custom)
        views.tvProfile.setTextAppearance(R.style.item_bottom_avigation_custom)
        views.llItem1.setOnClickListener {
            views.vp2Home.setCurrentItem(0, true)
            views.viewBgItem.visibility = View.VISIBLE
            views.viewBgItem2.visibility = View.INVISIBLE
            views.viewBgItem3.visibility = View.INVISIBLE
            views.viewBgItem4.visibility = View.INVISIBLE
            animStart(views.viewBgItem)
            views.tvHome.setTextAppearance(R.style.item_bottom_avigation_custom_selected)
            views.tvOrder.setTextAppearance(R.style.item_bottom_avigation_custom)
            views.tvPost.setTextAppearance(R.style.item_bottom_avigation_custom)
            views.tvProfile.setTextAppearance(R.style.item_bottom_avigation_custom)
            views.icHome.setImageResource(R.drawable.home_selected)
            views.icOrder.setImageResource(R.drawable.ic_store)
            views.icPost.setImageResource(R.drawable.chat)
            views.icProfile.setImageResource(R.drawable.profile_gray)
        }
        views.llItem3.setOnClickListener {
            views.vp2Home.setCurrentItem(1, true)
            views.viewBgItem2.visibility = View.VISIBLE

            views.viewBgItem.visibility = View.INVISIBLE
            views.viewBgItem3.visibility = View.INVISIBLE
            views.viewBgItem4.visibility = View.INVISIBLE
            animStart(views.viewBgItem2)
            views.tvHome.setTextAppearance(R.style.item_bottom_avigation_custom)
            views.tvOrder.setTextAppearance(R.style.item_bottom_avigation_custom_selected)
            views.tvPost.setTextAppearance(R.style.item_bottom_avigation_custom)
            views.tvProfile.setTextAppearance(R.style.item_bottom_avigation_custom)
            views.icHome.setImageResource(R.drawable.home)

            views.icOrder.setImageResource(R.drawable.order_selected)
            views.icPost.setImageResource(R.drawable.chat)
            views.icProfile.setImageResource(R.drawable.profile_gray)
        }
        views.llItem4.setOnClickListener {
            views.vp2Home.setCurrentItem(2, true)
            views.viewBgItem3.visibility = View.VISIBLE

            views.viewBgItem2.visibility = View.INVISIBLE
            views.viewBgItem.visibility = View.INVISIBLE
            views.viewBgItem4.visibility = View.INVISIBLE
            animStart(views.viewBgItem3)
            views.tvHome.setTextAppearance(R.style.item_bottom_avigation_custom)
            views.tvOrder.setTextAppearance(R.style.item_bottom_avigation_custom)
            views.tvPost.setTextAppearance(R.style.item_bottom_avigation_custom_selected)
            views.tvProfile.setTextAppearance(R.style.item_bottom_avigation_custom)
            views.icHome.setImageResource(R.drawable.home)

            views.icOrder.setImageResource(R.drawable.ic_store)
            views.icPost.setImageResource(R.drawable.chat_selected)
            views.icProfile.setImageResource(R.drawable.profile_gray)
        }
        views.llItem5.setOnClickListener {
            views.vp2Home.setCurrentItem(3, true)
            views.viewBgItem4.visibility = View.VISIBLE
            views.viewBgItem2.visibility = View.INVISIBLE
            views.viewBgItem3.visibility = View.INVISIBLE
            views.viewBgItem.visibility = View.INVISIBLE
            animStart(views.viewBgItem4)
            views.tvHome.setTextAppearance(R.style.item_bottom_avigation_custom)
            views.tvOrder.setTextAppearance(R.style.item_bottom_avigation_custom)
            views.tvPost.setTextAppearance(R.style.item_bottom_avigation_custom)
            views.tvProfile.setTextAppearance(R.style.item_bottom_avigation_custom_selected)
            views.icHome.setImageResource(R.drawable.home)
            views.icOrder.setImageResource(R.drawable.ic_store)
            views.icPost.setImageResource(R.drawable.chat)
            views.icProfile.setImageResource(R.drawable.profile_selected)
        }
    }

    fun animStart(view: View){
        view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_item_bot_na_custom))
    }

    override fun getBinding(): ActivityHomeStoreBinding = ActivityHomeStoreBinding.inflate(layoutInflater)
}