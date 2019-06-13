import React from 'react'
import {Route} from 'react-router-dom'
import HomePage from './HomePage';

const host = 'http://localhost:8080'


export default function (){
    return (
        <div>
            <Route 
                path='/' 
                render={props => <HomePage {...props} host={host}/>}
            />
        </div>
    )
}