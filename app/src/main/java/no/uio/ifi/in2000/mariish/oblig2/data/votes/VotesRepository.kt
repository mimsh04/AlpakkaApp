package no.uio.ifi.in2000.mariish.oblig2.data.votes

import no.uio.ifi.in2000.mariish.oblig2.model.votes.District
import no.uio.ifi.in2000.mariish.oblig2.model.votes.DistrictVotes

class VotesRepository{

    private val aggregatedDataSource=AggregatedVotesDataSource()
    private val individualDataSource=IndividualVotesDataSource()

    suspend fun getVotesForDistrict(district: District):List<DistrictVotes> {
        return when(district){
            District.DISTRICT1->individualDataSource.getIndividualVotes(district)
            District.DISTRICT2->individualDataSource.getIndividualVotes(district)
            District.DISTRICT3->aggregatedDataSource.getAggregatedVotes()
        }

    }









}