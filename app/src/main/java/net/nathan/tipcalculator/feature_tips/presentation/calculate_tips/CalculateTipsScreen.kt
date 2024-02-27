package net.nathan.tipcalculator.feature_tips.presentation.calculate_tips

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import net.nathan.tipcalculator.core.util.CalculateTipsStrings
import net.nathan.tipcalculator.core.util.ContentDescriptions
import net.nathan.tipcalculator.core.util.TipStrings
import net.nathan.tipcalculator.core.util.TipUseCaseStrings
import net.nathan.tipcalculator.feature_tips.presentation.calculate_tips.components.EmployeeTipAmountCard
import net.nathan.tipcalculator.feature_tips.presentation.calculate_tips.components.HintDecimalField
import net.nathan.tipcalculator.feature_tips.presentation.util.Screen

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CalculateTipsScreen(
    navController: NavController,
    viewModel: CalculateTipsViewModel = hiltViewModel()
){
    val state = viewModel.state.value
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val horizontalPadding = 16.dp

    LaunchedEffect(key1 = true){
        viewModel.updateEmployeeEntries()
        viewModel.eventFlow.collectLatest { event ->
            when(event){
                is CalculateTipsViewModel.CalculateEvent.Back -> {
                    navController.navigateUp()
                }
            }
        }
    }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = TipStrings.CALCULATE_TIPS,
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
                            viewModel.onEvent(CalculateTipsEvent.Back)
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
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.onBackground),
                content = {
                    val cornerRadius = 15.dp
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        FloatingActionButton(
                            onClick = {
                                scope.launch {
                                    val result = snackbarHostState.showSnackbar(
                                        message = TipStrings.CONFIRM_CLEAR_TIMES,
                                        actionLabel = TipStrings.YES,
                                        withDismissAction = true,
                                        duration = SnackbarDuration.Short
                                    )
                                    if (result == SnackbarResult.ActionPerformed) {
                                        viewModel.onEvent(CalculateTipsEvent.ClearAllTimes)
                                    }
                                }
                            },
                            shape = RoundedCornerShape(cornerRadius),
                            containerColor = Color.Red
                        ) {
                            Icon(
                                imageVector = Icons.Default.History,
                                contentDescription = ContentDescriptions.CLEAR_HOURS_BUTTON,
                                tint = Color.Black
                            )
                        }
                        Divider(
                            modifier = Modifier
                                .fillMaxHeight(0.7f)
                                .width(2.dp)
                        )
                        FloatingActionButton(
                            onClick = {
                                navController.navigate(
                                    Screen.EmployeeAddEditScreen.route+
                                            "?allowTimeEdit=${true}"+
                                            "&endTimeHour=${state.endTimePickerState.hour}"+
                                            "&endTimeMinute=${state.endTimePickerState.minute}"
                                )
                            },
                            shape = RoundedCornerShape(cornerRadius),
                            containerColor = Color.Green
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = ContentDescriptions.ADD_EMPLOYEE,
                                tint = Color.Black
                            )
                        }
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState)}
    ) {_ ->
        Box(
            contentAlignment = Alignment.TopStart,
            modifier = Modifier.fillMaxSize()
                .padding(top = 64.dp)
        ){
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                var time = if(state.endTimePickerState.hour < 10){
                    "0${state.endTimePickerState.hour}:"
                }else{
                    "${state.endTimePickerState.hour}:"
                }
                if(state.endTimePickerState.minute < 10){
                    time += "0${state.endTimePickerState.minute}"
                }else{
                    time += state.endTimePickerState.minute
                }
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .height(IntrinsicSize.Min)
                ){
                    Column(
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .weight(0.5f)
                    ) {
                        Text(
                            text = CalculateTipsStrings.TOTAL_TIPS_HEADER,
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 20.sp,
                            modifier = Modifier
                                .padding(horizontal = horizontalPadding)
                                .padding(bottom = 1.dp)
                        )
                        Card(
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            HintDecimalField(
                                value = state.totalTips,
                                hint = CalculateTipsStrings.TOTAL_TIPS_HINT,
                                textColour = MaterialTheme.colorScheme.onPrimaryContainer,
                                onValueChange = {
                                    viewModel.onEvent(CalculateTipsEvent.EnteredTotalTips(it))
                                },
                                onFocusChange = {
                                    viewModel.onEvent(CalculateTipsEvent.ChangedTotalTipsFocus(it))
                                },
                                isHintVisible = state.isTotalHintShown,
                                singleLine = true,
                                fontSize = 40.sp,
                                modifier = Modifier
                                    .width(IntrinsicSize.Min)
                                    .padding(horizontal = 10.dp)
                            )
                        }
                    }
                    Divider(
                        modifier = Modifier
                            .height(96.dp)
                            .width(2.dp)
                    )
                    Column(
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .weight(0.5f)
                    ) {
                        Text(
                            text = TipStrings.END_TIME,
                            style = MaterialTheme.typography.labelLarge,
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier
                                .padding(horizontal = horizontalPadding)
                        )
                        Card(
                            shape = RoundedCornerShape(10.dp),
                            onClick = {
                                viewModel.onEvent(CalculateTipsEvent.ShowTimeDialog)
                            }
                        ) {
                            Text(
                                text = time,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                fontSize = 40.sp,
                                modifier = Modifier
                                    .padding(horizontal = 10.dp)
                            )
                        }
                    }
                }
                Divider()
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 80.dp)
                ) {
                    var totalTipCount = state.totalTips.toDoubleOrNull()
                    if (totalTipCount == null) {
                        totalTipCount = 0.0
                    }
                    var tipsPerHour: Double = try {
                        totalTipCount / state.totalHours
                    }catch (e: Exception){
                        0.0
                    }
                    if(state.totalHours == 0.0){
                        tipsPerHour = 0.0
                    }
                    items(state.employees) { employee ->
                        var calcHours = employee.hours
                        var calcMins = employee.minutes
                        if(calcHours == null){
                            calcHours = 0
                        }
                        if(calcMins == null){
                            calcMins = 0
                        }
                        val hours: Double = calcHours.toDouble() + (calcMins.toDouble() / 60.0)
                        val tips = hours * tipsPerHour
                        Box(
                            modifier = Modifier.padding(horizontal = horizontalPadding)
                        ) {
                            EmployeeTipAmountCard(
                                modifier = Modifier.padding(4.dp),
                                name = employee.name,
                                tipAmount = tips,
                                hours = employee.hours,
                                minutes = employee.minutes,
                                onCardClick = {
                                    scope.launch {
                                        navController.navigate(
                                            Screen.EmployeeAddEditScreen.route + "?employeeId=${employee.id}" +
                                                    "&allowTimeEdit=${true}" +
                                                    "&endTimeHour=${state.endTimePickerState.hour}" +
                                                    "&endTimeMinute=${state.endTimePickerState.minute}"
                                        )
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
        if(state.showEndTimeDialog){
            Dialog(
                onDismissRequest = {
                    viewModel.onEvent(CalculateTipsEvent.HideTimeDialog)
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
                                text = TipStrings.ENTER_END_TIME,
                                style = MaterialTheme.typography.headlineSmall
                            )
                        }
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                                .weight(0.8f)
                        ){
                            TimePicker(
                                state = state.endTimePickerState,
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
                                    viewModel.onEvent(CalculateTipsEvent.HideTimeDialog)
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