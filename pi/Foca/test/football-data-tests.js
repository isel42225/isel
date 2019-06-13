'use strict'

const expect = require('chai').expect
const fs = require('fs')
const token = fs.readFileSync('token.txt', 'utf8')
const fb_data = require('./football-data-mock').init(token)

describe('Football Data', () => {

    it('should get All leagues', done => {
        fb_data
            .getLeagues()
            .then(data => {
                expect(data.count).to.be.deep.equal(147)
                done()
            })
            .catch(err => throwErrorAndTerminate(err ,done))
    })

    it('should get Teams of Premier League (id : 2021)', done =>{
        fb_data
            .getTeamsOfLeague(2021)
            .then(data => {
                expect(data.count).to.be.deep.equal(20)
                const teams = data.teams
                expect(teams[0].name).to.be.deep.equal('Arsenal')
                done()
            })
            .catch(err => throwErrorAndTerminate(err, done)) 
    })

    it('should get Matches of Real Madrid (id : 86) from 17-05-2018 to 26-05-2019', done => {
        const from = '2018-05-17'
        const to = '2019-05-26'
        
        fb_data
            .getMatchesOfTeam(86, from, to)
            .then(data => {
                expect(data.count).to.be.deep.equal(43)
                const matches = data.matches
                const first = matches[0]
                const last = matches[42]
                expect(first.score).to.be.deep.equal('Real Madrid CF 2 - 0 Getafe CF')
                expect(last.score).to.be.deep.equal('Real Madrid CF - Real Betis Balompié')
                expect(first.date >= from)
                    .to.be.true
                expect(last.date <= to)
                    .to.be.true
                done()
            })
            .catch(err => throwErrorAndTerminate(err, done))
    })

    it('should get Real Madrid (id :86)', done => {
        fb_data
            .getTeam(86)
            .then(team => {
                expect(team)
                    .to.have.property('venue', 'Estadio Santiago Bernabéu')
                done()
            })
            .catch(err => throwErrorAndTerminate(err, done))
    })
})

function throwErrorAndTerminate(err, done){
    expect.fail(err)
    done()
}