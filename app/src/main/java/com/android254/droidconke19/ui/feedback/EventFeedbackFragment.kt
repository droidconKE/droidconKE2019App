package com.android254.droidconke19.ui.feedback

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.android254.droidconke19.R
import com.android254.droidconke19.utils.nonNull
import com.android254.droidconke19.utils.observe
import com.android254.droidconke19.viewmodels.FeedBackViewModel
import kotlinx.android.synthetic.main.fragment_event_feedback.*
import org.jetbrains.anko.toast
import org.koin.android.ext.android.inject

class EventFeedbackFragment :Fragment() {
    private val feedBackViewModel : FeedBackViewModel by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_event_feedback, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //observe live data emitted by view model
        observeLiveData()
    }

    private fun observeLiveData() {
        feedBackViewModel.getEventFeedBackResponse().nonNull().observe(this) {
            handleFeedbackResponse(it)
        }
        feedBackViewModel.getEventFeedbackError().nonNull().observe(this) {
            handleDataError(it)
        }
    }

    private fun handleDataError(it: String) {
        activity?.toast(it)
    }

    private fun handleFeedbackResponse(@Suppress("UNUSED_PARAMETER") feedback: String) {
        progressBarEventFeedBack.visibility = View.GONE
        txtEventFeedback.setText("")
        activity?.toast("Thank you for your feedback")

    }
}