//import DOMPurify from 'dompurify';
//import {sanitize} from 'dompurify';
document.addEventListener('DOMContentLoaded', () => {
    // Handle Signup Form Submission
    const signupForm = document.getElementById('signup-form');
    if (signupForm) {
        signupForm.addEventListener("submit", function (event) {
            event.preventDefault();

            // Collect new student data from the form
            let newStudent = {
                name: document.getElementById("name").value,
                email: document.getElementById("email").value,
                address: document.getElementById("address").value,
                phoneNumber: document.getElementById("phone").value,
                password: document.getElementById("password").value
            };

            // Send the data using the Fetch API
            fetch('./api/studentLogin', {
                method: "POST",
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(newStudent)
            })
                .then(response => response.json())
                .then(data => {
                    document.getElementById('message').innerText = 'Signup successful!';
                    console.log('Success:', data);

                })
                .catch((error) => {
                    document.getElementById('message').innerText = 'Error during signup.';
                    console.error('Error:', error);
                });
        });
    }


    // Handle Login Form Submission
    const loginForm = document.getElementById("login-form");
    if (loginForm) {
        loginForm.addEventListener("submit", function(event) {
            event.preventDefault();

            let loginCredentials = {
                email: document.getElementById("usernameSignIn").value,
                password: document.getElementById("passwordSignIn").value
            };

            fetch("./api/studentLogin/login", {
                method: "POST",
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(loginCredentials)
            })
                .then(response => {
                    if (response.ok) {
                        // Handle successful response
                        return response.json().then(data => {
                            document.getElementById('message2').innerText = 'Login successful!';
                            console.log('Success:', data);
                            //localStorage.setItem('userId', document.getElementById("usernameSignIn").value);
                            window.location.href = "student_dashboard.html";
                        });
                    } else if (response.status === 401) {
                        // Handle unauthorized response
                        return response.json().then(errorData => {
                            document.getElementById('message2').innerText = errorData.error || 'Invalid email or password.';
                            console.error('Unauthorized:', errorData);
                        });
                    } else {
                        // Handle other errors
                        return response.json().then(errorData => {
                            document.getElementById('message2').innerText = errorData.error || 'An unexpected error occurred.';
                            console.error('Error:', errorData);
                        });
                    }
                })
                .catch((error) => {
                    // Handle network or other errors
                    document.getElementById('message2').innerText = 'An error occurred while trying to login.';
                    console.error('Fetch Error:', error);
                });
        });
    }
    const logoutButton = document.getElementById("log-out-button-dashboard");
    if (logoutButton) {
        logoutButton.addEventListener('click', function() {
            fetch('./api/studentLogin/logout', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                }
            })
                .then(response => {
                    if (response.ok) {
                        // Handle successful logout
                        return response.json().then(data => {
                            alert('Logout successful!');
                            console.log('Success:', data);
                            window.location.href = "signUp.html"; // Redirect to login page
                        });
                    } else {
                        // Handle errors
                        return response.json().then(errorData => {
                            alert('Logout failed.');
                            console.error('Error:', errorData);
                        });
                    }
                })
                .catch((error) => {
                    // Handle network or other errors
                    alert('An error occurred during logout.');
                    console.error('Fetch Error:', error);
                });
        });
    }

    const pdfLink = document.getElementById("PDF");
    if (pdfLink) {
        //pdfLink.style.cursor = 'pointer'; // Make sure the link looks clickable
        pdfLink.addEventListener('click', function(event) {
            event.preventDefault();

            fetch(`./api/studentLogin/pdf`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/pdf',
                }
            })
            .then(response => {
                        if (!response.ok) {
                            console.error('Failed to download PDF:', response.statusText);
                        } else {
                            alert('PDF downloaded successfully! Check your downloads');
                        }
                    })
                    .catch(error => console.error('Error:', error));
        });
    }
//


    const BTWform = document.getElementById("btwSaveBtn");
    if (BTWform) {
        BTWform.addEventListener('click', function(event) {
            event.preventDefault();
            let BTW_number = document.getElementById("btwInput").value;

            fetch("./api/studentLogin/BTW_number", {
                method: 'PUT',
                headers: {
                    'Content-Type': 'text/plain',
                },
                body: BTW_number
            })
            .then(response => {
                            if (!response.ok) {
                                console.error('Failed to download PDF:', response.statusText);
                            } else {
                                console.log('BTW saved successfully');
                            }
            })
                            .catch(error => console.error('Error:', error));
                    });
    }




    // Toggle Effect between Signup and Login
    const container = document.getElementById('container');
    const registerButton = document.getElementById('register');
    const loginButton = document.getElementById('login');

    if (registerButton) {
        registerButton.addEventListener('click', () => {
            container.classList.add('active');
        });
    }

    if (loginButton) {
        loginButton.addEventListener('click', () => {
            container.classList.remove('active');
        });
    }
const btwDisplay = document.getElementById('btwDisplay');
    fetchBTWData();
    function fetchBTWData() {
            fetch('./api/studentLogin/receiveBTW')
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Network response was not ok ' + response.statusText);
                    }
                    return response.text();
                })
                .then(data => {
                    btwDisplay.textContent = data;
                })
                .catch(error => {
                    console.error('There was a problem with the fetch operation:', error);
                    btwDisplay.textContent = 'Error loading data';
                });
        }

});