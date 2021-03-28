package ro.pub.cs.systems.eim.lab04.contactsmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class ContactsManagerActivity extends AppCompatActivity {

    private static final String showText= "Show additional fields";
    private static final String hideText = "Hide additional fields";
    private static final int CONTACTS_MANAGER_REQUEST_CODE = 2017;

    public class ShowHideOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            LinearLayout detailsLayout = (LinearLayout)findViewById(R.id.details_layout);
            Button showHide = (Button)findViewById(R.id.show__hide_button);
            if (detailsLayout.getVisibility() == LinearLayout.GONE) {
                detailsLayout.setVisibility(LinearLayout.VISIBLE);
                showHide.setText(hideText);
            }
            else {
                detailsLayout.setVisibility(LinearLayout.GONE);
                showHide.setText(showText);
            }
        }
    }

    public class SaveOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            String name = ((EditText)findViewById(R.id.name_text)).getText().toString();
            String phone = ((EditText)findViewById(R.id.phone_number)).getText().toString();
            String email = ((EditText)findViewById(R.id.email_address)).getText().toString();
            String address = ((EditText)findViewById(R.id.address)).getText().toString();
            String jobTitle = ((EditText)findViewById(R.id.position_text)).getText().toString();
            String company = ((EditText)findViewById(R.id.company_text)).getText().toString();
            String website = ((EditText)findViewById(R.id.website_text)).getText().toString();
            String im = ((EditText)findViewById(R.id.messaging_id)).getText().toString();

            Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
            intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
            if (name != null) {
                intent.putExtra(ContactsContract.Intents.Insert.NAME, name);
            }
            if (phone != null) {
                intent.putExtra(ContactsContract.Intents.Insert.PHONE, phone);
            }
            if (email != null) {
                intent.putExtra(ContactsContract.Intents.Insert.EMAIL, email);
            }
            if (address != null) {
                intent.putExtra(ContactsContract.Intents.Insert.POSTAL, address);
            }
            if (jobTitle != null) {
                intent.putExtra(ContactsContract.Intents.Insert.JOB_TITLE, jobTitle);
            }
            if (company != null) {
                intent.putExtra(ContactsContract.Intents.Insert.COMPANY, company);
            }
            ArrayList<ContentValues> contactData = new ArrayList<ContentValues>();
            if (website != null) {
                ContentValues websiteRow = new ContentValues();
                websiteRow.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE);
                websiteRow.put(ContactsContract.CommonDataKinds.Website.URL, website);
                contactData.add(websiteRow);
            }
            if (im != null) {
                ContentValues imRow = new ContentValues();
                imRow.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE);
                imRow.put(ContactsContract.CommonDataKinds.Im.DATA, im);
                contactData.add(imRow);
            }
            intent.putParcelableArrayListExtra(ContactsContract.Intents.Insert.DATA, contactData);
            startActivityForResult(intent, CONTACTS_MANAGER_REQUEST_CODE);
        }
    }

    public class CancelOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            setResult(Activity.RESULT_CANCELED, new Intent());
            finish();
        }
    }

    ShowHideOnClickListener showHideOnClickListener = new ShowHideOnClickListener();
    SaveOnClickListener saveOnClickListener = new SaveOnClickListener();
    CancelOnClickListener cancelOnClickListener = new CancelOnClickListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_manager);
        LinearLayout detailsLayout = (LinearLayout)findViewById(R.id.details_layout);
        detailsLayout.setVisibility(LinearLayout.GONE);
        Button showHide = (Button)findViewById(R.id.show__hide_button);
        showHide.setText(showText);
        Button save = (Button)findViewById(R.id.save_button);
        Button cancel = (Button)findViewById(R.id.cancel_button);
        showHide.setOnClickListener(showHideOnClickListener);
        save.setOnClickListener(saveOnClickListener);
        cancel.setOnClickListener(cancelOnClickListener);

        Intent intent = getIntent();
        if (intent != null) {
            String phone = intent.getStringExtra("ro.pub.cs.systems.eim.lab04.contactsmanager.PHONE_NUMBER_KEY");
            if (phone != null) {
                EditText phoneText = (EditText)findViewById(R.id.phone_number);
                phoneText.setText(phone);
            } else {
                Toast.makeText(this, getResources().getString(R.string.phone_error), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch(requestCode) {
            case CONTACTS_MANAGER_REQUEST_CODE:
                setResult(resultCode, new Intent());
                finish();
                break;
        }
    }
}