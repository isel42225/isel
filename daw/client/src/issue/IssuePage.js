import React from 'react'
import CommentList from './CommentList'
import NewCommentForm from './NewCommentForm'
import Dropdown from './Dropdown';
import LabelsCheckBoxs from '../project/LabelsCheckBoxs';


const RELS = {
    comments : 'http://api/issue/comments'
}

const FetchStates = {
    loading : 'loading',
    loaded : 'loaded'
}

export default class IssuePage extends React.Component {
    constructor(props){
        super(props)
        const {projectId, issueId} = props.match.params
        this.issueURI = `${this.props.host}/project/${projectId}/issues/${issueId}`
        this.state = {
            fetchState : FetchStates.loading,
            comments : []
        }
        this.submitNewComment = this.submitNewComment.bind(this)
        this.onStateChange = this.onStateChange.bind(this)
    }

    async componentDidMount() {
        try {
            const authHeader = {'Authorization' : `Basic ${this.props.token}`}
            const json = await fetch(this.issueURI, {headers : authHeader}).then(resp => resp.json())
            const issue = json.issue
            const comments = json._embedded['Comments']
            this.setState({
                fetchState : FetchStates.loaded,
                issue : issue,
                currentState : issue.state,
                currentLabels : issue.labels,
                comments : comments
            })
        }catch(e){
            alert(e)
        }
    }

    async submitNewComment(e, text){
        try{
            e.preventDefault()
            const url = `${this.issueURI}/comments`
            const newComment = {
                text : text
            }
            const options = {
                method : 'POST',
                headers : {
                    'Authorization' : `Basic ${this.props.token}`,
                    'Content-Type' : 'application/json'
                },
                body : JSON.stringify(newComment)
            }
            const resp = await fetch(url, options)
            if(resp.status !== 201){
                const error = await resp.json()
                debugger
                alert(error)
            }
        }catch(e){
            debugger
            alert(e)
        }
        
    }

    async onStateChange(e, state) {
        try{
            e.preventDefault()
            const url = `${this.issueURI}/${state}`
            const issue = this.state.issue
            const options = {
                method : 'PUT',
                headers : {
                    'Authorization' : `Basic ${this.props.token}`,
                    'Content-Type' : 'application/json'
                },
                body : JSON.stringify(issue)
            }
            const json = await fetch(url, options)
            this.setState({
                currentState : state
            })
        }catch(e){
            alert(e)
        }
    }

    renderContent() {
        const fetchState = this.state.fetchState
        switch(fetchState) {
            case FetchStates.loading : return this.renderLoading()
            case FetchStates.loaded : return this.renderLoaded()
        }
    }

    renderLoading() {
        return(
            <div>Loading...</div>
        )
    }

    renderLoaded() {
        const name = this.state.issue.name
        const currentState = this.state.currentState
        const comments = this.state.comments.comments
        const transitions = this.props.project.transitions[currentState]
        debugger
        return(
            <div>
                <h2 class='ui header'>{name}</h2>
                <Dropdown initial={currentState} transitions={transitions} onClick={this.onStateChange}/>
                <CommentList list={comments} />
                {currentState !== 'ARCHIVED' &&
                    <NewCommentForm onSubmit={this.submitNewComment} />
                }
            </div>   
        )
    }


    render() {
        return(
            <div class='ui container'>
                {this.renderContent()}
            </div>
        )
    }
}