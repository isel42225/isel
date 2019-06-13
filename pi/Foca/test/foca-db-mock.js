'use strict'

let gid = 0
let uId = 0
const groups = []

class FocaElasticMock {

    constructor(es){
        this.url =  `http://${es.host}:${es.port}/apifoca/`
    }
    static init(es){
        return new FocaElasticMock(es)
    }

    createGroup(userId, name, desc){
        const id = gid++
        groups[id] = {
            'id' : id,
            'user': uId++,
            'name':name,
            'description': desc,
            'teams' : []
        }

        const res = {
            'id':id,
            'message' : `The group ${name} was created.`,
            'getUrl': `/api/foca/myteams/${id}`
        }
       
        return Promise.resolve(res)
    }

    editGroup(id, name, desc){
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
        groups.splice(id, 1)
        const res = {
            'message' : `Team group with id : ${id} was deleted with success`,
            'successfull' : true
        }
        return Promise.resolve(res)
    }

    getAllGroups(){
        const res = groups.map(g => {
            return {
                'id' : g.id,
                'name': g.name,
                'description' : g.description
            }
        })
        return Promise.resolve(res)
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

    getGroup(id){
        return Promise.resolve(groups[id])
    }

    addTeamGroup(idGroup, team){
        const group = groups[idGroup]
        group.teams.push(team)
        const res = {
            'message' : `The team ${team.name} was added successfully to the group.`
        }
        return Promise.resolve(res)
    }

    getAllTeamsOfGroup(idGroup){
        const group = groups[idGroup]
        return Promise.resolve(group.teams)
    }
}

module.exports = FocaElasticMock