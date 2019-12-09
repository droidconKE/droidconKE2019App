package com.android254.droidconke19.ui.events


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.android254.droidconke19.R
import com.android254.droidconke19.datastates.FirebaseResult
import com.android254.droidconke19.models.EventTypeModel
import com.android254.droidconke19.models.WifiDetailsModel
import com.android254.droidconke19.viewmodels.EventTypeViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_event.*
import org.koin.android.ext.android.inject

class EventFragment : Fragment() {
    private val eventTypeViewModel: EventTypeViewModel by inject()

    private val fetchErrorSnackbar: Snackbar by lazy {
        Snackbar.make(
                events_relativeLayout,
                R.string.fetching_data_error_message,
                Snackbar.LENGTH_INDEFINITE
        ).setAction("Retry") { eventTypeViewModel.retry() }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_event, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //observe live data emitted by view model
        observeLiveData()
        //fetch data from firebase
        eventTypeViewModel.fetchSessions()
        showProgressBar()

    }

    private fun observeLiveData() {
        eventTypeViewModel.getWifiDetailsResponse().observe(this, Observer {
            handleFetchEventsResponse(it, eventTypesRv)
        })
        eventTypeViewModel.getWifiDetailsError().observe(this, Observer {
            handleDatabaseError(it)
        })
        eventTypeViewModel.wifiDetails.observe(viewLifecycleOwner, Observer {
            handleWifiDetails(it)
        })
    }

    private fun handleFetchEventsResponse(eventTypeModelList: List<EventTypeModel>?, eventTypesRv: RecyclerView) {
        hideProgressBar()
        when {
            eventTypeModelList != null -> initView(eventTypeModelList, eventTypesRv)
        }
    }

    private fun handleDatabaseError(databaseError: String?) {
        hideProgressBar()
        fetchErrorSnackbar.show()
    }

    private fun initView(eventTypeModelList: List<EventTypeModel>, eventTypesRv: RecyclerView) {
        eventTypesRv.isNestedScrollingEnabled = false
        eventTypesRv.adapter = EventTypeAdapter(eventTypeModelList, activity!!)

    }

    private fun handleWifiDetails(wifiDetails: FirebaseResult<WifiDetailsModel>) {
        if (wifiDetails is FirebaseResult.Success) {
            updateWifiDetailsOnUI(wifiDetails.data)
        } else if (wifiDetails is FirebaseResult.Error){
            handleWifiDetailsError(wifiDetails.exception)
        }
    }

    private fun updateWifiDetailsOnUI(wifiDetailsModel: WifiDetailsModel) {
        wifiSsidText.text = wifiDetailsModel.wifiSsid
        wifiPasswordText.text = wifiDetailsModel.wifiPassword
    }

    private fun handleWifiDetailsError(error: String?) {
        fetchErrorSnackbar.show()
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
