package fourtune.meeron.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import fourtune.meeron.presentation.ui.calendar.CalendarScreen
import fourtune.meeron.presentation.ui.login.LoginScreen
import fourtune.meeron.presentation.ui.main.MainScreen

sealed interface Navigate {
    fun route() = requireNotNull(this::class.qualifiedName)
    fun routeWith(path: Any) = route() + "/{$path}"
    fun route(argument: Any) = route() + "/$argument"

    object Login : Navigate
    object Main : Navigate
    object Calendar : Navigate
}

@Composable
fun MeeronNavigator(openCalendar:()->Unit) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Navigate.Main.route()
    ) {
        composable(Navigate.Login.route()) {
            LoginScreen()
        }
        composable(Navigate.Main.route()) {
            MainScreen(
                openCalendar = openCalendar
//                {
//                    navController.navigate(Navigate.Calendar.route())
//                }
            )
        }
        composable(Navigate.Calendar.route()) {
            CalendarScreen()
        }
    }
}
