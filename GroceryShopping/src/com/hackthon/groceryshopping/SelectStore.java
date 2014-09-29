/**
 * @author: Ramesh Kuthala
 */
package com.hackthon.groceryshopping;

import java.util.ArrayList;
import java.util.List;


import com.hackthon.utilities.StoreCity;
import com.hackthon.utilities.StoreStates;
import com.hackthon.utilities.Stores;
import com.hackthon.utilities.XMLParser;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class SelectStore extends Activity {

	private static String TAG = "Grocery "+SelectStore.class.toString();
	
	TextView errormsg;
	Button startShopping;
	Spinner stateSelector;
	Spinner citySelector;
	Spinner storeSelector;

	ArrayList<Stores> listOfStores;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_store);
        
        stateSelector = (Spinner) findViewById(R.id.selectstate);
        citySelector = (Spinner) findViewById(R.id.selectcity);
        storeSelector = (Spinner) findViewById(R.id.selectstore);
        
        errormsg = (TextView) findViewById(R.id.errormessage);
        startShopping = (Button)findViewById(R.id.startshopping);

        new DownloadStoreStates(this).execute();
        
    	stateSelector.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				
//				Log.d(TAG, "++Item selected: " + arg0.getItemAtPosition(arg2).toString() 
//						+" at: "+arg2);
				if(arg2 == 0)
					return;
				
				findViewById(R.id.citytext).setVisibility(View.VISIBLE);
				citySelector.setVisibility(View.VISIBLE);
				
				new DownloadStoreCities(SelectStore.this).execute(arg0.getItemAtPosition(arg2).toString());
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				Log.d(TAG, "Nothing selected");
			}
		});

    	citySelector.setOnItemSelectedListener(new OnItemSelectedListener() {
    		
    		@Override
    		public void onItemSelected(AdapterView<?> arg0, View arg1,
    				int arg2, long arg3) {
    			
    			Log.d(TAG, "Item selected: " + arg0.getItemAtPosition(arg2).toString());
    			if(arg2 == 0)
    				return;
    			
    			findViewById(R.id.storetext).setVisibility(View.VISIBLE);
				storeSelector.setVisibility(View.VISIBLE);

				new DownLoadStores(SelectStore.this).execute(arg0.getItemAtPosition(arg2).toString(),stateSelector.getSelectedItem().toString());
    		}
    		
    		@Override
    		public void onNothingSelected(AdapterView<?> arg0) {
    			Log.d(TAG, "Nothing selected");
    		}
    	});

    	storeSelector.setOnItemSelectedListener(new OnItemSelectedListener() {
    		
    		@Override
    		public void onItemSelected(AdapterView<?> arg0, View arg1,
    				int arg2, long arg3) {
    			
    			Log.d(TAG, "Item selected: " + arg0.getItemAtPosition(arg2).toString());
    			if(arg2 == 0)
    			{
//    				errormsg.setVisibility(View.VISIBLE);
//    				errormsg.setText("Select a store");
    				return;
    			}
    			
    			Stores selectedStore = listOfStores.get(arg2-1);
//    			Log.d(TAG, "++Selected store name: "+selectedStore.getStoreName());
//    			Log.d(TAG, "++Selected store address: "+selectedStore.getAddress());
    			CartData.getInstance().setSelectedStore(selectedStore);
    			CartData.getInstance().clearCart();
    			startShopping.setVisibility(View.VISIBLE);
    		}
    		
    		@Override
    		public void onNothingSelected(AdapterView<?> arg0) {
    			Log.d(TAG, "Nothing selected");
    		}
    	});
    	
    	startShopping.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
	            Intent i=new Intent(getApplicationContext(),SearchAndSelectItem.class);
                startActivity(i);
			}
		});
	}
	
	public void populateStateList(List<String> options)
	{
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(SelectStore.this,
				android.R.layout.simple_spinner_item, options);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		stateSelector.setAdapter(dataAdapter);
	}
	
	public void populateCityList(List<String> options)
	{
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(SelectStore.this,
				android.R.layout.simple_spinner_item, options);
		dataAdapter
		.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		citySelector.setAdapter(dataAdapter);
		
	}

	public void populateStoreList(ArrayList<Stores> options)
	{
		listOfStores = options;
		
		System.out.println("Size of stores is: " + options.size());
		List<String> names = new ArrayList<String>();
		names.add("Select an option");
		for(int i = 0; i < options.size(); i++)
			names.add(options.get(i).getStoreName());
		
//		System.out.println("++++++List size :"+ options.size());
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(SelectStore.this,
				android.R.layout.simple_spinner_item, names);
		dataAdapter
		.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		storeSelector.setAdapter(dataAdapter);
//		System.out.println("++++ Populated stores list +++++");
	}
	
	private class DownloadStoreStates extends AsyncTask<Void, Void, ArrayList<StoreStates>>{

		SelectStore storeSelectorInstance = null;
		public DownloadStoreStates(SelectStore storeSelectorInstance)
		{
			this.storeSelectorInstance = storeSelectorInstance;
		}
		
		@Override
		protected ArrayList<StoreStates> doInBackground(Void... params) {
			return XMLParser.getInstance().getStatesList();
		}
		
		@Override
		protected void onPostExecute(ArrayList<StoreStates> arrayOfStates)
		{
			List<String> options = new ArrayList<String>();
			options.add("Select an option");
			for(int i = 0; i < arrayOfStates.size(); i++)
				options.add(arrayOfStates.get(i).getState());

			this.storeSelectorInstance.populateStateList(options);
		}
	}
	private class DownloadStoreCities extends AsyncTask<String, Void, ArrayList<StoreCity>>{
		
		SelectStore storeSelectorInstance = null;
		public DownloadStoreCities(SelectStore instance)
		{
//			System.out.println("+++ Downloading store cities +++++");
			this.storeSelectorInstance = instance;
		}
		
		@Override
		protected ArrayList<StoreCity> doInBackground(String... params) {
			return XMLParser.getInstance().getCitiesList(params[0]);
		}
		
		@Override
		protected void onPostExecute(ArrayList<StoreCity> arrayOfStates)
		{
//			System.out.println("size of city list is: "+ arrayOfStates.size());
			List<String> options = new ArrayList<String>();
			options.add("Select an option");
			for(int i = 0; i < arrayOfStates.size(); i++)
				options.add(arrayOfStates.get(i).getCity());
			
			storeSelectorInstance.populateCityList(options);
		}
	}
	
	private class DownLoadStores extends AsyncTask<String, Void, ArrayList<Stores>>{
		
		SelectStore storeSelectorInstance = null;
		public DownLoadStores(SelectStore instance)
		{
			this.storeSelectorInstance = instance;
		}
		
		@Override
		protected ArrayList<Stores> doInBackground(String... params) {
			return XMLParser.getInstance().getStores(params[0], params[1]);
		}
		
		@Override
		protected void onPostExecute(ArrayList<Stores> arrayOfStates)
		{
			storeSelectorInstance.populateStoreList(arrayOfStates);
		}
	}
	
}
