/*
*Funcion para cargar las tareas
*/
function loadTask() {
    fetch("https://taskmanagercvds-bjdmg9hwaaa7erg0.eastus-01.azurewebsites.net/taskManager/getTasks", {
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
            ? `<input type="checkbox" class="task-checkbox" onclick="disabledButton('${task.id}',this)" checked disabled />` 
            : `<input type="checkbox" class="task-checkbox" onclick="disabledButton('${task.id}',this)" />`;
            const colors = `
            ${1 === task.priority ? 'first-priority' : ''}
            ${2 === task.priority ? 'second-priority' : ''}
            ${3 === task.priority ? 'third-priority' : ''}
            ${4 === task.priority ? 'four-priority' : ''}
            ${5 === task.priority ? 'five-priority' : ''}
            `;

            taskhtml+= `
            <div class="task ${isCompleted}">
                ${buttonCheck}
                <div class="task-priority">
                    <div class="circle ${colors}"><span>${task.priority}</span></div>
                    <h2>${task.name}</h2> 
                </div>  
                <p style="opacity: 0.8;"> ${task.description}</p>
                <p style="opacity: 0.8;"><i class="fas fa-calendar-alt"></i> Creation date: ${task.creationDate}</p>
                <p style="opacity: 0.8;"><i class="fas fa-calendar-check"></i> Due date: ${task.dueDate}</p>
                <p style="opacity: 0.8;"><i class="fas fa-exclamation-circle"></i> Difficulty: ${task.difficulty}</p>
                <p style="opacity: 0.8;"><i class="fas fa-clock"></i> Estimated Time: ${task.estimatedTime.toFixed(1)} hours</p>
                <button class="delete-button" onclick="deleteTask('${task.id}',this)"><i class="fas fa-trash-alt"></i></button>
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
    const difficultyTask = document.querySelector('input[name="taskDifficulty"]:checked').value;
    const priorityTask = document.getElementById("taskPriority").value;
    const time = document.getElementById("averageTime").value;
    if (!taskName || !description || !date || !difficultyTask || !priorityTask || !time) {
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
    if (time < 0){
        alert('You cannot add negative time');
        return;
   }
    let currentDate = new Date();
    currentDate.setHours(0, 0, 0, 0);
    let selectedDate = new Date(date);
    if (selectedDate < currentDate) {
        alert('The due date must be greater than the current date');
        return;
    }; 
    fetch("https://taskmanagercvds-bjdmg9hwaaa7erg0.eastus-01.azurewebsites.net/taskManager/saveTask",
        {
            headers: {
                "Accept": "application/json",
                "Content-Type": "application/json"
            },
            method: "POST",
            body: JSON.stringify({
                name: taskName,
                description: description,
                dueDate: date,
                difficulty: difficultyTask,
                priority: priorityTask,
                estimatedTime: time
            })
        })
        .then(function (res) { console.log(res); loadTask(); })
        .catch(function (res) { console.log(res) })
        //loadTask();
    }
    /*
    *Funcion para eliminar tareas
    */
    function deleteTask(taskId,button) {
        fetch(`https://taskmanagercvds-bjdmg9hwaaa7erg0.eastus-01.azurewebsites.net/taskManager/delete?id=${taskId}`, {
            method: 'DELETE',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        })
        .then(function (res) {
            if (res.ok) {
                button.parentElement.remove();
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
    function disabledButton(id,button){
        console.log(id);
        fetch(`https://taskmanagercvds-bjdmg9hwaaa7erg0.eastus-01.azurewebsites.net/taskManager/markTaskAsCompleted?id=${id}`, {
            method: 'PATCH',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        })
        .then(function (res) {
            if (res.ok) {
                const parent = button.parentElement;
                button.checked = true;
                button. disabled = true;
                parent.setAttribute('style', 'background-color: #1f877e');
                parent.querySelector('.task-priority').querySelector('h2').setAttribute('style','text-decoration: line-through');

            } else {
                console.log('Failed to delete task');
            }
        })
        .catch(function (error) {
            console.log('Error:', error);
        });
    }
    function generateRandomTasks(){
        fetch(`https://taskmanagercvds-bjdmg9hwaaa7erg0.eastus-01.azurewebsites.net/taskManager/generateTasksss`, {
            method: 'POST',
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
    
    $(document).ready(function () {
        loadTask();
    });

