package com.android254.droidconke19.ui.traveldetails

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.android254.droidconke19.R
import com.android254.droidconke19.models.TravelInfoModel
import com.android254.droidconke19.ui.widget.CollapsibleCard
import com.android254.droidconke19.utils.toast
import com.android254.droidconke19.viewmodels.TravelViewModel
import kotlinx.android.synthetic.main.fragment_travel.*
import org.koin.android.ext.android.inject

class TravelFragment : Fragment(R.layout.fragment_travel) {
    private val travelViewModel: TravelViewModel by inject()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        travelViewModel.getTravelDetails()
        observeLiveData()
    }

    private fun observeLiveData() {
        travelViewModel.getTravelDetailsResponse().observe(viewLifecycleOwner, Observer { travelInfoModel ->
            handleTravelInfoResponse(travelInfoModel)
        })
        travelViewModel.getFirebaseError().observe(viewLifecycleOwner, Observer { firebaseError ->
            handleError(firebaseError)
        })
    }

    private fun handleError(firebaseError: String) {
        activity?.toast(firebaseError)
    }

    private fun handleTravelInfoResponse(travelInfoModel: TravelInfoModel) {
        showInfo(travelInfoModel, shuttleInfoCard, carpoolingParkingCard, publicTransportationCard, rideSharingCard)
    }

    private fun showInfo(travelInfoModel: TravelInfoModel, shuttleServiceCard: CollapsibleCard, carpoolingParkingCard: CollapsibleCard, publicTransportationCard: CollapsibleCard, rideSharingCard: CollapsibleCard) {
        shuttleServiceCard.setCardDescription(travelInfoModel.shuttleInfo)
        carpoolingParkingCard.setCardDescription(travelInfoModel.carpoolingParkingInfo)
        publicTransportationCard.setCardDescription(travelInfoModel.publicTransportationInfo)
        rideSharingCard.setCardDescription(travelInfoModel.rideSharingInfo)
    }
}
