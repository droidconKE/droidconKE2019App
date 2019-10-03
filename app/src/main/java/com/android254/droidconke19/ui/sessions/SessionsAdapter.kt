package com.android254.droidconke19.ui.sessions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android254.droidconke19.R
import com.android254.droidconke19.models.Level
import com.android254.droidconke19.models.SessionsModel
import com.android254.droidconke19.repository.FavoritesStore
import com.android254.droidconke19.ui.filters.Filter
import com.android254.droidconke19.ui.speakers.SessionSpeakerImageAdapter
import com.android254.droidconke19.ui.speakers.SessionSpeakerTextAdapter
import kotlinx.android.synthetic.main.item_session.view.*

class SessionsAdapter(private val favoritesStore: FavoritesStore, private val itemClickListener: (SessionsModel) -> Unit) : RecyclerView.Adapter<SessionsAdapter.SessionsViewHolder>() {
    private val rawSessionsList = mutableListOf<SessionsModel>()

    private val allSessionsList = mutableListOf<AdapterItem>()
    private var filteredSessionsList: MutableList<AdapterItem>? = null

    private val sessionsList: List<AdapterItem>
        get() = filteredSessionsList ?: allSessionsList

    class SessionsViewHolder(itemView: View, val itemClickListener: (SessionsModel) -> Unit) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SessionsViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_session, parent, false)
        return SessionsViewHolder(itemView, itemClickListener)
    }

    override fun onBindViewHolder(holder: SessionsViewHolder, position: Int) {
        sessionsList[position].bindSession(holder, itemClickListener,favoritesStore)
    }

    override fun getItemCount(): Int {
        return sessionsList.size
    }

    fun update(events: List<SessionsModel>) {
        rawSessionsList.clear()
        rawSessionsList += events

        val sessionsById = events.groupBy { it.id }
        val newSessions = mutableListOf<AdapterItem>()

        for (id in sessionsById.keys) {
            val sessionList = sessionsById[id].orEmpty()
            newSessions += sessionList.mapIndexed { index, sessionsModel ->
                AdapterItem(sessionsModel)
            }
        }

        allSessionsList.clear()
        allSessionsList += newSessions

        notifyDataSetChanged()
    }

    private fun filterItems(sessionList: List<SessionsModel>, currentFilter: Filter) {
        val filteredSessions = sessionList.filter { it.isInFilter(currentFilter, favoritesStore) }
        val filteredSessionsById = filteredSessions.groupBy { it.id }
        val newFilteredSessions = mutableListOf<AdapterItem>()

        for (sessionAtId in filteredSessionsById.values) {
            newFilteredSessions += sessionAtId.mapIndexed { index, sessionsModel ->
                AdapterItem(sessionsModel)
            }
        }

        filteredSessionsList = newFilteredSessions
    }

    fun applyFilter(filter: Filter) {
        if (filter == Filter.empty()) {
            filteredSessionsList = null
        } else {
            filterItems(rawSessionsList, filter)
        }

        notifyDataSetChanged()
    }


}

class AdapterItem(
        private val sessionsModel: SessionsModel
) {
    fun bindSession(viewHolder: SessionsAdapter.SessionsViewHolder, itemClickListener: (SessionsModel) -> Unit, favoritesStore: FavoritesStore) =
            with(viewHolder.itemView) {
                sessionTitleText.text = sessionsModel.title
                sessionRoomText.text = sessionsModel.room
                sessionInAmPmText.text = sessionsModel.time_in_am
                sessionAudienceText.text = sessionsModel.session_audience.name
                sessionSpeakerImageRv.adapter = SessionSpeakerImageAdapter(sessionsModel.speakerList)
                sessionSpeakerImageRv.layoutManager = LinearLayoutManager(viewHolder.itemView.context, LinearLayoutManager.HORIZONTAL, false)
                sessionSpeakerNameRv.adapter = SessionSpeakerTextAdapter(sessionsModel.speakerList)
                sessionSpeakerNameRv.layoutManager = LinearLayoutManager(viewHolder.itemView.context, LinearLayoutManager.HORIZONTAL, false)

                when (sessionsModel.session_audience) {
                    Level.intermediate -> sessionAudienceText.setBackgroundResource(R.drawable.rounded_intermediate_textview_bg)
                    Level.advanced -> sessionAudienceText.setBackgroundResource(R.drawable.rounded_advanced_level_textvieww_bg)
                    Level.beginner -> sessionAudienceText.setBackgroundResource(R.drawable.rounded_beginner_level_textview_bg)
                    Level.general -> sessionAudienceText.setBackgroundResource(R.drawable.rounded_general_level_textview_bg)
                    else -> {
                    }
                }

                updateFavoriteIcon(bookMarkImg, favoritesStore)

                setOnClickListener {
                    itemClickListener(sessionsModel)
                }
            }

    private fun updateFavoriteIcon(bookMarkImg: ImageView?, favoritesStore: FavoritesStore) {
        val isFavorite = favoritesStore.isFavorite(sessionsModel)
        if (isFavorite) bookMarkImg?.visibility = View.VISIBLE else bookMarkImg?.visibility = View.GONE
    }
}