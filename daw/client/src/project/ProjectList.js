import React from 'react'
import ProjectListItem from './ProjectListItem';

export default class ProjectList extends React.Component {
    render() {
        
        const items = this.props.list
        return(
            <div>
            <div class='segment'>
                <div class="ui list">
                    {items.map(i => <ProjectListItem item={i.project}/>)}
                </div>
            </div>
        </div>
        )
    }
}
