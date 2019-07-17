package com.android254.droidconke19.ui.sessions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.android254.droidconke19.R
import com.android254.droidconke19.models.SessionTimeModel
import com.android254.droidconke19.models.SessionsModel
import com.android254.droidconke19.ui.schedule.ScheduleFragmentDirections
import com.android254.droidconke19.utils.nonNull
import com.android254.droidconke19.utils.observe
import com.android254.droidconke19.viewmodels.DayOneViewModel
import kotlinx.android.synthetic.main.fragment_day_one.*
import org.jetbrains.anko.toast
import org.koin.android.ext.android.inject
import java.util.*

class DayOneFragment : Fragment() {

    private var sessionsModelList: List<SessionsModel> = ArrayList()
    private var sessionTimeModelList: List<SessionTimeModel> = ArrayList()
    private var sessionIds: List<String> = ArrayList()
    private var isStarred: Boolean = false
    lateinit var sessionsAdapter: SessionsAdapter
    private val dayOneViewModel: DayOneViewModel by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_day_one, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionsAdapter = SessionsAdapter(sessionsModelList) {
            redirectToSessionDetails()
        }
        initView(sessionsRv)
        dayOneViewModel.getDayOneSessions()

        //observe live data emitted by view model
        observeLiveData()
    }

    private fun redirectToSessionDetails() {
        findNavController().navigate(ScheduleFragmentDirections.actionScheduleFragmentToSessionDetailsFragment())
    }

    private fun observeLiveData() {
        dayOneViewModel.getSessionsResponse().nonNull().observe(this) {
            sessionsModelList = it
            sessionsAdapter.setSessionsAdapter(sessionsModelList)
            loginProgressBar.visibility = View.GONE
        }

    }

    private fun handleError(databaseError: String?) {
        activity?.toast(databaseError.toString())
    }

    private fun initView(sessionsRv: RecyclerView) {
        sessionsRv.adapter = sessionsAdapter
    }

}
