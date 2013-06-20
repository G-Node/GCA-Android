/**
 * 
 */
package com.yasiradnan.conference;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
/**
 * @author Adnan
 *
 */
public class LocationMarkers extends FragmentActivity {

	private static GoogleMap supportMap;
	private ArrayList<LatLng> allCoordinates = new ArrayList<LatLng>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.location_marker);
		try {
		     MapsInitializer.initialize(this);
		 } catch (GooglePlayServicesNotAvailableException e) {
		     e.printStackTrace();
		 }
		locationMarkers();
		
	}
	
	public void locationMarkers()
	{
		/*Implement Location Markers*/
		BufferedReader jsonReader = new BufferedReader(new InputStreamReader(this.getResources().openRawResource(R.raw.map)));
		StringBuilder jsonBuilder = new StringBuilder();
		try {
			for (String line = null; (line = jsonReader.readLine()) != null;) {
				jsonBuilder.append(line).append("\n");
			}
			
			JSONTokener tokener = new JSONTokener(jsonBuilder.toString());
			JSONArray jsonArray = new JSONArray(tokener);
			
			
			for (int index = 0; index < jsonArray.length(); index++) 
			{
				
				JSONObject jsonObject = jsonArray.getJSONObject(index);
				/*get Latitude*/
				double getLat = jsonObject.getJSONObject("point").getDouble("lat");
				/*get Longitude*/
				double getLng = jsonObject.getJSONObject("point").getDouble("long");
				/*get Location Type*/
				int gettype = jsonObject.getInt("type");
				/**/
				int getZoomto = jsonObject.getInt("zoomto");
				/*Venue name*/
				String name   = jsonObject.getString("name");
				/**/
				LatLng myLoc = new LatLng(getLat, getLng);
				if(getZoomto == 1)
				{	
					/*only Food and Venue*/
					allCoordinates.add(myLoc);
					
				}
				FragmentManager fmanager = getSupportFragmentManager();
		        Fragment fragment = fmanager.findFragmentById(R.id.map);
				SupportMapFragment supportmapfragment = (SupportMapFragment)fragment;
				supportMap = supportmapfragment.getMap();
			        if(supportMap!=null){
			        	/*implementing custom markers for different location's*/
			        	switch (gettype) {
						case 0:
							/*Conference Venue Marker*/
							supportMap.addMarker(new MarkerOptions().position(myLoc)
				        	          .title(name).icon(BitmapDescriptorFactory.fromResource(R.drawable.conference)));
							break;
						case 1:
							/*University Marker*/
							supportMap.addMarker(new MarkerOptions().position(myLoc)
				        	          .title(name).icon(BitmapDescriptorFactory.fromResource(R.drawable.university)));
							break;
						case 2:
							/*Hotel -1 Marker*/
							supportMap.addMarker(new MarkerOptions().position(myLoc)
				        	          .title(name).icon(BitmapDescriptorFactory.fromResource(R.drawable.hotel_1)));
							break;
						case 3:
							/*Hotel -2 Marker*/
							supportMap.addMarker(new MarkerOptions().position(myLoc)
				        	          .title(name).icon(BitmapDescriptorFactory.fromResource(R.drawable.hotel_2)));
							break;
						case 4:
							/*Transport Marker*/
							supportMap.addMarker(new MarkerOptions().position(myLoc)
				        	          .title(name).icon(BitmapDescriptorFactory.fromResource(R.drawable.transport)));
							break;
						case 5:
							/*Food Marker*/
							supportMap.addMarker(new MarkerOptions().position(myLoc)
				        	          .title(name).icon(BitmapDescriptorFactory.fromResource(R.drawable.food)));
							break;

						default:
							break;
						}
			        }
			      
			}
			
			 /*Automatic zoom level*/
			 LatLngBounds.Builder builder = new LatLngBounds.Builder();
	        	for(LatLng m : allCoordinates) {
	        	    builder = builder.include(m);
	        	    }
	        	LatLngBounds bounds = builder.build();
	        	CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 
	                    this.getResources().getDisplayMetrics().widthPixels, 
	                    this.getResources().getDisplayMetrics().heightPixels, 
	                    50);
	        			/*Move Camera*/
	        			supportMap.moveCamera(cu);
	        			/*Set My Current Location Enable*/
			     		supportMap.setMyLocationEnabled(true);
			        	supportMap.getUiSettings().setMyLocationButtonEnabled(true);
			        	/*Set Comapass Enable*/
			        	supportMap.getUiSettings().setCompassEnabled(true);
			        	/*Set ZoomControl Enable*/
			        	supportMap.getUiSettings().setZoomControlsEnabled(false);
		} catch (FileNotFoundException e) {
			Log.e("jsonFile", "file not found");
		} catch (IOException e) {
			Log.e("jsonFile", "ioerror");
		} catch (JSONException e) {
			Log.e("jsonFile", Log.getStackTraceString(e));
		}
		
	}
}
