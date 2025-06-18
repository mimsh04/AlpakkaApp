package no.uio.ifi.in2000.mariish.oblig2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import no.uio.ifi.in2000.mariish.oblig2.theme.Mariish_oblig2Theme
import no.uio.ifi.in2000.mariish.oblig2.ui.home.HomeScreen
import no.uio.ifi.in2000.mariish.oblig2.ui.party.PartyScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Mariish_oblig2Theme {
                val navController= rememberNavController()
                NavHost(
                    navController=navController,
                    startDestination = "HomeScreen",
                    )
                {
                    composable("HomeScreen"){ HomeScreen(navController) }

                    

                    composable(
                        route = "PartyScreen/{partyId}",
                        arguments = listOf(navArgument("partyId") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val partyId = backStackEntry.arguments?.getString("partyId") ?: ""
                        PartyScreen(navController, partyId)
                    }

                }
            }
        }
    }
}
