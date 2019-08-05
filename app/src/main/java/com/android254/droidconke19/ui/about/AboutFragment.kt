package com.android254.droidconke19.ui.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.android254.droidconke19.R
import com.android254.droidconke19.models.AboutDetailsModel
import com.android254.droidconke19.utils.loadImage
import com.android254.droidconke19.utils.nonNull
import com.android254.droidconke19.utils.observe
import com.android254.droidconke19.viewmodels.AboutViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.fragment_about.*
import org.jetbrains.anko.toast
import org.koin.android.ext.android.inject

class AboutFragment : Fragment() {
    private val aboutViewModel: AboutViewModel by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_about, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //get about details
        fetchAboutDetails("about_droidconKE")

        //get organizers details
        fetchOrganizersDetails("organizers")

        //get sponsors details
        fetchSponsorsDetails("sponsors")
        showProgressBar()
        //observe live data
        observeLiveData()

    }

    private fun fetchSponsorsDetails(aboutType: String) {
        aboutViewModel.getSponsors(aboutType)
    }

    private fun fetchOrganizersDetails(aboutType: String) {
        aboutViewModel.getOrganizers(aboutType)
    }

    private fun observeLiveData() {
        aboutViewModel.getAboutDetailsResponse().nonNull().observe(this) { aboutDetailsList ->
            handleFetchAboutDetails(aboutDetailsList)
        }
        aboutViewModel.getAboutDetailsError().nonNull().observe(this) { databaseError ->
            handleDatabaseError(databaseError)
        }
        aboutViewModel.getOrganizersResponse().nonNull().observe(this) {
            handleGetOrganizersResponse(it)
        }
        aboutViewModel.getSponsorsResponse().nonNull().observe(this) {
            handleSponsorsResponse(it)
        }
    }

    private fun handleSponsorsResponse(it: List<AboutDetailsModel>) {
        hideProgressBar()
        sponsorsRv.adapter = AboutDetailsAdapter(it) {}

    }

    private fun handleGetOrganizersResponse(it: List<AboutDetailsModel>) {
        hideProgressBar()
        initView(it)
    }

    private fun handleDatabaseError(databaseError: String) {
        hideProgressBar()
        activity?.toast(databaseError)
    }

    private fun handleFetchAboutDetails(aboutDetailsList: List<AboutDetailsModel>) {
        hideProgressBar()
        aboutDetailsList.forEach {
            droidconDescText.text = it.bio
            eventImg.loadImage(it.logoUrl, R.drawable.splash)
        }
    }

    private fun initView(aboutDetailsList: List<AboutDetailsModel>) {
        val aboutDetailsAdapter = AboutDetailsAdapter(aboutDetailsList) {}
        organizersRv.layoutManager = GridLayoutManager(context, 2)
        organizersRv.adapter = aboutDetailsAdapter
    }

    private fun fetchAboutDetails(aboutType: String?) {
        aboutType?.let { aboutType -> aboutViewModel.fetchAboutDetails(aboutType) }
    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
        aboutLinear.visibility = View.GONE

    }

    private fun hideProgressBar() {
        progressBar.visibility = View.GONE
        aboutLinear.visibility = View.VISIBLE
    }

}
