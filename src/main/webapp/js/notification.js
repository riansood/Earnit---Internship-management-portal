document.addEventListener('DOMContentLoaded', function() {
    const unReadMessagesCount = document.getElementById('num-of-notif');
    const markAll = document.getElementById('mark-as-read');
    const commentTable = document.getElementById('commentTable').querySelector('tbody');

    function updateNotificationCount() {
        const unreadNotifications = document.querySelectorAll('.notificationCard.unread');
        unReadMessagesCount.textContent = unreadNotifications.length;
    }

    function addNotification(text) {
        const newRow = document.createElement('tr');
        const newNotification = document.createElement('div');
        newNotification.classList.add('notificationCard', 'unread');
        newNotification.innerHTML = `
            <div class="description">
                <p>${text}</p>
            </div>
        `;
        newRow.appendChild(newNotification);
        commentTable.appendChild(newRow);
        updateNotificationCount();

        // Attach click event to mark as read
        newNotification.addEventListener('click', () => {
            newNotification.classList.remove('unread');
            updateNotificationCount();
        });
    }

    fetch("./api/studentLogin/comment")
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            console.log(data); // Add this line to log the data
            // Ensure data is an array
            if (!Array.isArray(data)) {
                throw new Error('Expected an array of comments');
            }
            // Add each comment as a new notification
            data.forEach(comment => {
                addNotification(comment);
            });
        })
        .catch(error => {
            console.error('Error fetching comments:', error);
        });

    markAll.addEventListener('click', () => {
        const notifications = document.querySelectorAll('.notificationCard.unread');
        notifications.forEach(notification => {
            notification.classList.remove('unread');
        });
        updateNotificationCount();
    });

    // Initial count update
    updateNotificationCount();
});
