'use strict'

const service = require('../lib/task-service').init('http://localhost:9200/', 'tasks/task')
const expect = require('chai').expect

describe('serviceTest', () => {
    let id1, id2
    before((done) => {
        service.insert('Swimming', 'Swim workout on pool with 50 meters length', 'Olivais', (err, data) => {
            if (err) {
                expect.fail()
            }
            id1 = data
            service.insert('Study', 'Study web programming with node.js', 'ISEL or at home', (err, data) => {
                if (err) {
                    expect.fail()
                }
                id2 = data

                done()
            })
        })
    })

    describe('initTestSuit', () => {
        it('should provide valid service instance', done => {
            expect(service)
                .to.be.a('object')
                .and.have.a.property('uri', 'http://localhost:9200/tasks/task')
            done()
        })
    })

    describe('getTaskSuit', () => {
        it('should return one task', done => {
            service.getTask(id1, (err, data) => {
                if (err) {
                    expect.fail()
                }
                expect(data)
                    .to.be.an('object')
                expect(data._source)
                    .have.property('local', 'Olivais')
                done()
            })
        })

        it('should return 404 status', done => {
            service.getTask('test', (err) => {
                if (!err) {
                    expect.fail()
                }
                expect(err)
                    .to.be.an('error')
                    .and.have.a.property('message', '404 : Not Found')
                done()
            })
        })
    })

    describe('insertTestSuit', () => {
        it('should insert new Task', done => {
            service.insert('TestTitle', 'TestDescription', 'TestLocal', (err, id) => {
                if (err) {
                    expect.fail()
                }
                service.getTask(id, (err, data) => {
                    if (err) {
                        expect.fail()
                    }
                    expect(data)
                        .to.be.an('object')
                        .and.have.property('_id', id)

                    service.delete(id, () => { done() })
                })
            })
        })
    })

    describe('findByPropSuit', () => {

        it('should return array with all tasks', done => {
            service.findBy('_index', 'tasks', (err, data) => {
                if (err) {
                    expect.fail()
                }
                data.forEach(element => {
                    expect(element)
                        .to.be.an('object')
                })
                done()
            })
        })

        it('should return array only 1 length', done => {
            service.findBy('_id', id1, (err, data) => {
                if (err) {
                    expect.fail()
                }
                expect(data)
                    .to.be.an('array')
                    .and.to.have.length(1)
                done()
            })
        })

        it('should return empty array ', done => {
            service.findBy('foo', 'bar', (err, data) => {
                if (err) {
                    expect.fail()
                }

                expect(data)
                    .to.be.an('array')
                    .and.to.have.length(0)
                done()
            })
        })

    })

    describe('updateTestSuit', () => {

        it('should update newly created task', done => {

            service.getTask(id1, (get_err, task) => {
                if (get_err) {
                    expect.fail()
                }
                expect(task._source)
                    .to.have.a.property('local', 'Olivais')

                task._source.local = 'ISD'

                service.update(task, (upd_err, updatedTask) => {
                    if (upd_err) {
                        expect.fail()
                    }
                    expect(updatedTask)
                        .to.be.an('object')
                        .and.have.a.property('result', 'updated')
                    done()                    
                })
            })
        })
    })

    describe('deleteTestSuit', () => {

        it('should not delete anything', done => {
            service.delete('test', (err) => {
                expect(err)
                    .to.be.an('error')
                    .and.have.a.property('message', '404 : Not Found')
                done()
            })
        })

        it('should delete newly inserted task', done => {
            service.insert('TestA', 'TestB', 'ISEL', (err, id) => {
                if (err) {
                    expect.fail()
                }
                service.delete(id, (err, data) => {
                    if (err) {
                        expect.fail()
                    }

                    expect(data)
                        .to.be.an('object')
                        .and.have.a.property('result', 'deleted')

                    expect(data)
                        .to.have.a.property('_id', id)
                    done()
                })
            })
        })
    })
    
    after((done) => {
        service.delete(id1, () => { service.delete(id2, () => { done() }) })
    })
})