package datn.fpoly.myapplication.ui.home

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import datn.fpoly.myapplication.AppApplication
import datn.fpoly.myapplication.R
import datn.fpoly.myapplication.core.BaseActivity
import datn.fpoly.myapplication.databinding.ActivityHomeBinding
import datn.fpoly.myapplication.ui.adapter.AdapterViewPage
import datn.fpoly.myapplication.ui.fragment.FragmentOrder.FragmentOrder
import datn.fpoly.myapplication.ui.fragment.cart.CartFragment
import datn.fpoly.myapplication.ui.fragment.homeUser.HomeUserFragment
import datn.fpoly.myapplication.ui.fragment.postclient.PostClientFragment
import datn.fpoly.myapplication.ui.fragment.setting.FragmentSetting
import javax.inject.Inject
import com.airbnb.mvrx.viewModel

class HomeActivity: BaseActivity<ActivityHomeBinding>(), HomeUserViewModel.Factory {
    @Inject
    lateinit var homeUserViewModelFactory: HomeUserViewModel.Factory
    private lateinit var adapterVp: AdapterViewPage
    private val listFragment = mutableListOf<Fragment>()
    private val viewModel : HomeUserViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as AppApplication).appComponent.inject(this);
        super.onCreate(savedInstanceState)
        setContentView(views.root)
        setViewNavigation()
        viewModel.observeViewEvents {

        }

    }
    fun setViewNavigation(){
        listFragment.add(0, HomeUserFragment())
        listFragment.add(1, CartFragment())
        listFragment.add(2,FragmentOrder())
        listFragment.add(3,PostClientFragment())
        listFragment.add(4,FragmentSetting())
        adapterVp = AdapterViewPage(listFragment, this)
        views.vp2Home.adapter = adapterVp
        views.vp2Home.isUserInputEnabled= false
        views.vp2Home.setCurrentItem(0, true)
        views.viewBgItem.visibility = View.VISIBLE
        views.viewBgItem1.visibility = View.INVISIBLE
        views.viewBgItem2.visibility = View.INVISIBLE
        views.viewBgItem3.visibility = View.INVISIBLE
        views.viewBgItem4.visibility = View.INVISIBLE
        views.tvHome.setTextAppearance(R.style.item_bottom_avigation_custom_selected)
        views.tvCart.setTextAppearance(R.style.item_bottom_avigation_custom)
        views.tvOrder.setTextAppearance(R.style.item_bottom_avigation_custom)
        views.tvPost.setTextAppearance(R.style.item_bottom_avigation_custom)
        views.tvProfile.setTextAppearance(R.style.item_bottom_avigation_custom)
        views.llItem1.setOnClickListener {
            views.vp2Home.setCurrentItem(0, true)
            views.viewBgItem.visibility = View.VISIBLE
            views.viewBgItem1.visibility = View.INVISIBLE
            views.viewBgItem2.visibility = View.INVISIBLE
            views.viewBgItem3.visibility = View.INVISIBLE
            views.viewBgItem4.visibility = View.INVISIBLE
            animStart(views.viewBgItem)
            views.tvHome.setTextAppearance(R.style.item_bottom_avigation_custom_selected)
            views.tvCart.setTextAppearance(R.style.item_bottom_avigation_custom)
            views.tvOrder.setTextAppearance(R.style.item_bottom_avigation_custom)
            views.tvPost.setTextAppearance(R.style.item_bottom_avigation_custom)
            views.tvProfile.setTextAppearance(R.style.item_bottom_avigation_custom)
            views.icHome.setImageResource(R.drawable.home_selected)
            views.icCart.setImageResource(R.drawable.document)
            views.icOrder.setImageResource(R.drawable.ic_store)
            views.icPost.setImageResource(R.drawable.chat)
            views.icProfile.setImageResource(R.drawable.profile_gray)
        }
        views.llItem2.setOnClickListener {
            views.vp2Home.setCurrentItem(1, true)
            views.viewBgItem1.visibility = View.VISIBLE
            views.viewBgItem.visibility = View.INVISIBLE
            views.viewBgItem2.visibility = View.INVISIBLE
            views.viewBgItem3.visibility = View.INVISIBLE
            views.viewBgItem4.visibility = View.INVISIBLE
            animStart(views.viewBgItem1)
            views.tvHome.setTextAppearance(R.style.item_bottom_avigation_custom)
            views.tvCart.setTextAppearance(R.style.item_bottom_avigation_custom_selected)
            views.tvOrder.setTextAppearance(R.style.item_bottom_avigation_custom)
            views.tvPost.setTextAppearance(R.style.item_bottom_avigation_custom)
            views.tvProfile.setTextAppearance(R.style.item_bottom_avigation_custom)
            views.icHome.setImageResource(R.drawable.home)
            views.icCart.setImageResource(R.drawable.document_selected)
            views.icOrder.setImageResource(R.drawable.ic_store)
            views.icPost.setImageResource(R.drawable.chat)
            views.icProfile.setImageResource(R.drawable.profile_gray)
        }
        views.llItem3.setOnClickListener {
            views.vp2Home.setCurrentItem(2, true)
            views.viewBgItem2.visibility = View.VISIBLE
            views.viewBgItem1.visibility = View.INVISIBLE
            views.viewBgItem.visibility = View.INVISIBLE
            views.viewBgItem3.visibility = View.INVISIBLE
            views.viewBgItem4.visibility = View.INVISIBLE
            animStart(views.viewBgItem2)
            views.tvHome.setTextAppearance(R.style.item_bottom_avigation_custom)
            views.tvCart.setTextAppearance(R.style.item_bottom_avigation_custom)
            views.tvOrder.setTextAppearance(R.style.item_bottom_avigation_custom_selected)
            views.tvPost.setTextAppearance(R.style.item_bottom_avigation_custom)
            views.tvProfile.setTextAppearance(R.style.item_bottom_avigation_custom)
            views.icHome.setImageResource(R.drawable.home)
            views.icCart.setImageResource(R.drawable.document)
            views.icOrder.setImageResource(R.drawable.order_selected)
            views.icPost.setImageResource(R.drawable.chat)
            views.icProfile.setImageResource(R.drawable.profile_gray)
        }
        views.llItem4.setOnClickListener {
            views.vp2Home.setCurrentItem(3, true)
            views.viewBgItem3.visibility = View.VISIBLE
            views.viewBgItem1.visibility = View.INVISIBLE
            views.viewBgItem2.visibility = View.INVISIBLE
            views.viewBgItem.visibility = View.INVISIBLE
            views.viewBgItem4.visibility = View.INVISIBLE
            animStart(views.viewBgItem3)
            views.tvHome.setTextAppearance(R.style.item_bottom_avigation_custom)
            views.tvCart.setTextAppearance(R.style.item_bottom_avigation_custom)
            views.tvOrder.setTextAppearance(R.style.item_bottom_avigation_custom)
            views.tvPost.setTextAppearance(R.style.item_bottom_avigation_custom_selected)
            views.tvProfile.setTextAppearance(R.style.item_bottom_avigation_custom)
            views.icHome.setImageResource(R.drawable.home)
            views.icCart.setImageResource(R.drawable.document)
            views.icOrder.setImageResource(R.drawable.ic_store)
            views.icPost.setImageResource(R.drawable.chat_selected)
            views.icProfile.setImageResource(R.drawable.profile_gray)
        }
        views.llItem5.setOnClickListener {
            views.vp2Home.setCurrentItem(4, true)
            views.viewBgItem4.visibility = View.VISIBLE
            views.viewBgItem1.visibility = View.INVISIBLE
            views.viewBgItem2.visibility = View.INVISIBLE
            views.viewBgItem3.visibility = View.INVISIBLE
            views.viewBgItem.visibility = View.INVISIBLE
            animStart(views.viewBgItem4)
            views.tvHome.setTextAppearance(R.style.item_bottom_avigation_custom)
            views.tvCart.setTextAppearance(R.style.item_bottom_avigation_custom)
            views.tvOrder.setTextAppearance(R.style.item_bottom_avigation_custom)
            views.tvPost.setTextAppearance(R.style.item_bottom_avigation_custom)
            views.tvProfile.setTextAppearance(R.style.item_bottom_avigation_custom_selected)
            views.icHome.setImageResource(R.drawable.home)
            views.icCart.setImageResource(R.drawable.document)
            views.icOrder.setImageResource(R.drawable.ic_store)
            views.icPost.setImageResource(R.drawable.chat)
            views.icProfile.setImageResource(R.drawable.profile_selected)
        }
    }

    fun animStart(view: View){
        view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_item_bot_na_custom))
    }

    override fun getBinding(): ActivityHomeBinding = ActivityHomeBinding.inflate(layoutInflater)
    override fun create(initialState: HomeViewState): HomeUserViewModel =homeUserViewModelFactory.create(initialState)

}