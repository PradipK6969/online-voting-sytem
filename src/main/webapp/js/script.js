// Function to render election results chart
function renderResultsChart(data) {
    const ctx = document.getElementById('resultsChart').getContext('2d');

    // Prepare data for chart
    const labels = data.map(item => `${item.candidate} (${item.party})`);
    const votes = data.map(item => item.votes);
    const backgroundColors = [
        'rgba(255, 99, 132, 0.7)',
        'rgba(54, 162, 235, 0.7)',
        'rgba(255, 206, 86, 0.7)',
        'rgba(75, 192, 192, 0.7)',
        'rgba(153, 102, 255, 0.7)',
        'rgba(255, 159, 64, 0.7)'
    ];

    new Chart(ctx, {
        type: 'bar',
        data: {
            labels: labels,
            datasets: [{
                label: 'Votes',
                data: votes,
                backgroundColor: backgroundColors,
                borderColor: backgroundColors.map(color => color.replace('0.7', '1')),
                borderWidth: 1
            }]
        },
        options: {
            responsive: true,
            scales: {
                y: {
                    beginAtZero: true,
                    ticks: {
                        precision: 0
                    }
                }
            }
        }
    });
}

// Form validation for registration
document.addEventListener('DOMContentLoaded', function() {
    const registerForm = document.querySelector('form[action$="/register"]');
    if (registerForm) {
        registerForm.addEventListener('submit', function(e) {
            const dobInput = document.getElementById('dateOfBirth');
            const dob = new Date(dobInput.value);
            const today = new Date();
            const minAgeDate = new Date();
            minAgeDate.setFullYear(today.getFullYear() - 18);

            if (dob > minAgeDate) {
                alert('You must be at least 18 years old to register.');
                e.preventDefault();
            }
        });
    }

    // Add date picker constraints for election dates
    const electionForm = document.querySelector('form[action$="/save-election"]');
    if (electionForm) {
        const startDateInput = document.getElementById('startDate');
        const endDateInput = document.getElementById('endDate');

        startDateInput.addEventListener('change', function() {
            endDateInput.min = startDateInput.value;
            if (endDateInput.value && endDateInput.value < startDateInput.value) {
                endDateInput.value = startDateInput.value;
            }
        });
    }
});