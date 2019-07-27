package com.android254.droidconke19.ui.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.android254.droidconke19.R
import com.android254.droidconke19.models.AboutDetailsModel
import com.android254.droidconke19.utils.nonNull
import com.android254.droidconke19.utils.observe
import com.android254.droidconke19.viewmodels.AboutDetailsViewModel
import kotlinx.android.synthetic.main.fragment_about_details.*
import org.jetbrains.anko.toast
import org.koin.android.ext.android.inject

class AboutDetailsFragment : Fragment() {
    private val aboutDetailsViewModel: AboutDetailsViewModel by inject()
    private val aboutArgs: AboutDetailsFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_about_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //fetch about details
        fetchAboutDetails(aboutArgs.aboutType)
        //observe live data emitted by view model
        observeLiveData()
    }

    private fun observeLiveData() {
        aboutDetailsViewModel.getAboutDetailsResponse().nonNull().observe(this) { aboutDetailsList ->
            handleFetchAboutDetails(aboutDetailsList)
        }
        aboutDetailsViewModel.getAboutDetailsError().nonNull().observe(this) { databaseError ->
            handleDatabaseError(databaseError)
        }
    }

    private fun handleDatabaseError(databaseError: String) {
        activity?.toast(databaseError)
    }

    private fun handleFetchAboutDetails(aboutDetailsList: List<AboutDetailsModel>) {
        initView(aboutDetailsList)
    }

    private fun initView(aboutDetailsList: List<AboutDetailsModel>) {
        val aboutDetailsAdapter = AboutDetailsAdapter(aboutDetailsList) {}
        aboutDetailsRv.adapter = aboutDetailsAdapter
    }

    private fun fetchAboutDetails(aboutType: String?) {
        aboutType?.let { aboutType -> aboutDetailsViewModel.fetchAboutDetails(aboutType) }
    }
}