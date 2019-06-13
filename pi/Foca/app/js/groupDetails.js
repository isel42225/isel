'use strict'

const groupDetailsView = require('./../views/groupDetails.html')
const Handlebars = require('./../../node_modules/handlebars/dist/handlebars')
const teamsTemplate = require('./../views/list-group-teams.hbs')
const detailsTemplate = require('./../views/groupDetails.hbs')
const detailsResult = Handlebars.compile(detailsTemplate)
const teamsResult = Handlebars.compile(teamsTemplate)

module.exports = (divMain, id) => {
    divMain.innerHTML = groupDetailsView

    document
        .getElementById('buttonEdit') 
        .addEventListener('click', ev => editGroup(ev, id))

    document
        .getElementById('buttonMatches')
        .addEventListener('click', ev => getMatches(ev, id))
    
    fetchGroup(id)
        .then(group => showGroup(group))
}

function getMatches(ev, id){
    ev.preventDefault()
    window.location.hash = `#matches/${id}`
}

function fetchGroup(id){
    const url = `/api/foca/myteams/${id}`
    return fetch(url)
        .then(resp => resp.json())
}

function showGroup(group){
    const divDetails = document.getElementById('divDetails')
    divDetails.innerHTML = detailsResult(group)
    showTeams(group.teams, group.id)
}

function showTeams(teams, gid){
    const divGroupTeams = document.getElementById('divGroupTeams')
    divGroupTeams.innerHTML = teamsResult(teams)

    const deleteButtons = document.querySelectorAll('button.delete')

    deleteButtons.forEach(button => {
        const tid = button.getAttribute('team-id')
        button.addEventListener('click', ev => deleteTeamFromGroup(ev, tid, gid))
    })
}

function deleteTeamFromGroup(ev, tid, gid){
    ev.preventDefault()
    const deleteUrl = `/api/foca/myteams/${gid}/team/${tid}`
    fetch(deleteUrl, {method : 'DELETE'})
        .then(() => fetchGroup(gid))    // auto unwrap
        .then(group => showGroup(group))
}
function editGroup(ev, id){
    ev.preventDefault()
    const editName = document.getElementById('editName')
    const editDescription = document.getElementById('editDescription')

    let name = editName.value
    let description = editDescription.value
    
    if(!name){
        const groupName = document.getElementById('groupName')
        name = groupName.textContent.split(':').slice(1)
    }
    if(!description){
        const groupDescription = document.getElementById('groupDescription')
        description = groupDescription.textContent.split(':').slice(1)
    }
    
    const url = `/api/foca/myteams/${id}`
    const options = {
        method: 'PUT', 
        mode: 'cors', 
        cache: 'no-cache', 
        credentials: 'same-origin', 
        headers: {
            'Content-Type': 'application/json; charset=utf-8',
        },
        redirect: 'follow', 
        referrer: 'no-referrer',
        body: JSON.stringify({'name': name,'description': description}),
    }

    fetch(url, options)
        .then(resp => resp.json())
        .then(() => fetchGroup(id))
        .then(group => showGroup(group))
    
    editName.value = ''
    editDescription.value = ''
}