'use strict'
const Handlebars = require('handlebars/dist/handlebars')
const template = require('../views/alert.hbs')
const alertResult = Handlebars.compile(template)

module.exports ={
    showAlert
} 

function showAlert(message, type = 'danger') {
    document
        .getElementById('divAlerts')
        .innerHTML = alertResult({type, message})
}