'use strict'

class ParseFoca{

    static init(){
        return new ParseFoca()
    }

    group(group){
        const source = group._source
        return {
            'id' : group._id,
            'user': source.user,
            'name' : source.name,
            'description' : source.description,
            'teams' : source.teams
        }
    }

    simpleGroup(group){
        const source = group._source
        return {
            'id' : group._id,
            'user': source.user,
            'name' : source.name,
            'description' : source.description,
        }
    }
}

module.exports = ParseFoca


    