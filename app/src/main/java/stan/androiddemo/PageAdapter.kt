package stan.androiddemo

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 * Created by stanhu on 4/8/2017.
 */
class PageAdapter(fm: FragmentManager):FragmentPagerAdapter(fm){
    lateinit var mFragments: ArrayList<Fragment>
    lateinit var  mTitle: ArrayList<String>

    constructor(fm:FragmentManager,fragments:ArrayList<out Fragment>,title:ArrayList<String>):this(fm){
        mFragments = ArrayList<Fragment>(fragments)
        mTitle = title

    }

    override fun getPageTitle(position: Int): CharSequence {
        return if (mTitle == null) "" else mTitle[position]
    }

    override fun getCount(): Int {
       return mFragments.size
    }

    override fun getItem(position: Int): Fragment {
        return mFragments[position]
    }

}