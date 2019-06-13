'use strict'


const matchesView = require('../views/matches.html')
const Handlebars = require('./../../node_modules/handlebars/dist/handlebars')
const matchesResults  = Handlebars.compile(require('../views/matches.hbs'))



module.exports = (divMain, id) => {
    divMain.innerHTML = matchesView

    document
        .getElementById('buttonMatches')
        .addEventListener('click', ev => fetchMatches(ev,id))

}

async function fetchMatches(ev, id){
    ev.preventDefault()
    const from = document.getElementById('startDate').value
    const to = document.getElementById('endDate').value
    const url = `/api/foca/myteams/${id}/matches?from=${from}&to=${to}`
    const matches = await fetch(url).then(resp => resp.json()).then(data => data.matches)
    
    document.getElementById('divMatches').innerHTML = matchesResults(matches)
}
