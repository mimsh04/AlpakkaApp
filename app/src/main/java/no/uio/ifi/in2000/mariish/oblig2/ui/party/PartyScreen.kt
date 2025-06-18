package no.uio.ifi.in2000.mariish.oblig2.ui.party

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import no.uio.ifi.in2000.mariish.oblig2.data.alpacas.AlpacaPartiesRepository


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartyScreen(navController:NavController,partyId:String){

    val repository=remember{AlpacaPartiesRepository()}
    val viewModel: PartyViewModel = viewModel(factory = PartyViewModel.provideFactory(partyId, repository))
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {Text(text="Home")},
                navigationIcon = {
                    IconButton(onClick = {navController.popBackStack()}) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back to Home")
                    }
                }
            )
        }
    ){
        innerPadding->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        )
        {


            when(uiState) {
                is PartyViewModel.UiState.Loading -> {
                    CircularProgressIndicator()
                }

                is PartyViewModel.UiState.Success -> {
                    Log.d("PARTYSCREEN", "Feil ved henting av alpakkainformasjon")

                    val party = (uiState as PartyViewModel.UiState.Success).party

                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {


                        Spacer(modifier = Modifier.height(12.dp))

                        Text(text = party.name)


                        Spacer(modifier = Modifier.height(12.dp))


                        AsyncImage(
                            model = party.img,
                            contentDescription = "Party leader",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(125.dp)
                                .clip(CircleShape)

                        )

                        Text(
                            text = "leder:${party.leader}"
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(30.dp)
                                .background(Color(android.graphics.Color.parseColor(party.color)))
                        )

                        Spacer(modifier = Modifier.height(20.dp))


                        Text(
                            text = party.description
                        )



                    }


                }

                is PartyViewModel.UiState.Error -> {
                    Text(
                        text = "Error: ${(uiState as PartyViewModel.UiState.Error).errorMessage}",
                        color = Color.Red
                    )
                }

                is PartyViewModel.UiState.Empty -> {
                    Text(text = "No party found", color = Color.Gray)
                }


            }





        }
    }







}