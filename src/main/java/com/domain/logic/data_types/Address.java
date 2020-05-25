package com.domain.logic.data_types;

/**
 * Every Business.users.Guest in the system has an address, which contains: country, state, city, postalCode.
 * The class contains getters and setters for the use of the guests.
 */
public class Address {
    private String country;
    private String state;
    private String city;
    private String postalCode;

    public Address(String country, String state, String city, String postalCode) {
        this.country = country;
        this.state = state;
        this.city = city;
        this.postalCode = postalCode;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public String getState() {
        return state;
    }

    public String getCity() {
        return city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    @Override
    public String toString() {
        return "Business.data_types.Address{" +
                "country='" + country + '\'' +
                ", state='" + state + '\'' +
                ", city='" + city + '\'' +
                ", postalCode='" + postalCode + '\'' +
                '}';
    }

    public String getCountry() {
        return country;
    }

    public String getState() {
        return state;
    }

    public String getCity() {
        return city;
    }

    public String getPostalCode() {
        return postalCode;
    }
}
