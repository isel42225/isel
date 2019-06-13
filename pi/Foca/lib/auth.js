'use strict'

const rp = require('request-promise')

class Auth {
    static init(es) {
        return new Auth(es)
    }

    /**
     * @param {{host: string, port: number}} es 
     */
    constructor(es){
        this.usersRefresh = `http://${es.host}:${es.port}/users/_refresh`
        this.usersUrl = `http://${es.host}:${es.port}/users/user`
    }

    async createUser(fullname, username, password) {
        const user = {fullname, username, password}
        const options = {
            'uri': this.usersUrl,
            'json': true,
            'body': user
        }
        try{
            const res = await this.findUser(username)
            if(res.hits.hits.length != 0 ) throw {'statusCode' : 401, 'err' : 'Username already taken'}
            const resp = await rp.post(options)
            await  rp.post(this.usersRefresh)
            user._id = resp._id
            return user
        }catch(err){
            throw err
        }
    }

    getUser(userId) {
        return rp
            .get(`${this.usersUrl}/${userId}`)
            .then(body => JSON.parse(body))
            .then(obj => {return {
                '_id': obj._id,
                'fullname': obj._source.fullname,
                'username': obj._source.username,
            }})
    }
    authenticate(username, password) {
        return this.findUser(username).then(json => this.verificateCredentials(json, password) )        
    }
	
    verificateCredentials(obj, password){
        if(obj.hits.hits.length == 0) 
            throw {'statusCode': 404, 'err': 'Username not found!' }
        const first = obj.hits.hits[0]
        if(first._source.password != password) 
            throw {'statusCode': 401, 'err': 'Wrong credentials!' }
        return {
            '_id': first._id
        }
    }

    findUser(username){
        const url = `${this.usersUrl}/_search?q=username:${username}`
        return rp.get(url).then(JSON.parse)
    }
}

module.exports = Auth