'use strict'

const http = require('http')
const express = require('express')
const expressSession = require('express-session')
const morgan = require('morgan')
const webPack = require('webpack')
const webPackMiddleWare = require('webpack-dev-middleware')
const webPackConfig = require('./webpack.config.js')

const fs = require('fs')
const token = fs.readFileSync('token.txt', 'utf8')
const fbData = require('./data/football-data').init(token)
const focaWebApi = require('./web-api/foca-web-api')
const authWebApi = require('./web-api/auth-web-api')

const es = {
    host: 'localhost',
    port: '9200'
}
const focaDb = require('./data/foca-db').init(es)

const app = express()
/**
 * middlewares
 */
app.use(morgan('dev'))
app.use(express.json()) // add body property to request
app.use(expressSession({secret: 'keyboard cat', resave: false, saveUninitialized: true }))
app.use(webPackMiddleWare(webPack(webPackConfig)))   // webpack middleware to make development smoother

const service = require('./services/foca-service').init(fbData, focaDb)

// add routes to server
authWebApi(app, es)
focaWebApi(app, service)

http
    .createServer(app)
    .listen(3000, () => {
        console.log('Server listening on port 3000')
    })
