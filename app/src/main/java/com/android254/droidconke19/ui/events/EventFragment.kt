package com.android254.droidconke19.ui.events


import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.android254.droidconke19.R
import com.android254.droidconke19.datastates.FirebaseResult
import com.android254.droidconke19.models.EventTypeModel
import com.android254.droidconke19.models.WifiDetailsModel
import com.android254.droidconke19.utils.toast
import com.android254.droidconke19.viewmodels.EventTypeViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_event.*
import org.koin.android.ext.android.inject

class EventFragment : Fragment(R.layout.fragment_event) {
    private val eventTypeViewModel: EventTypeViewModel by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //observe live data emitted by view model
        observeLiveData()
        //fetch data from firebase
        eventTypeViewModel.fetchSessions()
        eventTypeViewModel.fetchWifiDetails()
        showProgressBar()

    }

    private fun observeLiveData() {
        eventTypeViewModel.getEventTypeResponse().observe(viewLifecycleOwner, Observer { eventTypeModelList ->
            handleFetchEventsResponse(eventTypeModelList, eventTypesRv)
        })
        eventTypeViewModel.getFirebaseError().observe(viewLifecycleOwner, Observer { firebaseError ->
            handleDatabaseError(firebaseError)
        })
        eventTypeViewModel.getWifiDetailsReponse().observe(viewLifecycleOwner, Observer { wifiDetailsResult ->
            updateWifiDetailsOnUI(wifiDetailsResult)
        })
    }

    private fun handleFetchEventsResponse(eventTypeModelList: List<EventTypeModel>?, eventTypesRv: RecyclerView) {
        hideProgressBar()
        when {
            eventTypeModelList != null -> initView(eventTypeModelList, eventTypesRv)
        }
    }

    private fun handleDatabaseError(databaseError: String) {
        hideProgressBar()
        activity?.toast(databaseError)
    }

    private fun initView(eventTypeModelList: List<EventTypeModel>, eventTypesRv: RecyclerView) {
        eventTypesRv.isNestedScrollingEnabled = false
        eventTypesRv.adapter = EventTypeAdapter(eventTypeModelList, activity!!)

    }

    private fun updateWifiDetailsOnUI(wifiDetailsModel: WifiDetailsModel) {
        wifiSsidText.text = wifiDetailsModel.wifiSsid
        wifiPasswordText.text = wifiDetailsModel.wifiPassword
    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
        eventTypeLinear.visibility = View.GONE

    }

    private fun hideProgressBar() {
        progressBar.visibility = View.GONE
        eventTypeLinear.visibility = View.VISIBLE
    }

}
