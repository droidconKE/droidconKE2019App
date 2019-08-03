package com.android254.droidconke19.ui.sessions

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.android254.droidconke19.R
import com.android254.droidconke19.models.EventDay
import com.android254.droidconke19.models.SessionsModel
import com.android254.droidconke19.repository.FavoritesStore
import com.android254.droidconke19.ui.filters.Filter
import com.android254.droidconke19.ui.filters.FilterStore
import com.android254.droidconke19.ui.schedule.ScheduleFragmentDirections
import com.android254.droidconke19.utils.nonNull
import com.android254.droidconke19.utils.observe
import com.android254.droidconke19.viewmodels.SessionsViewModel
import com.android254.droidconke19.viewmodels.SessionDetailsViewModel
import kotlinx.android.synthetic.main.fragment_day_session.*
import org.jetbrains.anko.toast
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.core.parameter.parametersOf

class SessionDayFragment : Fragment() {
    private val day: EventDay by lazy {
        checkNotNull(arguments?.getSerializable(KEY_EVENT_DAY) as EventDay)
    }
    private val sessionsViewModel: SessionsViewModel by inject()
    private val sessionDetailsViewModel: SessionDetailsViewModel by sharedViewModel()
    private val sharedPreferences: SharedPreferences by inject { parametersOf(context) }
    private val favoritesStore: FavoritesStore by lazy {
        FavoritesStore(sharedPreferences)
    }
    private val sessionsAdapter: SessionsAdapter by lazy {
        SessionsAdapter(favoritesStore) { redirectToSessionDetails(it) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_day_session, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView(sessionsRv)
        showProgressBar()
        //fetch sessions according to day
        fetchDaySessions()

        val filterStore = FilterStore.instance
        if (filterStore.filter != Filter.empty()) {
            applyFilter(filterStore.filter)
        }
        //observe live data emitted by view model
        observeLiveData()
    }

    private fun fetchDaySessions() {
        when (day) {
            EventDay.Thursday -> sessionsViewModel.getSessions("day_one")
            EventDay.Friday -> sessionsViewModel.getSessions("day_two")
        }
    }

    private fun redirectToSessionDetails(it: SessionsModel) {
        sessionDetailsViewModel.loadSessionDetails(it)
        findNavController().navigate(ScheduleFragmentDirections.actionScheduleFragmentToSessionDetailsFragment(it.title))
    }

    private fun observeLiveData() {
        sessionsViewModel.getSessionsResponse().nonNull().observe(this) { sessionList ->
            updateAdapterWithList(sessionList)
        }
        sessionsViewModel.getSessionsError().nonNull().observe(this){databaseError ->
            handleError(databaseError)
        }
    }

    private fun updateAdapterWithList(sessionList: List<SessionsModel>) {
        hideProgressBar()
        sessionsAdapter.update(sessionList)

    }

    fun scrollToTop() {
        sessionsRv.smoothScrollToPosition(0)
    }

    fun applyFilter(filter: Filter) {
        sessionsAdapter.applyFilter(filter)
    }

    private fun handleError(databaseError: String?) {
        hideProgressBar()
        activity?.toast(databaseError.toString())
    }

    private fun initView(sessionsRv: RecyclerView) {
        sessionsRv.adapter = sessionsAdapter
    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
        sessionsRv.visibility = View.GONE

    }

    private fun hideProgressBar() {
        progressBar.visibility = View.GONE
        sessionsRv.visibility = View.VISIBLE
    }


    companion object {
        private const val KEY_EVENT_DAY = "KEY_EVENT_DAY"
        private const val KEY_EVENT = "KEY_EVENT"

        fun newInstance(
                day: EventDay
        ) = SessionDayFragment().apply {
            arguments = bundleOf(KEY_EVENT_DAY to day)
        }

    }

}