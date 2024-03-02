package net.nathan.tipcalculator.feature_tips.presentation.add_edit_employee

import androidx.compose.ui.focus.FocusState

sealed class EmployeeNewEditEvent {
    data class EnteredName(val value: String): EmployeeNewEditEvent()
    data class ChangedNameFocus(val focusState: FocusState): EmployeeNewEditEvent()
    data class EnteredHours(val value: String): EmployeeNewEditEvent()
    data class ChangedHoursFocus(val focusState: FocusState): EmployeeNewEditEvent()
    data class EnteredMinutes(val value: String): EmployeeNewEditEvent()
    data class ChangedMinutesFocus(val focusState: FocusState): EmployeeNewEditEvent()
    data object HideTimeDialog: EmployeeNewEditEvent()
    data object ShowStartTimeDialog: EmployeeNewEditEvent()
    data object ShowEndTimeDialog: EmployeeNewEditEvent()
    data object SaveTimeDialog: EmployeeNewEditEvent()
    data object Delete: EmployeeNewEditEvent()
    data object SaveEmployee: EmployeeNewEditEvent()
    data object Back: EmployeeNewEditEvent()
    data object ToggleSelected: EmployeeNewEditEvent()
}