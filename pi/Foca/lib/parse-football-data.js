'use strict'

class ParseFootball{

    static init(){
        return new ParseFootball()
    }

    match(match){
        const id = match.id
        const homeTeam = match.homeTeam
        const awayTeam = match.awayTeam
        const status = match.status
        const date = match.utcDate
        const matchScore = match.score
    
        let score
    
        if(status === 'FINISHED'){
            score = `${homeTeam.name} ${matchScore.fullTime.homeTeam} - ${matchScore.fullTime.awayTeam} ${awayTeam.name}`
        }
        else {
            score = `${homeTeam.name} - ${awayTeam.name}`
        }

        return {
            'id' : id,
            'homeTeam' : homeTeam,
            'awayTeam' : awayTeam,
            'date' : date,
            'score' : score
        }
    }

    team(team){
        return {
            'id': team.id,
            'name' : team.shortName,
            'location': team.area.name,
            'emblemUrl': team.crestUrl,
            'venue' : team.venue
        }
    }

    league(league){
        // season can be null
        const season =  null || league.currentSeason
        const res =  {
            'id': league.id,
            'location': league.area.name,
            'name': league.name,
            'emblemUrl' : league.emblemUrl,
            'season': season
        }
        return res
    }
}

module.exports = ParseFootball


    