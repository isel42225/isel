'use strict'

const es = {
    host: 'localhost',
    port: '9200'
}
const service = require('./auth-mock').init(es)
const expect = require('chai').expect

const dummy = {
    'fullname' : 'Testy test',
    'username' : 'fake',
    'password' : '123'
}

const dummy2 = {
    'fullname' : 'dummy',
    'username' : 'dummy2',
    'password' : '123'
}

const dummy3 = {
    'fullname' : 'To ze',
    'username' : 'dummy3',
    'password' : '123'
}



describe('Auth tests', ()=> {
    
    it('should create a new user', done => {
        const {fullname, username , password} = dummy
        service
            .createUser(fullname, username, password)
            .then(user => {
                expect(user).to.have.property('_id')
                expect(user).to.have.property('fullname', 'Testy test')
                done()
            })
            .catch(err => {
                expect.fail(err)
                done()
            })
    })

    it('should create a new user and get it', done => {
        const {fullname, username , password} = dummy2
        service
            .createUser(fullname, username, password)
            .then(user => {
                expect(user).to.have.property('_id')
                expect(user).to.have.property('fullname', 'dummy')
                return Promise.resolve(user._id) 
            })
            .then(id => service.getUser(id))
            .then(user => {
                expect(user).to.have.property('password', '123')
                done()
            })
            .catch(err => {
                expect.fail(err)
                done()
            })
    })

    it('should create a new user, get it and authenticate it', done => {
        const {fullname, username , password} = dummy3
        service
            .createUser(fullname, username, password)
            .then(user => {
                expect(user).to.have.property('_id')
                expect(user).to.have.property('fullname', 'To ze')
                return Promise.resolve(user._id) 
            })
            .then(id => service.getUser(id))
            .then(user => {
                expect(user).to.have.property('password', '123')
                return Promise.resolve(user)
            })
            .then(user => Promise.all([
                service.authenticate(user.username, user.password),
                Promise.resolve(user._id)
            ]))
            .then(([uId , id]) => {
                expect(uId).to.be.deep.equal(id)
                done()
            })
            .catch(err => {
                expect.fail(err)
                done()
            })
    })
})
