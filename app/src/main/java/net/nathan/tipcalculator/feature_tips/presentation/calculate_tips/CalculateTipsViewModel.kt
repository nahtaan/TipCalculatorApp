package net.nathan.tipcalculator.feature_tips.presentation.calculate_tips

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import net.nathan.tipcalculator.feature_tips.data.di.IoDispatcher
import net.nathan.tipcalculator.feature_tips.domain.use_case.TipUseCases
import javax.inject.Inject

@HiltViewModel
class CalculateTipsViewModel @Inject constructor(
    private val tipUseCases: TipUseCases,
    @IoDispatcher
    private val dispatcher: CoroutineDispatcher
): ViewModel() {
    private val _state = mutableStateOf( CalculateTipsState() )
    val state: State<CalculateTipsState> = _state
    private var getEmployeeEntryItemsJob: Job? = null

    @OptIn(ExperimentalMaterial3Api::class)
    private val errorHandler = CoroutineExceptionHandler { _, e->
        e.printStackTrace()
        _state.value = _state.value.copy(
            error = e.message
        )
    }
    private val _eventFlow = MutableSharedFlow<CalculateEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    sealed class CalculateEvent{
        data object Back : CalculateEvent()
    }
    @OptIn(ExperimentalMaterial3Api::class)
    fun onEvent(event: CalculateTipsEvent){
        when(event){
            CalculateTipsEvent.Back -> {
                viewModelScope.launch {
                    _eventFlow.emit(CalculateEvent.Back)
                }
            }
            is CalculateTipsEvent.ChangedTotalTipsFocus -> {
                var shouldResetTotal = false
                var resetValue = _state.value.totalTips
                if(event.focusState.isFocused && _state.value.totalTips == "0"){
                    shouldResetTotal = true
                    resetValue = ""
                }else if (!event.focusState.isFocused && _state.value.totalTips == ""){
                    shouldResetTotal = true
                    resetValue = "0"
                }
                _state.value = _state.value.copy(
                    isTotalFocused = event.focusState.isFocused,
                    totalTips = if(shouldResetTotal) resetValue else _state.value.totalTips
                )
            }
            is CalculateTipsEvent.EnteredTotalTips -> {
                var str = event.value
                if(str.contains(".")){
                    if(!str.replace(".", "").isDigitsOnly()){
                        return;
                    }
                }else if (!str.isDigitsOnly()){
                    return
                }
                if(str.length > 7){ // limit the total characters for the tips value to avoid incorrect values being shown
                    return
                }
                str = str.replaceFirst("^0+(?!\$)", "")
                _state.value = _state.value.copy(
                    totalTips = str
                )
            }
            CalculateTipsEvent.ClearAllTimes -> {
                viewModelScope.launch {
                    tipUseCases.resetSelectedTimes()
                    updateEmployeeEntries()
                }
            }
            CalculateTipsEvent.HideTimeDialog -> {
                _state.value = _state.value.copy(
                    showEndTimeDialog = false
                )
            }
            CalculateTipsEvent.ShowTimeDialog -> {
                _state.value = _state.value.copy(
                    showEndTimeDialog = true
                )
            }
            CalculateTipsEvent.SaveTimeDialog -> {
                _state.value = _state.value.copy(
                    endTimeHour = _state.value.endTimePickerState.hour,
                    endTimeMinute = _state.value.endTimePickerState.minute,
                    showEndTimeDialog = false
                )
            }
            is CalculateTipsEvent.ToggleIsPaid -> {
                viewModelScope.launch {
                    val currentPaidStatus = _state.value.paidEmployees[event.employee.id ?: -1] ?: false
                    val newPaidEmployees = _state.value.paidEmployees.toMutableMap()
                    newPaidEmployees[event.employee.id ?: -1] = !currentPaidStatus
                    _state.value = _state.value.copy(
                        paidEmployees = newPaidEmployees
                    )
                }
            }
        }
    }

    fun getIsPaid(id: Int): Boolean{
        return _state.value.paidEmployees[id] ?: false
    }

    @OptIn(ExperimentalMaterial3Api::class)
    fun updateEmployeeEntries(){
        getEmployeeEntryItemsJob?.cancel()
        getEmployeeEntryItemsJob = viewModelScope.launch(dispatcher+errorHandler) {
            val result = tipUseCases.getAllSelectedEmployeeEntries()
            _state.value = _state.value.copy(
                employees = result,
            )
            var totalHours = 0.0
            for(employee in _state.value.employees){
                var calcHours = employee.hours
                var calcMins = employee.minutes
                if(calcHours == null){
                    calcHours = 0
                }
                if(calcMins == null){
                    calcMins = 0
                }
                totalHours += calcHours
                totalHours += calcMins / 60.0
            }
            _state.value = _state.value.copy(
                totalHours = totalHours
            )
        }
    }
}