package com.example.xiangzhc.realmsample;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by xiangzhc on 11.08.15.
 */
public class Contact extends RealmObject {
    @PrimaryKey
    private int id;

    private String firstName;

    private String familyName;

    private Address address;

    public Contact() {}

    public Contact(int id, String firstName, String familyName, Address address) {
        this.id = id;
        this.firstName = firstName;
        this.familyName = familyName;
        this.address = address;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }
}
