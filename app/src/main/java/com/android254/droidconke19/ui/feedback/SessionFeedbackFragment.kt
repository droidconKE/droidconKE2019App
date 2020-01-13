package com.android254.droidconke19.ui.feedback

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.android254.droidconke19.R
import com.android254.droidconke19.models.SessionsUserFeedback
import com.android254.droidconke19.utils.isSignedIn
import com.android254.droidconke19.viewmodels.SessionDataViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_session_feedback.*
import org.jetbrains.anko.toast
import org.koin.android.ext.android.inject

class SessionFeedbackFragment : Fragment() {
    private val sessionDataViewModel: SessionDataViewModel by inject()
    private val sessionFeedbackArgs: SessionFeedbackFragmentArgs by navArgs()
    private val firebaseAuth: FirebaseAuth by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_session_feedback, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionFeedbackTitleText.text = sessionFeedbackArgs.sessionTitle

        submitSessionFeedBackBtn.setOnClickListener {
            val sessionFeedback = sessionFeedbackEdit.text.toString()
            submitSessionFeedback(sessionFeedback)
        }
        //observe live data emitted by view model
        observeLiveData()
    }

    private fun submitSessionFeedback(sessionFeedback: String) {
        if (firebaseAuth.isSignedIn()) {
            when {
                !validateInputs(sessionFeedback) -> return
                else -> {
                    showProgressbar()
                    val sessionsUserFeedback = SessionsUserFeedback(firebaseAuth.currentUser?.uid!!, sessionFeedbackArgs.sessionId, sessionFeedbackArgs.dayNumber, sessionFeedbackArgs.sessionTitle, sessionFeedback)
                    sessionDataViewModel.sendSessionFeedBack(sessionsUserFeedback)
                }
            }
        }
    }

    private fun validateInputs(sessionFeedback: String): Boolean {
        var valid = true
        when {
            TextUtils.isEmpty(sessionFeedback) -> {
                sessionFeedbackLayout.error = getString(R.string.empty_field_error)
                sessionFeedbackEdit.requestFocus()
                valid = false
            }
            else -> {
                sessionFeedbackLayout.error = null
            }
        }
        return valid
    }

    private fun observeLiveData() {
        sessionDataViewModel.getSessionFeedBackResponse().observe(this, Observer{
            handleFeedbackResponse(it)
        })
        sessionDataViewModel.getFirebaseError().observe(this, Observer {
            handleDatabaseError(it)
        })

    }

    private fun handleFeedbackResponse(feedback: String) {
        hideProgressBar()
        sessionFeedbackEdit.setText("")
        activity?.toast(feedback)
    }

    private fun handleDatabaseError(databaseError: String) {
        hideProgressBar()
        activity?.toast(databaseError)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.action_profile)?.isVisible = false
        menu.findItem(R.id.eventFeedbackFragment)?.isVisible = false
    }

    private fun showProgressbar() {
        feedbackLinear.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.GONE
        feedbackLinear.visibility = View.VISIBLE
    }
}