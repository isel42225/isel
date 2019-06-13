import React from 'react'

export default function Dropdown(props) {
    debugger
    const initial = props.initial
    const transitions = props.transitions
    if(transitions.length > 0) {
        return (
            <div>
                <div class='text'>State : {initial}</div>
                <select>
                    <Menu list={props.transitions} onClick={props.onClick}/>
                </select>
            </div>
        )
    }
    else{
        return(
            <div class='text'>{initial}</div>
        )
    }
}

function Menu(props){
    const list = props.list
    const onClick = props.onClick
    if(!list) return null
    return (
       list.map(t => <option value={t} onClick={e => onClick(e, t)}>{t}</option>) 
    )
}