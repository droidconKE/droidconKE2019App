package com.android254.droidconke19.ui.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.android254.droidconke19.R
import com.android254.droidconke19.models.AboutDetailsModel
import com.android254.droidconke19.viewmodels.AboutViewModel
import org.jetbrains.anko.toast
import org.koin.android.ext.android.inject

class AboutDetailsFragment : Fragment() {
    private val aboutViewModel: AboutViewModel by inject()
    private val aboutArgs: AboutDetailsFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_about_details, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }



    private fun handleDatabaseError(databaseError: String) {
        activity?.toast(databaseError)
    }

    private fun handleFetchAboutDetails(aboutDetailsList: List<AboutDetailsModel>) {
        initView(aboutDetailsList)
    }

    private fun initView(aboutDetailsList: List<AboutDetailsModel>) {
        val aboutDetailsAdapter = AboutDetailsAdapter(aboutDetailsList) {}
    }



    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.action_profile)?.isVisible = false
    }
}