package net.nathan.tipcalculator.feature_tips.presentation.employee_list

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import net.nathan.tipcalculator.core.presentation.components.CheckButton
import net.nathan.tipcalculator.core.util.ContentDescriptions
import net.nathan.tipcalculator.core.util.TipStrings
import net.nathan.tipcalculator.feature_tips.presentation.employee_list.components.EmployeeEntryCard
import net.nathan.tipcalculator.feature_tips.presentation.util.Screen

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@ExperimentalMaterial3Api
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeEntriesScreen(
    navController: NavController,
    viewModel: EmployeeEntryListViewModel = hiltViewModel()
){
    val state = viewModel.state.value
    val snackbarHostState = remember { SnackbarHostState()}

    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = true){
        viewModel.getEmployeeEntries()
    }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = TipStrings.EMPLOYEE_LIST,
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
                navigationIcon = {},
                actions = {
                    CheckButton(
                        onCheckClick = {
                            viewModel.onEvent(EmployeeEntryListEvent.ToggleAllSelected)
                        },
                        colour = MaterialTheme.colorScheme.onPrimary,
                        isChecked = state.allSelected
                    )
                },
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
                                        message = TipStrings.CONFIRM_DELETE,
                                        actionLabel = TipStrings.YES,
                                        withDismissAction = true,
                                        duration = SnackbarDuration.Short
                                    )
                                    if(result==SnackbarResult.ActionPerformed){
                                        viewModel.onEvent(EmployeeEntryListEvent.DeleteAll)
                                    }
                                }
                            },
                            shape = RoundedCornerShape(cornerRadius),
                            containerColor = Color.Red
                        ){
                            Icon(
                                imageVector = Icons.Default.DeleteSweep,
                                contentDescription = ContentDescriptions.DELETE_ALL_BUTTON,
                                tint = Color.Black
                            )
                        }
                        Divider(modifier = Modifier
                            .fillMaxHeight(0.7f)
                            .width(2.dp))
                        FloatingActionButton(
                            onClick = {
                                navController.navigate(Screen.EmployeeAddEditScreen.route)
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
                        Divider(modifier = Modifier
                            .fillMaxHeight(0.7f)
                            .width(2.dp))
                        FloatingActionButton(
                            onClick = {
                                navController.navigate(Screen.CalculateTipsScreen.route)
                            },
                            shape = RoundedCornerShape(cornerRadius),
                            containerColor = Color.Yellow
                        ){
                            Icon(
                                imageVector = Icons.Default.Calculate,
                                contentDescription = ContentDescriptions.CALCULATE_BUTTON,
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
            modifier = Modifier.fillMaxSize().background(color = MaterialTheme.colorScheme.background)
        ){
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 64.dp)
                        .padding(bottom = 80.dp)
                ) {
                    items(state.employeeEntryItems){employee ->
                        Spacer(modifier = Modifier.height(6.dp))
                        Box(
                            modifier = Modifier.padding(horizontal = 12.dp)
                        ) {
                            EmployeeEntryCard(
                                modifier = Modifier.fillMaxWidth(),
                                employee = employee,
                                onDeleteClick = {
                                    viewModel.onEvent(EmployeeEntryListEvent.Delete(employee))
                                    scope.launch {
                                        val undo = snackbarHostState.showSnackbar(
                                            message = TipStrings.EMPLOYEE_DELETED+" (${employee.name})",
                                            actionLabel = TipStrings.UNDO,
                                            duration = SnackbarDuration.Short,
                                            withDismissAction = true
                                        )
                                        if(undo == SnackbarResult.ActionPerformed){
                                            viewModel.onEvent(EmployeeEntryListEvent.UndoDelete(employee))
                                        }
                                    }
                                },
                                onCardClick = {
                                    scope.launch {
                                        navController.navigate(
                                            Screen.EmployeeAddEditScreen.route + "?employeeId=${employee.id}"
                                        )
                                    }
                                },
                                onCheckClick = {
                                    viewModel.onEvent(EmployeeEntryListEvent.ToggleSelected(employee))
                                }
                            )
                        }
                        if(state.employeeEntryItems.indexOf(employee) == state.employeeEntryItems.size-1){
                            Spacer(modifier = Modifier.height(6.dp))
                        }
                    }
                }
            }
            if(state.isLoading){
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.semantics {
                            this.contentDescription = ContentDescriptions.LOADING_INDICATOR
                        }
                    )
                }
            }
            if(state.error != null){
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = state.error,
                        fontSize = 30.sp,
                        lineHeight = 36.sp
                    )
                }
            }
        }
    }
}