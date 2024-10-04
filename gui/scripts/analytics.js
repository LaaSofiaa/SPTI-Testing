const colors = {
    PURPLE: '#afd1c6',
    PINK: '#15505d',
    GREEN: '#acfb03',
    ORANGE: '#3c895f',
    BLUE: '#7bd179'
}

// callTasks ahora devuelve la promesa de los datos.
async function callTasks() {
    try {
        const res = await fetch("https://taskmanagercvds-bjdmg9hwaaa7erg0.eastus-01.azurewebsites.net/taskManager/getTasks");
        if (!res.ok) {
            throw new Error(`HTTP error! Status: ${res.status}`);
        }
        const data = await res.json();
        return data; // Devuelve los datos obtenidos
    } catch (error) {
        console.error('Error fetching the tasks:', error);
    }
}

async function loadDifficultyDiagram() {
    const data = await callTasks(); // Espera los datos de la función callTasks
    if (!data) return; // Si no hay datos, salir de la función para evitar errores.

    var ctx = document.getElementById("myCanvasTask").getContext('2d');

    let dataValues = {"High": 0, "Middle": 0, "Low": 0};

    // Recorrer los datos y contar las dificultades
    for (let i = 0; i < data.length; i++) {
        dataValues[data[i].difficulty] += 1;
    }

    // Crear el gráfico de barras
    const difficulties = ['High', 'Middle', 'Low'];
    var myChart = new Chart(ctx, {
        type: 'bar', // Puedes cambiar a 'line' o 'pie' según tus necesidades
        data: {
            labels: difficulties,
            datasets: [{
                label: 'Number Of Tasks By Difficulty', // Etiqueta de la serie
                data: [dataValues['High'], dataValues['Middle'], dataValues['Low']],
                backgroundColor: [colors['PINK'], colors['BLUE'], colors['PURPLE']],
                borderWidth: 2,
                borderColor: '#000', // Color del borde
                hoverBackgroundColor: [colors['PINK'], colors['BLUE'], colors['PURPLE']], // Color al pasar el mouse
            }]
        },
        options: {
            responsive: true,
            plugins: {
                title: {
                    display: true,
                    text: 'Task Distribution by Difficulty' // Título del gráfico
                },
                legend: {
                    display: true,
                    position: 'top', // Posición de la leyenda
                },
                tooltip: {
                    callbacks: {
                        label: function(tooltipItem) {
                            return tooltipItem.dataset.label + ': ' + tooltipItem.raw; // Tooltip con la etiqueta y valor
                        }
                    }
                }
            },
            scales: {
                x: {
                    title: {
                        display: true,
                        text: 'Difficulty' // Etiqueta del eje X
                    }
                },
                y: {
                    title: {
                        display: true,
                        text: 'Number Of Tasks' // Etiqueta del eje Y
                    },
                    beginAtZero: true // Comenzar el eje Y en 0
                }
            }
        }
    });
}


async function loadPriorityDiagram() {
    const data = await callTasks(); // Espera los datos de la función callTasks
    if (!data) return; // Si no hay datos, salir de la función para evitar errores.

    var ctx = document.getElementById("myCanvasPriority").getContext('2d');
    let dataValues = {1: 0, 2: 0, 3: 0, 4: 0, 5: 0};

    // Recorrer los datos y contar las prioridades
    for (let i = 0; i < data.length; i++) {
        dataValues[data[i].priority] += 1;
    }

    // Crear el gráfico de barras
    const priorities = [1, 2, 3, 4, 5];
    var myChart = new Chart(ctx, {
        type: 'bar', // Puedes cambiar a 'line' o 'pie' según tus necesidades
        data: {
            labels: priorities,
            datasets: [{
                label: 'Number Of Tasks By Priority', // Etiqueta de la serie
                data: [dataValues[1], dataValues[2], dataValues[3], dataValues[4], dataValues[5]],
                backgroundColor: [colors['GREEN'], colors['PINK'], colors['BLUE'], colors['PURPLE'], colors['ORANGE']],
                borderWidth: 2,
                borderColor: '#000', // Color del borde
                hoverBackgroundColor: [colors['GREEN'], colors['PINK'], colors['BLUE'], colors['PURPLE'], colors['ORANGE']], // Color al pasar el mouse
            }]
        },
        options: {
            responsive: true,
            plugins: {
                title: {
                    display: true,
                    text: 'Task Distribution by Priority' // Título del gráfico
                },
                legend: {
                    display: true,
                    position: 'top', // Posición de la leyenda
                },
                tooltip: {
                    callbacks: {
                        label: function(tooltipItem) {
                            return tooltipItem.dataset.label + ': ' + tooltipItem.raw; // Tooltip con la etiqueta y valor
                        }
                    }
                }
            },
            scales: {
                x: {
                    title: {
                        display: true,
                        text: 'Priority' // Etiqueta del eje X
                    }
                },
                y: {
                    title: {
                        display: true,
                        text: 'Number Of Tasks' // Etiqueta del eje Y
                    },
                    beginAtZero: true // Comenzar el eje Y en 0
                }
            }
        }
    });
}


async function loadTimeDiagram() {
    const data = await callTasks(); 
    if (!data) return; 

    let timeSum = {};  

    for (let i = 0; i < data.length; i++) {
        let taskTime = data[i].estimatedTime;

        if(data[i].isCompleted){
            if (timeSum[taskTime]) {
                timeSum[taskTime.toFixed(1)] += 1;
            } else {
                timeSum[taskTime.toFixed(1)] = taskTime;
            }
        }
    }

    let times = Object.keys(timeSum); 
    let avgTimes = times.map( t => timeSum[t] );  

        // Configuración del gráfico
        var ctx = document.getElementById("myCanvasTime").getContext('2d');
        var myChart = new Chart(ctx, {
            type: 'line',
            data: {
                labels: times,  // Etiquetas (tiempos)
                datasets: [{
                    label: 'Average Time',
                    data: avgTimes,  
                    borderColor: 'rgba(75, 192, 192, 1)',
                    backgroundColor: 'rgba(75, 192, 192, 0.2)',
                }]
            },
            options: {
                scales: {
                    x: {
                        title: {
                            display: true,
                            text: 'Time (Hours)'
                        }
                    },
                    y: {
                        title: {
                            display: true,
                            text: 'Number Of Tasks Completed'
                        }
                    }
                }
            }
        });
}

async function loadTotalTimeDiagram() {
    const data = await callTasks(); 
    if (!data) return; 

    var ctx = document.getElementById("myFullTimeTask").getContext('2d');

    let dataValues = {"High": 0, "Middle": 0, "Low": 0};

    // Recorrer los datos y contar los tiempos estimados de las tareas completadas
    for (let i = 0; i < data.length; i++) {
        if (data[i].isCompleted) {
            dataValues[data[i].difficulty] += data[i].estimatedTime;
        }   
    }

    const difficulties = ['High', 'Middle', 'Low'];
    var myChart = new Chart(ctx, {
        type: 'pie', // Tipo de gráfico: pastel
        data: {
            labels: difficulties,
            datasets: [{
                label: 'Total Time Spent by Difficulty', // Etiqueta de la serie
                data: [dataValues['High'], dataValues['Middle'], dataValues['Low']],
                backgroundColor: [colors['PINK'], colors['BLUE'], colors['PURPLE']],
                hoverBackgroundColor: [colors['PINK'], colors['BLUE'], colors['PURPLE']], // Colores al pasar el mouse
                borderColor: '#fff', // Color del borde
                borderWidth: 2 // Ancho del borde
            }]
        },
        options: {
            responsive: true,
            plugins: {
                title: {
                    display: true,
                    text: 'Total Time Spent by Difficulty' // Título del gráfico
                },
                legend: {
                    display: true,
                    position: 'top', // Posición de la leyenda
                },
                tooltip: {
                    callbacks: {
                        label: function(tooltipItem) {
                            return tooltipItem.dataset.label + ': ' + tooltipItem.raw + ' hours'; 
                        }
                    }
                }
            }
        }
    });
}

 function loadGraphic(selectedChart){
    var container = document.querySelector(".chart-container");
    switch (selectedChart){
        case "difficultyHistogram":
            container.innerHTML = `<canvas id="myCanvasTask"></canvas>`;
            loadDifficultyDiagram();
            break;
        case "tasksCompletedOverTime":
            container.innerHTML = `<canvas id="myCanvasTime"></canvas>`;
            loadTimeDiagram();
            break;
        case "taskAveragesByPriority":
            container.innerHTML = `<canvas id="myCanvasPriority"></canvas>`;
            loadPriorityDiagram();
            break;
        case "totalTimeSpent":
            container.innerHTML = `<canvas id="myFullTimeTask"></canvas>`;
            loadTotalTimeDiagram();
            break;
    }
 }               

document.getElementById('chartType').addEventListener("change", function(){
    const selectedChart = this.value;
    loadGraphic(selectedChart);
})



document.addEventListener('DOMContentLoaded', function(){
    var container = document.querySelector("#chartType").value;
    loadGraphic(container);
})






    
        