package com.example.newproject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Owner implements Serializable {
    private String first_name;
    private String seconed_name;
    private String email;
    private String password;
    private String phonenumber;
    private String address;
    private String pet_name;
    private String pet_type;
    private String pet_age;
    public String pet_gender;
    public String pet_color;
    public String pet_species;

    public String gender;
    public String age;
    public String additional_info_pet;
    public String imageUrl;
    public String additional_info;


    private double latitude;
    private double longitude;


    public Owner(){

    }

    public Owner(String first_name, String seconed_name, String email, String password, String phonenumber, String address, String pet_name, String pet_type, String pet_age, double latitude, double longitude) {
        this.first_name = first_name;
        this.seconed_name = seconed_name;
        this.email = email;
        this.password = password;
        this.phonenumber = phonenumber;
        this.address = address;
        this.pet_name = pet_name;
        this.pet_type = pet_type;
        this.pet_age = pet_age;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Method to create new Owner and update additional fields
    public static Owner createOwner(String first_name, String seconed_name, String email, String password,
                                    String phonenumber, String address, String pet_name, String pet_type,
                                    String pet_age, double latitude, double longitude,
                                    String gender, String age, String additional_info,
                                    String pet_gender, String pet_color, String pet_species,
                                    String additional_info_pet, String imageUrl) {
        Owner owner = new Owner(first_name, seconed_name, email, password,
                phonenumber, address, pet_name, pet_type,
                pet_age, latitude, longitude);
        owner.setGender(gender);
        owner.setAge(age);
        owner.setAdditional_info(additional_info);
        owner.setPet_gender(pet_gender);
        owner.setPet_color(pet_color);
        owner.setPet_species(pet_species);
        owner.setAdditional_info_pet(additional_info_pet);
        owner.setImageUrl(imageUrl);
        return owner;
    }
    // Convert this Owner object to a Map
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("first_name", first_name);
        result.put("seconed_name", seconed_name);
        result.put("email", email);
        result.put("password", password);
        result.put("phonenumber", phonenumber);
        result.put("address", address);
        result.put("pet_name", pet_name);
        result.put("pet_type", pet_type);
        result.put("pet_age", pet_age);
        result.put("latitude", latitude);
        result.put("longitude", longitude);
        result.put("gender", gender);
        result.put("age", age);
        result.put("additional_info", additional_info);
        result.put("pet_gender", pet_gender);
        result.put("pet_color", pet_color);
        result.put("pet_species", pet_species);
        result.put("additional_info_pet", additional_info_pet);
        result.put("imageUrl", imageUrl);

        return result;
    }


    public String getPet_gender() {
        return pet_gender;
    }

    public void setPet_gender(String pet_gender) {
        this.pet_gender = pet_gender;
    }

    public String getPet_color() {
        return pet_color;
    }

    public void setPet_color(String pet_color) {
        this.pet_color = pet_color;
    }

    public String getPet_species() {
        return pet_species;
    }

    public void setPet_species(String pet_species) {
        this.pet_species = pet_species;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAdditional_info_pet() {
        return additional_info_pet;
    }

    public void setAdditional_info_pet(String additional_info_pet) {
        this.additional_info_pet = additional_info_pet;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAdditional_info() {
        return additional_info;
    }

    public void setAdditional_info(String additional_info) {
        this.additional_info = additional_info;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getSeconed_name() {
        return seconed_name;
    }

    public void setSeconed_name(String seconed_name) {
        this.seconed_name = seconed_name;
    }

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

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPet_name() {
        return pet_name;
    }

    public void setPet_name(String pet_name) {
        this.pet_name = pet_name;
    }

    public String getPet_type() {
        return pet_type;
    }

    public void setPet_type(String pet_type) {
        this.pet_type = pet_type;
    }

    public String getPet_age() {
        return pet_age;
    }

    public void setPet_age(String pet_age) {
        this.pet_age = pet_age;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
