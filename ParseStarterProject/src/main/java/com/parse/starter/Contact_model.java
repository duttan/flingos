package com.parse.starter;

public class Contact_model {

    private String contactId, contactName, contactNumber, contactEmail, contactPhoto, contactOtherDetails;


    public  Contact_model(String contactId, String contactName, String contactNumber, String contactEmail, String contactPhoto, String contactOtherDetails)
    {
        this.contactId = contactId;
        this.contactName = contactName;
        this.contactEmail = contactEmail;
        this.contactNumber = contactNumber;
        this.contactPhoto = contactPhoto;
        this.contactOtherDetails = contactOtherDetails;

    }

    public String getContactId() {
        return contactId;
    }

    public String getContactName() {
        return contactName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public String getContactPhoto() {
        return contactPhoto;
    }

    public String getContactOtherDetails() {
        return contactOtherDetails;
    }




}
