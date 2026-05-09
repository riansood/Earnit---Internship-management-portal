package model;

public class Tax {
    public Integer id;
    public Double taxAmount;
    public boolean isPaid;

    public String email;
    public String date;
    public Double earning;

    public Tax(Integer id, Double taxAmount, boolean isPaid, String email, String date, Double earning) {
        this.id = id;
        this.taxAmount = taxAmount;
        this.isPaid = isPaid;
        this.email = email;
        this.date = date;
        this.earning = earning;
    }

    public Tax() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getTaxAmount() {
        return taxAmount;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getEarning() {
        return earning;
    }
}