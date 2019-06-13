import React from 'react'
import Button from '../ui-elems/Button'


/**
 * Login form that shows on root page
 * Sucessfull sign up and log in should redirect to home page 
 */
export default class LoginForm extends React.Component {
    constructor(props){
        super(props)
        this.state = {
            userInput : "",
            passwordInput : ""
        }
        this.userURI = `${props.host}/user`
    }
    
    handleOnInputChange(event){
        event.preventDefault()
        const {value, name} = event.target
        if(name === "password"){
            this.setState({
                passwordInput : value
            })
        }
        else {
            this.setState({
                userInput : value
            })
        }
    }
    
    async handleSignUp(event) {
        try{
            event.preventDefault()
            
            const username = this.state.userInput
            if(username === "") {
                alert('You must insert a username')   
            }
            
            const password = this.state.passwordInput
            if(password === ""){
                alert('You must insert a password')
            }

            const user = {
                'name' : username,
                'password' : password
            }

            const options = {
                method : 'PUT',
                body : JSON.stringify(user),
                headers : {'Content-type' : 'application/json'}
            }
            
            const token = await fetch(this.userURI, options).then(res => res.json())
            this.props.onSuccess(token)
        }catch(e){
            alert(e)
        }
        
    }

    async handleLogin(event){
        event.preventDefault()
        try{
            const username = this.state.userInput
            const password = this.state.passwordInput
            const user = {
                name : username,
                password : password
            }

            const options = {
                method : 'POST',
                body : JSON.stringify(user),
                headers : {'Content-type' : 'application/json'}
            }

            const token = await fetch(this.userURI, options).then(res => res.json())
            this.props.onSuccess(token)
        }catch(e){
            alert(e)
        }
    }
    
    render() {
        return (
            <div class = 'ui center aligned text container'>
                <form class='ui form' name='user-form'>
                    <div class='field'>
                        <label>Username</label>
                        <input type='text' 
                               name='username'
                               placeholder='Username'
                               onChange = {(event) => this.handleOnInputChange(event)}
                        />
                    </div>
                    <div class='field'>
                        <label>Password</label>
                        <input type='text'
                               name = 'password'
                               placeholder='Password'
                               onChange ={(event) => this.handleOnInputChange(event)}
                        />
                    </div>
                    <div>
                        <Button content={'Sign Up'}
                                onClick={(event) => this.handleSignUp(event)}      
                        />
                        <Button content={'Login'}
                                onClick={(event) => this.handleLogin(event)}
                        />
                        
                    </div>
            </form>
        </div>
        )
    }

}
