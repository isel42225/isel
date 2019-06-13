'use strict'

const rp = require('request-promise')
const parse = require('../lib/parse-football-data').init()
const errorHandler = require('../lib/error-handler')

class FootballData{

    constructor(token){
        this.token = token
        this.url = 'http://api.football-data.org/v2/'
    }

    static init(token){
        return new FootballData(token)
    }

    getLeagues(){
        const uri = `${this.url}competitions`
        
        return rp.get(uri)
            .then((body)=>JSON.parse(body))
            .then((json) => { 
                return {
                    'count' : json.count,
                    'leagues': json.competitions.map(parse.league)
                }
            })
            .catch(err => errorHandler(err))
    }

    getTeamsOfLeague(id){
        const options = {
            'uri' : `${this.url}competitions/${id}/teams`,
            'headers' : {
                'X-Auth-Token': this.token
            }
        }
        return rp.get(options)
            .then((body)=>JSON.parse(body))
            .then((json)=> {
                return {
                    'count' : json.count,
                    'competition' : parse.league(json.competition),
                    'teams' : json.teams.map(parse.team)
                }
            })
            .catch(err => errorHandler(err))
    }

    getTeam(idTeam){
        const options = {
            'uri' : `${this.url}teams/${idTeam}`,
            'headers' : {
                'X-Auth-Token': this.token
            }
        }

        return rp.get(options)
            .then((body)=>JSON.parse(body))
            .then((json)=> parse.team(json))
            .catch(err => errorHandler(err))
    }

    getMatchesOfTeam(id, from,to){
        const  options = {
            'uri' : `${this.url}teams/${id}/matches?dateFrom=${from}&dateTo=${to}`,
            'headers' : {
                'X-Auth-Token': this.token
            }
        }
    
        return rp.get(options)
            .then((body)=>JSON.parse(body))
            .then((json)=> {
                return {
                    'count' : json.count,
                    'matches' : json.matches.map(parse.match)
                }
            })
            .catch(err => errorHandler(err))
    }    
}

module.exports = FootballData