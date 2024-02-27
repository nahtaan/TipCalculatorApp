package net.nathan.tipcalculator.feature_tips.presentation.add_edit_employee

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import net.nathan.tipcalculator.core.util.NewEditStrings
import net.nathan.tipcalculator.feature_tips.data.di.IoDispatcher
import net.nathan.tipcalculator.feature_tips.domain.use_case.TipUseCases
import javax.inject.Inject

@OptIn(ExperimentalMaterial3Api::class)
@HiltViewModel
class EmployeeNewEditViewModel @Inject constructor(
    private val tipUseCases: TipUseCases,
    savedStateHandle: SavedStateHandle,
    @IoDispatcher
    private val dispatcher: CoroutineDispatcher
): ViewModel() {
    private var _state: MutableState<EmployeeNewEditState> = mutableStateOf(EmployeeNewEditState())
    var state: State<EmployeeNewEditState> = _state

    private val errorHandler = CoroutineExceptionHandler {_, e ->
        e.printStackTrace()
        _state.value = _state.value.copy(
            error = e.message,
            isLoading = false
        )
    }
    private var currentEmployeeId: Int? = null
    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    sealed class UiEvent{
        data class ShowSnackbar(val message: String): UiEvent()
        data object SaveEmployee: UiEvent()
        data object Back: UiEvent()
    }
    init {
        savedStateHandle.get<Int>("employeeId")?.let {id ->
            if(id != -1){
                viewModelScope.launch {
                    tipUseCases.getEmployeeEntryById(id)?.also { employee ->
                        currentEmployeeId = employee.id
                        _state.value = _state.value.copy(
                            employeeEntry = employee,
                            isLoading = false,
                            isEdit = true,
                            isNameHintVisible = employee.name.isBlank(),
                            isHoursHintVisible = employee.hours==0,
                            isMinutesHintVisible = employee.minutes==0,
                        )
                    }
                }
            }else{
                _state.value = _state.value.copy(
                    isLoading = false
                )
            }
        }
        savedStateHandle.get<Boolean>("allowTimeEdit")?.let {allowEdit ->
            _state.value = _state.value .copy(
                allowTimeEdit = allowEdit
            )
        }
        savedStateHandle.get<Int>("endTimeHour")?.let {hour ->
            _state.value = _state.value.copy(
                endHour = hour
            )
        }
        savedStateHandle.get<Int>("endTimeMinute")?.let {minute ->
            _state.value = _state.value.copy(
                endMinute = minute
            )
        }
    }
    @OptIn(ExperimentalMaterial3Api::class)
    fun onEvent(event: EmployeeNewEditEvent){
        when(event){
            EmployeeNewEditEvent.Back -> {
                viewModelScope.launch(dispatcher+errorHandler){
                    _eventFlow.emit(UiEvent.Back)
                }
            }
            is EmployeeNewEditEvent.ChangedHoursFocus -> {
                val shouldHoursHintShow = !event.focusState.isFocused && _state.value.employeeEntry.hours == -1
                _state.value = _state.value.copy(
                    isHoursHintVisible = shouldHoursHintShow
                )
            }
            is EmployeeNewEditEvent.ChangedMinutesFocus -> {
                val shouldMinutesHintShow = !event.focusState.isFocused && _state.value.employeeEntry.minutes == -1
                _state.value = _state.value.copy(
                    isMinutesHintVisible = shouldMinutesHintShow
                )
            }
            is EmployeeNewEditEvent.ChangedNameFocus -> {
                val shouldNameHintShow = !event.focusState.isFocused && _state.value.employeeEntry.name.isBlank()
                _state.value = _state.value.copy(
                    isNameHintVisible = shouldNameHintShow
                )
            }
            EmployeeNewEditEvent.Delete -> {
                viewModelScope.launch(dispatcher+errorHandler){
                    if(currentEmployeeId != null){
                        tipUseCases.deleteEmployeeEntry(_state.value.employeeEntry)
                    }
                    _eventFlow.emit(UiEvent.Back)
                }
            }
            is EmployeeNewEditEvent.EnteredHours -> {
                if(!event.value.isDigitsOnly()){
                    return
                }
                var count = event.value.toIntOrNull()
                if(event.value == ""){
                    count = null
                }
                if(count == null || count < 100){
                    _state.value = _state.value.copy(
                        employeeEntry = _state.value.employeeEntry.copy(
                            hours = count
                        )
                    )
                }
            }
            is EmployeeNewEditEvent.EnteredMinutes -> {
                if(!event.value.isDigitsOnly()){
                    return
                }
                var count = event.value.toIntOrNull()
                if(event.value == ""){
                    count = null
                }
                if(count == null || count < 60){
                    _state.value = _state.value.copy(
                        employeeEntry = _state.value.employeeEntry.copy(
                            minutes = count
                        )
                    )
                }
            }
            is EmployeeNewEditEvent.EnteredName -> {
                _state.value = _state.value.copy(
                    employeeEntry = _state.value.employeeEntry.copy(
                        name = event.value
                    )
                )
            }
            EmployeeNewEditEvent.SaveEmployee -> {
                viewModelScope.launch(dispatcher+errorHandler){
                    try{
                        if(currentEmployeeId != null){
                            tipUseCases.updateEmployeeEntry(_state.value.employeeEntry)
                        }else{
                            tipUseCases.addEmployeeEntry(_state.value.employeeEntry.copy(
                                id = null
                            ))
                        }
                        _eventFlow.emit(UiEvent.SaveEmployee)
                    }catch (e: Exception){
                        _eventFlow.emit(UiEvent.ShowSnackbar(
                            message = e.message ?: NewEditStrings.SAVE_ERROR
                        ))
                    }
                }
            }
            EmployeeNewEditEvent.ToggleSelected -> {
                _state.value = _state.value.copy(
                    employeeEntry = _state.value.employeeEntry.copy(
                        isSelected = !_state.value.employeeEntry.isSelected
                    )
                )
            }
            EmployeeNewEditEvent.SaveTimeDialog -> {
                val startHour = _state.value.timePickerState.hour
                val startMinute = _state.value.timePickerState.minute
                val endHour = _state.value.endHour
                val endMinute = _state.value.endMinute
                val startDecimalHours = startHour + (startMinute/60.0f)
                val nowDecimalHours = endHour  + (endMinute/60.0f)
                val workedDecimalHours = if(startDecimalHours <= nowDecimalHours){
                    nowDecimalHours-startDecimalHours
                }else{
                    24 - (startDecimalHours-nowDecimalHours)
                }
                val workedHours = workedDecimalHours.div(1).toInt()
                val workedMinutes = ((workedDecimalHours-workedHours)*60).div(1).toInt()
                _state.value = _state.value.copy(
                    employeeEntry = _state.value.employeeEntry.copy(
                        hours = workedHours,
                        minutes = workedMinutes
                    ),
                    showTimeDialog = false
                )
            }
            EmployeeNewEditEvent.HideTimeDialog -> {
                _state.value = _state.value.copy(
                    showTimeDialog = false
                )
            }
            EmployeeNewEditEvent.ShowTimeDialog -> {
                _state.value = _state.value.copy(
                    showTimeDialog = true
                )
            }
        }
    }
}