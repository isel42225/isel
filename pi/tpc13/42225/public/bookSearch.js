'use strict'

window.onload = () => {
    const results = 
                `
                <table class="table">
                    <thead>
                        <tr>
                            <th>Title</th>
                            <th>Authors</th>
                            <th>Subjects</th>
                        </tr>
                    </thead>
                    <tbody>
                        {{#each books}}
                            <tr>
                                <td>{{title}}</td>
                                <td>{{authors}}</td>
                                <td>{{subjects}}</td>
                            </tr>
                        {{/each}}
                    </tbody>
                </table>  
                `

    document
        .getElementById('buttonSearch')
        .addEventListener('click', bookSearch)

    const searchResultsView = Handlebars.compile(results)

    const inputTitle = document.getElementById('inputTitle')
    const inputAuthors = document.getElementById('inputAuthors')
    const divResults = document.getElementById('divResults')

    function bookSearch(ev) {
        ev.preventDefault()
        fetch(`http://localhost:3000/api/book/search?title=${inputTitle.value}&authors=${inputAuthors.value}`)
            .then(resp => resp.json())
            .then(books => searchResultsView(books))
            .then(view => divResults.innerHTML = view)
            .catch(console.log)
    }

    function searchResults(book) {
        const {title, authors, subjects} = book
        return `
            <ul class="list-group">
                <li class="list-group-item">Title: ${title}</li>
                <li class="list-group-item">Authors: ${authors}</li>
                <li class="list-group-item">Subjects: ${subjects}</li>
            </ul>
        `
    }
}
