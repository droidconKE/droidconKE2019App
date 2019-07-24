package com.android254.droidconke19.ui.speakers

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.android254.droidconke19.R
import com.android254.droidconke19.models.SpeakersModel
import kotlinx.android.synthetic.main.item_speaker.view.*


class SpeakersAdapter(private val speakersList: List<SpeakersModel>,private val itemClickListener: (SpeakersModel) -> Unit) : RecyclerView.Adapter<SpeakersAdapter.SpeakersViewHolder>() {

    inner class SpeakersViewHolder(itemView: View,private val itemClickListener: (SpeakersModel) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val speakerNameText = itemView.speaker_text
        private val speakerImg = itemView.speaker_image

        fun bindSpeakerDetails(speakersModel: SpeakersModel) {
            with(speakersModel) {
                Glide.with(itemView.context).load(photoUrl)
                        .thumbnail(Glide.with(itemView.context).load(photoUrl))
                        .apply(RequestOptions()
                                .placeholder(R.drawable.profile)
                                .diskCacheStrategy(DiskCacheStrategy.ALL))
                        .into(speakerImg)

                speakerNameText.text = name

                itemView.setOnClickListener {
                    itemClickListener(this)
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpeakersViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_speaker, parent, false)
        return SpeakersViewHolder(itemView,itemClickListener)
    }

    override fun onBindViewHolder(holder: SpeakersViewHolder, position: Int) {
        holder.bindSpeakerDetails(speakersList[position])
    }

    override fun getItemCount(): Int = speakersList.size

}
