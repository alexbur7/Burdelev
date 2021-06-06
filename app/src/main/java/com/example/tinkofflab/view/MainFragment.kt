package com.example.tinkofflab.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.tinkofflab.R
import com.google.android.material.tabs.TabLayout

class MainFragment:Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val  view = inflater.inflate(R.layout.fragment_main,container,false)
        val tabLayout = view.findViewById<TabLayout>(R.id.tabLayout)
        val viewPager = view.findViewById<ViewPager>(R.id.viewPager)
        setViewPagerAdapter(viewPager)
        tabLayout.setupWithViewPager(viewPager)
        return view
    }

    private fun setViewPagerAdapter(viewPager: ViewPager) {
        viewPager.adapter = object :
            FragmentStatePagerAdapter(childFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            override fun getCount() = 3

            override fun getItem(position: Int): Fragment {
                return ImageFragment.newInstance(position)
            }

            override fun getPageTitle(position: Int): CharSequence? {
                return when (position) {
                    0 -> context?.getString(R.string.last)
                    1 -> context?.getString(R.string.hot)
                    else -> context?.getString(R.string.top)
                }
            }
        }
    }
}