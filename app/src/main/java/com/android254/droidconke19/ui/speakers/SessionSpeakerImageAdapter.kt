package com.android254.droidconke19.ui.speakers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android254.droidconke19.R
import com.android254.droidconke19.models.SpeakersModel
import com.android254.droidconke19.utils.loadImage
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_session_speaker_image.view.*

class SessionSpeakerImageAdapter(private val speakerList: List<SpeakersModel>) : RecyclerView.Adapter<SessionSpeakerImageAdapter.SessionSpeakerViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SessionSpeakerViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_session_speaker_image, parent, false)
        return SessionSpeakerViewHolder(itemView)

    }

    override fun getItemCount(): Int = speakerList.size

    override fun onBindViewHolder(holder: SessionSpeakerViewHolder, position: Int) {
        holder.bindSpeakerSession(speakerList[position])
    }

    class SessionSpeakerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val sessionSpeakerImg = itemView.sessionSpeakerImg

        fun bindSpeakerSession(speakersModel: SpeakersModel) {
            with(speakersModel) {
                sessionSpeakerImg.loadImage(photoUrl, R.drawable.placeholder_image)
            }

        }

    }
}