package com.android254.droidconke19.ui.feedback

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.android254.droidconke19.R
import com.android254.droidconke19.models.SessionsModel
import com.android254.droidconke19.models.SessionsUserFeedback
import com.android254.droidconke19.utils.nonNull
import com.android254.droidconke19.utils.observe
import com.android254.droidconke19.viewmodels.SessionDataViewModel
import kotlinx.android.synthetic.main.fragment_session_feedback.*
import org.jetbrains.anko.toast
import org.koin.android.ext.android.inject

class SessionFeedbackFragment : Fragment() {
    private val sessionDataViewModel: SessionDataViewModel by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_session_feedback, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //observe live data emitted by view model
        observeLiveData()
    }

    private fun observeLiveData() {
        sessionDataViewModel.getSessionDataResponse().nonNull().observe(this) {
            handleFetchSessionData(it)

        }
        sessionDataViewModel.getSessionDataError().nonNull().observe(this) {
            handleDatabaseError(it)
        }
        sessionDataViewModel.getSessionFeedBackResponse().nonNull().observe(this) {
            handleFeedbackResponse(it)
        }
        sessionDataViewModel.getSessionFeedbackError().nonNull().observe(this) {
            handleDatabaseError(it)
        }

    }

    private fun handleFeedbackResponse(feedback: String) {
        progressBar.visibility = View.GONE
        txtSessionUserFeedback.setText("")
        activity?.toast("Thank you for your feedback")
    }

    private fun getSessionData(dayNumber: String, sessionId: Int) {
        sessionDataViewModel.getSessionDetails(dayNumber, sessionId)
    }

    private fun handleFetchSessionData(sessionsModel: SessionsModel) {
        //set the data on the view
        txtSessionFeedbackTitle.text = sessionsModel.title


    }

    private fun handleDatabaseError(databaseError: String) {
        activity?.toast(databaseError)
    }

    private fun postUserFeedback(userFeedback: SessionsUserFeedback) {

        progressBar.visibility = View.VISIBLE
        sessionDataViewModel.sendSessionFeedBack(userFeedback)

    }
}