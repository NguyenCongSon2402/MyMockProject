package com.example.mymockproject.view.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mymockproject.R
import com.example.mymockproject.base.BaseFragment
import com.example.mymockproject.base.ViewPagerAdapter
import com.example.mymockproject.databinding.FragmentM00MainBinding
import com.example.mymockproject.view.home.m01_home.M01HomeFragment
import com.example.mymockproject.view.home.m02_favourite.M02FavouriteFragment
import com.example.mymockproject.view.home.m03_setting.M03SettingFragment
import com.example.mymockproject.view.home.m04_profile.M04ProfileAFragment
import com.google.android.material.tabs.TabLayoutMediator

class M00MainFragment : BaseFragment<FragmentM00MainBinding>(FragmentM00MainBinding::inflate){

    private lateinit var mAdapter: ViewPagerAdapter
    private val listFragment: ArrayList<Fragment> = arrayListOf()
    private val listTitle: ArrayList<String> = arrayListOf("Trang chủ", "Yêu thích", "Cài đặt", "Cá nhân")
    private val listIcon = arrayListOf(
        R.drawable.ic_home,
        R.drawable.ic_heart,
        R.drawable.ic_setting,
        R.drawable.ic_profile
    )

    override fun initView() {

        listFragment.add(M01HomeFragment.newInstance())
        listFragment.add(M02FavouriteFragment.newInstance())
        listFragment.add(M03SettingFragment.newInstance())
        listFragment.add(M04ProfileAFragment.newInstance())

        setUpTabLayout()

    }

    override fun getData() {}

    override fun initObserve() {}

    private fun setUpTabLayout(){
        binding.viewPager.offscreenPageLimit = 4
        mAdapter = ViewPagerAdapter(
            requireActivity().supportFragmentManager,
            lifecycle,
            listFragment
        )

        binding.viewPager.adapter = mAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager){tab, pos ->
            tab.text = listTitle[pos]
            tab.setIcon(listIcon[pos])
        }.attach()
    }
}
