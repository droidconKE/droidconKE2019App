package com.android254.droidconke19.ui.speakers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionInflater
import com.android254.droidconke19.R
import kotlinx.android.synthetic.main.fragment_speaker.*

class SpeakerFragment : Fragment() {

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
        speaker_session_background.setOnClickListener {
            findNavController().navigate(SpeakerFragmentDirections.actionSpeakerFragmentToSessionDetailsFragment())
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.clear()
    }
}