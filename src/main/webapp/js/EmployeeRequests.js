document.addEventListener('DOMContentLoaded', () => {
    // Existing login form submission code
    const loginForm = document.getElementById("login_form");
    if (loginForm) {
        loginForm.addEventListener("submit", function(event) {
            event.preventDefault();

            let loginCredentials = {
                email: document.getElementById("email").value,
                password: document.getElementById("password").value
            };

            fetch("./api/employee/login", {
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
                        window.location.href = "dashboard_emp.html";
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

    // New comment form submission code
    const commentForm = document.getElementById("comment");
    if (commentForm) {
        commentForm.addEventListener('click', function(event) {
            event.preventDefault();

            let newComment = {
                comment_id: null,
                message: document.getElementById("insert-comment").value,
                isAccepted: null,
                student_id: document.getElementById("email").textContent
            };

            fetch("./api/comments", {
                method: "POST",
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(newComment)
            })
            .then(response => {
                if (response.ok) {
                    return response.json().then(data => {
                        alert('The comment was successfully submitted!');
                        console.log('Success:', data);
                    });
                } else {
                    return response.json().then(errorData => {
                        document.getElementById('comment_message').innerText = errorData.error || 'An unexpected error occurred.';
                        console.error('Error:', errorData);
                    });
                }
            })
            .catch((error) => {
                document.getElementById('comment_message').innerText = 'An error occurred while trying to submit the comment.';
                console.error('Fetch Error:', error);
            });
        });
    }
});
