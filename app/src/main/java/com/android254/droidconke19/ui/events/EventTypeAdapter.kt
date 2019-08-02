package com.android254.droidconke19.ui.events

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.android254.droidconke19.R
import com.android254.droidconke19.models.EventTypeModel
import com.android254.droidconke19.utils.loadImage
import kotlinx.android.synthetic.main.item_event_type.view.*

class EventTypeAdapter(private val eventTypesList: List<EventTypeModel>, private val context: Context) : RecyclerView.Adapter<EventTypeAdapter.EventViewHolder>() {

    inner class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val eventDescriptionText = itemView.eventDescriptionText
        private val eventImg = itemView.eventImg

        fun bindEvents(eventTypeModel: EventTypeModel) {
            with(eventTypeModel) {
                eventImg.loadImage(eventImageUrl,R.drawable.splash)
                eventDescriptionText.text = description
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_event_type, parent, false)
        return EventViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bindEvents(eventTypesList[position])
    }

    override fun getItemCount(): Int = eventTypesList.size


}
