/**
 * Handles the toggling effect. When clicking 'Sign Up' or 'Sign In' triggers Javascript to modify
 * the classes of elements(container, sign-up, sign-in, etc.) which in return triggers CSS animations
 * and transitions defined by transform: translateX(...) to achieve the moving effect between the two forms.
 * @type {HTMLElement}
 *
 */

// Get the container element by its ID
const container= document.getElementById('container');

//Get the register and login button by their IDs
const registerButton= document.getElementById('register');
const loginButton= document.getElementById('login');


//Add event listener to the register button and then add 'active' class to the container to show the register form
registerButton.addEventListener('click', () => {
    container.classList.add('active');
});
/*
//Add event listener to the login button and then remove 'active' class to the container to show the login side
loginButton.addEventListener('click', () => {
    container.classList.remove('active');
});*/

 function fetchStudentInfo(email) {
        fetch(`./api/studentLogin/${email}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            }
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            updateStudentInfo(data);
        })
        .catch(error => console.error('Error fetching student info:', error));
    }