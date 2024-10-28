// Escapar HTML para evitar inyecciones de código en los datos de la tarea
function escapeHtml(unsafe) {
    return unsafe
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;");
}

// Cargar todas las tareas y renderizarlas en el contenedor de tareas
function loadTask() {
    fetch("https://localhost:443/taskManager/getTasks", {
        headers: {
            "Accept": "application/json",
            "Content-Type": "application/json"
        },
        method: "GET"
    })
    .then(res => {
        if (!res.ok) throw new Error('Error en la solicitud: ' + res.status);
        return res.json();  
    })
    .then(data => {
        const taskContainer = document.getElementById("task-container");
        taskContainer.innerHTML = ''; 

        data.forEach(task => {
            const isCompleted = task.isCompleted ? 'COMPLETED' : '';
            const buttonCheck = `<input type="checkbox" class="task-checkbox" 
                                    onclick="disabledButton('${task.id}', this)" 
                                    ${task.isCompleted ? 'checked disabled' : ''} />`;

            // Clase CSS de prioridad según el nivel
            const priorityClasses = ['first-priority', 'second-priority', 'third-priority', 'fourth-priority', 'fifth-priority'];
            const taskPriorityClass = priorityClasses[task.priority - 1] || '';

            const taskDiv = document.createElement('div');
            taskDiv.className = `task ${isCompleted}`;
            taskDiv.innerHTML = `
                ${buttonCheck}
                <div class="task-priority">
                    <div class="circle ${taskPriorityClass}"><span>${escapeHtml(task.priority)}</span></div>
                    <h2>${escapeHtml(task.name)}</h2> 
                </div>  
                <p>${escapeHtml(task.description)}</p>
                <p><i class="fas fa-calendar-alt"></i> Fecha de creación: ${escapeHtml(task.creationDate)}</p>
                <p><i class="fas fa-calendar-check"></i> Fecha de vencimiento: ${escapeHtml(task.dueDate)}</p>
                <p><i class="fas fa-exclamation-circle"></i> Dificultad: ${escapeHtml(task.difficulty)}</p>
                <p><i class="fas fa-clock"></i> Tiempo estimado: ${parseFloat(task.estimatedTime).toFixed(1)} horas</p>
                <button class="delete-button" onclick="deleteTask('${task.id}', this)"><i class="fas fa-trash-alt"></i></button>
            `;

            taskContainer.appendChild(taskDiv);
        });
    })
    .catch(error => console.log('Error:', error));
}

// Agregar nueva tarea
function addTask() {
    const taskName = document.getElementById("taskTitle").value.trim();
    const description = document.getElementById("taskDescription").value.trim();
    const date = document.getElementById("taskDueDate").value;
    const difficultyTask = document.querySelector('input[name="taskDifficulty"]:checked').value;
    const priorityTask = document.getElementById("taskPriority").value;
    const time = parseFloat(document.getElementById("averageTime").value);

    // Validaciones de entrada
    if (!taskName || !description || !date || !difficultyTask || !priorityTask || isNaN(time)) {
        return alert('Por favor, completa todos los campos');
    }

    if (taskName.length > 30) return alert('El título es demasiado largo');
    if (description.length > 50) return alert('La descripción es demasiado larga');
    if (time < 0) return alert('No puedes agregar un tiempo negativo');

    const currentDate = new Date();
    currentDate.setHours(0, 0, 0, 0);
    const selectedDate = new Date(date);

    if (selectedDate < currentDate) {
        return alert('La fecha de vencimiento debe ser posterior a la fecha actual');
    }

    fetch("https://localhost:443/taskManager/saveTask", {
        headers: { "Accept": "application/json", "Content-Type": "application/json" },
        method: "POST",
        body: JSON.stringify({
            name: taskName,
            description,
            dueDate: date,
            difficulty: difficultyTask,
            priority: priorityTask,
            estimatedTime: time
        })
    })
    .then(res => { 
        if (res.ok) loadTask(); 
    })
    .catch(error => console.log('Error:', error));
}

// Eliminar una tarea específica por su ID
function deleteTask(taskId, buttonElement) {
    if (!confirm('¿Estás seguro de que deseas eliminar esta tarea?')) return;

    fetch(`https://localhost:443/taskManager/deleteTask?id=${taskId}`, {
        headers: {
            "Accept": "application/json",
            "Content-Type": "application/json"
        },
        method: "DELETE"
    })
    .then(res => { 
        if (res.ok) {
            const taskElement = buttonElement.closest('.task');
            taskElement.remove();
        }
    })
    .catch(error => console.log('Error:', error));
}

// Marcar una tarea como completada
function disabledButton(taskId, checkbox) {
    fetch(`https://localhost:443/taskManager/markTaskAsCompleted?id=${taskId}`, {
        headers: {
            "Accept": "application/json",
            "Content-Type": "application/json"
        },
        method: "PUT"
    })
    .then(res => { 
        if (res.ok) {
            checkbox.disabled = true;
            const taskElement = checkbox.closest('.task');
            taskElement.classList.add('COMPLETED');
        }
    })
    .catch(error => console.log('Error:', error));
}

// Generar tareas aleatorias para prueba
function generateRandomTasks() {
    fetch("https://localhost:443/taskManager/generateRandomTasks", {
        headers: {
            "Accept": "application/json",
            "Content-Type": "application/json"
        },
        method: "POST"
    })
    .then(res => {
        if (res.ok) loadTask(); 
    })
    .catch(error => console.log('Error:', error));
}

// Ejecuta loadTask al cargar la página para mostrar todas las tareas
document.addEventListener("DOMContentLoaded", loadTask);
