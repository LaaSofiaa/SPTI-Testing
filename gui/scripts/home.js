
document.addEventListener('DOMContentLoaded', function(){
    fetch('../components/navBar.html')
        .then(response => response.text())
        .then(data => {
            console.log(data);
            document.querySelector("header").innerHTML = data;
        })
        .catch(error => console.error('Error loading the navbar:',error));
})