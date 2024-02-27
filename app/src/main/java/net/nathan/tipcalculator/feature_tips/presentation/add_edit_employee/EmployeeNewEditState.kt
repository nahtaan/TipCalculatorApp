package net.nathan.tipcalculator.feature_tips.presentation.add_edit_employee

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePickerState
import net.nathan.tipcalculator.feature_tips.domain.model.EmployeeEntryItem

data class EmployeeNewEditState @OptIn(ExperimentalMaterial3Api::class) constructor(
    val isNameHintVisible: Boolean = true,
    val isHoursHintVisible: Boolean = true,
    val isMinutesHintVisible: Boolean = true,
    val employeeEntry: EmployeeEntryItem = EmployeeEntryItem(
        name = "",
        hours = 0,
        minutes = 0,
        isSelected = true,
        id = null
    ),
    val showTimeDialog: Boolean = false,
    val allowTimeEdit: Boolean = false,
    val endHour: Int = 0,
    val endMinute: Int = 0,
    val timePickerState: TimePickerState = TimePickerState(0,0,true),
    val isEdit: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)