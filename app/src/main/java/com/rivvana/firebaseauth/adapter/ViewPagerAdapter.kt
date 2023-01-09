package com.rivvana.firebaseauth.adapter

import android.icu.text.CaseMap
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewPagerAdapter (supportFragmentManager: FragmentManager) : FragmentPagerAdapter (supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private var FragmentList = ArrayList<Fragment>()
    private var FragmentTitleList = ArrayList<String>()


    override fun getCount(): Int {
        return FragmentList.size
    }

    override fun getItem(position: Int): Fragment {
        return FragmentList[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
         return FragmentTitleList[position]
    }

    fun addFragment (fragment: Fragment, title: String){
         FragmentList.add(fragment)
         FragmentTitleList.add(title)
    }
}