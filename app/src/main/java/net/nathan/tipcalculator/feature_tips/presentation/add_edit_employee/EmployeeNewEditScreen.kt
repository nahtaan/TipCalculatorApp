package net.nathan.tipcalculator.feature_tips.presentation.add_edit_employee

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import net.nathan.tipcalculator.core.presentation.components.CheckButton
import net.nathan.tipcalculator.core.presentation.components.DeleteButton
import net.nathan.tipcalculator.core.util.ContentDescriptions
import net.nathan.tipcalculator.core.util.NewEditStrings
import net.nathan.tipcalculator.core.util.TipStrings
import net.nathan.tipcalculator.feature_tips.presentation.add_edit_employee.components.HintPositiveNumberField
import net.nathan.tipcalculator.feature_tips.presentation.add_edit_employee.components.HintTextField

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
    val horizontalPadding = 16.dp
    val verticalPadding = 16.dp

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
                    modifier = Modifier.fillMaxWidth()
                ) {
                    HintTextField(
                        text = state.employeeEntry.name,
                        hint = NewEditStrings.NAME_HINT,
                        textColour = MaterialTheme.colorScheme.onPrimaryContainer,
                        onValueChange = {
                            viewModel.onEvent(EmployeeNewEditEvent.EnteredName(it))
                        },
                        onFocusChange = {
                            viewModel.onEvent(EmployeeNewEditEvent.ChangedNameFocus(it))
                        },
                        singleLine = true,
                        isHintVisible = state.isNameHintVisible,
                        fontSize = 40.sp,
                        modifier = Modifier.padding(
                            horizontal = horizontalPadding,
                            vertical = verticalPadding
                        )
                    )
                }
                Divider(Modifier.height(3.dp))
                if(!state.allowTimeEdit){
                    return@Box
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    HintPositiveNumberField(
                        value = state.employeeEntry.hours,
                        hint = NewEditStrings.HOURS_HINT,
                        textColour = MaterialTheme.colorScheme.onPrimaryContainer,
                        onValueChange = {
                            viewModel.onEvent(EmployeeNewEditEvent.EnteredHours(it))
                        },
                        onFocusChange = {
                            viewModel.onEvent(EmployeeNewEditEvent.ChangedHoursFocus(it))
                        },
                        singleLine = true,
                        fontSize = 40.sp,
                        modifier = Modifier.padding(
                            vertical = verticalPadding
                            )
                            .padding(start = horizontalPadding)
                            .weight(0.5f)
                    )
                    Divider(modifier = Modifier
                        .height(120.dp)
                        .width(2.dp))
                    HintPositiveNumberField(
                        value = state.employeeEntry.minutes,
                        hint = NewEditStrings.MINUTES_HINT,
                        textColour = MaterialTheme.colorScheme.onPrimaryContainer,
                        onValueChange = {
                            viewModel.onEvent(EmployeeNewEditEvent.EnteredMinutes(it))
                        },
                        onFocusChange = {
                            viewModel.onEvent(EmployeeNewEditEvent.ChangedMinutesFocus(it))
                        },
                        singleLine = true,
                        fontSize = 40.sp,
                        modifier = Modifier.padding(
                            vertical = verticalPadding
                            )
                            .padding(start = horizontalPadding)
                            .weight(0.5f)
                    )
                }
                Divider(Modifier.height(3.dp))
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .padding(top = 10.dp),
                    horizontalArrangement = Arrangement.Center
                ){
                    Button(
                        onClick = {
                            viewModel.onEvent(EmployeeNewEditEvent.ShowTimeDialog)
                        }
                    ){
                        Text(
                            text = TipStrings.ENTER_START_TIME,
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                }
                if(state.showTimeDialog) {
                    Dialog(
                        onDismissRequest = {
                            viewModel.onEvent(EmployeeNewEditEvent.HideTimeDialog)
                        }
                    ){
                        Card(
                            modifier = Modifier
                                .wrapContentHeight()
                                .height(IntrinsicSize.Min),
                            shape = RoundedCornerShape(10.dp),
                        ) {
                            Column(
                                modifier = Modifier.padding(top = 10.dp, bottom = 10.dp),
                                verticalArrangement = Arrangement.Center
                            ){
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(0.1f),
                                    horizontalArrangement = Arrangement.Center
                                ){
                                    Text(
                                        text = TipStrings.ENTER_START_TIME,
                                        style = MaterialTheme.typography.headlineSmall
                                    )
                                }
                                Row(
                                    horizontalArrangement = Arrangement.Center,
                                    modifier = Modifier.fillMaxWidth()
                                        .weight(0.8f)
                                ){
                                    TimePicker(
                                        state = state.timePickerState,
                                        modifier = Modifier,
                                        colors = TimePickerDefaults.colors(),
                                        layoutType = TimePickerLayoutType.Vertical
                                    )
                                }
                                Row(
                                    horizontalArrangement = Arrangement.Center,
                                    modifier = Modifier.fillMaxWidth()
                                        .weight(0.1f)
                                ){
                                    Button(
                                        onClick = {
                                            viewModel.onEvent(EmployeeNewEditEvent.SaveTimeDialog)
                                        },
                                        modifier = Modifier
                                    ){
                                        Text(
                                            text = TipStrings.CONFIRM
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
