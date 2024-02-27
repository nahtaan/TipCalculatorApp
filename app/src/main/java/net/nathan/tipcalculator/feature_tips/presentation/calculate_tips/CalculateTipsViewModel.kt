package net.nathan.tipcalculator.feature_tips.presentation.calculate_tips

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
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
                val shouldShowTotalTipHint = !event.focusState.isFocused && _state.value.totalTips == ""
                _state.value = _state.value.copy(
                    isTotalHintShown = shouldShowTotalTipHint
                )
            }
            is CalculateTipsEvent.EnteredTotalTips -> {
                val str = event.value.filter { it.isDigit() || it == '.' }
                if(str.length > 7){ // limit the total characters for the tips value to avoid incorrect values being shown
                    return
                }
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
        }
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