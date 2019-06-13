import React from 'react'

export default function CommentList(props) {
    const list = props.list
    return(
        <div>
            <h2 class='ui header'>Comments</h2>
            {list.map(c => {
                return(
                    <div>
                        {c.text}
                    </div>
                )
            })}
        </div>
    )
}