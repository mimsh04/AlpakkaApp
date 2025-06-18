package no.uio.ifi.in2000.mariish.oblig2.ui.home


import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import no.uio.ifi.in2000.mariish.oblig2.model.alpacas.PartyInfo

@Composable
fun HomeScreen( navController: NavController) {
    val viewModel: HomeScreenViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()
    val modifier = Modifier



    when(uiState){
        is HomeScreenViewModel.UiState.Loading -> {
            CircularProgressIndicator(
                modifier = Modifier.wrapContentWidth(Alignment.CenterHorizontally)
            )
        }
        is HomeScreenViewModel.UiState.Success -> {
            val parties = (uiState as HomeScreenViewModel.UiState.Success).parties
            PhotoGridScreen(parties = parties,modifier, navController)
        }
        is HomeScreenViewModel.UiState.Error -> {
            val errorMessage = (uiState as HomeScreenViewModel.UiState.Error).errorMessage
            Text(text = "Error: $errorMessage")
        }
        is HomeScreenViewModel.UiState.Empty -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Ingen partier funnet")

            }

        }
    }
}



@Composable
fun PhotoGridScreen(
    parties: List<PartyInfo>,
    modifier: Modifier = Modifier,
    navController: NavController

) {
    BoxWithConstraints {
        val isWideScreen = maxWidth > 600.dp
        val gridCellSize = if (isWideScreen) 200.dp else 150.dp



        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ){
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = gridCellSize),
                modifier = modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                items(parties) { party ->
                    AlpacaCard(
                        party = party,
                        modifier = modifier.padding(4.dp)
                            .aspectRatio(1f),

                        onClickAction = { partyId->
                            try {
                                navController.navigate("PartyScreen/$partyId")
                            }catch (e: Exception) {
                                Log.d("HOMESCREEN", "Failed to navigate to PartyScreen/$partyId", e)
                            }
                        }

                    )
                }

            }
            VoteList()



        }
    }


}



@Composable
fun AlpacaCard(
    party: PartyInfo,
    modifier: Modifier,
    onClickAction: (String) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clickable { onClickAction(party.id) },
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),

    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp)
                .background(Color(android.graphics.Color.parseColor(party.color))),
            contentAlignment=Alignment.Center
            ) {

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text=party.name,
                    color=Color.Black
                )


                AsyncImage(
                    model = party.img,
                    contentDescription = "Party leader",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(115.dp)
                        .clip(CircleShape)

                )

                Text(
                    text="leder:${party.leader}"
                )
            }
        }
    }
}

