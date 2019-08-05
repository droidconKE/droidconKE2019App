package com.android254.droidconke19.ui.speakers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.android254.droidconke19.R
import com.android254.droidconke19.utils.loadImage
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.fragment_speaker.*

class SpeakerFragment : Fragment() {
    private val speakerArgs: SpeakerFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_speaker, container, false)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(R.transition.speaker_shared_enter)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showSpeakerDetails(speakerArgs)
        speaker_session_background.setOnClickListener {
            findNavController().navigate(SpeakerFragmentDirections.actionSpeakerFragmentToSessionDetailsFragment(speakerArgs.sessionModel, speakerArgs.sessionModel.title))
        }
    }

    private fun showSpeakerDetails(speakerArgs: SpeakerFragmentArgs) {
        speakerNameText.text = speakerArgs.speakerModel.name
        speakerDescriptionText.text = speakerArgs.speakerModel.bio
        speakerCompanyText.text = speakerArgs.speakerModel.company
        speaker_image.loadImage(speakerArgs.speakerModel.photoUrl, R.drawable.placeholder_image)
        speakerArgs.sessionModel.title
        speakerSessionSummaryText.text = speakerArgs.sessionModel.time


    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.action_profile)?.isVisible = false
        menu.findItem(R.id.eventFeedbackFragment)?.isVisible = false
    }
}