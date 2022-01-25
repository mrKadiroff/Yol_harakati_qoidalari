package com.example.thetraffic.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.thetraffic.FirstFragment
import com.example.thetraffic.SecondFragment

import com.example.thetraffic.tab_fragments.OgohFragment


class TrafficMainAdapter(fragmentManager: FragmentManager) :
    FragmentStatePagerAdapter(
        fragmentManager,
        BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT

    ) {

    override fun getCount(): Int = 4


    override fun getItem(position: Int): Fragment {
        return OgohFragment.newInstance(position + 1, false)
    }
}