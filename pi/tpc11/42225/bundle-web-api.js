'use strict'

/**
 * @param {WebServer} app With a use() method to add a new route.
 * @param {Bundle} bundles Object of class Bundles with methods: create(), get(), etc
 */
module.exports = (app, bundles) => {
    if(!bundles) 
        throw new Error('You must provide a Bundles instance as second argument')
    app.post('/api/bundle', postBundle)
    app.get('/api/bundle/:bundleId', getBundle)
    app.use(resourceNotFound)
    app.use(errorHandler)

    /**
     * POST /api/bundle?name=<name> -- insere um novo bundle
     */
    function postBundle(req, res, next) {
        const name = req.query.name
        // bundles.create(name, (err, data) => {
        //     if(err) return next(err)
        //     res
        //         .status(201)
        //         .json(data)
        // })
        bundles
            .create(name)
            .then(data => res.status(201).json(data))
            .catch(err => next(err))
    }

    /**
     * GET /api/bundle/<id> -- lê um bundle com o id
     */
    function getBundle(req, res, next) {
        // bundles.get(req.params.bundleId, (err, bundle) => {
        //     if(err) return next(err)
        //     res.json(bundle)
        // })
        bundles
            .get(req.params.bundleId)
            .then(bundle => res.json(bundle))
            .catch(err => 
                next(err)) //<=> next ?
    }

    function resourceNotFound(req, res, next) {
        next({
            'statusCode': 404,
            'message': 'Resource Not Found'
        })
    }

    // next param needed for express routing recognition of error function
    // eslint-disable-next-line no-unused-vars 
    function errorHandler(err, req, res, next) {
        res.statusCode = err.statusCode
        res.json(err)
    }
}

