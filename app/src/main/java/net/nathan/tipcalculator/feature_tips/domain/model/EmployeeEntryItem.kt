package net.nathan.tipcalculator.feature_tips.domain.model

data class EmployeeEntryItem(
    val name: String,
    val hours: Int?,
    val minutes: Int?,
    val isSelected: Boolean,
    val id: Int?
)