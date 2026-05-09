// js/profile.js

document.addEventListener('DOMContentLoaded', function() {
    fetchStudent();

    function fetchStudent() {
        fetch('./api/studentLogin/email', {
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
            updateStudent(data);
        })
        .catch(error => console.error('Error fetching student info:', error));
    }


    function updateStudent(student) {
        document.getElementById('name').textContent = student.name;
        document.getElementById('address').textContent = student.address;
        document.getElementById('email').textContent = student.email;
        document.getElementById('phone-number-profile').textContent = student.phoneNumber;
    }

    const deleteAccount = document.getElementById("deleteAccount");
    if (deleteAccount) {
        deleteAccount.addEventListener("click", function(event) {
            event.preventDefault();

            fetch("./api/studentLogin/delete", {
                method: "DELETE"

            })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                window.location.href = 'signUp.html';
            })
            .catch(error => {
                console.error('Fetch Error:', error);
                alert('There was an error deleting your account. Please try again.');
            });
        });
    }

});

function editProfile() {
    console.log('Edit button clicked');

    // Make input fields visible and span elements hidden
    document.getElementById('nameInput').classList.remove('hidden');
    document.getElementById('addressInput').classList.remove('hidden');
    document.getElementById('phoneNumberInput').classList.remove('hidden');

    document.getElementById('name').classList.add('hidden');
    document.getElementById('address').classList.add('hidden');
    document.getElementById('phone-number-profile').classList.add('hidden');

    // Show Save button and hide Edit button
    document.getElementById('saveProfile').classList.remove('hidden');
    document.getElementById('editProfile').classList.add('hidden');

    // Populate input fields with current values
    document.getElementById('nameInput').value = document.getElementById('name').textContent;
    document.getElementById('addressInput').value = document.getElementById('address').textContent;
    document.getElementById('phoneNumberInput').value = document.getElementById('phone-number-profile').textContent;

    // Make input fields editable and clickable
    document.getElementById('nameInput').disabled = false;
    document.getElementById('addressInput').disabled = false;
    document.getElementById('phoneNumberInput').disabled = false;

    // Enable pointer events to make fields clickable
    document.getElementById('nameInput').style.pointerEvents = 'auto';
    document.getElementById('addressInput').style.pointerEvents = 'auto';
    document.getElementById('phoneNumberInput').style.pointerEvents = 'auto';
}

function saveProfile() {
    console.log('Save button clicked');

    const nameInput = document.getElementById('nameInput');
    const addressInput = document.getElementById('addressInput');
    const phoneInput = document.getElementById('phoneNumberInput');

    let editInformation = {
        name: nameInput.value,
        address: addressInput.value,
        phoneNumber: phoneInput.value
    };

    console.log('Profile information to save:', editInformation);

    fetch("./api/studentLogin/editProfile", {
        method: "PUT",
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(editInformation)
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return response.json();
    })
    .then(data => {
        console.log('Profile saved successfully:', data);

        // Update the span elements with new values
        document.getElementById('name').textContent = editInformation.name;
        document.getElementById('address').textContent = editInformation.address;
        document.getElementById('phone-number-profile').textContent = editInformation.phoneNumber;

        // Hide input fields and show span elements
        document.getElementById('nameInput').classList.add('hidden');
        document.getElementById('addressInput').classList.add('hidden');
        document.getElementById('phoneNumberInput').classList.add('hidden');

        document.getElementById('name').classList.remove('hidden');
        document.getElementById('address').classList.remove('hidden');
        document.getElementById('phone-number-profile').classList.remove('hidden');

        // Show Edit button and hide Save button
        document.getElementById('saveProfile').classList.add('hidden');
        document.getElementById('editProfile').classList.remove('hidden');

        // Disable input fields and pointer events
        document.getElementById('nameInput').disabled = true;
        document.getElementById('addressInput').disabled = true;
        document.getElementById('phoneNumberInput').disabled = true;

        document.getElementById('nameInput').style.pointerEvents = 'none';
        document.getElementById('addressInput').style.pointerEvents = 'none';
        document.getElementById('phoneNumberInput').style.pointerEvents = 'none';
    })
    .catch(error => {
        console.error('Error saving profile info:', error);
    });
}

// Disable input fields and pointer events initially
document.getElementById('nameInput').disabled = true;
document.getElementById('addressInput').disabled = true;
document.getElementById('phoneNumberInput').disabled = true;

document.getElementById('nameInput').style.pointerEvents = 'none';
document.getElementById('addressInput').style.pointerEvents = 'none';
document.getElementById('phoneNumberInput').style.pointerEvents = 'none';
