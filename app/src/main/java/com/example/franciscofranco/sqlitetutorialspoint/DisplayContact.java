package com.example.franciscofranco.sqlitetutorialspoint;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class DisplayContact extends AppCompatActivity {

    int from_Where_I_Am_Coming = 0;
    private DBHelper mydb;

    TextView name;
    TextView phone;
    TextView email;
    TextView street;
    TextView place;
    int id_To_Update = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d("FRANCO_DEBUG", "DisplayCOntact Activity onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_contact);

        name = (TextView) findViewById(R.id.editTextName);
        phone = (TextView) findViewById(R.id.editTextPhone);
        email = (TextView) findViewById(R.id.editTextStreet);
        street = (TextView) findViewById(R.id.editTextEmail);
        place = (TextView) findViewById(R.id.editTextCity);

        mydb = new DBHelper(this);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            int Value = extras.getInt("id");

            if (Value > 0) {
                // means this is the view part not the add contact part
                Cursor rs = mydb.getData(Value);

                rs.moveToFirst();
                id_To_Update = Value;


                String temp_name = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_NAME));
                String temp_phone = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_PHONE));
                String temp_email = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_EMAIL));
                String temp_street = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_STREET));
                String temp_place = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_CITY));

                if (!rs.isClosed()) {
                    rs.close();
                }

                Button b = (Button) findViewById(R.id.button1);
                b.setVisibility(View.INVISIBLE);

                name.setText((CharSequence) temp_name);
                name.setFocusable(false);
                name.setClickable(false);

                phone.setText((CharSequence) temp_phone);
                phone.setFocusable(false);
                phone.setClickable(false);

                email.setText((CharSequence) temp_email);
                email.setFocusable(false);
                email.setClickable(false);

                street.setText((CharSequence) temp_street);
                street.setFocusable(false);
                street.setClickable(false);

                place.setText((CharSequence) temp_place);
                place.setFocusable(false);
                place.setClickable(false);
            }
        }
        Log.d("FRANCO_DEBUG", "oncreate finished Display Contact activity");
    }


    // this is for the activity menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            int Value = extras.getInt("id");
            if (Value > 0) {
                getMenuInflater().inflate(R.menu.display_contact, menu);
            } else {
                getMenuInflater().inflate(R.menu.menu_main, menu);
            }
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("FRANCO_DEBUG", "onOptionsItemSelected");
        super.onOptionsItemSelected(item);
        switch(item.getItemId()) {
            case R.id.Edit_Contact:
                Log.d("FRANCO_DEBUG", "onOptionsItemSelected edit");
                Button b = (Button) findViewById(R.id.button1);
                b.setVisibility(View.VISIBLE);

                name.setEnabled(true);
                name.setFocusableInTouchMode(true);
                name.setClickable(true);

                phone.setEnabled(true);
                phone.setFocusableInTouchMode(true);
                phone.setClickable(true);

                email.setEnabled(true);
                email.setFocusableInTouchMode(true);
                email.setClickable(true);

                street.setEnabled(true);
                street.setFocusableInTouchMode(true);
                street.setClickable(true);

                place.setEnabled(true);
                place.setFocusableInTouchMode(true);
                place.setClickable(true);

                return true;
            case R.id.Delete_Contact:
                Log.d("FRANCO_DEBUG", "onOptionsItemSelected delete");
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.deleteContact)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mydb.deleteContact(id_To_Update);
                                Toast.makeText(getApplicationContext(), "Deleted Successfully",
                                        Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // user cancelled the dialog
                            }
                        });
                AlertDialog d = builder.create();
                d.setTitle("Are you sure");
                d.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void run(View view) {
        Log.d("FRANCO_DEBUG", "inside run");
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int Value = extras.getInt("id");
            if (Value > 0) {
                if (mydb.updateContact(id_To_Update, name.getText().toString(),
                        phone.getText().toString(), email.getText().toString(),
                        street.getText().toString(), place.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "not Updated", Toast.LENGTH_SHORT).show();
                }

            } else {
                if (mydb.insertContact(name.getText().toString(),
                        phone.getText().toString(), email.getText().toString(),
                        street.getText().toString(), place.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "done", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "not done", Toast.LENGTH_SHORT).show();
                }

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        }
    }
}
