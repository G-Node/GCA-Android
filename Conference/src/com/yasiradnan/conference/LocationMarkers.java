/**
 * 
 */
package com.yasiradnan.conference;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
/**
 * @author Adnan
 *
 */
public class LocationMarkers extends FragmentActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.location_marker);
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
				double getLat = jsonObject.getJSONObject("point").getDouble("lat");
				double getLng = jsonObject.getJSONObject("point").getDouble("long");
				String name   = jsonObject.getString("name");
				LatLng myLoc = new LatLng(getLat, getLng);
				Log.d("Long", Double.toString(getLng));				
				FragmentManager fmanager = getSupportFragmentManager();
		        Fragment fragment = fmanager.findFragmentById(R.id.map);
				SupportMapFragment supportmapfragment = (SupportMapFragment)fragment;
				 GoogleMap supportMap = supportmapfragment.getMap();
			        if(supportMap!=null){
			        	Marker dhaka = supportMap.addMarker(new MarkerOptions().position(myLoc)
			        	          .title(name));
			        	supportMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLoc, 15));
			        	supportMap.animateCamera(CameraUpdateFactory.zoomTo(18), 2000, null); 
			        	/*Set My Current Location Enable*/
			     		supportMap.setMyLocationEnabled(true);
			        	supportMap.getUiSettings().setMyLocationButtonEnabled(true);
			        	/*Set Comapass Enable*/
			        	supportMap.getUiSettings().setCompassEnabled(true);
			        	supportMap.getUiSettings().setZoomControlsEnabled(false);
			
				
			}
			}
		} catch (FileNotFoundException e) {
			Log.e("jsonFile", "file not found");
		} catch (IOException e) {
			Log.e("jsonFile", "ioerror");
		} catch (JSONException e) {
			Log.e("jsonFile", "error while parsing json");
		}
		
	}
}
