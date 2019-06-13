'use strict'

const rp = require('request-promise')
const parse = require('../lib/parse-foca-db').init()
const errorHandler = require('../lib/error-handler')

class FocaElastic{

    constructor(es){
        this.url =  `http://${es.host}:${es.port}/apifoca/`
        this.refresh = `http://${es.host}:${es.port}/apifoca/_refresh`
    }

    static init(es){
        return new FocaElastic (es)
    }
	
    async createGroup(userId, name, desc){
        const options = {
            url: `${this.url}myteams`,
            json: true,
            body: { 'user': userId,'name': name, 'description': desc, 'teams' :[] }
        }

        try{
            const data = await rp.post(options)
            await rp.post(this.refresh)
        
            return {
                'id' : data._id,
                'message' : `The group ${name} was created.`,
                'getUrl': `/api/foca/myteams/${data._id}`
            }
        } catch (err){
            return errorHandler(err)
        }
    }
    
    editGroup(id, name, desc){
        return this
            .getGroup(id)
            .then(group => {
                const options = {
                    url: `${this.url}myteams/${id}`,
                    json: true,
                    body: { 'name': name, 'description': desc, 'teams': group.teams  }
                }
                return rp.put(options)
            })
            .then( () => {
                return {
                    'message' : 'Group details were updated successfully.',
                    'newName' : name,
                    'newDescription' : desc,
                    'successfull' : true
                }
            })
            .catch(err => errorHandler(err))
    }	

    async deleteGroup(id){
        const url = `${this.url}myteams/${id}`
        try{
            await rp.delete(url)
            await rp.post(this.refresh)
            return {
                'message' : `Team group with id : ${id} was deleted with success`,
                'successfull' : true
            }
        }catch(err){
            return errorHandler(err)
        } 
    }

    getAllGroups(){
        const url = `${this.url}_search?size=1000`
        return rp
            .get(url)
            .then(data => JSON.parse(data).hits.hits)
            .then(groups => groups.map(parse.simpleGroup))
            .catch(err => errorHandler(err))
    }

    getUserGroups(userId){
        const url = `${this.url}myteams/_search?q=user:${userId}`
        return rp
            .get(url)
            .then(data => 
                JSON.parse(data).hits.hits)
            .then(groups => 
                groups.map(parse.simpleGroup))
            .catch(errorHandler)
    }

    getGroup(id){
        const url = `${this.url}myteams/${id}`
        return rp
            .get(url)
            .then(data => {
                return JSON.parse(data)
            })
            .then(group => 
                parse.group(group)
            )
            .catch(err => errorHandler(err))
    }

    addTeamGroup(idGroup, team){
        return this
            .getGroup(idGroup)
            .then(group => {
                group.teams.push(team)
                const options = {
                    url: `${this.url}myteams/${idGroup}`,
                    json: true,
                    body: group
                }
                return rp.put(options)
            })
            .then(() => {
                return {
                    'message': `The team ${team.name} was added successfully to the group.`,
                    'successfull' : true
                }
            })
            .catch(err => errorHandler(err))
    }

    deleteTeamGroup(idGroup, idTeam){
        return this.getGroup(idGroup)
            .then(group => {
                const team = group.teams.find(team => team.id == idTeam)
                const index = group.teams.indexOf(team)
                if (index >= 0) {
                    group.teams.splice(index, 1)
                }
           
                const options = {
                    url: `${this.url}myteams/${idGroup}`,
                    json: true,
                    body: group
                }
                return rp.put(options).then(team)
            })
            .then((team) => {
                return {
                    'message': `The team ${team.name} was deleted successfully of the group.`,
                    'successfull' : true
                }
            })
            .catch(err => errorHandler(err))
        
    }

    getAllTeamsOfGroup(idGroup){
        return this.getGroup(idGroup)
            .then(group => {
                return group.teams
            })
            .catch(err => errorHandler(err))
    }
}

module.exports = FocaElastic