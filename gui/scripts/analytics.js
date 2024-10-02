/*function loadDifficultyDiagram(){
    fetch("http://localhost:80/taskManager/getTasks")
    .then(res => res.json())
    .then(data => {
        const difficulties = ['High', 'Medium', 'Low'];
        var myChart = new Chart(ctx, {
            type: 'bar',
            data: {
                labels: difficulties,

            }
        })
}*/





let myChart;
 
function createChart(type, data, options) {
    const ctx = document.getElementById('analyticsChart').getContext('2d');
    if (myChart) {
        myChart.destroy();
    }
    myChart = new Chart(ctx, {
        type: type,
        data: data,
        options: options
    });
}
 
function difficultyHistogram() {
    // Gráfico de barras para el histograma de dificultad
    createChart('bar', {
        labels: ['Fácil', 'Medio', 'Difícil'],
        datasets: [{
            label: 'Número de tareas',
            data: [5, 10, 3], // Datos de ejemplo
            backgroundColor: ['#66c2a5', '#fc8d62', '#8da0cb']
        }]
    }, {
        responsive: true,
        plugins: {
            title: {
                display: true,
                text: 'Histograma de Dificultad'
            },
            tooltip: {
                callbacks: {
                    label: function(context) {
                        return `Número de tareas: ${context.parsed.y}`;
                    }
                }
            }
        },
        scales: {
            y: {
                beginAtZero: true,
                title: {
                    display: true,
                    text: 'Número de tareas'
                }
            },
            x: {
                title: {
                    display: true,
                    text: 'Nivel de dificultad'
                }
            }
        }
    });
}
 
function tasksCompletedOverTime() {
    // Gráfico de líneas para tareas finalizadas por tiempo
    createChart('line', {
        labels: ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun'],
        datasets: [{
            label: 'Tareas finalizadas',
            data: [12, 19, 3, 5, 2, 3], // Datos de ejemplo
            borderColor: '#8da0cb',
            tension: 0.1
        }]
    }, {
        responsive: true,
        plugins: {
            title: {
                display: true,
                text: 'Número de tareas finalizadas por tiempo'
            },
            tooltip: {
                callbacks: {
                    label: function(context) {
                        return `Tareas finalizadas: ${context.parsed.y}`;
                    }
                }
            }
        },
        scales: {
            y: {
                beginAtZero: true,
                title: {
                    display: true,
                    text: 'Número de tareas'
                }
            },
            x: {
                title: {
                    display: true,
                    text: 'Mes'
                }
            }
        }
    });
}
 
function taskAveragesByPriority() {
    // Gráfico de barras para promedios de tareas por prioridad
    createChart('bar', {
        labels: ['Baja', 'Media', 'Alta'],
        datasets: [{
            label: 'Tiempo promedio (horas)',
            data: [2, 4, 6], // Datos de ejemplo
            backgroundColor: ['#66c2a5', '#fc8d62', '#8da0cb']
        }]
    }, {
        responsive: true,
        plugins: {
            title: {
                display: true,
                text: 'Promedios de tareas por prioridad'
            },
            tooltip: {
                callbacks: {
                    label: function(context) {
                        return `Tiempo promedio: ${context.parsed.y} horas`;
                    }
                }
            }
        },
        scales: {
            y: {
                beginAtZero: true,
                title: {
                    display: true,
                    text: 'Tiempo promedio (horas)'
                }
            },
            x: {
                title: {
                    display: true,
                    text: 'Nivel de prioridad'
                }
            }
        }
    });
}
 
function totalTimeSpent() {
    // Gráfico circular para tiempo total invertido por tareas realizadas
    createChart('pie', {
        labels: ['Proyecto A', 'Proyecto B', 'Proyecto C', 'Proyecto D'],
        datasets: [{
            data: [30, 50, 20, 10], // Datos de ejemplo
            backgroundColor: ['#66c2a5', '#fc8d62', '#8da0cb', '#e78ac3']
        }]
    }, {
        responsive: true,
        plugins: {
            title: {
                display: true,
                text: 'Tiempo total invertido por tareas realizadas'
            },
            tooltip: {
                callbacks: {
                    label: function(context) {
                        let label = context.label || '';
                        if (label) {
                            label += ': ';
                        }
                        label += context.parsed + ' horas';
                        return label;
                    }
                }
            }
        }
    });
}
 
document.getElementById('chartType').addEventListener('change', function() {
    switch(this.value) {
        case 'difficultyHistogram':
            difficultyHistogram();
            break;
        case 'tasksCompletedOverTime':
            tasksCompletedOverTime();
            break;
        case 'taskAveragesByPriority':
            taskAveragesByPriority();
            break;
        case 'totalTimeSpent':
            totalTimeSpent();
            break;
    }
});
 
// Inicializar con el primer gráfico
difficultyHistogram();


    
        