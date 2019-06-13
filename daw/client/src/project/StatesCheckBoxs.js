import React from 'react'

export default class StatesCheckBoxs extends React.Component {

    buildCheckbox(mapEntry){
        
        const state = mapEntry.state
        const transitions = mapEntry.canGoTo
        const id = `${state}Entry`
        const onStateChange = this.props.onStateChange
        
        return(
            <div >
                <input type='checkbox' id={id} onChange={(e) => onStateChange(e, state)} />
                <label for={id}>{state}</label>
                <div class='inline fields'>
                    <label>Can transitate to :</label>
                    {transitions.map(t => this.buildTransition(state, t))}
                </div>
            </div>
        )
    }

    buildTransition(state, transition){
        const onTransitionChange = this.props.onTransitionChange
        
        return(
            <div class='field'>
                <input type='checkbox' onChange={(e) => onTransitionChange(e,state, transition)}/>
                <label>{transition}</label>
            </div>
        )
    }


    render() {
        const states = this.props.states
        const statesMap = {}
        states.forEach( s => {
            statesMap[s] = {
                state : s,
                canGoTo : states.filter(ss => ss !== s)
            }
        })
        return(
            <div>
                <h3>States</h3>
                {states.map(s => this.buildCheckbox(statesMap[s]))}
            </div>
        )
    }
}