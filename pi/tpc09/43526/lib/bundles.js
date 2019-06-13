'use strict'

const request = require('request')
const parallel = require('./parallel')

class Bundles {

    constructor(es) {
        this.urlBooks =  `http://${es.host}:${es.port}/${es.books_index}/book/`
        this.urlBundles = `http://${es.host}:${es.port}/${es.bundles_index}/bundle/`
    }

    static init(es) {
        return new Bundles(es)    
    }

    get(id, cb) {
        const url = `${this.urlBundles}${id}`
        request.get(url, (err, res, body) => {
            if(!checkError(200, cb, err, res, body))
                cb(null, JSON.parse(body)._source)
        })
    }
    delete(id, cb) {
        const url = `${this.urlBundles}${id}`
        request.delete(url, (err, res, body) => {
            if(!checkError(200, cb, err, res, body))
                cb(null, JSON.parse(body))
        })

    }
    create(name, cb) {
        const options = {
            url: `${this.urlBundles}`,
            json: true,
            body: { 'name': name, 'books': [] }
        }
        request.post(options, (err, res, body) => {
            if(!checkError(201, cb, err, res, body))
                cb(null, body)
        })
    }
    addBook(id, pgid, cb){
        const url = `${this.urlBooks}${pgid}`
    
        const requests = [ 
            cb => this.get(id, (err,data) => {cb(err, data)}) , 
            cb => request.get(url,(err, res, body) => {
                if(!checkError(200,cb,err, res, body))
                    cb(null, body)})
        ]
        
        parallel(requests, (err , results) => {
            if(err)
                throw err
            
            const bundle = results[0]
            const book = JSON.parse(results[1])._source
            const idx = bundle.books.findIndex(b => b.id == pgid)
            if(idx >= 0) 
                return cb(null)
            bundle.books.push({
                'id': pgid,
                'title': book.title
            })
            const options = {
                'url': `${this.urlBundles}${id}`,
                'json': true,
                'body': bundle
            }
            request.put(options, (err, res, body) => {
                if(!checkError(200, cb, err, res, body)) {
                    cb(null, body)
                }
            })
        })
    }
}

function checkError(statusCode, cb, err, res, body) {
    if(err) {
        cb(err)
        return true
    }
    if(res.statusCode != statusCode) {
        cb({
            code: res.statusCode,
            message: res.statusMessage,
            error: body
        })
        return true
    }
    return false
}

module.exports = Bundles