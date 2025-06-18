package no.uio.ifi.in2000.mariish.oblig2.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import no.uio.ifi.in2000.mariish.oblig2.data.alpacas.AlpacaPartiesRepository
import no.uio.ifi.in2000.mariish.oblig2.model.votes.District


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoteList(){
    val repository=AlpacaPartiesRepository()

    var expanded by rememberSaveable { mutableStateOf(false) }
    val districtOptions:List<District> =District.entries.toList()
    val textFieldState = rememberTextFieldState(District.DISTRICT1.toString())
    var valgtDistrikt by rememberSaveable { mutableStateOf(District.DISTRICT1) }
    var voteList by remember { mutableStateOf<List<Pair<String,Int?>>>(emptyList()) }

    LaunchedEffect(valgtDistrikt) {
        voteList=repository.getPartyAndVotes(valgtDistrikt)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE0E0E0))
    ){
        Column(
            modifier = Modifier.padding(5.dp)
        ){
            Row(
                modifier = Modifier.fillMaxWidth()
            ){

                Text(
                    text="Parti",
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )

                Text(
                    text="Antall Stemmer",
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )

            }
            HorizontalDivider(
                thickness = 1.dp,
                color = Color.Black
            )
            Column(
                modifier = Modifier.padding(5.dp)
            ) {
                voteList.forEach { (party, votes) ->
                    if (votes != null) {
                        VotesInfo(party, votes)
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }


        }

    }
    Column(
        modifier = Modifier
    ){
        ExposedDropdownMenuBox(
            expanded =expanded,
            onExpandedChange ={expanded=it}

        ) {
            TextField(
                value =textFieldState.text.toString(),
                onValueChange ={},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded=expanded) },
                modifier=Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest ={expanded=false},
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Start)

            )

            {
                districtOptions.forEach { option->
                    DropdownMenuItem(
                        text ={Text(option.toString())},
                        onClick ={
                            textFieldState.setTextAndPlaceCursorAtEnd(option.toString())
                            valgtDistrikt=option
                            expanded=false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,

                    )
                }
            }


        }
    }



}

@Composable
fun VotesInfo(party: String, votes: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = party ,
            modifier = Modifier.weight(1f),
            fontSize = 20.sp
        )
        Text(
            text = votes.toString(),
            modifier = Modifier.weight(1f),
            fontSize = 20.sp
        )
    }
}


