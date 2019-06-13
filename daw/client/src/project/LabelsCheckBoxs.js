import React from 'react'

export default class LabelsCheckBoxs extends React.Component {
    
    buildLabelCheckBox(name, onChange) {
        //debugger
        return(
            <div key={name}>
                <input type='checkbox' id={name} name='label' onChange={(e) => onChange(e, name)}/>
                <label for={name}>{name}</label>
            </div>
        )
    }

    render() {
        
        const labels = this.props.labels
        const onChange = this.props.onChange
        return(
            <div>
                <div>
                    <h3 class='ui header'>Labels</h3>
                </div>
                {labels.map(l => this.buildLabelCheckBox(l, onChange))}
            </div>
        )
    }
}