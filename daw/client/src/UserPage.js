import React from 'react'
import Button from './ui-elems/Button'

export default function UserPage(props) {
    const onLogout = props.onLogout
        return(
            <Button content='Log out' onClick={onLogout} />
        )
}