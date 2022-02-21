package fourtune.meeron.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import fourtune.meeron.presentation.ui.login.LoginScreen

sealed interface Navigate {
    fun route() = requireNotNull(this::class.qualifiedName)
    fun routeWith(path: Any) = route() + "/{$path}"
    fun route(argument: Any) = route() + "/$argument"

    object Login : Navigate
    object Main : Navigate
}

@Composable
fun MeeronNavigator() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Navigate.Login.route()) {
        composable(Navigate.Login.route()) {
            LoginScreen()
        }
        composable(Navigate.Main.route()) {
        }
    }
}


