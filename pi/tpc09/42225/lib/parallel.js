'use strict'

module.exports = (tasks , cb) => {
    let results = []
    let count = 0    
    tasks.forEach((task, i) => {
        task((err , data) => {
            if(err)
                return cb(err)
        
            results[i] = data
            count++
            if(count === tasks.length)
                cb(null, results)
        })
    })
}