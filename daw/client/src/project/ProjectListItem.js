import React from 'react'
import {Link} from 'react-router-dom'

export default class ProjectListItem extends React.Component {

    render() {
        const {name, description} = this.props.item
        const link = {
            pathname : `/projects/${name}`
        }
        return (
            <div class='item'>
                <div class='content'>
                    <Link to={link}>
                        <h1>{name}</h1>
                    </Link>
                    <p>{description}</p>
                </div>
            </div>
        )
        
    }
}