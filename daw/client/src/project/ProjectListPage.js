import React from 'react'
import {Route,Link, Switch} from 'react-router-dom'
import ProjectList from './ProjectList';
import ProjectPage from './ProjectPage'
import NewProjectForm from './NewProjectForm'
import ProjectService from './ProjectService';
import Button from '../ui-elems/Button';

const FetchStates = {
    loading : 'loading',
    loaded : 'loaded'
}

class ProjectsPage extends React.Component {
    constructor(props){
        super(props)
        this.service = new ProjectService(props.host, props.token)
        this.state = {
            fetchState : FetchStates.loading
        }
        this.onNewProject = this.onNewProject.bind(this)
    }

   async componentDidMount() {
        const resp = await this.service.fetchProjects()
        const projects = resp.projects
        const _templates = resp._templates
        this.setState({
            fetchState : FetchStates.loaded,
            projects : projects,
            _templates : _templates
        })
    }

    onNewProject(project){
        const projects = this.state.projects
        this.setState({
            projects : projects.concat(project)
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

    renderLoading(){
        return(
            <div>Loading...</div>
        )
    }

    renderLoaded(){
        const host = this.props.host
        const projects = this.state.projects
        const isExactRoute = this.props.match.isExact
        
        if(isExactRoute){
            return(
                <div>
                    <ProjectList list={projects}/>
                    <Link to='/projects/new'>
                        <Button content='New'/>
                    </Link>
                </div>
            )
        }
        else{
            const template = this.state._templates
            const token = this.props.token
            return(
                <div>
                    <Switch>
                        <Route 
                            exact path='/projects/new'
                            render={props => <NewProjectForm {...props} host={host} token={token} template={template} onNewProject={this.onNewProject}/>}
                        />
                        <Route 
                            path='/projects/:projectId' 
                            render={props => <ProjectPage {...props} host={host} token={token} />} 
                        />
                    </Switch>
                </div>
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

export default ProjectsPage