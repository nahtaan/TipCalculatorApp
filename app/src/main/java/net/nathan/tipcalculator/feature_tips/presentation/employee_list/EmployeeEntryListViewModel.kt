package net.nathan.tipcalculator.feature_tips.presentation.employee_list

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import net.nathan.tipcalculator.feature_tips.data.di.IoDispatcher
import net.nathan.tipcalculator.feature_tips.domain.model.EmployeeEntryItem
import net.nathan.tipcalculator.feature_tips.domain.use_case.TipUseCases
import javax.inject.Inject

@HiltViewModel
class EmployeeEntryListViewModel @Inject constructor(
    private val tipUseCases: TipUseCases,
    @IoDispatcher
    private val dispatcher: CoroutineDispatcher
): ViewModel() {
    private val _state = mutableStateOf(EmployeeEntryListState())
    val state: State<EmployeeEntryListState> = _state

    private var getEmployeeEntryItemsJob: Job? = null
    private val errorHandler = CoroutineExceptionHandler {_, e ->
        e.printStackTrace()
        _state.value = _state.value.copy(
            error = e.message,
            isLoading = false
        )
    }

    fun onEvent(event: EmployeeEntryListEvent){
        when(event){
            is EmployeeEntryListEvent.Delete -> {
                viewModelScope.launch(dispatcher+errorHandler) {
                    tipUseCases.deleteEmployeeEntry(event.employee)
                    getEmployeeEntries()
                }
            }
            is EmployeeEntryListEvent.UndoDelete -> {
                viewModelScope.launch(dispatcher+errorHandler) {
                    tipUseCases.addEmployeeEntry(event.employee)
                    getEmployeeEntries()
                }
            }
            EmployeeEntryListEvent.DeleteAll -> {
                viewModelScope.launch(dispatcher+errorHandler) {
                    tipUseCases.deleteAllEmployeeEntries()
                    getEmployeeEntries()
                }
            }
            is EmployeeEntryListEvent.ToggleSelected -> {
                viewModelScope.launch(dispatcher+errorHandler) {
                    _state.value = _state.value.copy(
                        allSelected = false
                    )
                    tipUseCases.toggleEmployeeSelected(employee = event.employee)
                    getEmployeeEntries()
                }
            }
            EmployeeEntryListEvent.ToggleAllSelected -> {
                viewModelScope.launch(dispatcher+errorHandler) {
                    val selected = !_state.value.allSelected
                    _state.value = _state.value.copy(
                        allSelected = selected
                    )
                    _state.value.employeeEntryItems.forEach {
                        if(it.isSelected && !selected){
                            tipUseCases.toggleEmployeeSelected(it)
                        }else if (!it.isSelected && selected){
                            tipUseCases.toggleEmployeeSelected(it)
                        }
                    }
                    getEmployeeEntries()
                }
            }
        }
    }
    fun getEmployeeEntries(){
        getEmployeeEntryItemsJob?.cancel()
        getEmployeeEntryItemsJob = viewModelScope.launch(dispatcher+errorHandler) {
            val result = tipUseCases.getAllEmployeeEntries()
            _state.value = _state.value.copy(
                employeeEntryItems = result,
                isLoading = false
            )
        }
    }
}