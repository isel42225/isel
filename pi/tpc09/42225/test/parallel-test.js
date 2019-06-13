'use strict'
const parallel = require('./../lib/parallel')
const expect = require('chai').expect

describe('Parallel', () => {

    it('should return results in correct order', done => {

        const tasks = [
            cb => setTimeout(() => cb(null, 'one'), 200),
            cb => setTimeout(() => cb(null, 'two'), 100)
        ]
        
        parallel(tasks,(err, results) => {
            const first = results[0]
            const second = results[1]

            expect(first).to.equal(first, 'one')
            expect(second).to.equal(second, 'two') 
            done()
        }) 
    })
})