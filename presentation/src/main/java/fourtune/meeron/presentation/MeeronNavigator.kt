package fourtune.meeron.presentation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
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

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MeeronNavigator() {
    val navController = rememberAnimatedNavController()
    AnimatedNavHost(
        navController = navController,
        startDestination = Navigate.Login.route()
    ) {

        composable(route = Navigate.Login.route()) {
            LoginScreen(isLoginSuccess = {
                navController.popBackStack()
                navController.navigate(Navigate.Main.route())
            })
        }
        composable(route = Navigate.Main.route()) {
            MainScreen(
                openCalendar = {
                    navController.navigate(Navigate.Calendar.route())
                }
            )

        }
        composable(
            route = Navigate.Calendar.route(),
            enterTransition = { slideInVertically(initialOffsetY = { it }) },
            exitTransition = { slideOutVertically(targetOffsetY = { it }) },
        ) {
            CalendarScreen(
                onBack = { navController.navigateUp() },
                showAll = { navController.navigate(Navigate.ShowAll.route()) }
            )
        }

        composable(Navigate.ShowAll.route()) {
            ShowAllScreen(onAction = { navController.navigateUp() })
        }
    }
}
