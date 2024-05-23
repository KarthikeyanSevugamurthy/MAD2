package com.example.myapplication6;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    DataHelper myDb;
    EditText editName, editSurname, editMarks, editTextId, editMobile;
    Button btnAddData, btnviewAll, btnDelete, btnviewUpdate, btnDatePicker,btnSearchMobile, btnSearchDate,btnviewAll1;
    String selectedDate;
    ListView listView;
    ArrayAdapter<String> adapter;
    ArrayList<String> listItem;
    String mobileNumber;
    String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDb = new DataHelper(this);

        editName = findViewById(R.id.editText_name);
        editSurname = findViewById(R.id.editText_surname);
        editMarks = findViewById(R.id.editText_Marks);
        editTextId = findViewById(R.id.editText_id);
        editMobile = findViewById(R.id.editText_mobile);
        btnAddData = findViewById(R.id.button_add);
        btnviewAll = findViewById(R.id.button_viewAll);
        btnviewAll1 = findViewById(R.id.button_viewAll1);
        btnviewUpdate = findViewById(R.id.button_update);
        btnDelete = findViewById(R.id.button_delete);
        btnDatePicker = findViewById(R.id.button_datePicker);
        btnSearchMobile = findViewById(R.id.button_search_mobile);
        btnSearchDate = findViewById(R.id.button_search_date);
        listView = findViewById(R.id.listView);
        listItem = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItem);

        listView.setAdapter(adapter);

        AddData();
        viewAll();
        viewAll1();
        UpdateData();
        DeleteData();
        selectDate();
        searchByMobile();
        searchByDate();
    }

    public void selectDate() {
        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                                Toast.makeText(MainActivity.this, "Selected Date: " + selectedDate, Toast.LENGTH_SHORT).show();
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });
    }

    public void DeleteData() {
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer deletedRows = myDb.deleteData(editTextId.getText().toString());
                if (deletedRows > 0)
                    Toast.makeText(MainActivity.this, "Data Deleted", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(MainActivity.this, "Data not Deleted", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void UpdateData() {
        btnviewUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isUpdate = myDb.updateData(
                        editTextId.getText().toString(),
                        editName.getText().toString(),
                        editSurname.getText().toString(),
                        editMarks.getText().toString(),
                        editMobile.getText().toString(),
                        selectedDate
                );
                if (isUpdate)
                    Toast.makeText(MainActivity.this, "Data Updated", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(MainActivity.this, "Data not Updated", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void AddData() {
        btnAddData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isInserted = myDb.insertData(
                        editName.getText().toString(),
                        editSurname.getText().toString(),
                        editMarks.getText().toString(),
                        editMobile.getText().toString(),
                        selectedDate
                );
                if (isInserted)
                    Toast.makeText(MainActivity.this, "Data Inserted", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(MainActivity.this, "Data not Inserted", Toast.LENGTH_LONG).show();
            }
        });
    }
    public void searchByMobile() {
        btnSearchMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobile = editMobile.getText().toString().trim();
                if (!mobile.isEmpty()) {
                    Cursor res = myDb.getDataByMobile(mobile);
                    if (res.getCount() == 0) {
                        showMessage("Error", "No data found for mobile number: " + mobile);
                        return;
                    }
                    displayData(res);
                } else {
                    showMessage("Error", "Please enter mobile number to search.");
                }
            }
        });
    }

    public void searchByDate() {
        btnSearchDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Assuming date format is "dd/MM/yyyy"
                String date = selectedDate;
                if (date != null && !date.isEmpty()) {
                    Cursor res = myDb.getDataByDate(date);
                    if (res.getCount() == 0) {
                        showMessage("Error", "No data found for selected date: " + date);
                        return;
                    }
                    displayData(res);
                } else {
                    showMessage("Error", "Please select a date to search.");
                }
            }
        });
    }

    public void displayData(final Cursor cursor) {
        final ArrayList<String> listData = new ArrayList<>();
        final ArrayList<String> mobileNumbers = new ArrayList<>();
        while (cursor.moveToNext()) {
            listData.add("ID: " + cursor.getString(0) +
                    "\nName: " + cursor.getString(1) +
                    "\nSurname: " + cursor.getString(2) +
                    "\nMarks: " + cursor.getString(3) +
                    "\nMobile: " + cursor.getString(4) +
                    "\nDate of Birth: " + cursor.getString(5));
            mobileNumbers.add(cursor.getString(4)); // Store the mobile number
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);

        listView.setAdapter(adapter);

        // Set click listener on ListView items
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Create a PopupMenu
                PopupMenu popupMenu = new PopupMenu(MainActivity.this, view);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

                // Get the selected mobile number
                final String selectedMobileNumber = mobileNumbers.get(position);

                // Set item click listener for the PopupMenu
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        // Handle menu item clicks here
                        int itemId = item.getItemId();
                        if (itemId == R.id.action_sms) {
                            // Handle SMS action
                            String message = "Your message here";
                            Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
                            smsIntent.setData(Uri.parse("smsto:" + selectedMobileNumber)); // Set the recipient's phone number
                            smsIntent.putExtra("sms_body", message);
                            startActivity(smsIntent);
                            return true;
                        } else if (itemId == R.id.action_whatsapp) {
                            // Handle WhatsApp action
                            final String selectedMobileNumber = mobileNumbers.get(position);

                            // Create a Uri with the WhatsApp number
                            Uri uri = Uri.parse("https://wa.me/" + "+91"+selectedMobileNumber);

                            // Create an intent with ACTION_VIEW and the WhatsApp Uri
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                           // intent.setType("text/plain");
                            // Set the message you want to send as an extra
                            intent.putExtra(Intent.EXTRA_TEXT, "Your message here");

                            // Start the activity with the intent
                            startActivity(intent);
                            /*String message = "Your message here";
                            Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                            whatsappIntent.setType("text/plain");
                            whatsappIntent.putExtra(Intent.EXTRA_TEXT, message);
                            whatsappIntent.putExtra("jid", selectedMobileNumber + "@s.whatsapp.net"); //phone number without "+" prefix
                            whatsappIntent.setPackage("com.whatsapp");
                            try {
                                startActivity(whatsappIntent);
                            } catch (ActivityNotFoundException e) {
                                Toast.makeText(MainActivity.this, "WhatsApp is not installed", Toast.LENGTH_SHORT).show();
                            }*/
                            return true;
                        }
                        return false;
                    }
                });

                // Show the PopupMenu
                popupMenu.show();
            }
        });
    }

    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }



    public void viewAll() {
        btnviewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor res = myDb.getAllData();
                if (res.getCount() == 0) {
                    showMessage("Error", "Nothing found");
                    return;
                }

                StringBuffer buffer = new StringBuffer();
                while (res.moveToNext()) {
                    buffer.append("Id: " + res.getString(0) + "\n");
                    buffer.append("Name: " + res.getString(1) + "\n");
                    buffer.append("Surname: " + res.getString(2) + "\n");
                    buffer.append("Marks: " + res.getString(3) + "\n");
                    buffer.append("Mobile: " + res.getString(4) + "\n");
                    buffer.append("Date of Birth: " + res.getString(5) + "\n\n");
                }

                showMessage("Data", buffer.toString());
            }
        });


    }
    public void viewAll1() {
        btnviewAll1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor res = myDb.getAllData();
                if (res.getCount() == 0) {
                    showMessage("Error", "Nothing found");
                    return;
                }

                listItem.clear();
                while (res.moveToNext()) {
                    String data = "Id :" + res.getString(0) + "\n" +
                            "Name :" + res.getString(1) + "\n" +
                            "Surname :" + res.getString(2) + "\n" +
                            "Marks :" + res.getString(3) + "\n"+
                            "Mobile: " + res.getString(4) + "\n"+"Date of Birth: " + res.getString(5) + "\n\n";
                    listItem.add(data);
                }

                // Notify the adapter that the dataset has changed
                adapter.notifyDataSetChanged();
            }
        });
    }



    public void showMessage(String title, String Message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }
}

