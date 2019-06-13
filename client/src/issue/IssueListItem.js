import React from 'react'
import {Link} from 'react-router-dom'

export default class IssueListItem extends React.Component {

    render() {
        
        const issue = this.props.item.issue
        const projectId = issue.project
        return(
            <div>
                <p>
                    <Link to={{pathname: `/projects/${projectId}/issues/${issue.id}`}}>
                        {issue.name}
                    </Link> 
                    {issue.state}<br/>
                    {issue.createdOn}
                </p>
            </div>
        )
    }
    
}