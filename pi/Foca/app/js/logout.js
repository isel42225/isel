'use strict'

const logoutView = require('../views/logout.html')
const util = require('./utils.js')

module.exports = (divMain, showNavbar) => {
    divMain.innerHTML = logoutView

    document
        .getElementById('buttonYes')
        .addEventListener('click', yesHandler)

    document
        .getElementById('buttonNo')
        .addEventListener('click', noHandler)

    async function yesHandler(ev){
        ev.preventDefault()
        const url = '/api/auth/logout'
        const options = {
            method : 'POST',
            headers : {
                'Content-type' : 'application/json'
            },
            credentials : 'same-origin'
        }
        try{
            const resp = await fetch(url, options)
            const body = await resp.json()
            
            if(resp.status != 200){
                throw body
            }
            await showNavbar()
            window.location.hash = '#focaIndex'
        }catch(err){
            util.showAlert(JSON.stringify(err), 'danger')
        }
        
    }
        
    function noHandler(ev){
        ev.preventDefault()
        window.location.hash = '#focaIndex'
    }
}

