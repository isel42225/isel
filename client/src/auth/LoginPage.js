import React from 'react'
import {Redirect} from 'react-router-dom'
import LoginForm from './LoginForm';

export default class LoginPage extends React.Component {

    constructor(props){
        super(props)
        this.state = {
            user : undefined
        }
        this.onSuccess = this.onSuccess.bind(this)
    }

    onSuccess(user){
        const token = user.user.token
        this.props.onAuthenticated(token)
        this.setState({
            user : user
        })
    }

    render() {
        const host = this.props.host
        const user = this.state.user
        //debugger
        if(user){
            return <Redirect to='/'/>
        }
        return(
            <div>
                <LoginForm 
                    host={host} 
                    onSuccess={this.onSuccess}
                />
            </div>
        )
    }
}