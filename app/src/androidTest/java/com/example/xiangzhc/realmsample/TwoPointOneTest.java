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
public class TwoPointOneTest extends AndroidTestCase {
    Realm realm;

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

        final Contact contact = new Contact(1, "Xiangzhong", "Chang", address);

        final Contact contact2 = new Contact(2, "Yan", "Zhang", address);

        realm.executeTransaction(new Realm.Transaction() {

            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(contact);
                realm.copyToRealmOrUpdate(contact2);
            }
        });
    }

    public void testBasic() {
        assertEquals(2, realm.where(Contact.class).findAll().size());
        assertEquals(2, realm.where(Address.class).findAll().size());
        assertEquals(2, realm.where(Address.class).equalTo("id", 1).findAll().size());


        realm.executeTransaction(new Realm.Transaction() {

            @Override
            public void execute(Realm realm) {
                RealmQuery<Contact> resultSet = realm.where(Contact.class).equalTo("id", 1);
                Contact contact = resultSet.findFirst();
                contact.getAddress().removeFromRealm();

                assertNull(contact.getAddress());// contact.setAddress(null) is NEVER called!

                RealmQuery<Contact> rs1 = realm.where(Contact.class).equalTo("id", 2);
                contact = rs1.findFirst();

                assertNotNull(contact.getAddress());

                assertEquals(1, realm.where(Address.class).findAll().size());
            }
        });

    }
}
