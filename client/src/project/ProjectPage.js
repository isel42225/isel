import React from 'react'
import {Switch,Link, Route} from 'react-router-dom'
import IssueList from './../issue/IssueList'
import NewIssueForm from './../issue/NewIssueForm'
import IssuePage from './../issue/IssuePage'
import Button from './../ui-elems/Button'

const FetchStates = {
    loading : 'loading',
    loaded : 'loaded'
}

export default class SingleProjectPage extends React.Component {
    constructor(props){
        super(props)
        this.id = this.props.match.params.projectId
        this.projectURI = `${props.host}/project/${this.id}`
        this.projectIssuesURI = `${this.projectURI}/issues`
        this.state = {
            fetchState : FetchStates.loading,
            issues : [],
            project : {}
        }
        this.onNewIssue = this.onNewIssue.bind(this)
    }

    async componentDidMount() {
        try {
            const authHeader = {'Authorization' : `Basic ${this.props.token}`}
            const projectPromise = fetch(this.projectURI, {headers : authHeader}).then(resp => resp.json())
            const issuesPromise =  fetch(this.projectIssuesURI, {headers : authHeader}).then(resp => resp.json())
            const [projectResponse, issuesResponse] = await Promise.all([projectPromise, issuesPromise])
            
            this.setState( {
                fetchState : FetchStates.loaded,
                issues : issuesResponse.issues,
                project : projectResponse.project
            })
        }catch(e){
            alert(e)
        }
    }

    onNewIssue(issue){

    }

    renderContent() {
        const fetchState = this.state.fetchState
        switch(fetchState) {
            case FetchStates.loading : return this.renderLoading()
            case FetchStates.loaded : return this.renderLoaded()
        }
    }

    renderLoading(){
        return (
                <div>Loading...</div>
            )
    }

    renderLoaded() {
        
        const issues = this.state.issues
        const labels = this.state.project.labels
        const isExactRoute = this.props.match.isExact
        const host = this.props.host
        if(isExactRoute) {
            return (
                <div>
                    <div class='ui sizer vertical segment'>
                        <div class='ui medium header'>Issues</div>
                        <IssueList list={issues} />
                        <Link to={{pathname : `${this.id}/issues/new`}}>
                            <Button content='New' />
                        </Link>
                    </div>
                </div>
            )
        }
        else {
            const token = this.props.token
            return(
                <Switch>
                    <Route 
                        path='/projects/:projectId/issues/new'
                        render={props => <NewIssueForm {...props} host={host} token={token} labels={labels} />}
                    />
                    <Route
                        path='/projects/:projectId/issues/:issueId'
                        render={props => <IssuePage {...props} host={host} token={token} project={this.state.project} />}
                    />
                </Switch>
            )
        }
    }

    render() {
        return (
            <div>
                {this.renderContent()}
            </div>
        )
    }
}