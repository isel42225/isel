'use strict'

module.exports = errorHandler

function errorHandler(err) {
    return Promise.reject({
        statusCode: err.statusCode,
        message: err.message,
        error: err
    })
}