import React from 'react'
import NavItem from './NavItem'

const RELS = {
    'http://api/projects': 'Projects',
    'http://api/user' : 'User'
}

const routesMap = new Map()
routesMap.set('http://api/projects', '/projects')
routesMap.set('http://api/user', '/user')

/**
 * Builds the navBar
 */
export default class NavBar extends React.Component {

    render() {
        const resources = this.props.resources
        const tabs = []
        for(const resource in resources){
           const tab = mapResourceToTab(resource)
           if(tab){
                tabs.push(tab)
           }
        }
        return(
            <div class='ui tabular menu'>
                {tabs.map(mapTabToNavItem)}
            </div>
        )
    }
}

function mapResourceToTab(resource) {
    const route = routesMap.get(resource)
    let ret = undefined
    if(route){
        const tabName = RELS[resource]
        const tab = {
            name : tabName,
            route : route
        }
        ret = tab
        return ret
    }
}

function mapTabToNavItem(tab) {
    return(
        <NavItem 
            name={tab.name}
            route={tab.route}
        />
    )
}