package com.android254.droidconke19.ui.speakers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android254.droidconke19.R
import com.android254.droidconke19.models.SpeakersModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_session_speaker.view.*

class SessionSpeakerAdapter(private val speakerList : List<SpeakersModel>): RecyclerView.Adapter<SessionSpeakerAdapter.SessionSpeakerViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SessionSpeakerViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_session_speaker, parent, false)
        return SessionSpeakerViewHolder(itemView)

    }

    override fun getItemCount(): Int  = speakerList.size

    override fun onBindViewHolder(holder: SessionSpeakerViewHolder, position: Int) {
        holder.bindSpeakerSession(speakerList[position])
    }

    class SessionSpeakerViewHolder(itemView : View): RecyclerView.ViewHolder(itemView) {
        private val sessionSpeakerImg = itemView.sessionSpeakerImg
        private val sessionSpeakerText = itemView.sessionSpeakerText


        fun bindSpeakerSession(speakersModel: SpeakersModel){
            with(speakersModel){
                Glide.with(itemView.context).load(photoUrl)
                        .thumbnail(Glide.with(itemView.context).load(photoUrl))
                        .apply(RequestOptions()
                                .placeholder(R.drawable.profile)
                                .diskCacheStrategy(DiskCacheStrategy.ALL))
                        .into(sessionSpeakerImg)

                sessionSpeakerText.text  = name

            }

        }

    }
}