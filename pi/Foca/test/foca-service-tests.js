'use strict'

const expect = require('chai').expect
const fs = require('fs')
const token = fs.readFileSync('token.txt', 'utf8')
const football = require('../data/football-data').init(token)

const es = {
    'host':'localhost',
    'port':9200
}
let userId = 0

const elastic = require('../data/foca-db').init(es)
const service = require('../services/foca-service').init(football, elastic)

describe('FocaService', () => {
    let testId
    before(done => {
        service
            .createGroup(userId++,'test', 'testing')
            .then(data => {
                testId = data.id
                return Promise.all([
                    service.addTeamGroup(testId, 61),
                    service.addTeamGroup(testId, 57)
                ])   
            })
            .then(() => done())
            .catch(err => throwErrorAndTerminate(err, done))
            
    })

    after(done => {
        service
            .deleteGroup(testId)
            .then(() => done())
    })


    it('should get all leagues', done => {
        service
            .getLeagues()
            .then(data => {
                expect(data.count)
                    .to.be.deep.equal(147)
                const leagues = data.leagues
                expect(leagues.find(t => t.id = 2021))  // find premier league
                done()
            })
            .catch(err => throwErrorAndTerminate(err, done)) 
    })
    

    it('should return teams of Premier League (id:2021)', done => {
        service
            .getTeamsOfLeague(2021)
            .then(data => {
                const comp = data.competition
                expect(comp.name)
                    .to.be.deep.equal('Premier League')
    
                const teams = data.teams
                const tottenham = teams.find(t => t.id === 73) 
                expect(tottenham)
                    .to.have.a.property('name', 'Tottenham')
                expect(tottenham)
                    .to.have.a.property('venue', 'Wembley Stadium')
                done()
            })
            .catch(err => throwErrorAndTerminate(err, done))
    })

    it('should create group get it update it and delete it ', done => {
        service
            .createGroup(userId++, 'test', 'testing')
            .then(data => {
                if(!data.id) return Promise.reject('Missing id property on group creation')
                const id = data.id
                expect(data.getUrl)
                    .to.be.deep.equal(`/api/foca/myteams/${id}`)
                return Promise.all([
                    service.getGroup(id), 
                    Promise.resolve(id)
                ])
            })
            .catch(err => throwErrorAndTerminate(err, done))
            .then(([group, id]) => {
                expect(group.name)
                    .to.be.deep.equal('test')
                expect(group.userId)
                return Promise.all([
                    service.editGroup(id, 'dummy', 'dumb'),
                    Promise.resolve(id)
                ])
            })
            .catch(err => throwErrorAndTerminate(err, done))
            .then(([updated , id]) => {
                expect(updated)
                    .to.have.a.property('newDescription', 'dumb')
            
                return Promise.all([
                    service.deleteGroup(id),
                    Promise.resolve(id)
                ])
            })
            .catch(err => throwErrorAndTerminate(err, done))
            .then(([data, id]) => {
                expect(data)
                    .to.have.a.property('successfull', true)
                return service.getGroup(id)
            })
            .then(() => throwErrorAndTerminate(new Error('Group not deleted!'), done))
            .catch(() => done())            
    })
    
    it('should create a group and add team to it and then delete the team', done => {
        service
            .createGroup(userId++, 'test', 'testing')
            .then(data => {
                if(!data.id) return Promise.reject('Missing id property on group creation')
                const id = data.id
                // add Real Madrid (id : 86)
                return service.addTeamGroup(id, 86).then(() => 
                    id)
            })
            .catch(err => throwErrorAndTerminate(err, done))
            .then(id => {
                return Promise.all([
                    service.getGroup(id),
                    Promise.resolve(id)
                ])
            })
            .catch(err => throwErrorAndTerminate(err, done))
            .then(([group, id]) => {
                expect(group)
                    .to.have.a.property('name', 'test')
                const rm = group.teams[0]
                expect(rm)
                    .to.have.a.property('name', 'Real Madrid')
                return service.deleteTeamGroup(id, 86).then(() => id)
            })
            .catch(err => throwErrorAndTerminate(err, done))
            .then(id => {
                return Promise.all([
                    service.getGroup(id),
                    Promise.resolve(id)
                ]) 
            })
            .catch(err => throwErrorAndTerminate(err, done))
            .then(([group, id]) => {
                const teams = group.teams
                expect(teams)
                    .to.have.length(0)
                return service.deleteGroup(id).then(id)
            })
            .catch(err => throwErrorAndTerminate(err, done))
            .then(id => {
                return service.getGroup(id)
            })
            .then(() => throwErrorAndTerminate(new Error('Group not deleted!'), done))
            .catch(() => done())
    })

    it('should return the matches of team between two dates', done => {
        service
            .matchesOfGroup(testId, '2018-08-18', '2018-08-30')
            .then(data => {
                expect(data.matches[0].homeTeam)
                    .to.have.property('name','Chelsea FC') 
                done()
            })
    })

    it('should return userGroups', done => {
        service
            .getUserGroups(userId)
            .then(groups => {
                expect(groups).to.have.length(0)
                done()
            })
            .catch(err => throwErrorAndTerminate(err, done))
    })
})

function throwErrorAndTerminate(err, done){
    expect.fail(err)
    done()
}
       
