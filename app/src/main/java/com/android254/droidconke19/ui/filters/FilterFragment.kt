package com.android254.droidconke19.ui.filters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.chip.Chip
import com.android254.droidconke19.R
import com.android254.droidconke19.models.Level
import com.android254.droidconke19.models.Stage
import com.android254.droidconke19.models.Type
import com.android254.droidconke19.ui.widget.RoundedBottomSheetFragment
import kotlinx.android.synthetic.main.fragment_filter.*

class FilterFragment : RoundedBottomSheetFragment() {
    private val filterStore = FilterStore.instance
    private var onFilterChanged: (Filter) -> Unit = {}

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_filter, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        closeButton.setOnClickListener { dismiss() }

        favoritesChip.isChecked = filterStore.filter.isFavorites
        favoritesChip.isCloseIconVisible = favoritesChip.isChecked

        favoritesChip.setOnCheckedChangeListener { compoundButton, isChecked ->
            val chip = compoundButton as Chip
            chip.isCloseIconVisible = isChecked
            filterStore.toggleFavorites()
            onFilterChanged(filterStore.filter)
        }
        favoritesChip.setOnCloseIconClickListener {
            val chip = it as Chip
            chip.isChecked = false
        }

        val stages = Stage.values().toList().minus(Stage.None).map { stage ->
            stage.value
        }

        val stageChips = getChips(stages)
        stageChips.forEachIndexed { index, chip ->
            chip.isChecked = filterStore.filter.stages.contains(Stage.values()[index])
            chip.isCloseIconVisible = chip.isChecked

            chip.setOnCheckedChangeListener { compoundButton, isChecked ->
                val c = compoundButton as Chip
                c.isCloseIconVisible = isChecked
                filterStore.toggleStage(Stage.values()[index])
                onFilterChanged(filterStore.filter)
            }
            chip.setOnCloseIconClickListener { view ->
                val c = view as Chip
                c.isChecked = false
            }
            stagesChipGroup.addView(chip)
        }

        val types = Type.values().toList().minus(Type.None).map { type ->
            type.value
        }
        val typeChips = getChips(types)
        typeChips.forEachIndexed { index, chip ->
            chip.isChecked = filterStore.filter.types.contains(Type.values()[index])
            chip.isCloseIconVisible = chip.isChecked

            chip.setOnCheckedChangeListener { compoundButton, isChecked ->
                val c = compoundButton as Chip
                c.isCloseIconVisible = isChecked
                filterStore.toggleType(Type.values()[index])
                onFilterChanged(filterStore.filter)
            }
            chip.setOnCloseIconClickListener { view ->
                val c = view as Chip
                c.isChecked = false
            }
            typesChipGroup.addView(chip)
        }

        val levels = Level.values().toList().minus(Level.None).map { level ->
            level.name
        }
        val levelChips = getChips(levels)
        levelChips.forEachIndexed { index, chip ->
            chip.isChecked = filterStore.filter.levels.contains(Level.values()[index])
            chip.isCloseIconVisible = chip.isChecked

            chip.setOnCheckedChangeListener { compoundButton, isChecked ->
                val c = compoundButton as Chip
                c.isCloseIconVisible = isChecked
                filterStore.toggleLevel(Level.values()[index])
                onFilterChanged(filterStore.filter)
            }
            chip.setOnCloseIconClickListener { view ->
                val c = view as Chip
                c.isChecked = false
            }
            levelChipGroup.addView(chip)
        }
    }

    private fun getChips(values: List<String>): List<Chip> {
        return values.map { value ->
            FilterChip(requireContext()).apply { text = value }
        }
    }

    companion object {
        fun newInstance(
                onFilterChanged: (Filter) -> Unit
        ) = FilterFragment().apply {
            this.onFilterChanged = onFilterChanged
        }
    }
}