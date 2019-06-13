'use strict'

const expect = require('chai').expect

const es = {
    host: 'localhost',
    port: '9200'
}

const foca_data = require('../data/foca-db').init(es)

const team = {
    'id': 57,
    'name': 'Arsenal',
    'location': 'England',
    'emblemUrl': 'http://upload.wikimedia.org/wikipedia/en/5/53/Arsenal_FC.svg',
    'venue': 'Emirates Stadium'
}

let userId = 0

describe('foca-db Test', () => {
    let testId
    before(done => {
        foca_data
            .createGroup(userId++,'test', 'testing')
            .then(data => {
                testId = data.id 
                done()
            })
            .catch(err => {
                expect.fail(err)
                done()
            })
           
    })

    after(done => {
        foca_data
            .deleteGroup(testId)
            .then(() => done())
            .catch(err => {
                expect.fail(err)
                done()
            })
    })


    it('Should create a foca-db', done => {
        expect(foca_data.url).to.be.equals(`http://${es.host}:${es.port}/apifoca/`)
        done()
    })

    it('Should create and update a bundle and then delete it', done => {
        foca_data
            .createGroup(userId++, 'best', 'best of the best')
            .then(data => {
                expect(data)
                    .to.be.an('object')
                    .and.have.a.property('message', 'The group best was created.')
                return data.id
            })
            .catch(err =>  throwErrorAndTerminate(err, done))
            .then(id => {
                return Promise.all([
                    foca_data.editGroup(id,'notbest', 'not best of the best'),
                    Promise.resolve(id)
                ])
            })
            .catch(err =>  throwErrorAndTerminate(err, done))
            .then(([data, id]) => {
                expect(data)
                    .to.be.an('object')
                    .and.have.a.property('newName', 'notbest')
                return foca_data.deleteGroup(id)
            })
            .catch(err =>  throwErrorAndTerminate(err, done))
            .then(data => {
                expect(data)
                    .to.have.property('successfull', true)
                done()
            })
            .catch(err =>  throwErrorAndTerminate(err, done))
    })
    

    it('should get a group', done => {
        foca_data
            .getGroup(testId)
            .then(group => {
                expect(group)
                    .to.be.an('object')
                    .and.have.property('name', 'test')
                expect(group).to.have.property('user')
                done()
            })
            .catch(err => throwErrorAndTerminate(err, done))
    })

    it('should add a team in the group and get it', done => {
        foca_data
            .addTeamGroup(testId, team)
            .then(data => {
                expect(data)
                    .to.be.an('object')
                    .and.have.property('message', 'The team Arsenal was added successfully to the group.')
            })
            .catch(err =>  throwErrorAndTerminate(err, done))
            .then(() => {
                return foca_data.getAllTeamsOfGroup(testId)
            })
            .catch(err =>  throwErrorAndTerminate(err, done))
            .then(teams => {
                expect(teams)
                    .to.be.an('array')
                    .and.have.lengthOf(1)
                done()
            })
            .catch(err => throwErrorAndTerminate(err, done))
    })
    

    it('should list user groups', done => {
        foca_data
            .getUserGroups(userId)
            .then(groups => {
                expect(groups).to.have.length(0)
                done()
            })
            .catch(err => throwErrorAndTerminate(err, done))
    })
    
    it('should list all groups', done => {
        
        foca_data
            .getAllGroups()
            .then(groups => {
                expect(groups)
                    .to.have.length(1)
                done()
            })
            .catch(err => throwErrorAndTerminate(err , done))
    })
})

function throwErrorAndTerminate(err, done){
    expect.fail(err)
    done()
}
