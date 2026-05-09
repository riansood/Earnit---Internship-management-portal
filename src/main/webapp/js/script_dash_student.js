const allDropdown = document.querySelectorAll('#sidebar .side-dropdown');
allDropdown.forEach(item=> {
    const a = item.parentElement.querySelector('a:first-child');
    a.addEventListener('click', function (e) {
        //console.log(a);
        e.preventDefault();

        if(item.classList.contains('active')) {
            allDropdown.forEach(i=> {
                const aLink = item.parentElement.querySelector('a:first-child');
                aLink.classList.remove('active');
                i.classList.remove('show');
            })
        }

        this.classList.toggle('active');
        item.classList.toggle('show');
    })
})


//profile drop of the profile
const profile = document.querySelector('nav .profile');
const imgProfile = profile.querySelector('img');
const dropdownProfile = profile.querySelector('.profile-link');

imgProfile.addEventListener('click', function () {
    dropdownProfile.classList.toggle('show');
})


function editBTW() {
    var btwDisplay = document.getElementById('btwDisplay');
    var btwInput = document.getElementById('btwInput');
    var btwEditBtn = document.getElementById('btwEditBtn');
    var btwSaveBtn = document.getElementById('btwSaveBtn');

    btwDisplay.classList.add('hidden');
    btwInput.classList.remove('hidden');
    btwInput.value = btwDisplay.innerText.trim();

    btwEditBtn.classList.add('hidden');
    btwSaveBtn.classList.remove('hidden');
}

// Function to save edited BTW number
function saveBTW() {
    var btwDisplay = document.getElementById('btwDisplay');
    var btwInput = document.getElementById('btwInput');
    var btwEditBtn = document.getElementById('btwEditBtn');
    var btwSaveBtn = document.getElementById('btwSaveBtn');

    // Update display with new value
    btwDisplay.innerText = btwInput.value.trim();

    btwDisplay.classList.remove('hidden');
    btwInput.classList.add('hidden');

    btwEditBtn.classList.remove('hidden');
    btwSaveBtn.classList.add('hidden');
}

function checkBTW() {
    var btwDisplay = document.getElementById('btwDisplay').innerText.trim();
    if (btwDisplay === '') {
        alert('Cannot access Tax page without a BTW number.');
    } else {
        window.location.href = 'Tax.html';
    }
}

