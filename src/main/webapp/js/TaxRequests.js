// import DOMPurify from 'dompurify';
// import {sanitize} from 'dompurify';

document.addEventListener('DOMContentLoaded', () => {
    // Handle Signup Form Submission
    const taxForm = document.getElementById('tax_form');
    if (taxForm) {
        taxForm.addEventListener("submit", function (event) {
            event.preventDefault();

            const earnings = document.getElementById('earning').value;
            const date = document.getElementById('date').value;

            if (earnings && date) {
                let earningsData = JSON.parse(localStorage.getItem('earningsData')) || [];
                earningsData.push({ date: date, earnings: earnings });
                localStorage.setItem('earningsData', JSON.stringify(earningsData));
            } else {
                alert('Please enter both earnings and date.');
            }

            let newTax = {
                id: null,
                taxAmount: null,
                isPaid: null,
                email: null,
                date: document.getElementById("date").value,
                earning: document.getElementById("earning").value
            };

            document.getElementById("tax").innerHtml=

            // request for creating a Tax
            fetch('./api/tax', {
                method: "PUT",
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(newTax)
            })
                .then(response => response.json())
                .then(data => {
                    console.log('Success:', data);
                    fetchTax(newTax.earning);
                })
                .catch((error) => {
                    console.error('Error:', error);
                });
        });
    }
});

function fetchTax(earning) {
    fetch('./api/tax', {
        method: "GET",
        headers: {
            'Content-Type': 'application/json',
        }
    })
        .then(response => response.json())
        .then(data => {
            document.getElementById('tax').innerHTML = `<p>${data.taxAmount}</p>`;
            alert('Tax has been submitted');
            console.log('Tax data:', data);
        }).catch((error) => {
                  document.getElementById('message').innerText = 'You cannot enter your earnings because you do not have BTW number.';
                  console.error('Error:', error);
              });

}
