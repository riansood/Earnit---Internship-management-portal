document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('vat-form');

    if (form) {
        form.addEventListener("submit", submitVATform);
    }
});

function submitVATform(event) {
    event.preventDefault();

    // Ensure addition-residential-address has a default value
    if (document.getElementById("addition-residential-address").value === "") {
        document.getElementById("addition-residential-address").value = "-";
    }

    let newVATformSection1 = {
        form_id: null,
        student_id: null,
        companyName: document.getElementById("trade-name").value,
        companyAddress: `${document.getElementById("business-address-StreetName").value} ${document.getElementById("house-number").value} ${document.getElementById("addition").value} ${document.getElementById("postal-code").value} ${document.getElementById("city").value}`,
        businessStartDate: document.getElementById("start-date").value,
        businessPhoneNumber: document.getElementById("company-phone").value,
        businessWebsite: document.getElementById("company-website").value,
        studentName: document.getElementById("entrepreneur's-name").value,
        studentAddress: `${document.getElementById("residential-address").value} ${document.getElementById("house-number-entrepreneur").value} ${document.getElementById("addition-residential-address").value} ${document.getElementById("postal-code-residential-address").value} ${document.getElementById("city-residential-address").value}`,
        studentPhoneNumber: document.getElementById("phone-number-of-entrepreneur").value,
        dateOfBirth: document.getElementById("date-of-birth").value,
        BSN: document.getElementById("BSN").value,
        natureOfBusiness: document.getElementById("nature-of-business").value,
        earningsFirstYear: document.getElementById("estimated-revenue").value,
        companySpendings: document.getElementById("estimated-costs").value,
        noOfEmployers: document.getElementById("people-to-work-for").value,
        initialsAndSurname: document.getElementById("initial(s)-name").value,
        dateOfSignature: document.getElementById("date-signature-field").value,
        signatureBase64: null
    };

    let signatureFile = document.getElementById("signature-photo").files[0];
    if (signatureFile) {
        let reader = new FileReader();
        reader.readAsDataURL(signatureFile);
        reader.onloadend = function () {
            newVATformSection1.signatureBase64 = reader.result.split(',')[1]; // Get Base64 part only

            // Submit the form data once the file is read
            sendForm(newVATformSection1);
        };
    } else {
        // Submit the form data if no file is selected
        sendForm(newVATformSection1);
    }
}

function sendForm(formData) {
    fetch('./api/vatForm', {
        method: "POST",
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(formData)
    })
    .then(response => response.json())
    .then(data => {
        document.getElementById('messageVat').innerText = 'VAT form submission successful!';
        console.log('Success:', data);
    })
    .catch((error) => {
        document.getElementById('messageVat').innerText = 'Error during VAT form submission.';
        console.error('Error:', error);
    });
}