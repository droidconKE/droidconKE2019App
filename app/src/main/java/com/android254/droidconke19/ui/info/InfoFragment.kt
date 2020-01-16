package com.android254.droidconke19.ui.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.android254.droidconke19.R
import com.android254.droidconke19.ui.about.AboutFragment
import com.android254.droidconke19.ui.events.EventFragment
import com.android254.droidconke19.ui.traveldetails.TravelFragment
import kotlinx.android.synthetic.main.fragment_info.*
import java.util.*

class InfoFragment : Fragment(R.layout.fragment_info) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewPager(viewpager)
        tabLayout.setupWithViewPager(viewpager)

    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(childFragmentManager)
        adapter.addFragment(EventFragment(), getString(R.string.event))
        adapter.addFragment(TravelFragment(), getString(R.string.travel))
        adapter.addFragment(AboutFragment(), getString(R.string.about))
        viewPager.adapter = adapter

    }

    inner class ViewPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        private val mFragmentList = ArrayList<Fragment>()
        private val mFragmentTitleList = ArrayList<String>()

        override fun getItem(position: Int): Fragment {
            return mFragmentList[position]
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }

        fun addFragment(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return mFragmentTitleList[position]
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.action_profile)?.isVisible = false
        menu.findItem(R.id.eventFeedbackFragment)?.isVisible = false
    }
}
