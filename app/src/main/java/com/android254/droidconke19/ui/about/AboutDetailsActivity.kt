package com.android254.droidconke19.ui.about

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import com.android254.droidconke19.R
import com.android254.droidconke19.models.AboutDetailsModel
import com.android254.droidconke19.utils.nonNull
import com.android254.droidconke19.utils.observe
import com.android254.droidconke19.utils.toast
import com.android254.droidconke19.viewmodels.AboutDetailsViewModel
import kotlinx.android.synthetic.main.activity_about_details.*
import kotlinx.android.synthetic.main.content_about_details.*
import org.koin.android.ext.android.inject
import java.util.*

class AboutDetailsActivity : AppCompatActivity() {
    private var aboutDetailsList: List<AboutDetailsModel> = ArrayList()
    lateinit var aboutType: String

    private val aboutDetailsViewModel: AboutDetailsViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_details)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initView()

        //get intent extras
        val extraIntent = intent
        aboutType = extraIntent.getStringExtra("aboutType")

        when (aboutType) {
            "about_droidconKE" -> supportActionBar?.title = "About droidconKE"
            "organizers" -> supportActionBar?.title = "Organizers"
            "sponsors" -> supportActionBar?.title = "Sponsors"
        }

        //fetch about details
        fetchAboutDetails(aboutType)

        //observe live data emitted by view model
        observeLiveData()
    }

    private fun observeLiveData() {
        aboutDetailsViewModel.getAboutDetailsResponse().nonNull().observe(this) {
            handleFetchAboutDetails(it)
        }
        aboutDetailsViewModel.getAboutDetailsError().nonNull().observe(this) {
            handleDatabaseError(it)
        }

    }

    private fun fetchAboutDetails(aboutType: String) {
        aboutDetailsViewModel.fetchAboutDetails(aboutType)
    }

    private fun handleFetchAboutDetails(aboutDetailsModelList: List<AboutDetailsModel>?) {
        when {
            aboutDetailsModelList != null -> {
                aboutDetailsList = aboutDetailsModelList
                initView()
            }
        }
    }

    private fun handleDatabaseError(databaseError: String) {
        toast(databaseError)
    }

    private fun initView() {
        aboutDetailsRv.itemAnimator = DefaultItemAnimator()
        val aboutDetailsAdapter = AboutDetailsAdapter(aboutDetailsList, this) {
            //handle on click
        }
        aboutDetailsRv.adapter = aboutDetailsAdapter
    }


}
