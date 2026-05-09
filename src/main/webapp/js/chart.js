document.addEventListener('DOMContentLoaded', function() {
    var ctx = document.getElementById("myChart").getContext('2d');

    // Retrieve stored data from localStorage
    let earningsData = JSON.parse(localStorage.getItem('earningsData')) || [];

    // Prepare data for the chart
    let labels = earningsData.map(entry => entry.date);
    let data = earningsData.map(entry => entry.earnings);

    var myChart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: labels,
            datasets: [{
                label: 'Earnings in $',
                data: data,
                backgroundColor: [
                    'rgba(85, 85, 85, 1)',
                ],
                borderColor: [
                    'rgb(41, 155, 99)',
                ],
                borderWidth: 1
            }]
        },
        options: {
            responsive: true
        }
    });
});