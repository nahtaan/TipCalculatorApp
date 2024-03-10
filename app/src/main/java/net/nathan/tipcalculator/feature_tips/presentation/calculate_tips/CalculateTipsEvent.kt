package net.nathan.tipcalculator.feature_tips.presentation.calculate_tips

import androidx.compose.ui.focus.FocusState
import net.nathan.tipcalculator.feature_tips.domain.model.EmployeeEntryItem

sealed class CalculateTipsEvent {
    data class EnteredTotalTips(val value: String): CalculateTipsEvent()
    data class ChangedTotalTipsFocus(val focusState: FocusState): CalculateTipsEvent()
    data class ToggleIsPaid(val employee: EmployeeEntryItem) : CalculateTipsEvent()
    data object ShowTimeDialog: CalculateTipsEvent()
    data object HideTimeDialog: CalculateTipsEvent()
    data object Back : CalculateTipsEvent()
    data object ClearAllTimes : CalculateTipsEvent()
    data object SaveTimeDialog : CalculateTipsEvent()
}