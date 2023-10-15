package datn.fpoly.myapplication.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class AdapterViewPage(val listFragment: MutableList<Fragment>, supportFragmentManager: FragmentActivity): FragmentStateAdapter(supportFragmentManager) {
    override fun getItemCount(): Int = listFragment.size

    override fun createFragment(position: Int): Fragment {
        return listFragment[position]
    }


}