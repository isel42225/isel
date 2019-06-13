'use strict'

class FootballMock{

    constructor(token){
        this.token = token
        this.url = 'http://api.football-data.org/v2/'
    }

    static init(token){
        return new FootballMock(token)
    }

    getLeagues(){
        const res = {
            'count': 147,
            'leagues': [
                {
                    'id': 2021,
                    'name': 'Premmier League',
                    'location': 'England',
                    'emblem_url': null,
                    'season':
                    {
                        'startDate':'2018-08-10',
                        'endDate': '2019-05-12',
                        'winner': null 
                    }
                },
                {
                    'id': 2054,
                    'name': 'League Two',
                    'location': 'England',
                    'emblem_url': null,
                    'season':
                    {
                        'startDate':'2018-08-04',
                        'endDate': '2019-05-04',
                        'winner': null 
                    }
                }

            ]
        }
        return Promise.resolve(res)
    }

    getTeamsOfLeague(id){
        if(id < 0) {
            const error = {
                'status code' : 400,
                'message' : 'League must be a positive integer'
            }
            return Promise.reject(error)
        }
        const res = {
            'count': 20,
            'competition': {
                'id': 2021,
                'name':'Premier League',
                'location': 'England',
            },
            'teams': [
                {
                    'id': 346,
                    'name': 'Arsenal',
                    'location':'England',
                    'emblemUrl':'http://upload.wikimedia.org/wikipedia/en/5/53/Arsenal_FC.svg',
                    'venue': 'Emirates Stadium'
                },
                {
                    'id': 73,
                    'name': 'Tottenham',
                    'location':'England',
                    'emblemUrl':'http://upload.wikimedia.org/wikipedia/de/b/b4/Tottenham_Hotspur.svg',
                    'venue':'Wembley Stadium'
                }
            ]
        }
        return Promise.resolve(res)
    }

    getTeam(idTeam){
        if(idTeam < 0 ){
            const error = {
                'status code' : 400,
                'message' : 'Team id must be a positive integer'
            }
            return Promise.reject(error)
        }

        const team = {
            'idTeam' : idTeam,
            'name': 'Real Madrid' ,
            'location': 'Spain',
            'emblemUrl' : 'http://upload.wikimedia.org/wikipedia/de/3/3f/Real_Madrid_Logo.svg',
            'venue' : 'Estadio Santiago Bernabéu'
        }
        return Promise.resolve(team)
    }

    // eslint-disable-next-line no-unused-vars
    getMatchesOfTeam(id, from,to){
        const res = {
            'count' : 43,
            'matches': []
        }
        res.matches[0] = {
            'homeTeam' : 'Real Madrid CF',
            'awayTeam' : 'Getafe CF',
            'date' : '2018-08-19T20:15:00Z',
            'score' : 'Real Madrid CF 2 - 0 Getafe CF'
        }

        res.matches[42] = {
            'homeTeam' : 'Real Madrid CF',
            'awayTeam' : 'Real Betis Balompié',
            'date' : '2019-05-25T00:00:00Z',
            'score' : 'Real Madrid CF - Real Betis Balompié'
        }
        return Promise.resolve(res)
    }
}

module.exports = FootballMock