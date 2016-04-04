package com.example.xiangzhc.realmsample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.Iterator;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Realm realm = Realm.getInstance(getApplicationContext());

        Address address = new Address(1, "Norway", "Oslo", "Kringsjaa");

        final Contact contact = new Contact(1, "Xiangzhong", "Chang", address);

        final Contact contact2 = new Contact(2, "Xiangzhong2", "Chang2", address);

        realm.executeTransaction(new Realm.Transaction() {

            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(contact);
                realm.copyToRealmOrUpdate(contact2);

                Log.v("--->", "contacts.len = " + realm.where(Contact.class).findAll().size() + ", address.len " + realm.where(Address.class).findAll().size());

                /**
                 * In realm, each property object is a deep copy.
                 * So when removing contact1.address, contact2.address will not get affected,
                 * even though (contact1.address.id == contact2.address.id), i.e. Primary key is the same
                 *
                 * The primary key is useful while Contact get updated, contact.address.id will be
                 * used for saveOrUpdate.
                 */
                RealmQuery<Contact> rs = realm.where(Contact.class);
                rs.findFirst().getAddress().removeFromRealm();

                RealmQuery<Contact> rs1 = realm.where(Contact.class).equalTo("id", 1);
                Log.v("--->", "contact.address = " + rs1.findFirst().getAddress());

                RealmQuery<Contact> rs2 = realm.where(Contact.class).equalTo("id", 2);
                Log.v("--->", "contact2.address = " + rs2.findFirst().getAddress());

                Log.v("--->", "contacts.len = " + realm.where(Contact.class).findAll().size() + ", address.len " + realm.where(Address.class).findAll().size());

                RealmResults<Address> addr = realm.where(Address.class).findAll();

                Iterator<Address> iter = addr.iterator();
                while (iter.hasNext()) {
                    Address address = iter.next();

                    Log.v("--->", address.toString());
                }
            }
        });

    }

}
