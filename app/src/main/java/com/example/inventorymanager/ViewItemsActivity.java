package com.example.inventorymanager;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import androidx.appcompat.app.AppCompatActivity;

public class ViewItemsActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private ListView listViewItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_items);

        dbHelper = new DatabaseHelper(this);
        listViewItems = findViewById(R.id.listViewItems);

        displayItems();
    }

    private void displayItems() {
        Cursor cursor = dbHelper.getAllItems();

        String[] fromColumns = {
                DatabaseHelper.COLUMN_NAME,
                DatabaseHelper.COLUMN_QUANTITY,
                DatabaseHelper.COLUMN_PRICE,
                DatabaseHelper.COLUMN_LOCATION
        };

        int[] toViews = {
                R.id.textViewName,
                R.id.textViewQuantity,
                R.id.textViewPrice,
                R.id.textViewLocation
        };

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                this,
                R.layout.item_layout,
                cursor,
                fromColumns,
                toViews,
                0
        );

        listViewItems.setAdapter(adapter);
    }
}