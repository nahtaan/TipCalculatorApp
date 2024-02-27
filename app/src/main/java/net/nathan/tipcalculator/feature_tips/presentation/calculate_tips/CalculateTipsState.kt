package net.nathan.tipcalculator.feature_tips.presentation.calculate_tips

import android.icu.util.Calendar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePickerState
import net.nathan.tipcalculator.feature_tips.domain.model.EmployeeEntryItem

data class CalculateTipsState @OptIn(ExperimentalMaterial3Api::class) constructor(
    val totalTips: String = "",
    val totalHours: Double = 0.0,
    val employees: List<EmployeeEntryItem> = emptyList(),
    val isTotalHintShown: Boolean = true,
    val endTimePickerState: TimePickerState = TimePickerState(
        Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
        Calendar.getInstance().get(Calendar.MINUTE),
        true
    ),
    val showEndTimeDialog: Boolean = false,
    val error: String? = null
)