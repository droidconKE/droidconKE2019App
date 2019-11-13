package com.android254.droidconke19.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer

typealias ViewCreator = (ViewGroup) -> DynamicViewHolder

abstract class DynamicViewAdapter : RecyclerView.Adapter<DynamicViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DynamicViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(viewType, parent, false)
        return DynamicViewHolder(view)
    }

    override fun getItemCount(): Int = 0

    override fun onBindViewHolder(holder: DynamicViewHolder, position: Int) {
        holder.bind(objForPosition(position))
    }

    abstract fun objForPosition(position: Int): Any

}

class DynamicViewHolder(
        override val containerView: View
) : RecyclerView.ViewHolder(containerView), LayoutContainer {
    fun bind(data: Any) {

    }
}
