package droiddevelopers254.droidconke.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import droiddevelopers254.droidconke.R
import droiddevelopers254.droidconke.ui.filters.Filter
import kotlinx.android.parcel.Parcelize
import java.util.*
import kotlin.collections.ArrayList

interface Filterable {
    fun isInFilter(filter: Filter): Boolean
}

@Entity(tableName = "sessionsList")
@Parcelize
data class SessionsModel(
        @PrimaryKey
        var id: Int = 0,
        var speaker_id: ArrayList<Int> = ArrayList(),
        val stage: Stage,
        var room_id: Int = 0,
        var main_tag: String = "",
        var room: String = "",
        var speakers: List<String> = ArrayList(),
        var starred: String = "",
        var time: String = "",
        var title: String = "",
        var topic: String = "",
        var url: String = "",
        var duration: String = "",
        var description: String = "",
        var session_color: String = "",
        var topic_id: Int = 0,
        var type_id: Int = 0,
        var type: Type,
        var documentId: String = "",
        var timestamp: String = "",
        var day_number: String = "",
        var time_in_am: String = "",
        var am_pm_label: String = "",
        var session_audience: String = "",
        val level :Level

) : Parcelable, Filterable {
    override fun isInFilter(filter: Filter): Boolean {
        return if (filter == Filter.empty()) {
            true
        } else {
            var result = true
            if (filter.stages.isNotEmpty()) {
                result = result && filter.stages.contains(stage)
            }
            if (filter.types.isNotEmpty()) {
                result = result && filter.types.contains(type)
            }
            if (filter.levels.isNotEmpty()) {
                result = result && filter.levels.contains(level)
            }
            return result
        }
    }

    fun contains(query: String): Boolean {
        val text = query.toLowerCase(Locale.getDefault())
        var result = false

        result = result || title.toLowerCase(Locale.getDefault()).contains(text)

        if (speakers.isNotEmpty()) {
            val candidates = speakers.map { it.toLowerCase(Locale.getDefault()) }
            result = result || candidates.any { it.contains(text) }
        }

        result = result || description.toLowerCase(Locale.getDefault()).contains(text)

        return result
    }
}

enum class Stage {
    MainHall, Room1, Room2, Room3,None
}

enum class Type(val value: String, val resId: Int) {
    Session("Session", R.drawable.ic_outline_video_label),
    LightningTalk("Lightning talk", R.drawable.ic_outline_flash_on),
    Workshop("Workshop", R.drawable.ic_outline_build),
    PanelDiscussion("Panel discussion", R.drawable.ic_outline_question_answer),
    None("None", 0)
}

enum class Level(val resId: Int) {
    Introductory(R.drawable.ic_outline_signal_cellular_one),
    Intermediate(R.drawable.ic_outline_signal_cellular_two),
    Advanced(R.drawable.ic_outline_signal_cellular_three),
    None(0)
}

