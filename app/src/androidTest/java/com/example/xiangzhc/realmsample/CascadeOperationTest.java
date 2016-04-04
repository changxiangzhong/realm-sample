package com.example.xiangzhc.realmsample;

import android.content.Context;
import android.test.AndroidTestCase;
import android.test.mock.MockContext;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by xiangzhc on 4/4/16.
 */
public class CascadeOperationTest extends AndroidTestCase {
    Realm realm;
    Contact contact;

    public void setUp() throws Exception {
        super.setUp();
        Context context = new MockContext();

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
        Address address = new Address(1, "Norway", "Oslo", "Kringsjaa");

        contact = new Contact(1, "Xiangzhong", "Chang", address);

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
                assertEquals(1, realm.where(Contact.class).findAll().size());
                assertEquals(1, realm.where(Address.class).findAll().size());
                // Conclusion, cascade insert is supported.
            }
        });
    }

    public void testCascadeUpdate() {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(contact);// Contact has PK. But Address hasn't

                assertEquals(1, realm.where(Contact.class).findAll().size());
                assertEquals(2, realm.where(Address.class).findAll().size());
                // Since Address doesn't have PK
                // Cascade update is supported.
            }
        });
    }


    public void testCascadeRemoval() {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Contact contact = realm.where(Contact.class).findAll().first();
                contact.removeFromRealm();
                assertNotNull(contact);
                assertEquals(false, contact.isValid());
                assertEquals(false, CascadeOperationTest.this.contact.isValid());

                assertEquals(1, realm.where(Address.class).findAll().size());
                // Cascade removal not support
            }
        });
    }

}
