'use strict'

class Auth {
    static init(es) {
        return new Auth(es)
    }

    /**
     * @param {{host: string, port: number}} es 
     */
    constructor(es){
        this.users = {}
        this.count = 0
    }

    createUser(fullname, username, password) {
        const user = {fullname, username, password}
        const id = this.count += 1
        user._id = id
        this.users[id] = user
        return Promise.resolve(user)
    }

    getUser(userId) {
        const user = this.users[userId]
        if(!user)
            return Promise.reject({statusCode: 404, err: 'User not found!'})
        return Promise.resolve(user)
    }

    authenticate(username, password) {
        const arr = Object
            .keys(this.users)
            .map(key => this.users[key])
            .filter(user => user.username == username)
        if(arr.length == 0)
            throw {'statusCode': 404, 'err': 'Username not found!' }
        const user = arr[0]
        if(user.password != password)
            throw {'statusCode': 401, 'err': 'Wrong credentials!' }
        return user._id
    }
}

module.exports = Auth