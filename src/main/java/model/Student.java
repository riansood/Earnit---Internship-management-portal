package model;

public class Student{
    public String address;
    public String email;
    public String password;
    public String name;
    public String BTW_number;
    public String phoneNumber;
    public String salt;


    public Student( String name, String email, String address, String password, String phoneNumber, String BTW_number) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.BTW_number = BTW_number;
    }

    public Student(){}


    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }


    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBTW_number() {
        return BTW_number;
    }

    public String getPhoneNumber(){
        return this.phoneNumber;
    }

    @Override
    public String toString() {
        return String.format("Student Details:\nName: %s\nEmail: %s\nAddress: %s\nBTW Number: %s",
                             name, email, address, BTW_number);
    }

}