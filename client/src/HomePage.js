import React from 'react'
import NavBar from './nav/NavBar'
import {Route, Switch, Redirect} from 'react-router-dom'
import ProjectListPage from './project/ProjectListPage'
import LoginPage from './auth/LoginPage';
import UserPage from './UserPage';

/**
 * Fetch NavBar Items
 */

 const FetchStates = {
     loading : 'loading',
     loaded : 'loaded'
 }


export default class HomePage extends React.Component {
    constructor(props){
        super(props)
        this.state = {
            fetchState : FetchStates.loading,
            token : undefined
        }
        this.homeURI = `${this.props.host}/home`
        this.onAuthenticated = this.onAuthenticated.bind(this)
        this.onLogout = this.onLogout.bind(this)
    }

    async componentDidMount() {
        //await delay(3000)    
        const json = await fetch(this.homeURI).then(resp => resp.json())
        const resources = json.resources
        this.setState({
            fetchState : FetchStates.loaded,
            resources : resources
        })
    }

    onAuthenticated (token) {
        this.setState({
            token : token
        })
    }

    onLogout() {
        this.setState({
            token : undefined
        })
    }

    renderContent() {
        const fetchState = this.state.fetchState
        switch(fetchState){
            case FetchStates.loading : return this.renderLoading()
            case FetchStates.loaded : return this.renderLoaded()
            default : return () => {console.log()} // Error ?
        }
    }

    renderLoading() {
        return (
            <div>Loading...</div>
        )
    }

    renderLoaded() {
        const token = this.state.token
        const host = this.props.host
        if(!token){
            return(
                <div>
                    <LoginPage host={host} onAuthenticated={this.onAuthenticated} />
                </div>
            )
        }
        else{

        }
        return (
            <div>
                <NavBar resources={this.state.resources} />
                <Switch>
                    <Route 
                        path='/projects' 
                        render={props => <ProjectListPage {...props} host={host} token={token} />}
                    />
                    <Route 
                        path='/user' 
                        render={props => <UserPage {...props} onLogout={this.onLogout} />} 
                    />
                </Switch>
            </div>
        )
    }

    render() {
        return (
            <div>
                {this.renderContent()}
            </div>    
        )
    }
}

function delay (ms) {
    return new Promise((resolve, reject) => {
        setTimeout(() => resolve(), ms)
    })
}
    