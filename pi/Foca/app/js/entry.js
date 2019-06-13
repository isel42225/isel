'use strict'

require('./../../node_modules/bootstrap/dist/css/bootstrap.css')
require('./../../node_modules/bootstrap/dist/js/bootstrap')
const Handlebars = require('./../../node_modules/handlebars/dist/handlebars')
const listGroups = require('./list-groups.js')
const groupDetails = require('./groupDetails.js')
const listLeagues = require('./list-leagues.js')
const listTeamsOfLeague = require('./list-teams-league.js')
const login = require('./login.js')
const logout = require('./logout.js')
const matches = require('./matches.js')
const index = require('./focaIndex.js')
const mainView = require('./../views/main.html')
const navbarView = Handlebars.compile(require('./../views/navbar.hbs'))
const util = require('./utils.js')

document.body.innerHTML = mainView

const divMain = document.getElementById('divMain')
const divNavbar = document.getElementById('divNavbar')

showNavBar()
    .then(() => {
        window.onload = showView
        window.onhashchange = showView
        showView()
    })

function showView() {
    let [path, params] = window.location.hash.split('/')
    if(path === '') path = '#focaIndex'
    switch(path) {
        case '#focaIndex':
            index(divMain)
            break
        case '#groups':
            handlePath(params, groupDetails, listGroups)
            break
        case '#leagues':
            handlePath(params, listTeamsOfLeague, listLeagues)
            break
        case '#login':
            login(divMain, showNavBar)
            break
        case '#logout':
            logout(divMain, showNavBar)
            break
        case '#matches' : 
            matches(divMain, params)
            break
        default:
            divMain.innerHTML = 'Oops! Resource not found'
    }
    updateNav(path, divNavbar)
}

async function showNavBar(){
    const url = 'api/auth/session'
    const resp = await fetch(url)
    const body = await resp.json()
    if(resp.status != 200){
        util.showAlert(JSON.stringify(body), 'danger')
    }
    divNavbar.innerHTML = navbarView(body)
}

function updateNav(path) {
    const current = document.querySelector('a.active')
    if(current) current.classList.remove('active')
    const nav = document.getElementById('nav' + path)
    if(!nav) return
    nav.classList.add('active')
}

function handlePath(id, present, absent){
    if(id)
        return present(divMain, id)
    return absent(divMain)
}