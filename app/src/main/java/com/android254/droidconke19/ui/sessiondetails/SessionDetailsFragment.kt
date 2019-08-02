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
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.android254.droidconke19.R
import com.android254.droidconke19.models.SessionsModel
import com.android254.droidconke19.models.SpeakersModel
import com.android254.droidconke19.ui.speakers.SpeakersAdapter
import com.android254.droidconke19.utils.isSignedIn
import com.android254.droidconke19.utils.nonNull
import com.android254.droidconke19.utils.observe
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
import kotlin.collections.ArrayList

class SessionDetailsFragment : Fragment() {
    private val sessionDetailsViewModel: SessionDetailsViewModel by sharedViewModel()
    private val sharedPreferences: SharedPreferences by inject { parametersOf(context) }
    private val firebaseAuth: FirebaseAuth by inject()
    lateinit var session: SessionsModel

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_session_details, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        session_favorite.setOnClickListener {
            if (!firebaseAuth.isSignedIn()) {
                findNavController().navigate(R.id.signInDialogFragment)
                return@setOnClickListener
            }
            val userId = firebaseAuth.currentUser!!.uid
            lifecycleScope.launch {
                progress_bar.visibility = View.VISIBLE
//                if (sessionDetailsViewModel.addToFavourites(sharedPreferences, userId)) {
//                    activity?.toast(getString(R.string.session_added))
//                } else {
//                    activity?.toast(getString(R.string.session_removed))
//                }
                styleFavouritesButton(sessionDetailsViewModel.isFavourite(sharedPreferences))
                progress_bar.visibility = View.GONE
            }

        }
        styleFavouritesButton(sessionDetailsViewModel.isFavourite(sharedPreferences))

        bottom_app_bar.replaceMenu(R.menu.menu_bottom_appbar)
        handleBottomBarMenuClick()

        //observer live data
        observeLiveData()
    }

    private fun observeLiveData() {
        sessionDetailsViewModel.getSessionDetails().nonNull().observe(this) { sessionModel ->
            session = sessionModel
            setupViews(sessionModel)
        }
        sessionDetailsViewModel.message.observe(this, Observer {
            activity?.toast(it)
        })
    }

    private fun setupViews(sessionModel: SessionsModel) {
        sessionTimeRoomText.text = getString(R.string.session_room_and_session_duration, sessionModel.duration, sessionModel.room)
        sessionStartTimeText.text = sessionModel.time
        sessionDescriptionText.text = sessionModel.description
        intendedAudienceText.text = sessionModel.session_audience
        topicChip.text = sessionModel.topic
        typeChip.text = sessionModel.type.value
        sessionSpeakersRv.adapter = SpeakersAdapter(sessionModel.speakerList) { speakerModel ->
            val extras = FragmentNavigatorExtras(
                    speaker_image to "speakerImage"
            )
            val sessionDetailsFragment = SessionDetailsFragmentDirections.actionSessionDetailsFragmentToSpeakerFragment(speakerModel, sessionModel)
            findNavController().navigate(sessionDetailsFragment, extras)
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
                R.id.action_map -> {}
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
    }
}