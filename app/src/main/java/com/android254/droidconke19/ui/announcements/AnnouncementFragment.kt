package com.android254.droidconke19.ui.announcements

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.android254.droidconke19.R
import com.android254.droidconke19.models.Announcement
import com.android254.droidconke19.utils.nonNull
import com.android254.droidconke19.utils.observe
import com.android254.droidconke19.viewmodels.AnnouncementViewModel
import kotlinx.android.synthetic.main.fragment_announcements.*
import org.jetbrains.anko.toast
import org.koin.android.ext.android.inject

class AnnouncementFragment : Fragment() {
    private val announcementViewModel: AnnouncementViewModel by inject()
    private var announcementList: List<Announcement> = ArrayList()
    private lateinit var announcementAdapter: AnnouncementsAdapter

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_announcements, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        announcementAdapter = AnnouncementsAdapter(announcementList)

        //get announcements
        getAnnouncements()
        showProgressBar()
        //show announcement list
        initView()

        //observe live data
        observeLiveData()
    }

    private fun getAnnouncements() {
        announcementViewModel.getAnnouncements()
    }

    private fun initView() {
        announcementsRv.adapter = announcementAdapter
    }

    private fun observeLiveData() {
        announcementViewModel.getAnnouncementsResponse().nonNull().observe(this) {
            hideProgressBar()
            announcementAdapter.setAnnouncements(it)
        }
        announcementViewModel.getAnnouncementError().nonNull().observe(this) {
            handleError(it)

        }

    }

    private fun handleError(databaseError: String?) {
        hideProgressBar()
        activity?.toast(databaseError.toString())
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.action_profile)?.isVisible = false
        menu.findItem(R.id.eventFeedbackFragment)?.isVisible = false
    }

    private fun showProgressBar(){
        progressBar.visibility = View.VISIBLE
        announcementsRv.visibility = View.GONE

    }

    private fun hideProgressBar(){
        progressBar.visibility = View.GONE
        announcementsRv.visibility = View.VISIBLE
    }

}