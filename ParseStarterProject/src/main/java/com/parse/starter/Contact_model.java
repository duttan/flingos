package com.parse.starter;

import java.util.List;
import java.util.Objects;

public class Contact_model {

    private String contactId, contactName,contactEmail, contactPhoto, contactOtherDetails;

    private List<String> contactNumber;


    public  Contact_model(String contactId, String contactName, List<String> contactNumber, String contactEmail, String contactPhoto, String contactOtherDetails)
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

    public List<String> getContactNumber() {
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


    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Contact_model that = (Contact_model) o;
        return !(this.contactName == that.contactName);

    }

    @Override
    public int hashCode() {

        return Objects.hash(contactName);
    }
}
