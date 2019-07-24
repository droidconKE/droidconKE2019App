package com.android254.droidconke19.ui.about

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android254.droidconke19.R
import com.android254.droidconke19.ui.info.InfoFragmentDirections
import kotlinx.android.synthetic.main.fragment_about.*

class AboutFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_about, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //load about details
        //about type is used to fetch for the specific clicked one
        aboutDroidconText.setOnClickListener {
            val aboutAction = InfoFragmentDirections.actionInfoFragmentToAboutDetailsFragment("about_droidconKE")
            findNavController().navigate(aboutAction)
        }
        organizersText.setOnClickListener {
            val aboutAction = InfoFragmentDirections.actionInfoFragmentToAboutDetailsFragment("organizers")
            findNavController().navigate(aboutAction)
        }
        sponsorsText.setOnClickListener {
            val aboutAction = InfoFragmentDirections.actionInfoFragmentToAboutDetailsFragment("sponsors")
            findNavController().navigate(aboutAction)
        }
    }
}
