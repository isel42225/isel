'use strict'

const request = require('request')

class TaskService {

    constructor (host , index){
        this.uri = host + index
    }

    getTask(id, cb){
        request.get(`${this.uri}/${id}`, (err, res, body) => {
            if(err){
                return cb(err)
            }
            if(res.statusCode != 200){
                return cb(Error(`${res.statusCode} : ${res.statusMessage}`))
            }
            return cb(null, JSON.parse(body))
        })
        
    }

    findBy(prop, value, cb){
        const url = `${this.uri}/_search?q=${prop}:${value}`
        request.get(url, (err, res, body) => {
            if(err){
                return cb(err)
            }
            if(res.statusCode != 200){
                return cb(Error(`${res.statusCode} : ${res.statusMessage}`))
            }
            return cb(null, JSON.parse(body).hits.hits)
        })
    }

    delete(id, cb){
        const url = `${this.uri}/${id}`
        request.delete(url, (err, res, body) => {
            if(err){
                return cb(err)
            }
            if(res.statusCode != 200){
                return cb(Error(`${res.statusCode} : ${res.statusMessage}`))
            } 
            return cb(null, JSON.parse(body))
        })
    }

    insert(title, description, local, cb){
        
        const ins = {
            'uri':this.uri,
            'body': 
            {
                'title' :title,
                'description': description,
                'local': local
            },
            'json': true
        }

        request.post(ins, (err ,res, body)=> {
            if (err) {
                return cb(err)
            }
            return cb(null, body._id)
        })
    }

    update(task, cb){
        
        const updt = {
            'uri': `${this.uri}/${task._id}`,
            'body':task._source,
            'json':true
        }
        request.put(updt, (err, res, body) => {
            if (err) {
                return cb(err)
            }
            if(res.statusCode != 200){
                return cb(Error(`${res.statusCode} : ${res.statusMessage}`))
            }
            else{
                return cb(null, body)
            }

        })
    }

    static init (host , index){
        return new TaskService(host , index)
    }
}

module.exports = TaskService
