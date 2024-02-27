package net.nathan.tipcalculator.feature_tips.presentation.util

sealed class Screen(val route: String) {
    data object EmployeeEntryListScreen: Screen("employeeEntryList_screen")
    data object EmployeeAddEditScreen: Screen("employeeAddEdit_screen")
    data object CalculateTipsScreen: Screen("calculateTips_screen")
}