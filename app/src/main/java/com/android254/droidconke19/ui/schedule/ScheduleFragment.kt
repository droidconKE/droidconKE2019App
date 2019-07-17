package com.android254.droidconke19.ui.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.android254.droidconke19.R
import com.android254.droidconke19.ui.agenda.AgendaFragment
import com.android254.droidconke19.ui.filters.Filter
import com.android254.droidconke19.ui.filters.FilterChip
import com.android254.droidconke19.ui.filters.FilterFragment
import com.android254.droidconke19.ui.filters.FilterStore
import com.android254.droidconke19.ui.sessions.DayOneFragment
import com.android254.droidconke19.ui.sessions.DayTwoFragment
import kotlinx.android.synthetic.main.fragment_schedule.*
import kotlinx.android.synthetic.main.view_active_filters.*
import java.util.*

class ScheduleFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_schedule, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager(viewpager)
        tabLayout.setupWithViewPager(viewpager)

        onFilterChanged(FilterStore.instance.filter)

        fab.setOnClickListener { openFilters() }
        activeFiltersContainer.setOnClickListener { openFilters() }

        clearFilterButton.setOnClickListener {
            val store = FilterStore.instance
            store.clear()
            onFilterChanged(Filter.empty())
        }
    }

    private fun openFilters() {
        val fragment = FilterFragment.newInstance(this::onFilterChanged)
        fragment.show(childFragmentManager, fragment.tag)
    }

    private fun onFilterChanged(filter: Filter) {
        val isFilterActive = filter != Filter.empty()
        fab.isVisible = isFilterActive.not()
        activeFiltersContainer.isVisible = isFilterActive

        if (isFilterActive) {
            activeFiltersChipGroup.removeAllViews()
            val items = filter.getActiveFilters(requireContext())
            val chips = items
                    .map { FilterChip(requireContext()).apply { text = it } }

            chips.forEach {
                it.disable()
                activeFiltersChipGroup.addView(it)
            }
        }

        val adapter = viewpager.adapter as ViewPagerAdapter
        for (index in 0 until adapter.count) {
            val fragment = adapter.getFragmentAt(index)
            //fragment.applyFilter(filter)
        }
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(childFragmentManager)
        adapter.addFragment(DayOneFragment(), getString(R.string.day_one_label))
        adapter.addFragment(DayTwoFragment(), getString(R.string.day_two_label))
        adapter.addFragment(AgendaFragment(), "Agenda")
        viewPager.adapter = adapter
    }

    inner class ViewPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager) {
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

        fun getFragmentAt(index: Int): Fragment? {
            return mFragmentList[index]
        }


        override fun getPageTitle(position: Int): CharSequence? {
            return mFragmentTitleList[position]
        }
    }


}
