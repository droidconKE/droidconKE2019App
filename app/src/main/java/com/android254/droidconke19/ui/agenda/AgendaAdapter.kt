package com.android254.droidconke19.ui.agenda

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.android254.droidconke19.R
import com.android254.droidconke19.models.AgendaModel
import com.android254.droidconke19.utils.loadImage
import kotlinx.android.synthetic.main.item_agenda.view.*


class AgendaAdapter(private val agendaModelList: List<AgendaModel>, private val context: Context) : RecyclerView.Adapter<AgendaAdapter.AgendaViewHolder>() {
    inner class AgendaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val agendaTitleText = itemView.agendaTitleText
        private val agendaTimelineText = itemView.agendaTimelineText
        private val agendaImg = itemView.agendaImg

        @SuppressLint("Range")
        fun bindAgendas(agendaModel: AgendaModel) {
            with(agendaModel) {
                agendaImg.loadImage(iconUrl, R.drawable.placeholder_image)
                agendaTitleText.text = title
                agendaTimelineText.text = time
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AgendaViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_agenda, parent, false)
        return AgendaViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AgendaViewHolder, position: Int) {
        holder.bindAgendas(agendaModelList[position])
    }

    override fun getItemCount(): Int = agendaModelList.size


}
