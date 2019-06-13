import React from 'react'
import IssueListItem from './IssueListItem'

export default class IssueList extends React.Component {

    render() {
        
        const issues = this.props.list
        return(
            <div>
                <div class='segment'>
                    <div class="ui list">
                     {issues.map(i => <IssueListItem item={i}/>)}
                    </div>
                </div>
            </div>
        )
    }
}