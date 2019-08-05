package com.android254.droidconke19.ui.feedback

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.android254.droidconke19.R
import com.android254.droidconke19.models.UserEventFeedback
import com.android254.droidconke19.utils.isSignedIn
import com.android254.droidconke19.utils.nonNull
import com.android254.droidconke19.utils.observe
import com.android254.droidconke19.viewmodels.FeedBackViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_event_feedback.*
import org.jetbrains.anko.toast
import org.koin.android.ext.android.inject

class EventFeedbackFragment : Fragment() {
    private val feedBackViewModel: FeedBackViewModel by inject()
    private val firebaseAuth: FirebaseAuth by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_event_feedback, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        submitSessionFeedBackBtn.setOnClickListener {
            val eventFeedback = eventFeedbackEdit.text.toString()
            submitEventFeedback(eventFeedback)
        }
        //observe live data emitted by view model
        observeLiveData()
    }

    private fun submitEventFeedback(eventFeedback: String) {
        if (firebaseAuth.isSignedIn()) {
            when {
                !validateInputs(eventFeedback) -> return
                else -> {
                    showProgressbar()
                    val userEventFeedback = UserEventFeedback(eventFeedback)
                    feedBackViewModel.sendEventFeedBack(userEventFeedback)
                }
            }
        }

    }

    private fun validateInputs(eventFeedback: String): Boolean {
        var valid = true
        when {
            TextUtils.isEmpty(eventFeedback) -> {
                eventFeedbackLayout.error = getString(R.string.empty_field_error)
                eventFeedbackEdit.requestFocus()

                valid = false
            }
            else -> {
                eventFeedbackLayout.error = null
            }
        }

        return valid
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
        hideProgressBar()
        activity?.toast(it)
    }

    private fun handleFeedbackResponse(feedback: String) {
        hideProgressBar()
        eventFeedbackEdit.setText("")
        activity?.toast(feedback)

    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.action_profile)?.isVisible = false
        menu.findItem(R.id.eventFeedbackFragment)?.isVisible = false
    }

    private fun showProgressbar() {
        eventFeedbackLinear.visibility = View.GONE
        progressBarEventFeedBack.visibility = View.VISIBLE

    }

    private fun hideProgressBar() {
        progressBarEventFeedBack.visibility = View.GONE
        eventFeedbackLinear.visibility = View.VISIBLE
    }
}