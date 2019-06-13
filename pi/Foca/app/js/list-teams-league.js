'use strict'

const allTeamsView = require('./../views/allTeams.html')
const Handlebars = require('./../../node_modules/handlebars/dist/handlebars.js')
const teamListTemplate = require('./../views/listTeams.hbs')
const teamListResults = Handlebars.compile(teamListTemplate)
const util = require('./utils.js')

module.exports = (divMain, lid) => {
    divMain.innerHTML = allTeamsView
    
    
    const groupsUrl = '/api/foca/myteams'
    
    Promise
        .all([
            fetchTeams(lid),
            fetch(groupsUrl).then(res => res.json())
        ])
        .then(([teams, groups]) => showTeams(teams, groups))
        .catch(err => showError(err))
}

async function fetchTeams(lid){
    const teamsUrl = `/api/foca/leagues/${lid}/teams`
    const resp = await fetch(teamsUrl)
    const data = await resp.json()
    if(resp.status != 200)
        return Promise.reject(data.error)
    return Promise.resolve(data.teams)
}

function showTeams(teams, groups){
    const divTeams = document.getElementById('divTeams')
    divTeams.innerHTML = teamListResults({teams, groups})

    document
        .getElementById('buttonAdd')
        .addEventListener('click', addTeamsToGroups)
}

function addTeamsToGroups(ev){
    ev.preventDefault()

    const dict = {}
    Array
        .from(document.querySelectorAll('input.form-check-input'))
        .filter(box => box.checked === true)
        .forEach(ck => {
            const tid = ck.getAttribute('team-id')
            const gid = ck.getAttribute('group-id')
            const value = dict[tid]
            if(!value){
                dict[tid] = {
                    'id': tid,
                    'groups' : []
                }
            }
            dict[tid].groups.push(gid)
        })
    addTeams(dict)
}

function addTeams(dict){
    for(const key in dict){
        const value = dict[key]
        const {id, groups} = value
        groups.forEach(group => {
            const url = `/api/foca/myteams/${group}/team/${id}`
            fetch(url, {method : 'PUT'})
        }) 
    }
    util.showAlert('Teams Added!', 'success')
}

function showError(err){
    let msg = 'Server Error, please try again later.'
    if(err.statusCode === 403)
        msg = 'You dont have permission to see this league.'
    util.showAlert(msg,'danger')
}
