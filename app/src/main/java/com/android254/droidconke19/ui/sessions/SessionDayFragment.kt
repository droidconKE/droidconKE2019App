package com.android254.droidconke19.ui.sessions

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
import com.android254.droidconke19.ui.filters.Filter
import com.android254.droidconke19.ui.filters.FilterStore
import com.android254.droidconke19.ui.schedule.ScheduleFragmentDirections
import com.android254.droidconke19.utils.nonNull
import com.android254.droidconke19.utils.observe
import com.android254.droidconke19.viewmodels.DayOneViewModel
import com.android254.droidconke19.viewmodels.SessionDetailsViewModel
import kotlinx.android.synthetic.main.fragment_day_one.*
import org.jetbrains.anko.toast
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class SessionDayFragment : Fragment() {
    private val sessionsAdapter: SessionsAdapter by lazy {
        SessionsAdapter{ redirectToSessionDetails(it) }
    }
    private val dayOneViewModel: DayOneViewModel by inject()
    private val sessionDetailsViewModel: SessionDetailsViewModel by sharedViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_day_one, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView(sessionsRv)
        showProgressBar()
        dayOneViewModel.getDayOneSessions()

        val filterStore = FilterStore.instance
        if (filterStore.filter != Filter.empty()) {
            applyFilter(filterStore.filter)
        }
        //observe live data emitted by view model
        observeLiveData()
    }

    private fun redirectToSessionDetails(it: SessionsModel) {
        sessionDetailsViewModel.loadSessionDetails(it)
        findNavController().navigate(ScheduleFragmentDirections.actionScheduleFragmentToSessionDetailsFragment())
    }

    private fun observeLiveData() {
        dayOneViewModel.getSessionsResponse().nonNull().observe(this) {sessionList ->
            updateAdapterWithList(sessionList)
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
        activity?.toast(databaseError.toString())
    }

    private fun initView(sessionsRv: RecyclerView) {
        sessionsRv.adapter = sessionsAdapter
    }

    private fun showProgressBar(){
        progressBar.visibility = View.VISIBLE
        sessionsRv.visibility = View.GONE

    }

    private fun hideProgressBar(){
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