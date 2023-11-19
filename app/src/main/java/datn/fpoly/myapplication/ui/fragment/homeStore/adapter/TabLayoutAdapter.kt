package datn.fpoly.myapplication.ui.fragment.homeStore.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import datn.fpoly.myapplication.ui.fragment.homeStore.fragment.CompleteFragment
import datn.fpoly.myapplication.ui.fragment.homeStore.fragment.WaitFragment
import datn.fpoly.myapplication.ui.fragment.homeStore.fragment.WashingFragment
import javax.inject.Inject

class TabLayoutAdapter @Inject constructor(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                WaitFragment()
            }

            1 -> {
                WashingFragment()
            }

            2 -> {
                CompleteFragment()
            }

            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}
