package lab03.eim.systems.cs.pub.phonedialer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class PhoneDialerActivity extends AppCompatActivity {

    private EditText phoneText;
    private ImageButton callButton;
    private ImageButton hangButton;
    private ImageButton backspaceButton;
    private ImageButton contactsImageButton;
    private Button genericButton;

    private class GenericButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String text = phoneText.getText().toString() + ((Button)view).getText().toString();
            phoneText.setText(text);
        }
    }

    private class BackspaceButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String text = phoneText.getText().toString();
            if (text.length() > 0)
                phoneText.setText(text.substring(0, text.length() - 1));
        }
    }

    private class HangUpButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            finish();
        }
    }

    private ContactsImageButtonClickListener contactsImageButtonClickListener = new ContactsImageButtonClickListener();
    private class ContactsImageButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String phoneNumber = phoneText.getText().toString();
            if (phoneNumber.length() > 0) {
                Intent intent = new Intent("ro.pub.cs.systems.eim.lab04.contactsmanager.intent.action.ContactsManagerActivity");
                intent.putExtra("ro.pub.cs.systems.eim.lab04.contactsmanager.PHONE_NUMBER_KEY", phoneNumber);
                startActivityForResult(intent, Constants.CONTACTS_MANAGER_REQUEST_CODE);
            } else {
                Toast.makeText(getApplication(), getResources().getString(R.string.phone_error), Toast.LENGTH_LONG).show();
            }
        }
    }

    private class CallButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (ContextCompat.checkSelfPermission(PhoneDialerActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        PhoneDialerActivity.this,
                        new String[]{Manifest.permission.CALL_PHONE},
                        Constants.PERMISSION_REQUEST_CALL_PHONE);
            } else {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + phoneText.getText().toString()));
                startActivity(intent);
            }
        }
    }

    private CallButtonClickListener callButtonClickListener = new CallButtonClickListener();
    private HangUpButtonClickListener hangUpButtonClickListener = new HangUpButtonClickListener();
    private GenericButtonClickListener genericButtonClickListener = new GenericButtonClickListener();
    private BackspaceButtonClickListener backspaceButtonClickListener = new BackspaceButtonClickListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_dialer);


        phoneText = (EditText)findViewById(R.id.phone_number_edit_text);
        backspaceButton = (ImageButton)findViewById(R.id.backspace_image_button);
        backspaceButton.setOnClickListener(backspaceButtonClickListener);
        hangButton = (ImageButton)findViewById(R.id.hangup_call_button);
        hangButton.setOnClickListener(hangUpButtonClickListener);
        callButton = (ImageButton)findViewById(R.id.phone_call_button);
        callButton.setOnClickListener(callButtonClickListener);
        for (int i = 0; i < Constants.buttonIds.length; i++) {
            genericButton = (Button)findViewById(Constants.buttonIds[i]);
            genericButton.setOnClickListener(genericButtonClickListener);
        }
        contactsImageButton = (ImageButton)findViewById(R.id.contacts_button);
        contactsImageButton.setOnClickListener(contactsImageButtonClickListener);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case Constants.CONTACTS_MANAGER_REQUEST_CODE:
                Toast.makeText(this, "Activity returned with result " + resultCode, Toast.LENGTH_LONG).show();
                break;
        }
    }
}