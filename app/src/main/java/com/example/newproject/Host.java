package com.example.newproject;

import com.google.firebase.database.PropertyName;

import java.util.HashMap;
import java.util.Map;

public class Host {




    private String first_name;
    private String last_name;
    private String email;
    private String password;
    private String phonenumber;
    private String address;
    private String additional_info;
    public String gender;
    public String dateOfBirth;
    public String StartDate;
    public String EndDate;


    private String SelectedPetNames;

    public String other_pet_type;
    public String imageUrl;
    public int counter;
    public int RoomNumber; // Note the uppercase 'R'
    public int price;



    private double latitude;
    private double longitude;
    private String propertyType;
    private String haveOtherPet;
    private String amenities;


    public Host (){}

    public Host(String first_name, String last_name, String email, String password, String phonenumber, String address, String additional_info, double latitude, double longitude) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
        this.phonenumber = phonenumber;
        this.address = address;
        this.additional_info = additional_info;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Method to create new Host and update additional fields
    public static Host createHost(String first_name, String last_name, String email, String password,
                                  String phonenumber, String address, String additional_info,
                                  double latitude, double longitude, String gender,
                                  int RoomNumber, String imageUrl,String StartDate,String EndDate,int price,String dateOfBirth,int counter,String other_pet_type) {
        Host host = new Host(first_name, last_name, email, password,
                phonenumber, address, additional_info, latitude, longitude);

        host.setGender(gender);
        host.setRoomNumber(RoomNumber);
        host.setImageUrl(imageUrl);
        host.setStartDate(StartDate);
        host.setEndDate(EndDate);
        host.setPrice(price);
        host.setDateOfBirth(dateOfBirth);
        host.setCounter(counter);
        host.setOther_pet_type(other_pet_type);


        return host;
    }

    // Convert this Host object to a Map
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("first_name", first_name);
        result.put("last_name", last_name);
        result.put("email", email);
        result.put("password", password);
        result.put("phonenumber", phonenumber);
        result.put("address", address);
        result.put("additional_info", additional_info);
        result.put("latitude", latitude);
        result.put("longitude", longitude);
        result.put("gender", gender);
        result.put("RoomNumber", RoomNumber);
        result.put("price", price);
        result.put("imageUrl", imageUrl);
        result.put("StartDate",StartDate);
        result.put("EndDate",EndDate);
        result.put("dateOfBirth",dateOfBirth);
        result.put("counter",counter);
        result.put("other_pet_type",other_pet_type);



        return result;
    }





    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
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

    public String getAdditional_info() {
        return additional_info;
    }

    public void setAdditional_info(String additional_info) {
        this.additional_info = additional_info;
    }



    public String getGender() {
        return gender;
    }


    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }




    public String getSelectedPetNames() {
        return SelectedPetNames;
    }

    public void setSelectedPetNames(String selectedPetNames) {
        SelectedPetNames = selectedPetNames;
    }



    public String getOther_pet_type() {
        return other_pet_type;
    }

    public void setOther_pet_type(String other_pet_type) {
        this.other_pet_type = other_pet_type;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
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

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public String getHaveOtherPet() {
        return haveOtherPet;
    }

    public void setHaveOtherPet(String haveOtherPet) {
        this.haveOtherPet = haveOtherPet;
    }

    public String getAmenities() {
        return amenities;
    }

    public void setAmenities(String amenities) {
        this.amenities = amenities;
    }

    // Assuming this is your variable

    @PropertyName("EndDate")
    public String getEndDate() {
        return this.EndDate;
    }

    @PropertyName("EndDate")
    public void setEndDate(String endDate) {
        this.EndDate = endDate;
    }



    @PropertyName("StartDate")
    public String getStartDate() {
        return this.StartDate;
    }


    @PropertyName("StartDate")
    public void setStartDate(String startDate) {
        this.StartDate = startDate;
    }




    @PropertyName("RoomNumber") // Keep the 'R' uppercase to match with Firebase
    public int getRoomNumber() {
        return RoomNumber;
    }

    @PropertyName("RoomNumber") // Keep the 'R' uppercase to match with Firebase
    public void setRoomNumber(int RoomNumber) {
        this.RoomNumber = RoomNumber;
    }

}
