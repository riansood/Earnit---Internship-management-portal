document.addEventListener('DOMContentLoaded', function() {
    const allDropdown = document.querySelectorAll('#sidebar .side-dropdown');
    let rank = 1;

    allDropdown.forEach(item => {
        const a = item.parentElement.querySelector('a:first-child');
        a.addEventListener('click', function(e) {
            e.preventDefault();

            if (item.classList.contains('active')) {
                allDropdown.forEach(i => {
                    const aLink = i.parentElement.querySelector('a:first-child');
                    aLink.classList.remove('active');
                    i.classList.remove('show');
                });
            }

            this.classList.toggle('active');
            item.classList.toggle('show');
        });
    });

    document.getElementById('searchInput').addEventListener('keyup', function() {
        const filter = this.value.toLowerCase();
        const rows = document.querySelectorAll('.content-table tbody tr');

        rows.forEach(row => {
            const emailCell = row.querySelector('td:nth-child(3)');
            const email = emailCell.textContent.toLowerCase();

            if (email.includes(filter)) {
                row.style.display = '';
            } else {
                row.style.display = 'none';
            }
        });
    });

    fetchStudents();

    function fetchStudents() {
        fetch('./api/employee/students', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            }
        })
        .then(response => response.json())
        .then(data => {
            data.forEach(email => {
                fetchCommentStatus(email).then(status => {
                                    addRow(email, status);
                                });
            });
        })
        .catch(error => console.error('Error fetching students:', error));
    }

    function fetchCommentStatus(email) {
            return fetch(`./api/comments/status/${email}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                }
            })
            .then(response => response.json())
            .then(data => data.isAccepted ? 'Approved' : 'Declined')
            .catch(error => {
                console.error('Error fetching comment status:', error);
                return 'Unknown'; // Fallback status in case of an error
            });
        }

     function addRow(email, status) {
            const tableBody = document.getElementById('tableBody');
            const newRow = document.createElement('tr');

            newRow.innerHTML = `
                <td>${rank}</td>
                <td><a href="single_application.html?email=${encodeURIComponent(email)}">${email}</a></td>
                <td id="status">${status}</td>
            `;
            tableBody.appendChild(newRow);
            rank++;
        }

});
