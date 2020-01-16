package com.android254.droidconke19.ui.schedule

import android.content.SharedPreferences
import android.os.Bundle
import android.util.ArrayMap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.lifecycleScope
import com.android254.droidconke19.R
import com.android254.droidconke19.models.EventDay
import com.android254.droidconke19.ui.filters.Filter
import com.android254.droidconke19.ui.filters.FilterChip
import com.android254.droidconke19.ui.filters.FilterFragment
import com.android254.droidconke19.ui.filters.FilterStore
import com.android254.droidconke19.ui.sessions.SessionDayFragment
import com.android254.droidconke19.utils.isSignedIn
import com.android254.droidconke19.viewmodels.SessionDetailsViewModel
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_schedule.*
import kotlinx.android.synthetic.main.view_active_filters.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import org.threeten.bp.format.DateTimeFormatter

class ScheduleFragment : Fragment(R.layout.fragment_schedule) {
    private val auth: FirebaseAuth by inject()
    private val sessionDetailsViewModel: SessionDetailsViewModel by viewModel()
    private val sharedPreferences: SharedPreferences by inject { parametersOf(context) }


    private val onTabSelectedListener = object : TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab) {
            viewpager.setCurrentItem(tab.position, true)
        }

        override fun onTabReselected(tab: TabLayout.Tab) {
            val adapter = viewpager.adapter as SessionsViewPagerAdapter
            val fragment = adapter.getFragmentAt(viewpager.currentItem)
            fragment?.scrollToTop()
        }

        override fun onTabUnselected(tab: TabLayout.Tab) = Unit
    }

    private val sessionsViewPagerAdapter: SessionsViewPagerAdapter by lazy {
        SessionsViewPagerAdapter(childFragmentManager)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launchWhenStarted {
            if (auth.isSignedIn()) {
                sessionDetailsViewModel.fetchFavourites(sharedPreferences, auth.currentUser!!.uid)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewpager.adapter = sessionsViewPagerAdapter


        tabLayout.addOnTabSelectedListener(onTabSelectedListener)
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

        val adapter = viewpager.adapter as SessionsViewPagerAdapter
        for (index in 0 until adapter.count) {
            val fragment = adapter.getFragmentAt(index)
            fragment?.applyFilter(filter)
        }
    }


    class SessionsViewPagerAdapter(fm: FragmentManager
    ) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        private val cache = ArrayMap<Int, SessionDayFragment>()

        override fun getItem(position: Int): Fragment {
            val eventDay = EventDay.values()[position]
            return SessionDayFragment.newInstance(eventDay).also {
                cache[position] = it
            }
        }

        fun getFragmentAt(index: Int): SessionDayFragment? {
            return cache[index]
        }

        override fun getCount(): Int {
            return EventDay.values().size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            val formatter = DateTimeFormatter.ofPattern("EEE, MMMM d")
            return EventDay.values()[position].toDate().format(formatter)
        }

    }

}
