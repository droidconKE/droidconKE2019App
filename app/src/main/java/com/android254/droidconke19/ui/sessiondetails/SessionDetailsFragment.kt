package com.android254.droidconke19.ui.sessiondetails

import android.content.Intent
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.os.Bundle
import android.provider.CalendarContract
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.android254.droidconke19.R
import com.android254.droidconke19.models.ReserveSeatModel
import com.android254.droidconke19.models.SessionsModel
import com.android254.droidconke19.ui.speakers.SpeakersAdapter
import com.android254.droidconke19.utils.isSignedIn
import com.android254.droidconke19.utils.toast
import com.android254.droidconke19.viewmodels.SessionDetailsViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_session_details.*
import kotlinx.android.synthetic.main.item_speaker.*
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.core.parameter.parametersOf
import java.util.*

class SessionDetailsFragment : Fragment(R.layout.fragment_session_details) {
    private val sessionDetailsViewModel: SessionDetailsViewModel by sharedViewModel()
    private val sharedPreferences: SharedPreferences by inject { parametersOf(context) }
    private val firebaseAuth: FirebaseAuth by inject()
    lateinit var session: SessionsModel
    private val sessionDetailsFragmentArgs: SessionDetailsFragmentArgs by navArgs()

    private val navListener = NavController.OnDestinationChangedListener { controller, destination, _ ->
        if (controller.currentDestination?.id == R.id.sessionDetailsFragment) {
            progress_bar.visibility = View.GONE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        session = sessionDetailsFragmentArgs.sessionModel

        clickListeners()
        styleFavouritesButton(sessionDetailsViewModel.isFavourite(sharedPreferences, session))

        bottom_app_bar.replaceMenu(R.menu.menu_bottom_appbar)
        handleBottomBarMenuClick()
        //observer live data
        observeLiveData()
        setupViews(session)
        setTextLabel(sessionDetailsViewModel.isSeatReserved(sharedPreferences, session))
        findNavController().addOnDestinationChangedListener(navListener)
    }

    override fun onDestroyView() {
        findNavController().removeOnDestinationChangedListener(navListener)
        super.onDestroyView()
    }

    private fun clickListeners() {
        sessionReserveSeatText.setOnClickListener {
            progress_bar.visibility = View.VISIBLE
            reserveSeat()
        }

        session_favorite.setOnClickListener {
            if (!firebaseAuth.isSignedIn()) {
                findNavController().navigate(R.id.signInDialogFragment)
                return@setOnClickListener
            }
            val userId = firebaseAuth.currentUser!!.uid
            lifecycleScope.launch {
                progress_bar.visibility = View.VISIBLE
                if (sessionDetailsViewModel.addToFavourites(sharedPreferences, userId, session)) {
                    activity?.toast(getString(R.string.session_added))
                } else {
                    activity?.toast(getString(R.string.session_removed))
                }
                styleFavouritesButton(sessionDetailsViewModel.isFavourite(sharedPreferences, session))
                progress_bar.visibility = View.GONE
            }

        }

        sessionFeedbackText.setOnClickListener {
            val sessionFeedbackAction = SessionDetailsFragmentDirections.actionSessionDetailsFragmentToSessionFeedbackFragment(session.day_number, session.day_number, session.id, session.title)
            findNavController().navigate(sessionFeedbackAction)
        }
    }

    private fun reserveSeat() {
        if (!firebaseAuth.isSignedIn()) {
            findNavController().navigate(R.id.signInDialogFragment)
            return
        }
        val reserveSeatModel = ReserveSeatModel(session.id.toString(), session.day_number, session.title, firebaseAuth.currentUser!!.uid)
        lifecycleScope.launch {
            sessionDetailsViewModel.reserveSeat(reserveSeatModel, sharedPreferences, session)
            setTextLabel(sessionDetailsViewModel.isSeatReserved(sharedPreferences, session))
        }

    }

    private fun setTextLabel(seatReserved: Boolean) {
        when {
            seatReserved -> {
                sessionReserveSeatText.text = getString(R.string.seat_served_text)
                sessionReserveSeatText.setTextColor(ContextCompat.getColor(context!!, R.color.colorGreen))
            }
            else -> {
                sessionReserveSeatText.text = getString(R.string.session_detail_reserve_seat)
                sessionReserveSeatText.setTextColor(ContextCompat.getColor(context!!, R.color.colorBlack))
            }
        }
    }

    private fun observeLiveData() {
        sessionDetailsViewModel.getReserveSeatResponse().observe(viewLifecycleOwner, Observer { reserveSeatResponse ->
            handleReserveSeatResponse(reserveSeatResponse)
        })
    }

    private fun handleReserveSeatResponse(reserveSeatResponse: String) {
        progress_bar.visibility = View.GONE
        activity?.toast(reserveSeatResponse)
    }

    private fun setupViews(sessionModel: SessionsModel) {
        sessionTimeRoomText.text = getString(R.string.session_room_and_session_duration, sessionModel.duration, sessionModel.stage)
        sessionStartTimeText.text = sessionModel.time
        sessionDescriptionText.text = sessionModel.description
        intendedAudienceText.text = sessionModel.session_audience.name
        topicChip.text = sessionModel.topic
        typeChip.text = sessionModel.type.value
        sessionSpeakersRv.adapter = SpeakersAdapter(sessionModel.speakerList) { speakerModel ->
            val extras = FragmentNavigatorExtras(
                    speaker_image to "speakerImage"
            )
            val sessionDetailsFragment = SessionDetailsFragmentDirections.actionSessionDetailsFragmentToSpeakerFragment(speakerModel, sessionModel)
            findNavController().navigate(sessionDetailsFragment, extras)
        }
        when (sessionModel.start_status) {
            "notStarted" -> {
                divider_top_feedback.visibility = View.GONE
                session_feedback_title.visibility = View.GONE
                sessionFeedbackText.visibility = View.GONE
                sessionReserveSeatText.visibility = View.VISIBLE
            }
            "sessionEnded" -> {
                divider_top_feedback.visibility = View.VISIBLE
                session_feedback_title.visibility = View.VISIBLE
                sessionFeedbackText.visibility = View.VISIBLE
                sessionReserveSeatText.visibility = View.GONE
            }
        }

    }

    private fun styleFavouritesButton(isFavourites: Boolean) {
        if (isFavourites) {
            session_favorite.setImageResource(R.drawable.ic_bookmark_black_24dp)
            session_favorite.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context!!, R.color.colorWhite))
            session_favorite.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(context!!, R.color.colorPrimary))
        } else {
            session_favorite.setBackgroundResource(R.drawable.ic_bookmark_border_black_24dp)
            session_favorite.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context!!, R.color.colorPrimary))
            session_favorite.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(context!!, R.color.white))
        }
    }

    private fun handleBottomBarMenuClick() {
        bottom_app_bar.setOnMenuItemClickListener { item ->
            val id = item.itemId
            when (id) {
                R.id.action_share -> {
                    val shareSession = Intent()
                    shareSession.action = Intent.ACTION_SEND
                    shareSession.putExtra(Intent.EXTRA_TEXT, "Check out ${session.title}" + getString(R.string.droidcoke_hashtag) + "\n" + getString(R.string.droidconke_site))
                    shareSession.type = "text/plain"
                    startActivity(shareSession)
                }
            }
            when (id) {
                R.id.action_calendar -> {
                    val day = when (session.day_number) {
                        "day_one" -> 8
                        "day_two" -> 9
                        else -> 8
                    }

                    val stringDelimitter = ":"
                    val startTimeParts = session.time_in_am.split(stringDelimitter)
                    val endTimeParts = session.end_time_in_am.split(stringDelimitter)

                    val startMillis: Long = Calendar.getInstance().run {
                        set(2019, 8, day, startTimeParts[0].toInt(), startTimeParts[1].toInt())
                        timeInMillis
                    }

                    val endMillis: Long = Calendar.getInstance().run {
                        set(2019, 8, day, endTimeParts[0].toInt(), endTimeParts[1].toInt())
                        timeInMillis
                    }

                    val intent = Intent(Intent.ACTION_INSERT)
                            .setData(CalendarContract.Events.CONTENT_URI)
                            .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startMillis)
                            .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endMillis)
                            .putExtra(CalendarContract.Events.TITLE, session.title)
                            .putExtra(CalendarContract.Events.EVENT_LOCATION, "Ihub,Senteu Plaza")
                    activity?.startActivity(intent)
                }
            }

            false
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.action_profile)?.isVisible = false
        menu.findItem(R.id.eventFeedbackFragment)?.isVisible = false
    }
}