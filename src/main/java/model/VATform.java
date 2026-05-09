package model;

public class VATform {
    public Integer form_id;
    public String student_id;
    public String companyName;
    public String companyAddress;
    public String businessStartDate;
    public String businessPhoneNumber;
    public String businessWebsite;
    public String studentName;
    public String studentAddress;
    public String dateOfBirth;
    public String BSN;
    public String natureOfBusiness;
    public Double earningsFirstYear;
    public Double companySpendings;
    public String noOfEmployers;
    public String initialsAndSurname;
    public String dateOfSignature;
    public String studentPhoneNumber;
    public String signatureBase64;

    public VATform(Integer form_id, String student_id, String companyName, String companyAddress, String businessStartDate, String businessPhoneNumber, String businessWebsite, String studentName, String studentAddress, String dateOfBirth, String BSN, String natureOfBusiness, Double earningsFirstYear, Double companySpendings, String noOfEmployers, String initialsAndSurname, String dateOfSignature, String studentPhoneNumber, String signatureBase64) {
        this.form_id=form_id;
        this.student_id=student_id;
        this.companyName = companyName;
        this.companyAddress = companyAddress;
        this.businessStartDate = businessStartDate;
        this.businessPhoneNumber = businessPhoneNumber;
        this.businessWebsite = businessWebsite;
        this.studentName = studentName;
        this.studentAddress = studentAddress;
        this.dateOfBirth = dateOfBirth;
        this.BSN = BSN;
        this.natureOfBusiness = natureOfBusiness;
        this.earningsFirstYear = earningsFirstYear;
        this.companySpendings = companySpendings;
        this.noOfEmployers = noOfEmployers;
        this.initialsAndSurname = initialsAndSurname;
        this.dateOfSignature = dateOfSignature;
        this.studentPhoneNumber = studentPhoneNumber;
        this.signatureBase64 = signatureBase64;
    }

    public String getSignatureBase64() {
        return signatureBase64;
    }

    public String extractStreetName(String address) {
        String[] parts = address.split(" ");
        StringBuilder streetNameBuilder = new StringBuilder();
        for (int i = 0; i < parts.length; i++) {
            if (parts[i].matches("\\d+")) {
                break;
            }
            if (i > 0) {
                streetNameBuilder.append(" ");
            }
            streetNameBuilder.append(parts[i]);
        }
        return streetNameBuilder.toString();
    }

    public String extractHouseNumber(String address) {
        String[] parts = address.split(" ");
        for (String part : parts) {
            if (part.matches("\\d+")) {
                return part;
            }
        }
        return "";
    }

    public String extractAddition(String address) {
        String[] parts = address.split(" ");
        boolean foundHouseNumber = false;
        for (String part : parts) {
            if (foundHouseNumber) {
                if (!part.matches("\\d+")) {
                    return part;
                } else {
                    foundHouseNumber = false;  // if the addition is a number, skip it
                }
            }
            if (part.matches("\\d+")) {
                foundHouseNumber = true;
            }
        }
        return "";
    }

    public String extractPostalCode(String address) {
        String[] parts = address.split(" ");
        if (parts.length >= 2) {
            return parts[parts.length - 2];
        }
        return "";
    }

    public String extractCity(String address) {
        String[] parts = address.split(" ");
        if (parts.length >= 1) {
            return parts[parts.length - 1];
        }
        return "";
    }



    public String getDay(String date) {
        return date.split("-")[0];
    }

    public String getMonth(String date) {
        return date.split("-")[1];
    }

    public String getYear(String date) {
        return date.split("-")[2];
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public String getBusinessStartDate() {
        return businessStartDate;
    }


    public VATform() {

    }

    public String getBusinessPhoneNumber() {
        return businessPhoneNumber;
    }

    public String getBusinessWebsite() {
        return businessWebsite;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getStudentAddress() {
        return studentAddress;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getBSN() {
        return BSN;
    }

    public String getNatureOfBusiness() {
        return natureOfBusiness;
    }

    public Double getEarningsFirstYear() {
        return earningsFirstYear;
    }


    public Double getCompanySpendings() {
        return companySpendings;
    }

    public String getNoOfEmployers() {
        return noOfEmployers;
    }

    public String getInitialsAndSurname() {
        return initialsAndSurname;
    }

    public String getDateOfSignature() {
        return dateOfSignature;
    }

    public String getStudentPhoneNumber() {
        return studentPhoneNumber;
    }

    public String toString() {
        return "VATform{" +
                "form_id=" + form_id +
                ", student_id='" + student_id + '\'' +
                ", companyName='" + companyName + '\'' +
                ", companyAddress='" + companyAddress + '\'' +
                ", businessStartDate='" + businessStartDate + '\'' +
                ", businessPhoneNumber='" + businessPhoneNumber + '\'' +
                ", businessWebsite='" + businessWebsite + '\'' +
                ", studentName='" + studentName + '\'' +
                ", studentAddress='" + studentAddress + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", BSN='" + BSN + '\'' +
                ", natureOfBusiness='" + natureOfBusiness + '\'' +
                ", earningsFirstYear=" + earningsFirstYear +
                ", companySpendings=" + companySpendings +
                ", noOfEmployers=" + noOfEmployers +
                ", initialsAndSurname='" + initialsAndSurname + '\'' +
                ", dateOfSignature='" + dateOfSignature + '\'' +
                ", studentPhoneNumber='" + studentPhoneNumber + '\'' +
                '}';
    }
}
