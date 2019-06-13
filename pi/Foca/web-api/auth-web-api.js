'use strict'

const passport = require('passport')
const auth = require('./../lib/auth.js')


module.exports = (app, es) => {
    const authService = auth.init(es)
	
    passport.serializeUser((user, done) => done(null, user._id))
    passport.deserializeUser((userId, done) => authService
        .getUser(userId)
        .then(user => done(null, user))
        .catch(err => done(err))
    )
    app.use(passport.initialize())
    app.use(passport.session())

    app.get('/api/auth/session', getSession)
    app.post('/api/auth/login', login)
    app.post('/api/auth/logout', logout)
    app.post('/api/auth/signup', signup)

    // eslint-disable-next-line no-unused-vars
    function getSession(req, resp, next) {
        const fullname = req.isAuthenticated() ? req.user.fullname : undefined
        resp.json({
            'auth': req.isAuthenticated(), // <=> req.user != undefined
            'fullname': fullname
        })
    }
	
    function login(req, resp, next) {
        authService
            .authenticate(req.body.username, req.body.password)
            .then(user => {
                req.login(user, (err) => {
                    if(err) next(err)
                    else resp.json(user)
                })
            })
            .catch(err => next(err))
    }
    function logout(req, resp, next) {
        req.logout()
        resp.json({
            'success':true
        })
    }
    function signup(req, resp, next) {
        authService
            .createUser(req.body.fullname, req.body.username, req.body.password)
            .then(user => {
                req.login(user, (err) => {
                    if(err) next(err)
                    else resp.json(user)
                })
            })
            .catch(next)
    }
}