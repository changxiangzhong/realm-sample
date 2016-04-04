package com.example.xiangzhc.realmsample;

import android.content.Context;
import android.test.AndroidTestCase;
import android.test.mock.MockContext;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by xiangzhc on 4/4/16.
 */
public class CascadeOperationTest2 extends AndroidTestCase {
    Realm realm;
    Contact2 contact;

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

        contact = new Contact2(1, "Xiangzhong", "Chang", address);

        realm.executeTransaction(new Realm.Transaction() {

            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(contact);
            }
        });
    }

    public void testCascadeInsert() {
        realm.executeTransaction(new Realm.Transaction() {

            @Override
            public void execute(Realm realm) {
                assertEquals(1, realm.where(Contact2.class).findAll().size());
                assertEquals(1, realm.where(Address2.class).findAll().size());
                // Conclusion, cascade insert is supported.
            }
        });
    }

    public void testCascadeUpdate() {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(contact);// Contact has PK. But Address hasn't

                assertEquals(1, realm.where(Contact2.class).findAll().size());
                assertEquals(1, realm.where(Address2.class).findAll().size());
                // Since Address doesn't have PK
                // Cascade update is supported.
            }
        });
    }

    public void testCascadeRemoval() {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Contact2 contact = realm.where(Contact2.class).findAll().first();
                contact.removeFromRealm();
                assertNotNull(contact);
                assertEquals(false, contact.isValid());
                assertEquals(false, CascadeOperationTest2.this.contact.isValid());

                assertEquals(1, realm.where(Address2.class).findAll().size());
                // Cascade removal not support
            }
        });
    }

    /**
     *          | Cascade update            | Cascade removal
     * ------------------------------------------------------------------------
     * W/ PK    | Property  updated         | Property not removed
     * ------------------------------------------------------------------------
     * W/o PK   | Property  duplicated      | Property not removed
     * */
}
