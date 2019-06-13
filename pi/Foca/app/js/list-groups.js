'use strict'

const allGroupsView = require('./../views/allGroups.html')
const Handlebars = require('./../../node_modules/handlebars/dist/handlebars')
const groupListTemplate = require('./../views/listGroups.hbs')
const groupListResults = Handlebars.compile(groupListTemplate)


module.exports = (divMain => {
    divMain.innerHTML = allGroupsView
        
    document
        .getElementById('buttonCreate')
        .addEventListener('click', createGroup)

    getGroups()
        .then(groups => listGroups(groups))
        
})

function createGroup(ev){
    ev.preventDefault()
    const inputName = document.getElementById('inputName')
    const inputDescription = document.getElementById('inputDescription')

    const name = inputName.value
    const description = inputDescription.value
    
    getGroups()
        .then(groups => 
            Promise.all([
                Promise.resolve(groups), 
                postGroup(name, description)
                    .then(res => buildGroup(res.id, name, description) )
            ])
        ) 
        .then(([groups, group]) => addGroupToList(groups, group))
        .then(groups => listGroups(groups))
    
    inputName.value = ''
    inputDescription.value = ''
}

function listGroups(groups){
    const divGroups = document.getElementById('divGroups')
    divGroups.innerHTML = groupListResults({groups})
    registerDeleteListener()
}

function getGroups(){
    const url = '/api/foca/myteams'
    return fetch(url).then(resp => resp.json())
}

function postGroup(name, description){
    const options = {
        method: 'POST', 
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

    return fetch('/api/foca/myteams', options).then(resp => resp.json())            
}

function deleteGroup(ev, gid){
    ev.preventDefault()
    const url = `/api/foca/myteams/${gid}`
    
    getGroups()
        .then(groups => 
            fetch(url, {method : 'DELETE'})
                .then(resp => resp.json())
                .then(() => Promise.resolve(groups))
        )
        .then((groups) => findGroupAndRemove(groups, gid))
        .then(groups => listGroups(groups))
}

function registerDeleteListener(){
    const deleteButtons = document.querySelectorAll('button.delete')
    deleteButtons.forEach(db => {
        const id = db.getAttribute('group-id')
        db.addEventListener('click', ev => deleteGroup(ev,id))
    })
}

function buildGroup(id, name, description){
    return {'id': id, 'name': name, 'description': description}
}

function addGroupToList(groups, group){
    groups.push(group)
    return groups
}

function findGroupAndRemove(groups, gid){
    const idx = groups.findIndex(g => g.id === gid)
    groups.splice(idx,1)
    return groups
}