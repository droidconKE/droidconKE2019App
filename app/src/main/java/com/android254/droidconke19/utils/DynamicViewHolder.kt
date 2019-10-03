package com.android254.droidconke19.utils

import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer

typealias ViewCreator = (ViewGroup) -> DynamicViewHolder

class DynamicViewAdapter : RecyclerView.Adapter<DynamicViewHolder>() {

    private var data = listOf<DynamicDataModel>()
    private val viewCreators = SparseArray<ViewCreator>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DynamicViewHolder =
            viewCreators.get(viewType).invoke(parent)

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: DynamicViewHolder, position: Int) {
        holder.bind(data[position])
    }

}

abstract class DynamicViewHolder(
        override val containerView: View
) : RecyclerView.ViewHolder(containerView), LayoutContainer {
    abstract fun bind(data: Any)
}

abstract class DynamicDataModel {
    val viewType: Int
        get() = getViewCreator().hashCode()

    abstract fun getViewCreator(): ViewCreator

    open fun areItemsThemSame(other: DynamicDataModel): Boolean = false

    open fun areContentsTheSame(other: DynamicDataModel): Boolean = false
}
