import React, {useState} from 'react'
import Button from '../ui-elems/Button';

export default function NewCommentForm(props) {
    const onSubmit = props.onSubmit
    const [text, setText] = useState('')
    return(
        <form class='ui form'>
            <div class='field'>
                <div class="ui input">
                    <input type="text" placeholder='Leave a comment...' onChange={(e) => {setText(e.target.value)}}/>
                </div>
                <Button content='Comment' onClick={(e) => onSubmit(e, text)} />
            </div>
        </form>
    )
}