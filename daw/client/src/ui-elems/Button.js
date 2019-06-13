import React from 'react'

function Button(props) {
    return(
            <button 
                class = "ui button"
                onClick = {props.onClick}
            >
                {props.content}
            </button>
        )
}

export default Button