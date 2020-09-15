package org.d3if1008.fundamentals222.Adapter

import android.content.Context
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import org.d3if1008.fundamentals222.R
import org.d3if1008.fundamentals222.View.Fragment.FollowersFragment
import org.d3if1008.fundamentals222.View.Fragment.FollowingFragment

class SectionPagerAdapter(private val mContext: Context, fm : FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT){
    var username : String ="username"

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when(position){
            0-> fragment =
                FollowersFragment.newInstance(username)
            1-> fragment =
                FollowingFragment.newInstance(username)
        }
        return fragment as Fragment
    }

    @StringRes
    private val TAB_TITLES = intArrayOf(R.string.tabText1, R.string.tabText2)

    @Nullable
    override fun getPageTitle(position: Int): CharSequence? {
        return mContext.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int =2
}