package no.uio.ifi.in2000.mariish.oblig2.data.alpacas

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import no.uio.ifi.in2000.mariish.oblig2.data.votes.VotesRepository
import no.uio.ifi.in2000.mariish.oblig2.model.alpacas.PartyInfo
import no.uio.ifi.in2000.mariish.oblig2.model.votes.District

class AlpacaPartiesRepository{

    private val dataSource=AlpacaPartiesDataSource()
    private val votesRepository=VotesRepository()

    suspend fun getParties():List<PartyInfo> = withContext(Dispatchers.IO){
        dataSource.getParties()
    }

    suspend fun getID(id:String): PartyInfo? {
        return getParties().find { it.id==id }
    }

    suspend fun getPartyAndVotes(district: District): List<Pair<String, Int?>> {
        val parties=getParties()
        val votes= votesRepository.getVotesForDistrict(district)

        Log.d("VOTEDEBUG", "Votes for $district: $votes")


        return parties.map{party->
            val partyVotes= votes.find{it.alpacaPartyId==party.id}?.numberOfVotesForParty
            Pair(party.name,partyVotes)
        }


    }
}