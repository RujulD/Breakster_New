package com.example.rujul.breakster;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class SelectCategoryList extends AppCompatActivity {
    ListView listView ;
EditText search;
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_category_list);
       search  = (EditText) findViewById(R.id.editText);


        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.list);
        listView.setTextFilterEnabled(true);
        // Defined Array values to show in ListView
        String[] values = new String[] { "Burgers",
                "Namkeens",
                "Our Specials",
                "Rolls",
                "Samosas",
                "Sandwiches",
                "Toasts",
                "Vadas",
                "Wraps"
        };

        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data

         adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);


        // Assign adapter to ListView
        listView.setAdapter(adapter);

        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition     = position;

                // ListView Clicked item value
                String  itemValue    = (String) listView.getItemAtPosition(position);

                // Show Alert
                Toast.makeText(getApplicationContext(),
                        "Position :"+itemPosition+"  ListItem : " +itemValue , Toast.LENGTH_LONG)
                        .show();

                Intent i = new Intent(SelectCategoryList.this, UploadService.class);
                i.putExtra("categoryname", itemValue );
                startActivity(i);

            }

        });
        search.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {

            }

            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {

            }

            public void afterTextChanged(Editable arg0) {
                SelectCategoryList.this.adapter.getFilter().filter(arg0);

            }
        });

    }

}
