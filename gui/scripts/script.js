/*
*Funcion para cargar las tareas
*/
function loadTask() {
    fetch("http://localhost:8080/taskManager/getTasks", {
        headers: {
            "Accept": "application/json",
            "Content-Type": "application/json"
        },
        method: "GET"
    })
    .then(function (res) {
        if (!res.ok) {
            throw new Error('Error en la solicitud: ' + res.status);
        }
        return res.json();  
    })
    .then(function (data) {
        console.log(data);
        let taskList = [];
        let taskhtml = '';
        for(let i=0; i<data.length;i++){
            const task = data[i];
            let isCompleted = task.isCompleted ? 'COMPLETED' : '';
            let buttonCheck = task.isCompleted 
            ? `<input type="checkbox" class="task-checkbox" onclick="disabledButton('${task.id}')" checked disabled />` 
            : `<input type="checkbox" class="task-checkbox" onclick="disabledButton('${task.id}')" />`;

            taskhtml+= `
            <div class="task ${isCompleted}">
                ${buttonCheck}
                <h2>${task.name}</h2>   
                <p>${task.description}</p>
                <p>Creation date: ${task.creationDate}</p>
                <p>Due date: ${task.dueDate}</p>
                <button class="delete-button" onclick="deleteTask('${task.id}')"><i class="fas fa-trash-alt"></i></button>
            </div>` 
        }
        document.getElementById("task-container").innerHTML = taskhtml;
    })
    .catch(function (error) {
        console.log('Error:', error);
    });
}

/*
*Funcion para añadir una tarea
*/
function addTask(){
    const taskName = document.getElementById("taskTitle").value;
    const description = document.getElementById("taskDescription").value;
    const date = document.getElementById("taskDueDate").value;
    if (!taskName || !description || !date) {
        alert('Please fill all the fields');
        return;
    }
    if(taskName.length > 30){
        alert('The title is too long');
        return;
    }
    if (description.length > 50) {
        alert('The description is too long');
        return;
    }
   
    let currentDate = new Date();
    currentDate.setHours(0, 0, 0, 0);
    let selectedDate = new Date(date);
    if (selectedDate < currentDate) {
        alert('The due date must be greater than the current date');
        return;
    };   
    fetch("http://localhost:8080/taskManager/saveTask",
        {
            headers: {
                "Accept": "application/json",
                "Content-Type": "application/json"
            },
            method: "POST",
            body: JSON.stringify({
                name: taskName,
                description: description,
                dueDate: date
            })
        })
        .then(function (res) { console.log(res); loadTask(); })
        .catch(function (res) { console.log(res) })
        loadTask();
    }
    /*
    *Funcion para eliminar tareas
    */
    function deleteTask(taskId) {
        fetch(`http://localhost:8080/taskManager/delete?id=${taskId}`, {
            method: 'DELETE',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        })
        .then(function (res) {
            if (res.ok) {
                console.log('Task deleted successfully');
                loadTask();
            } else {
                console.log('Failed to delete task');
            }
        })
        .catch(function (error) {
            console.log('Error:', error);
        });
    }
    /*
    *Funcion para marcar una tarea como completada
    */
    function disabledButton(id){
        console.log(id);
        fetch(`http://localhost:8080/taskManager/markTaskAsCompleted?id=${id}`, {
            method: 'PATCH',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        })
        .then(function (res) {
            if (res.ok) {
                loadTask();
            } else {
                console.log('Failed to delete task');
            }
        })
        .catch(function (error) {
            console.log('Error:', error);
        });
    }
    
    
    window.addTask = addTask;
    window.deleteTask = deleteTask;
    $(document).ready(function () {
        loadTask();
    });

