package com.example.xiangzhc.realmsample;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by xiangzhc on 11.08.15.
 */
public class Address2 extends RealmObject {
    @PrimaryKey
    private int id;

    private int refCnt;

    private String country;

    private String city;

    private String street;

    public Address2() {}

    public Address2(int id, String country, String city, String street) {
        this.id = id;
        this.country = country;
        this.city = city;
        this.street = street;
    }

    public int increaseRefCnt() {
        return ++refCnt;
    }

    public int decreaseRefCnt() {
        return --refCnt;
    }

    public void removeIfNoReferrer() {
        if(--refCnt <= 0) {
            super.removeFromRealm();
        }
    }

    @Deprecated
    @Override
    public void removeFromRealm() {
        super.removeFromRealm();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }
}
