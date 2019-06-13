'use strict'

module.exports = (tasks , callback) => {
    let results = [] , count = 0
    const length = tasks.length    
    for(let i = 0; i < length; i++) { 
        tasks[i]((err , data) => {
            if(err){ 
                return callback(err)
            }
            results[i] = data
            if(++count === length){
                return callback(null, results)
            }
        })
    }
}