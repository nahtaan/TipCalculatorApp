package net.nathan.tipcalculator.feature_tips.presentation.add_edit_employee

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import net.nathan.tipcalculator.core.presentation.components.CheckButton
import net.nathan.tipcalculator.core.presentation.components.DeleteButton
import net.nathan.tipcalculator.core.presentation.components.OutlinedTimePickerButton
import net.nathan.tipcalculator.core.presentation.components.TimePickerDialog
import net.nathan.tipcalculator.core.util.ContentDescriptions
import net.nathan.tipcalculator.core.util.NewEditStrings
import net.nathan.tipcalculator.core.util.TipStrings
import net.nathan.tipcalculator.feature_tips.presentation.add_edit_employee.components.HintPositiveNumberField

@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalMaterial3Api
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun EmployeeNewEditScreen(
    navController: NavController,
    viewModel: EmployeeNewEditViewModel = hiltViewModel()
){
    val state = viewModel.state.value
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = true){
        viewModel.eventFlow.collectLatest {event ->
            when(event){
                EmployeeNewEditViewModel.UiEvent.Back -> {
                    navController.navigateUp()
                }
                EmployeeNewEditViewModel.UiEvent.SaveEmployee -> {
                    navController.navigateUp()
                }
                is EmployeeNewEditViewModel.UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = event.message,
                        duration = SnackbarDuration.Short,
                        withDismissAction = true
                    )
                }

            }
        }
    }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = if(state.isEdit) TipStrings.EDIT_EMPLOYEE else TipStrings.NEW_EMPLOYEE,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                    scrolledContainerColor = MaterialTheme.colorScheme.primary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                navigationIcon = {
                     IconButton(
                         onClick = {
                             viewModel.onEvent(EmployeeNewEditEvent.Back)
                         },
                         modifier = Modifier.padding(start = 8.dp)
                     ){
                         Icon(
                             imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                             contentDescription = ContentDescriptions.BACK,
                             tint = MaterialTheme.colorScheme.onPrimary
                         )
                     }
                },
                actions = {},
                scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
            )
        },
        bottomBar = {
            BottomAppBar(
                actions = {
                    DeleteButton(
                        onDeleteClick = {
                            scope.launch {
                                val confirm = snackbarHostState.showSnackbar(
                                    message = NewEditStrings.CONFIRM_DELETE,
                                    actionLabel = NewEditStrings.YES,
                                    duration = SnackbarDuration.Short,
                                    withDismissAction = true
                                )
                                if(confirm == SnackbarResult.ActionPerformed){
                                    viewModel.onEvent(EmployeeNewEditEvent.Delete)
                                }
                            }
                        }
                    )
                    CheckButton(
                        onCheckClick = {
                            scope.launch {
                                viewModel.onEvent(EmployeeNewEditEvent.ToggleSelected)
                            }
                        },
                        colour = MaterialTheme.colorScheme.onSecondaryContainer,
                        isChecked = state.employeeEntry.isSelected
                    )
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {
                            viewModel.onEvent(EmployeeNewEditEvent.SaveEmployee)
                        },
                        shape = CircleShape,
                        containerColor = MaterialTheme.colorScheme.onPrimary
                    ){
                        Icon(
                            imageVector = Icons.Default.Save,
                            contentDescription = ContentDescriptions.SAVE_EMPLOYEE
                        )
                    }
                }
            )
        },
        snackbarHost = {SnackbarHost(hostState = snackbarHostState)}
    ){_ ->
        Box(
            contentAlignment = Alignment.TopStart,
            modifier = Modifier
                .fillMaxSize()
        ){
            Column(
                modifier = Modifier
                    .padding(top = 64.dp)
                    .fillMaxSize()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    OutlinedTextField(
                        value = state.employeeEntry.name,
                        onValueChange = {str ->
                            viewModel.onEvent(EmployeeNewEditEvent.EnteredName(str))
                        },
                        modifier = Modifier
                            .padding(10.dp)
                            .onFocusChanged {
                                viewModel.onEvent(EmployeeNewEditEvent.ChangedNameFocus(it))
                            },
                        textStyle = TextStyle(
                            fontSize = 40.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        ),
                        label = {
                            Text(NewEditStrings.NAME_HINT)
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                            focusedBorderColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }
                if(!state.allowTimeEdit){
                    return@Box
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    HintPositiveNumberField(
                        value = state.employeeEntry.hours,
                        label = {Text(NewEditStrings.HOURS_HINT)},
                        textColour = MaterialTheme.colorScheme.onPrimaryContainer,
                        onValueChange = {
                            viewModel.onEvent(EmployeeNewEditEvent.EnteredHours(it))
                        },
                        onFocusChange = {
                            viewModel.onEvent(EmployeeNewEditEvent.ChangedHoursFocus(it))
                        },
                        singleLine = true,
                        fontSize = 40.sp,
                        colours = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                            focusedBorderColor = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier.weight(0.5f)
                            .padding(horizontal = 7.dp)
                            .padding(bottom = 7.dp)
                        )
                    HintPositiveNumberField(
                        value = state.employeeEntry.minutes,
                        label = {Text(NewEditStrings.MINUTES_HINT)},
                        textColour = MaterialTheme.colorScheme.onPrimaryContainer,
                        onValueChange = {
                            viewModel.onEvent(EmployeeNewEditEvent.EnteredMinutes(it))
                        },
                        onFocusChange = {
                            viewModel.onEvent(EmployeeNewEditEvent.ChangedMinutesFocus(it))
                        },
                        singleLine = true,
                        fontSize = 40.sp,
                        colours = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                            focusedBorderColor = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier
                            .weight(0.5f)
                            .padding(horizontal = 7.dp)
                            .padding(bottom = 7.dp)
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min),
                    horizontalArrangement = Arrangement.Center
                ){
                    OutlinedTimePickerButton(
                        modifier = Modifier
                            .padding(horizontal = 19.dp)
                            .weight(0.5f),
                        onClick = {
                            viewModel.onEvent(EmployeeNewEditEvent.ShowStartTimeDialog)
                        },
                        title = TipStrings.START_TIME,
                        hour = state.startTimePickerState.hour,
                        minute = state.startTimePickerState.minute,
                        titleStyle = MaterialTheme.typography.labelLarge,
                        titleSize = 20.sp,
                        titleColour = MaterialTheme.colorScheme.onBackground,
                        timeSize = 40.sp,
                        timeColour = MaterialTheme.colorScheme.onPrimaryContainer,
                        timeStyle = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        shape = RoundedCornerShape(5.dp),
                        borderColour = MaterialTheme.colorScheme.secondary,
                        borderWidth = 1.dp
                    )
                    OutlinedTimePickerButton(
                        modifier = Modifier
                            .padding(horizontal = 19.dp)
                            .weight(0.5f),
                        onClick = {
                            viewModel.onEvent(EmployeeNewEditEvent.ShowEndTimeDialog)
                        },
                        title = TipStrings.END_TIME,
                        hour = state.endTimePickerState.hour,
                        minute = state.endTimePickerState.minute,
                        titleStyle = MaterialTheme.typography.labelLarge,
                        titleSize = 20.sp,
                        titleColour = MaterialTheme.colorScheme.onBackground,
                        timeSize = 40.sp,
                        timeColour = MaterialTheme.colorScheme.onPrimaryContainer,
                        timeStyle = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        shape = RoundedCornerShape(5.dp),
                        borderColour = MaterialTheme.colorScheme.secondary,
                        borderWidth = 1.dp
                    )
                }
                // handle time picker dialogues
                if(state.showStartTimeDialog || state.showEndTimeDialog) {
                    TimePickerDialog(
                        onSave = {
                            viewModel.onEvent(EmployeeNewEditEvent.SaveTimeDialog)
                        },
                        onDismiss = {
                            viewModel.onEvent(EmployeeNewEditEvent.HideTimeDialog)
                        },
                        timePickerState = if(state.showStartTimeDialog) state.startTimePickerState else state.endTimePickerState,
                        header = if (state.showStartTimeDialog) TipStrings.ENTER_START_TIME else TipStrings.ENTER_END_TIME,
                        cardColors = CardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                            disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                            disabledContentColor = MaterialTheme.colorScheme.onSecondaryContainer
                        ),
                        timePickerColours = TimePickerDefaults.colors(
                            clockDialColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                            timeSelectorSelectedContainerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                        ),
                        headerColour = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }
}
