'use strict'

const parallel = require('./../lib/parallel')
const expect = require('chai').expect

describe('Parallel API', () => {

    it('should return a ["one","two"] !', done => {
        const tasks = [
            cb => setTimeout(() => cb(null, 'one'), 200),
            cb => setTimeout(() => cb(null, 'two'), 100)
        ]
		
        parallel(tasks,(err, results) => {
            if(err){
                expect.fail()	
            }
            expect(results).to.be.an('array').and.have.lengthOf(2)
            expect(results[0]).equals('one')
            expect(results[1]).equals('two')
            done()
        })
    })

    it('should return a error!', done => {
        const tasks = [
            cb => setTimeout(() => cb(Error('One cb failed'), null), 200),
            cb => setTimeout(() => cb(null, null), 100)
        ]
		
        parallel(tasks,(err)=> {
            if(!err){
                expect.fail()	
            }
            expect(err)
                .to.be.an('error')
                .and.have.a.property('message','One cb failed' )
            done()
        })
    })
})