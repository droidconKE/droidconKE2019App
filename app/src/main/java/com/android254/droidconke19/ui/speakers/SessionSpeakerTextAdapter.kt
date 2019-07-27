package com.android254.droidconke19.ui.speakers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android254.droidconke19.R
import com.android254.droidconke19.models.SpeakersModel
import kotlinx.android.synthetic.main.item_session_speaker_names.view.*

class SessionSpeakerTextAdapter(private val speakerList: List<SpeakersModel>) : RecyclerView.Adapter<SessionSpeakerTextAdapter.SessionSpeakerTextViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SessionSpeakerTextAdapter.SessionSpeakerTextViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_session_speaker_names, parent, false)
        return SessionSpeakerTextViewHolder(itemView)
    }

    override fun getItemCount(): Int = speakerList.size

    override fun onBindViewHolder(holder: SessionSpeakerTextViewHolder, position: Int) {
        holder.bindSpeakerText(speakerList[position])
    }

    class SessionSpeakerTextViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val sessionSpeakerText = itemView.sessionSpeakerText

        fun bindSpeakerText(speakerModel: SpeakersModel) {
            with(speakerModel) {
                sessionSpeakerText.text = name
            }
        }

    }
}