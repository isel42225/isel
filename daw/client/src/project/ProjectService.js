
export default class ProjectService {
    constructor(host, token){
        this.baseUrl = `${host}/project/`
        this.authHeader = {'Authorization' : `Basic ${token}`}
    }

    fetchProjects(){
       return fetch(this.baseUrl, {headers : this.authHeader}).then(resp => resp.json())
    }
}