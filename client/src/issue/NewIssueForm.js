import React from 'react'
import LabelCheckBoxList from './../project/LabelsCheckBoxs'
import Button from '../ui-elems/Button';

export default class NewIssueForm extends React.Component {
    constructor(props) {
        super(props)
        this.state = {
            name : '',
            description : '',
            labels : []
        }
        this.onNameInputChange = this.onNameInputChange.bind(this)
        this.onDescriptionInputChange = this.onDescriptionInputChange.bind(this)
        this.onLabelChange = this.onLabelChange.bind(this)
        this.submitNewIssue = this.submitNewIssue.bind(this)
    }

    onNameInputChange(e) {
        e.preventDefault()
        const name = e.target.value
        this.setState({
            name : name
        })
    }

    onDescriptionInputChange(e) {
        e.preventDefault()
        const description = e.target.value
        this.setState({
            description : description
        })
    }

    onLabelChange(e, label) {
        const isChecked = e.target.isChecked
        const labels = this.state.labels
        if(isChecked){
            this.setState({
                labels : labels.concat(label)
            })
        }
        else{
            this.setState({
                labels : labels.filter(l => l !== label)
            })
        }
    }

    async submitNewIssue(e) {
        e.preventDefault()
        const name = this.state.name
        const description = this.state.description
        const labels = this.state.labels
        const project = this.props.match.params.projectId
        const url = `${this.props.host}/project/${project}/issues`
        const issue = {
            name : name,
            description : description,
            project : project,
            labels : labels
        }
        const options = {
            method : 'POST',
            headers : {
                'Authorization' : `Basic ${this.props.token}`,
                'Content-Type' : 'application/json'
            },
            body : JSON.stringify(issue)
        }
        const resp = await fetch(url, options)
    }

    render() {
        return(
            <div>
                <form class='ui form'>
                    <div class='inline field'>
                        <label>Name</label>
                        <input type='text' onChange={this.onNameInputChange}/>
                    </div>
                    <div class='inline field'>
                        <label>Description</label>
                        <input type='text' onChange={this.onDescriptionInputChange}/>
                    </div>
                    <LabelCheckBoxList labels={this.props.labels} onChange={this.onLabelChange} />
                </form>
                <Button content='Submit' onClick={this.submitNewIssue} />
            </div>
        )
    }
}