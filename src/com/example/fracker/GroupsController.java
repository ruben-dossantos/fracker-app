package com.example.fracker;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class GroupsController extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.groups_controller);
		ListView listView = (ListView) findViewById(R.id.listGroups);
		
		
		 List<String> your_array_list = new ArrayList<String>();
         your_array_list.add("INESC CENAS");
         your_array_list.add("CRITICAL CENAS");
         your_array_list.add("ISEP CENAS");
         your_array_list.add("FEUP CENAS");
         your_array_list.add("PORTO CENAS");
         your_array_list.add("ERASMUS");

         // This is the array adapter, it takes the context of the activity as a 
         // first parameter, the type of list view as a second parameter and your 
         // array as a third parameter.
         ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                 this, 
                 R.layout.black_textview,
                 your_array_list );

         listView.setAdapter(arrayAdapter); 
		
	}
	
}
