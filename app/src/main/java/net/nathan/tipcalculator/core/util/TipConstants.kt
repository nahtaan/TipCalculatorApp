package net.nathan.tipcalculator.core.util

object ContentDescriptions{
    const val BACK = "Back"
    const val LOADING_INDICATOR = "Loading"
    const val ADD_EMPLOYEE = "Add Employee"
    const val DELETE_BUTTON = "Delete"
    const val CHECK_BUTTON = "Toggle Checked"
    const val SAVE_EMPLOYEE = "Save Employee"
    const val CALCULATE_BUTTON = "Calculate Tips"
    const val DELETE_ALL_BUTTON = "Delete All Employees"
    const val CLEAR_HOURS_BUTTON = "Clear All Hours"
}
object TipUseCaseStrings{
    const val INVALID_NAME = "Please ensure that the name is not empty!"
}
object TipStrings{
    const val EMPLOYEE_DELETED = "Employee Entry has been deleted."
    const val UNDO = "Undo"
    const val EMPLOYEE_LIST = "Tip Calculator"
    const val CONFIRM_DELETE = "Are you sure you want to clear all employees?"
    const val YES = "Yes"
    const val EDIT_EMPLOYEE = "Edit Employee"
    const val NEW_EMPLOYEE = "New Employee"
    const val CALCULATE_TIPS = "Calculate Tips"
    const val CONFIRM_CLEAR_TIMES = "Are you sure you want to clear all hours?"
    const val END_TIME = "End Time:"
    const val ENTER_START_TIME = "Enter Start Time"
    const val ENTER_END_TIME = "Enter End Time"
    const val CONFIRM = "Confirm"
}
object NewEditStrings{
    const val NAME_HINT = "Name:"
    const val HOURS_HINT = "Hours:"
    const val MINUTES_HINT = "Minutes:"
    const val SAVE_ERROR = "Unable to save employee. Ensure the name is not blank and hours and minutes are greater than 0."
    const val YES = "Yes"
    const val CONFIRM_DELETE = "Are you sure you want to delete this employee?"
}
object CalculateTipsStrings{
    const val TOTAL_TIPS_HEADER = "Total Tips:"
    const val TOTAL_TIPS_HINT = "Total..."
}