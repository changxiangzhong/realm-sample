package com.example.xiangzhc.realmsample;

import android.content.Context;
import android.test.AndroidTestCase;
import android.test.mock.MockContext;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;

/**
 * Created by xiangzhc on 4/4/16.
 */
public class TwoPointOneTest2 extends AndroidTestCase {
    Realm realm;

    public void setUp() throws Exception {
        super.setUp();

        RealmConfiguration config = new RealmConfiguration.Builder(getContext()).
                schemaVersion(1).
                name("test.realm").
                inMemory().deleteRealmIfMigrationNeeded().build();

        realm = Realm.getInstance(config);

        setupData();
    }

    public void tearDown() {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.deleteAll();
            }
        });
        realm.close();
    }

    private void setupData() {
        Address2 address = new Address2(1, "Norway", "Oslo", "Kringsjaa");

        final Contact2 contact = new Contact2(1, "Xiangzhong", "Chang", address);

        final Contact2 contact2 = new Contact2(2, "Yan", "Zhang", address);

        realm.executeTransaction(new Realm.Transaction() {

            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(contact);
                realm.copyToRealmOrUpdate(contact2);
            }
        });
    }

    public void testBasic() {
        assertEquals(2, realm.where(Contact2.class).findAll().size());
        assertEquals(1, realm.where(Address2.class).findAll().size());
        assertEquals(1, realm.where(Address2.class).equalTo("id", 1).findAll().size());


        realm.executeTransaction(new Realm.Transaction() {

            @Override
            public void execute(Realm realm) {
                RealmQuery<Contact2> resultSet = realm.where(Contact2.class).equalTo("id", 1);
                Contact2 contact = resultSet.findFirst();
                contact.getAddress().removeFromRealm(true);

                assertNull(contact.getAddress());// contact.setAddress(null) is NEVER called!

                RealmQuery<Contact2> rs1 = realm.where(Contact2.class).equalTo("id", 2);
                contact = rs1.findFirst();

                assertNull(contact.getAddress());

                assertEquals(0, realm.where(Address.class).findAll().size());
            }
        });

    }

    public void testRefCounter() {
        assertEquals(2, realm.where(Contact2.class).findAll().size());
        assertEquals(1, realm.where(Address2.class).findAll().size());
        assertEquals(1, realm.where(Address2.class).equalTo("id", 1).findAll().size());


        realm.executeTransaction(new Realm.Transaction() {

            @Override
            public void execute(Realm realm) {
                RealmQuery<Contact2> resultSet = realm.where(Contact2.class).equalTo("id", 1);
                Contact2 contact = resultSet.findFirst();
                contact.getAddress().removeIfNoReferrer();

                assertNull(contact.getAddress());// contact.setAddress(null) is NEVER called!

                RealmQuery<Contact2> rs1 = realm.where(Contact2.class).equalTo("id", 2);
                contact = rs1.findFirst();

                assertNotNull(contact.getAddress());

                assertEquals(1, realm.where(Address.class).findAll().size());
            }
        });

    }
}
