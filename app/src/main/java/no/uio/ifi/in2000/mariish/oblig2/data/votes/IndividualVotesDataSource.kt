package no.uio.ifi.in2000.mariish.oblig2.data.votes

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.gson.gson
import no.uio.ifi.in2000.mariish.oblig2.model.votes.District
import no.uio.ifi.in2000.mariish.oblig2.model.votes.DistrictVotes


class IndividualVotesDataSource {

    private val client=HttpClient{
        install(ContentNegotiation){
            gson()
        }
    }

    private val urlDistrict1="https://in2000-proxy.ifi.uio.no/alpacaapi/v2/district1"
    private val urlDistrict2="https://in2000-proxy.ifi.uio.no/alpacaapi/v2/district2"


    suspend fun getIndividualVotes(district: District): List<DistrictVotes> {
        return try{
            val url=when(district){
                District.DISTRICT1->urlDistrict1
                District.DISTRICT2->urlDistrict2
                else->throw IllegalArgumentException("District $district is not handled by IndividualVotesDataSource")
            }
            val response: List<IndividualVotes> = client.get(url).body()
            return transFormedResponse(IndividualResponse(response),district)


        }
        catch(e:Exception){
            Log.d("INDIVIDUALVOTESDATASOURCE","Could not fetch votes:${e.message}",e)
            emptyList()
        }

    }


}

fun transFormedResponse(response: IndividualResponse,district: District):List<DistrictVotes>{
    val transformedVotes=response.votes
        .groupBy { it.id }
        .map{(id,votes)->
            DistrictVotes(
                district = district,
                alpacaPartyId = id,
                numberOfVotesForParty = votes.size
            )
        }

    return transformedVotes


}

data class IndividualVotes(
    val id:String
)

data class IndividualResponse(
    val votes:List<IndividualVotes>
)