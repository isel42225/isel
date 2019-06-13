import React from 'react'
import {Redirect} from 'react-router-dom'
import LabelsCheckBoxs from './LabelsCheckBoxs'
import StatesCheckBoxs from './StatesCheckBoxs'
import Button from '../ui-elems/Button';

export default class NewProjectForm extends React.Component {
    constructor(props){
        super(props)
        this.state = {
            name : '',
            description : '',
            labels : [],
            states : [],
            transitions : new Map(),
            submitted : false
        }
        this.onNameInputChange = this.onNameInputChange.bind(this)
        this.onDescriptionInputChange = this.onDescriptionInputChange.bind(this)
        this.onLabelChange = this.onLabelChange.bind(this)
        this.onStateChange = this.onStateChange.bind(this)
        this.onTransitionChange = this.onTransitionChange.bind(this)
        this.submitNewProject = this.submitNewProject.bind(this)
    }

    onNameInputChange(e){
        e.preventDefault()
        const name = e.target.value
        this.setState({
            name : name
        })
    }

    onDescriptionInputChange(e){
        e.preventDefault()
        const description = e.target.value
        this.setState({
            description : description
        })
    }

    onLabelChange(e, label){
        
        const isCheck = e.target.checked
        const labels = this.state.labels
        if(isCheck){
            this.setState({
                labels : labels.concat(label)
            })
        }
        else{
            this.setState({
                labels : labels.filter(l => l !== label) //remove unchecked label
            })
        }
        
    }

    onStateChange(e, state){
        const isCheck = e.target.checked
        const states = this.state.states
        
        if(isCheck) {
            this.setState({
                states : states.concat(state)
            })
        }
        else{
            this.setState({
                states : states.filter(s => s !== state)
            })
            // TODO remover as transições
        }
    }

    onTransitionChange(e, state, transition){
        const isCheck = e.target.checked
        const value = this.state.transitions.get(state)
        const stateTransitions =  value ? value : []
        const transitions = this.state.transitions 
        debugger
        
        if(isCheck) {
            this.setState({
                transitions : transitions.set(state, stateTransitions.concat(transition))
            })
        }
        else{
            this.setState({
                transitions : transitions.set(state, stateTransitions.filter(s => s !== state) )
            })
        }
    }

    async submitNewProject(e){
        e.preventDefault()
        const url = `${this.props.host}/project/`
        const token = this.props.token
        const transitions = {}

        for(const entry of this.state.transitions.entries()){
            transitions[entry[0]] = entry[1]
        }
        const project = {
            name : this.state.name,
            description : this.state.description,
            labels : this.state.labels,
            states : this.state.states,
            transitions : transitions
        }
        const options = {
            method : 'PUT',
            headers : {
                'Authorization' : `Basic ${token}`,
                'Content-Type' : 'application/json'
            },
            body : JSON.stringify(project)
        }
        debugger
        const res = await fetch(url, options)
        if(res.status === 201){
            const json = await res.json()
            this.props.onNewProject(json)
            this.setState({
                submitted : true
            })
        }
    }

    render() {
        const submitted = this.state.submitted
        if(submitted){
            return(
                <Redirect to='/projects' />
            )
        }
        const template = this.props.template.default.properties
        const name = template[0]
        const description = template[1]
        const labels = template[2].value
        const states = template[3].value
        
        return(
            <div class = 'ui container'>
                <div class='ui grid'>
                        <form class='ui form'>
                            <div class='inline field'>
                                <label>{name.name}</label>
                                <input type='text' onChange={this.onNameInputChange}/>
                            </div>
                            <div class='inline field'>
                                <label>{description.name}</label>
                                <input type='text' onChange={this.onDescriptionInputChange}/>
                            </div>
                            <LabelsCheckBoxs labels={labels} onChange={this.onLabelChange} />
                            <StatesCheckBoxs states={states} onStateChange={this.onStateChange} onTransitionChange={this.onTransitionChange} />
                            <div class='right floated four wide column'>
                                <Button content='Submit new project' onClick={this.submitNewProject} />
                            </div>
                            
                    </form>
                </div>
        </div>
        )
    }



}