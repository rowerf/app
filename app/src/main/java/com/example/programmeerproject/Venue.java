package com.example.programmeerproject;

public class Venue {
    /* Fields */
    private int venue_id;
    private String venue_name;
    private String category;
    private String telephone;
    private String street;
    private String zipcode;
    private String city;
    private float latitude;
    private float longitude;
    private float distance;

    /* Constructors */
    public Venue() {}

    public Venue(String name, String telephone, String category, String street, String zipcode,
                 String city,float latitude, float longitude, float distance){
        this.venue_name = name;
        this.telephone = telephone;
        this.category = category;
        this.street = street;
        this.zipcode = zipcode;
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
        this.distance = distance;
    }

    /* Properties: Getters and setters */
    public int get_venue_id() {return venue_id; }
    public void set_venue_id(int venue_id) {this.venue_id = venue_id; }

    public String get_venue_name() { return venue_name; }
    public void set_venue_name(String venue_name) {this.venue_name = venue_name; }

    public String getTelephone(){ return telephone; }
    public void setTelephone(String telephone) {this.telephone = telephone; }

    public String getCategory() { return category; }
    public void setCategory(String category) {this.category = category; }

    public String getStreet() {return street; }
    public void setStreet(String street) {this.street = street; }

    public String getZipcode() {return zipcode; }
    public void setZipcode(String zipcode) {this.zipcode = zipcode; }

    public String getCity() {return city; }
    public void setCity(String city) { this.city = city; }

    public float getLatitude() {return latitude; }
    public void setLatitude(float latitude) {this.latitude = latitude; }

    public float getLongitude() {return longitude; }
    public void setLongitude(float longitude) {this.longitude = longitude; }

    public float getDistance() {return distance; }
    public void setDistance(float distance) {this.distance = distance; }
}
