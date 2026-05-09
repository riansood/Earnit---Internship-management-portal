document.addEventListener('DOMContentLoaded', function() {
    const urlParams = new URLSearchParams(window.location.search);
    const email = urlParams.get('email');

    if (email) {
        const decodedEmail = decodeURIComponent(email);
        fetchStudentInfo(decodedEmail);
    }

     const pdfLink = document.getElementById("pdf");
        if (pdfLink) {
            //pdfLink.style.cursor = 'pointer'; // Make sure the link looks clickable
            pdfLink.addEventListener('click', function(event) {
                event.preventDefault();

                fetch(`./api/studentLogin/pdf/${email}`, {
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

    const taxDisplay = document.getElementById('tax');

        function fetchTaxData() {
            fetch(`./api/tax/instance/${email}`)
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Network response was not ok ' + response.statusText);
                    }
                    return response.text();
                })
                .then(data => {
                    taxDisplay.textContent = data;
                })
                .catch(error => {
                    console.error('There was a problem with the fetch operation:', error);
                    btwDisplay.textContent = 'Error loading data';
                });
        }

        // Call the function to fetch the data
        fetchTaxData();

    function updateStudentInfo(student) {
        document.getElementById('name').textContent = `${student.name}`;
//        document.getElementById('username').textContent = `@${student.username}`;
        document.getElementById('email').textContent = `${student.email}`;
        document.getElementById('btw').textContent = `${student.BTW_number}`;
    }

     const approveButton = document.getElementById('approved');
        if (approveButton) {
            approveButton.addEventListener('click', function(event) {
                event.preventDefault();
                updateCommentStatus(email);
            });
        }

        function updateCommentStatus(email) {
            fetch(`./api/comments/accept/${email}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ isAccepted: true })
            })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                alert('The application was successfully approved!');
                return response.json();
            })
            .then(data => {

            })
            .catch(error => console.error('Error updating comment status:', error));
        }

});



