'use strict'

module.exports = (app, service) => {
    if(!service) 
        throw new Error('You must provide a service instance as second argument')
    app.get('/api/foca/leagues', getLeagues)
    app.get('/api/foca/leagues/:id/teams', getTeamsOfLeague)
    app.post('/api/foca/myteams', createGroup)
    app.get('/api/foca/myteams', getUserGroups)
    app.get('/api/foca/myteams/:id', getGroup)
    app.put('/api/foca/myteams/:id', editGroup)
    app.delete('/api/foca/myteams/:id', deleteGroup)
    app.put('/api/foca/myteams/:gid/team/:tid', addTeamGroup)
    app.delete('/api/foca/myteams/:gid/team/:tid', deleteTeamGroup)
    app.get('/api/foca/myteams/:id/matches', matchesOfGroup)
    app.use(resourceNotFound)
    app.use(errorHandler)

    /**
    * function used to get all the leagues
    * @param {parameter representing the request from the client} req 
    * @param {parameter representing the response that will be sent to the client} res 
    * @param {parameter representing the next method to be called} next
    */
    function getLeagues(req, res, next){
        service
            .getLeagues()
            .then(data => 
                res.status(200).json(data))
            .catch(err => next(err))
    }

    /**
    * function to get all teams in the league
    * @param {parameter representing the request from the client} req 
    * @param {parameter representing the response that will be sent to the client} res 
    */
    function getTeamsOfLeague(req, res, next) {
        const id = req.params.id
        service
            .getTeamsOfLeague(id)
            .then(data => res.status(200).json(data))
            .catch(err => next(err))
    }

    function createGroup(req, res, next){
        if(!req.user || !req.user._id)
            next({'statusCode': 401, err: 'Unauthenticated user cannot create bundles!'})
        const userId = req.user._id
        const {name, description} = req.body
        service
            .createGroup(userId, name, description)
            .then(data => 
                res.status(201).json(data))
            .catch(err => next(err))
    }

    // eslint-disable-next-line no-unused-vars
    function getGroups(req, res, next){
        service
            .getGroups()
            .then(data => 
                res.json(data))
            .catch(next)
    }

    function getUserGroups(req, res, next){
        const userId = req.user ? req.user._id : undefined
        service
            .getUserGroups(userId)
            .then(groups => res.json(groups))
            .catch(next)
    }

    function getGroup(req, res, next){
        const id = req.params.id
        service
            .getGroup(id)
            .then(data => res.status(200).json(data))
            .catch(err => next(err))
    }

    function editGroup(req, res, next){
        const id = req.params.id
        const {name, description} = req.body
        service
            .editGroup(id, name, description)
            .then(data => res.status(200).json(data))
            .catch(err => next(err))
    }

    function deleteGroup(req, res , next) {
        const id = req.params.id
        service
            .deleteGroup(id)
            .then(data => res.status(200).json(data))
            .catch(err => next(err))
    }

    function addTeamGroup(req, res, next){
            
        const {tid, gid} =  req.params
    
        service
            .addTeamGroup(gid, tid)
            .then(data => res.status(201).json(data))
            .catch(err => next(err)) 
    }

    function deleteTeamGroup(req, res, next){
        
        const {tid, gid} =  req.params
    
        service
            .deleteTeamGroup(gid, tid)
            .then(data => res.status(200).json(data))
            .catch(err => next(err))
    }

    function matchesOfGroup(req, res, next){    
        const {from , to} = req.query
        const id = req.params.id
        service
            .matchesOfGroup(id, from, to)
            .then(data => res.status(200).json(data))
            .catch(err => next(err))
    }

    function resourceNotFound(req, res, next){
        next({
            'statusCode': 404,
            'message': 'Resource Not Found'
        })
    }

    // eslint-disable-next-line no-unused-vars
    function errorHandler(err, req, res, next) {
        res
            .status(err.statusCode)
            .json(err)
    }

}

