package com.android254.droidconke19.ui.authentication

import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.android254.droidconke19.R
import com.android254.droidconke19.utils.toast
import com.android254.droidconke19.viewmodels.SessionDetailsViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.sign_out_dialog.*
import kotlinx.android.synthetic.main.sign_out_dialog.cancelText
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class SignOutDialogFragment : DialogFragment() {
    private val firebaseAuth: FirebaseAuth by inject()
    private val sessionDetailsViewModel: SessionDetailsViewModel by viewModel()
    private val sharedPreferences: SharedPreferences by inject { parametersOf(context) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.sign_out_dialog, container)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        clickListeners()
    }

    private fun clickListeners() {
        cancelText.setOnClickListener {
            dismiss()
        }
        signOutBtn.setOnClickListener {
            unsubscribeNotifications()
            firebaseAuth.signOut()
            activity?.toast(getString(R.string.success))

        }
    }

    private fun unsubscribeNotifications() = lifecycleScope.launch {
        firebaseAuth.currentUser?.let {
            sessionDetailsViewModel.removeAllFavourites(sharedPreferences, it.uid)
        }
    }
}