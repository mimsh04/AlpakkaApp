package no.uio.ifi.in2000.mariish.oblig2.data.votes


import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.gson.gson
import no.uio.ifi.in2000.mariish.oblig2.model.votes.District
import no.uio.ifi.in2000.mariish.oblig2.model.votes.DistrictVotes

class AggregatedVotesDataSource {

    private val client=HttpClient{
        install(ContentNegotiation){
            gson()
        }
    }




    suspend fun getAggregatedVotes(): List<DistrictVotes> {
        return try{
            val baseUrl="https://in2000-proxy.ifi.uio.no/alpacaapi/v2/district3"
            val response:AggregateResponse = client.get(baseUrl).body()
            return transFormedResponse(response,District.DISTRICT3)

        }
        catch(e:Exception){
            Log.d("AGGREGATEDVOTESSOURCE", "Could not fetch votes: ${e.message}", e)
            emptyList()
        }

    }



}

fun transFormedResponse(response: AggregateResponse,district: District):List<DistrictVotes>{
    val transformedVotes=response.parties
        .map{votes->
            DistrictVotes(
                district=district,
                alpacaPartyId = votes.partyId,
                numberOfVotesForParty =votes.votes
            )
        }
    return transformedVotes

}

data class Votes(
    val partyId:String,
    val votes:Int
)

data class AggregateResponse(
    val parties:List<Votes>
)


//val response:List<Votes> = client.get(baseUrl).body()
//            val transformedVotes= transFormedResponse(AggregateResponse(response),district)
//            return transformedVotes