package fourtune.meeron.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import fourtune.meeron.presentation.ui.calendar.CalendarScreen
import fourtune.meeron.presentation.ui.calendar.all.ShowAllScreen
import fourtune.meeron.presentation.ui.login.LoginScreen
import fourtune.meeron.presentation.ui.main.MainScreen

sealed interface Navigate {
    fun route() = requireNotNull(this::class.qualifiedName)
    fun routeWith(path: Any) = route() + "/{$path}"
    fun route(argument: Any) = route() + "/$argument"

    object Login : Navigate
    object Main : Navigate
    object Calendar : Navigate
    object ShowAll : Navigate
}

@Composable
fun MeeronNavigator() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Navigate.Login.route()
    ) {
        composable(Navigate.Login.route()) {
            LoginScreen(isLoginSuccess = {
                navController.popBackStack()
                navController.navigate(Navigate.Main.route())
            })
        }
        composable(Navigate.Main.route()) {
            MainScreen(
                openCalendar = {
                    navController.navigate(Navigate.Calendar.route())
                }
            )
        }
        composable(Navigate.Calendar.route()) {
            CalendarScreen(
                onBack = { navController.navigateUp() },
                showAll = { navController.navigate(Navigate.ShowAll.route()) })
        }

        composable(Navigate.ShowAll.route()) {
            ShowAllScreen(onAction = { navController.navigateUp() })
        }
    }
}
