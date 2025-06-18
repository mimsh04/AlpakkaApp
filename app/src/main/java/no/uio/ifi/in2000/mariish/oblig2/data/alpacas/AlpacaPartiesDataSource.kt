package no.uio.ifi.in2000.mariish.oblig2.data.alpacas





import android.util.Log
import io.ktor.client.HttpClient
import no.uio.ifi.in2000.mariish.oblig2.model.alpacas.Parties
import no.uio.ifi.in2000.mariish.oblig2.model.alpacas.PartyInfo
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.gson.gson



class AlpacaPartiesDataSource {

    private val client = HttpClient{
        install(ContentNegotiation){
            gson()
        }
    }


    private val baseUrl="https://in2000-proxy.ifi.uio.no/alpacaapi/v2/alpacaparties"

    suspend fun getParties():List<PartyInfo>{
        return try{
            val response:Parties=client
                .get(baseUrl)
                .body()
            response.parties
        } catch (e:Exception){
            Log.d("ALPACAPARTIESDATASOURCE","kunne ikke hente api")
            emptyList()
        }


    }


}