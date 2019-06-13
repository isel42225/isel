'use strict'

const allLeaguesView = require('./../views/allLeagues.html')
const Handlebars = require('./../../node_modules/handlebars/dist/handlebars')
const leagueListTemplate = require('./../views/listLeagues.hbs')
const leagueListResults = Handlebars.compile(leagueListTemplate)

module.exports = (divMain => {
    divMain.innerHTML = allLeaguesView
    const divLeagues = document.getElementById('divLeagues')

    const url = '/api/foca/leagues'
    fetch(url)
        .then(resp => resp.json())
        .then(leagues => leagueListResults(leagues))
        .then(view => divLeagues.innerHTML = view)
})