package net.nathan.tipcalculator.feature_tips.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import net.nathan.tipcalculator.core.util.CalculateTipsStrings
import net.nathan.tipcalculator.feature_tips.presentation.add_edit_employee.EmployeeNewEditScreen
import net.nathan.tipcalculator.feature_tips.presentation.calculate_tips.CalculateTipsScreen
import net.nathan.tipcalculator.feature_tips.presentation.employee_list.EmployeeEntriesScreen
import net.nathan.tipcalculator.feature_tips.presentation.employee_list.EmployeeEntryListViewModel
import net.nathan.tipcalculator.feature_tips.presentation.util.Screen
import net.nathan.tipcalculator.ui.theme.TipCalculatorTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TipCalculatorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val navController = rememberNavController()
                    val listViewModel: EmployeeEntryListViewModel = hiltViewModel()
                    NavHost(
                        navController = navController,
                        startDestination = Screen.EmployeeEntryListScreen.route,
                        enterTransition = {
                            fadeIn(animationSpec = tween(0))
                        },
                        exitTransition = {
                            fadeOut(animationSpec = tween(0))
                        }
                    ){
                        composable(
                            route = Screen.EmployeeEntryListScreen.route
                        ){
                            EmployeeEntriesScreen(
                                navController = navController,
                                viewModel = listViewModel
                            )
                        }
                        composable(
                            route = Screen.EmployeeAddEditScreen.route+
                                    "?employeeId={employeeId}" +
                                    "&allowTimeEdit={allowTimeEdit}" +
                                    "&endTimeHour={endTimeHour}" +
                                    "&endTimeMinute={endTimeMinute}",
                            arguments = listOf(
                                navArgument(
                                    name = "employeeId"
                                ){
                                    type = NavType.IntType
                                    defaultValue = -1
                                },
                                navArgument(
                                    name = "allowTimeEdit"
                                ){
                                    type = NavType.BoolType
                                    defaultValue = false
                                },
                                navArgument(
                                    name = "endTimeHour"
                                ){
                                    type = NavType.IntType
                                    defaultValue = 0
                                },
                                navArgument(
                                    name = "endTimeMinute"
                                ){
                                    type = NavType.IntType
                                    defaultValue = 0
                                }
                            )
                        ){
                            EmployeeNewEditScreen(
                                navController = navController
                            )
                        }
                        composable(
                            route = Screen.CalculateTipsScreen.route
                        ){
                            CalculateTipsScreen(
                                navController = navController
                            )
                        }
                    }
                }
            }
        }
    }
}
