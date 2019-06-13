'use strict'

class FocaService {

    constructor(fbData, focaDb){
        this.footData = fbData
        this.focaData = focaDb
    }

    static init(fbData, focaDb){
        return new FocaService (fbData, focaDb)
    }

    getLeagues(){
        return this.footData.getLeagues()
    }

    getTeamsOfLeague(id){
        return this.footData.getTeamsOfLeague(id)
    }

    createGroup(userId, name, desc){
        return this.focaData.createGroup(userId, name, desc)
    }

    deleteGroup(id){
        return this.focaData.deleteGroup(id)
    }

    getGroups(){
        return this.focaData.getAllGroups()
    }

    getUserGroups(userId){
        if(!userId){
            return Promise.reject({
                'statusCode': 401, 
                err: 'You must be logged in to see your groups!'
            })
        }
        return this.focaData.getUserGroups(userId)
    }

    getGroup(id){
        return this.focaData.getGroup(id)
    }

    editGroup(id, name, desc){
        return this.focaData.editGroup(id, name, desc)
    }

    
    addTeamGroup(idGroup, idTeam){
        return this.footData.getTeam(idTeam)
            .then((team) => this.focaData.addTeamGroup(idGroup, team))
    }

    deleteTeamGroup(idGroup, idTeam){
        return this.focaData.deleteTeamGroup(idGroup, idTeam)
    }

    matchesOfGroup(idGroup, from, to){
        let promises = []
        let allMatches= {
            count : 0,
            matches : []
        }
        return this.focaData.getAllTeamsOfGroup(idGroup)
            .then(teams => {
                if(teams.length === 0)
                    teams //DO ERROR?
                teams.forEach(team => {
                    promises.push(this.footData.getMatchesOfTeam(team.id,from,to))
                })
            })
            .then(()=> Promise.all(promises))
            .then(arr => {
                arr.forEach(element => {
                    element.matches.forEach(match => {
                        allMatches.matches.push(match)
                    })
                })
            })
            .then(()=>{
                allMatches.matches = 
                    allMatches.matches.filter((match, index, self) =>
                        index === self.findIndex((t)=> t.id === match.id)) 
            })
            .then(()=> {
                allMatches.matches.sort(compareDates)
                allMatches.count = allMatches.matches.length 
                return allMatches
            })
    }
} 

function compareDates(matchA, matchB){
    let dateA = new Date(matchA.date) 
    let dateB = new Date(matchB.date)  
    return dateA.getTime() - dateB.getTime()
}


module.exports = FocaService