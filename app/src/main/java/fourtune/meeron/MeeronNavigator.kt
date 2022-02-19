package fourtune.meeron

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import fourtune.meeron.main.MainScreen

sealed interface Navigate {
    fun route() = requireNotNull(this::class.qualifiedName)
    fun routeWith(path: Any) = route() + "/{$path}"
    fun route(argument: Any) = route() + "/$argument"

    object Main : Navigate
}

@Composable
fun MeeronNavigator() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Navigate.Main.route()) {
        composable(Navigate.Main.route()) {
            MainScreen()
        }
    }
}


