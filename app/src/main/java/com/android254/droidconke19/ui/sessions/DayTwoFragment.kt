package com.android254.droidconke19.ui.sessions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.android254.droidconke19.R
import com.android254.droidconke19.models.SessionsModel
import com.android254.droidconke19.ui.filters.Filter
import com.android254.droidconke19.ui.filters.FilterStore
import com.android254.droidconke19.utils.nonNull
import com.android254.droidconke19.utils.observe
import com.android254.droidconke19.viewmodels.DayTwoViewModel
import kotlinx.android.synthetic.main.fragment_day_two.*
import org.jetbrains.anko.toast
import org.koin.android.ext.android.inject
import java.util.*

class DayTwoFragment : Fragment() {
    private var sessionsModelList: List<SessionsModel> = ArrayList()
    private val dayTwoViewModel: DayTwoViewModel by inject()
    private val sessionsAdapter: SessionsAdapter by lazy {
        SessionsAdapter{ redirectToSessionDetails() }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_day_two, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView(sessionsRv)
        dayTwoViewModel.getDayTwoSessions()

        val filterStore = FilterStore.instance
        if (filterStore.filter != Filter.empty()) {
            applyFilter(filterStore.filter)
        }
        //observe live data emitted by view model
        observerLiveData()
    }

    private fun redirectToSessionDetails() {
    }

    private fun observerLiveData() {
        dayTwoViewModel.getSessionsResponse().nonNull().observe(this) {sessionList ->
            updateAdapterWithList(sessionList)
        }
        dayTwoViewModel.getSessionsError().nonNull().observe(this) {
            handleError(it)
        }
    }

    private fun updateAdapterWithList(sessionList: List<SessionsModel>) {
        sessionsAdapter.update(sessionList)
    }

    private fun handleError(databaseError: String) {
        activity?.toast(databaseError)
    }

    private fun initView(sessionsRv: RecyclerView) {
        sessionsRv.adapter = sessionsAdapter
    }

    fun applyFilter(filter: Filter) {
        sessionsAdapter.applyFilter(filter)
    }

}
