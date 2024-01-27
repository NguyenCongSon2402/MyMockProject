package com.example.mymockproject.base

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(
    val fm: FragmentManager,
    val lifeCycle: Lifecycle,
    val listFragment: ArrayList<Fragment>
) : FragmentStateAdapter(fm, lifeCycle){
    override fun getItemCount(): Int {
        return listFragment.size
    }

    override fun createFragment(position: Int): Fragment {
        return listFragment[position]
    }
}
