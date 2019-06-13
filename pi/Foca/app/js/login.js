'use strict'

const loginView = require('./../views/login.html')
const util = require('./utils.js')

module.exports = (divMain, showNavbar) => {
    divMain.innerHTML = loginView

    const inputFullname = document.getElementById('inputFullname')
    const inputUsername = document.getElementById('inputUsername')
    const inputPassword = document.getElementById('inputPassword')

    document
        .getElementById('buttonSignup')
        .addEventListener('click', signUpHandler)

    document
        .getElementById('buttonLogin')
        .addEventListener('click', loginHandler)

    async function signUpHandler(ev){
        ev.preventDefault()
        const url = 'http://localhost:3000/api/auth/signup'
        const options = {
            method : 'POST',
            body: JSON.stringify({
                'fullname' : inputFullname.value,
                'username' : inputUsername.value,
                'password' : inputPassword.value
            }),
            headers : {
                'Content-type' : 'application/json'
            },
            credentials : 'same-origin'
        }
        try{
            const resp  = await fetch(url, options)
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

    async function loginHandler(ev){
        ev.preventDefault()
        const url = 'http://localhost:3000/api/auth/login'
        const options = {
            method : 'POST',
            body: JSON.stringify({
                'username' : inputUsername.value,
                'password' : inputPassword.value
            }),
            headers : {
                'Content-type' : 'application/json'
            },
            credentials : 'same-origin'
        }
        try{
            const resp  = await fetch(url, options)
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
}