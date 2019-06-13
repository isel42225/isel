import React from 'react'
import {Link} from 'react-router-dom'

export default class NavItem extends React.Component {

    render() {
        const name = this.props.name
        const route = this.props.route
        return(
            <div class="item" data-tab="tab-name">
                <Link to={route}>{name}</Link>
            </div>
        )
    }
}