'use strict'

const rp = require('request-promise')

class Bundles {

    constructor(es) {
        this.urlBooks =  `http://${es.host}:${es.port}/${es.books_index}/book/`
        this.urlBundles = `http://${es.host}:${es.port}/${es.bundles_index}/bundle/`
    }

    static init(es) {
        return new Bundles(es)    
    }

    get(id) {
        return rp
            .get(`${this.urlBundles}${id}`)
            .then(body => JSON.parse(body)._source)
    }
    delete(id) {
        return rp
            .delete(`${this.urlBundles}${id}`)
            .then(body => JSON.parse(body))
    }
    create(name) {
        const options = {
            url: `${this.urlBundles}`,
            json: true,
            body: { 'name': name, 'books': [] }
        }
        return rp
            .post(options)
    }
    addBook(id, pgid){
        const promises = [
            this.get(id),
            rp.get(`${this.urlBooks}${pgid}`).then(body => JSON.parse(body)._source)
        ]
    
        return  Promise
            .all(promises)
            .then(([bundle, book]) => {
                const idx = bundle.books.findIndex(b => b.id == pgid)
                if(idx >= 0) 
                    return Promise.reject(new Error('Book already exists in bundle'))
                bundle.books.push({
                    'id': pgid,
                    'title': book.title
                })
                const options = {
                    url: `${this.urlBundles}${id}`,
                    json: true,
                    body: bundle
                }
                return rp.put(options)
            })
    }
}

// function checkError(statusCode, cb, err, res, body) {
//     if(err) {
//         cb(err)
//         return true
//     }
//     if(res.statusCode != statusCode) {
//         cb({
//             code: res.statusCode,
//             message: res.statusMessage,
//             error: body
//         })
//         return true
//     }
//     return false
// }

module.exports = Bundles