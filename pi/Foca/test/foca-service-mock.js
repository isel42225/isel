'use strict'

//Emulate the groups
let groups = []
//Emulate the id of groups    
let count = 1
//Emulate userIds
let uId = 0

const teams = {
    '86' : {
        'id' : 86,
        'name' : 'Real Madrid'
    },
    '61' : {
        'id': 61,
        'name': 'Chelsea FC',
    },
    '57' : {
        'id': 57,
        'name': 'Arsenal FC'
    }
}

class MockService {

    // useless arguments to be coerent with "real" service
    // eslint-disable-next-line no-unused-vars
    static init(fbData, focaDb){
        return new MockService ()
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

    // eslint-disable-next-line no-unused-vars
    getTeamsOfLeague(id){
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
                    'name': 'Watford',
                    'location':'England',
                    'emblemUrl':'https://upload.wikimedia.org/wikipedia/en/e/e2/Watford.svg',
                    'venue': 'Vicarage Road Stadium'
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

    createGroup(userId,name, desc){
        const id = count++
        groups[id] = {
            'id' : id,
            'user' : uId++,
            'name':name,
            'description': desc,
            'teams' : []
        }
        const res = {
            'id' : id,
            'message': `The group ${name} was created`,
            'getUrl': `/api/foca/myteams/${id}`
        }
        return Promise.resolve(res)
    }

    getGroup(id){
        let res = groups[id]
        return res === undefined ? 
            Promise.reject(new Error('Group does not exist')) : Promise.resolve(res)
    }

    getUserGroups(userId){
        const res = groups
            .filter(g => g.userId === userId)
            .map(g => {
                return {
                    'id' : g.id,
                    'name': g.name,
                    'description' : g.description
                }
            })
        return Promise.resolve(res)
    }

    editGroup(id,name, desc){
        let res = groups[id]
        res.name = name
        res.desc = desc

        groups[id] = res
        
        const resp = {
            'message' : 'Group details were updated successfully',
            'newName': name,
            'newDescription': desc
        }

        return Promise.resolve(resp)
    }

    deleteGroup(id){
        const res = {
            'message' : `Team group with id : ${id} was deleted with success`,
            'successfull' : true
        }
        delete groups[id]
        return Promise.resolve(res)
    }
  
    addTeamGroup(idGroup, idTeam){
        let group = groups[idGroup]
        let team = teams[idTeam]
        group.teams.push(team)
        groups[idGroup] = group
        const res = {
            'message': `The team ${team.name} was added successfully to the group.`
        }
        return Promise.resolve(res)
    }

    deleteTeamGroup(idGroup, idTeam){
        const group = groups[idGroup]
        const team = teams[idTeam]
        const idx = group.teams.indexOf(team)
        group.teams.splice(idx , 1)
        
        const res = {
            'message': 'The team was removed from the group.'
        }
        return Promise.resolve(res)
    }

    // eslint-disable-next-line no-unused-vars
    matchesOfGroup(idGroup, from, to, cb){
        const res = {
            'count' : 2,
            'matches': [
                {
                    'id' : 10,
                    'competition' : {
                        'id': 2021,
                        'name': 'Premier League'
                    },
                    'date' : '2018-06-23T18:00:00Z',
                    'homeTeam': {
                        'id' : 61,
                        'name' : 'Chelsea FC'
                    }, 
                    'awayTeam' : {
                        'id' : 57,
                        'name': 'Arsenal'
                    }, 
                    'score' : 'Chelsea 3 - 2 Arsenal'
                },
                {
                    'id' : 11,
                    'competition' : {
                        'id': 221,
                        'name': 'PI Cup'
                    },
                    'date' : '2018-06-26T18:00:00Z',
                    'homeTeam': {
                        'id' : 2,
                        'name' : 'Santa Fc'
                    }, 
                    'awayTeam' : {
                        'id' : 1,
                        'name': 'Ice Fc'
                    }, 
                    'score' : {
                        'homeTeam': 3,
                        'awayTeam': 3
                    }
                }
            ]
        }
        return Promise.resolve(res)
    }

} 

module.exports = MockService