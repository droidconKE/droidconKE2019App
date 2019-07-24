package com.android254.droidconke19.ui.sessions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.android254.droidconke19.R
import com.android254.droidconke19.models.SessionsModel
import com.android254.droidconke19.ui.filters.Filter
import com.android254.droidconke19.ui.speakers.SessionSpeakerAdapter
import kotlinx.android.synthetic.main.item_session.view.*

class SessionsAdapter(private val itemClickListener: (SessionsModel) -> Unit) : RecyclerView.Adapter<SessionsAdapter.SessionsViewHolder>() {
    private val rawItems = mutableListOf<SessionsModel>()

    private val allItems = mutableListOf<AdapterItem>()
    private var filteredItems: MutableList<AdapterItem>? = null

    private val items: List<AdapterItem>
        get() = filteredItems ?: allItems

    class SessionsViewHolder(itemView: View, val itemClickListener: (SessionsModel) -> Unit) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SessionsViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_session, parent, false)
        return SessionsViewHolder(itemView, itemClickListener)
    }

    override fun onBindViewHolder(holder: SessionsViewHolder, position: Int) {
        items[position].bindSession(holder,itemClickListener)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun update(events: List<SessionsModel>) {
        rawItems.clear()
        rawItems += events

        val eventsByTime = events.groupBy { it.id }
        val newItems = mutableListOf<AdapterItem>()

        for (time in eventsByTime.keys) {
            val items = eventsByTime[time].orEmpty()
            newItems += items.mapIndexed { index, event ->
                AdapterItem(event)
            }
        }

        allItems.clear()
        allItems += newItems

        notifyDataSetChanged()
    }

    private fun filterItems(events: List<SessionsModel>, currentFilter: Filter) {
        val filteredEvents = events.filter { it.isInFilter(currentFilter) }
        val filteredEventsByTime = filteredEvents.groupBy { it.id }
        val newFilteredItems = mutableListOf<AdapterItem>()

        for (eventsAtTime in filteredEventsByTime.values) {
            newFilteredItems += eventsAtTime.mapIndexed { index, event ->
                AdapterItem(event)
            }
        }

        filteredItems = newFilteredItems
    }

    fun applyFilter(filter: Filter) {
        if (filter == Filter.empty()) {
            filteredItems = null
        } else {
            filterItems(rawItems, filter)
        }

        notifyDataSetChanged()
    }


}
class AdapterItem(
        private val sessionsModel: SessionsModel
){
    fun bindSession(viewHolder : SessionsAdapter.SessionsViewHolder, itemClickListener: (SessionsModel) -> Unit) =
            with(viewHolder.itemView){
                sessionTitleText.text = sessionsModel.title
                sessionRoomText.text = sessionsModel.room
                sessionInAmPmText.text = "${sessionsModel.time_in_am}${sessionsModel.am_pm_label}"
                sessionAudienceText.text = sessionsModel.session_audience
                sessionSpeakerRv.adapter = SessionSpeakerAdapter(sessionsModel.speakerList)

                when (sessionsModel.session_audience) {
                    "intermediate" -> sessionAudienceText.setBackgroundColor(ContextCompat.getColor(context, R.color.colorDeepOrange))
                    "advanced" -> sessionAudienceText.setBackgroundColor(ContextCompat.getColor(context, R.color.tag_text_red))
                    "beginner" -> sessionAudienceText.setBackgroundColor(ContextCompat.getColor(context, R.color.colorGreen))
                    "general" -> sessionAudienceText.setBackgroundColor(ContextCompat.getColor(context, R.color.colorLightBlue))
                }

                setOnClickListener {
                    itemClickListener(sessionsModel)
                }
            }
}