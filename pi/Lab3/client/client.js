'use strict'

const program = require('commander')
const task_service = require('../lib/task-service').init('http://localhost:9200/', 'tasks/task')

const fullUrl = (path = '') => {
    let url = `http://${program.host}:${program.port}/`
    if (program.index) {
        url += program.index + '/'
        if (program.type) {
            url += program.type + '/'
        }
    }
    return url + path.replace(/^\/*/, '')
}

program
    .usage('[options] <command> [...]')
    .option('-o, --host <hostname>', 'hostname [localhost]', 'localhost')
    .option('-p, --port <number>', 'port number [9200]', '9200')
    .option('-j, --json', 'format output as JSON')
    .option('-i, --index <name>', 'which index to use')
    .option('-t, --type <type>', 'default type for bulk operations')

program
    .command('url [path]')
    .description('generate the URL for the options and path (default is /)')
    .action((path = '/') => console.log(fullUrl(path)))


program
    .command('listTasks ')
    .description('lists all tasks in db')
    .action(() => {
        task_service.findBy('_index', 'tasks', (err, data) => {
            if(err){
                console.error(err)
            }

            console.log(data)
        } )
    })

program
    .command('getTask <id> ')
    .description('print info for specific task')
    .action((id) => {
        task_service.getTask(id, (err, data) => {
            if(err){
                console.error(err)
            }

            console.log(data)
        } )
    })

program
    .command('insertTask <title> <description> <local> ')
    .description('insert a new task')
    .action((title, description, local) => {
        task_service.insert(title, description, local , (err, id) => {
            if(err){
                console.error(err)
            }

            console.log(id)
        } )
    })

program
    .command('deleteTask <id>')
    .description('delete a task')
    .action((id) => {
        task_service.delete(id , (err, data) => {
            if(err){
                console.error(err)
            }

            console.log(data)
        } )
    })

program
    .command('updateTask <id> <title> <description> <local> ')
    .description('update existing task')
    .action((id, title, description, local) => {
           
        const obj = {
            '_id': id,
            '_source': {
                'title': title,
                'description': description,
                'local': local
            }
        }
            
        task_service.update(obj , (err, data) => {
            if(err){
                console.error(err)
            }

            console.log(data)
        } )
    })
        
   

program.parse(process.argv)

if (!program.args.filter(arg => typeof arg === 'object').length) {
    program.help()
}