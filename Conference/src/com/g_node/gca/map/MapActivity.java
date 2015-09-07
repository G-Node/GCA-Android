/**
 * Copyright (c) 2014, German Neuroinformatics Node (G-Node)
 * Copyright (c) 2013, Yasir Adnan <adnan.ayon@gmail.com>
 * Customized for 2nd version by Shumail Mohy-ud-Din <shumailmohyuddin@gmail.com>
 * License: BSD-3 (See LICENSE)
 */

package com.g_node.gca.map;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.g_node.gcaa.R;

public class MapActivity extends FragmentActivity {

    private static GoogleMap supportMap;
    String gtag = "GCA-map";

    private ArrayList<LatLng> allCoordinates = new ArrayList<LatLng>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        
        //getActionBar().setIcon(getResources().getDrawable(R.drawable.icon_maps));
        getActionBar().setTitle("Maps");
        
        getActionBar().setDisplayHomeAsUpEnabled(true);
        int conn_code = MapsInitializer.initialize(this);
        Log.d(gtag,"ImpConnectionCode:" + conn_code);
        if(conn_code==ConnectionResult.SUCCESS){  	
	        locationMarkers();        
	        
	        //Lisener for infoWindow to get LAT & LONG of that marker
	        supportMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
	
	            public void onInfoWindowClick(Marker marker) {
	                LatLng position = marker.getPosition();
	            	Log.i(gtag, "Marker Info Clicked - LAT: " + position.latitude + ", LONG: " + position.longitude);
	            	
	            	Intent navigateIntent = new Intent(Intent.ACTION_VIEW);
	            	
	            	String currentLat =  String.valueOf(position.latitude);
	            	String currentLong =  String.valueOf(position.longitude);
	            	String currLabel = marker.getTitle();
	            	
	            	String geoLocation =  "geo:0,0?q=" + currentLat + "," + currentLong + "(" + currLabel + ")";
					navigateIntent.setData(Uri.parse(geoLocation));
					startActivity(navigateIntent);
	            	
	            }
	        });
	        
	        //adapter for custom info-window - added icon for navigation
	        supportMap.setInfoWindowAdapter(new InfoWindowAdapter() {
				
				@Override
				public View getInfoWindow(Marker arg0) {
					// TODO Auto-generated method stub
					return null;
				}
				
				@Override
				public View getInfoContents(Marker arg0) {
					// TODO Auto-generated method stub
					// Getting view from the layout file info_window_layout
	                View v = getLayoutInflater().inflate(R.layout.info_window_layout, null);
	
	                // Getting reference to the TextView to set title
	                TextView note = (TextView) v.findViewById(R.id.note);
	
	                note.setText(arg0.getTitle() );
	
	                // Returning the view containing InfoWindow contents
	                return v;
				}
				
			});
        }
    }
    
    
    //Main map points function
    public void locationMarkers() {
        /*
         * Implement Location Markers
         */
        BufferedReader jsonReader = new BufferedReader(new InputStreamReader(this.getResources()
                .openRawResource(R.raw.map)));
        StringBuilder jsonBuilder = new StringBuilder();
        try {
            for (String line = null; (line = jsonReader.readLine()) != null;) {
                jsonBuilder.append(line).append("\n");
            }

            JSONTokener tokener = new JSONTokener(jsonBuilder.toString());
            JSONArray jsonArray = new JSONArray(tokener);

            for (int index = 0; index < jsonArray.length(); index++) {

                JSONObject jsonObject = jsonArray.getJSONObject(index);
                /*
                 * getting Latitude
                 */
                double getLat = jsonObject.getJSONObject("point").getDouble("lat");
                /*
                 * getting Longitude
                 */
                double getLng = jsonObject.getJSONObject("point").getDouble("long");
                /*
                 * getting Location Type
                 */
                int gettype = jsonObject.getInt("type");
                /*
                 * getting zoomto value
                 */
                int getZoomto = 1 ;
                if (jsonObject.has("zoomto")){
                	getZoomto = jsonObject.getInt("zoomto");
                }
                /*
                 * Venue name
                 */
                String name = jsonObject.getString("name");
                LatLng myLoc = new LatLng(getLat, getLng);
                if (getZoomto == 1) {
                    /*
                     * Adding only food and venue coordinates for automatic zoom
                     * level
                     */
                    allCoordinates.add(myLoc);
                }
                FragmentManager fmanager = getSupportFragmentManager();
                Fragment fragment = fmanager.findFragmentById(R.id.map);
                SupportMapFragment supportmapfragment = (SupportMapFragment)fragment;
                              
                supportMap = supportmapfragment.getMap();
                
                if (supportMap != null) {
                    /*
                     * implementing different colors markers for different
                     * location's
                     */
                    switch (gettype) {
                        case 0:
                            /*
                             * Conference Venue Marker
                             */
                            supportMap.addMarker(new MarkerOptions()
                                    .position(myLoc)
                                    .title(name)
                                    .icon(BitmapDescriptorFactory
                                            .fromResource(R.drawable.conference)));
                            break;
                        case 1:
                            /*
                             * University Marker
                             */
                            supportMap.addMarker(new MarkerOptions()
                                    .position(myLoc)
                                    .title(name)
                                    .icon(BitmapDescriptorFactory
                                            .fromResource(R.drawable.university)));
                            break;
                        case 2:
                            /*
                             * Hotel -1 Marker
                             */
                            supportMap
                                    .addMarker(new MarkerOptions()
                                            .position(myLoc)
                                            .title(name)
                                            .icon(BitmapDescriptorFactory
                                                    .fromResource(R.drawable.hotel_1)));
                            break;
                        case 3:
                            /*
                             * Hotel -2 Marker
                             */
                            supportMap
                                    .addMarker(new MarkerOptions()
                                            .position(myLoc)
                                            .title(name)
                                            .icon(BitmapDescriptorFactory
                                                    .fromResource(R.drawable.hotel_2)));
                            break;
                        case 4:
                            /*
                             * Transport Marker
                             */
                            supportMap.addMarker(new MarkerOptions()
                                    .position(myLoc)
                                    .title(name)
                                    .icon(BitmapDescriptorFactory
                                            .fromResource(R.drawable.transport)));
                            break;
                        case 5:
                            /*
                             * Food Marker
                             */
                            supportMap.addMarker(new MarkerOptions().position(myLoc).title(name)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.food)));
                            break;

                        default:
                            break;
                    }
                }

            }

            /*
             * Automatic zoom level
             */
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (LatLng m : allCoordinates) {
                builder = builder.include(m);
            }
            LatLngBounds bounds = builder.build();
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 
            		this.getResources().getDisplayMetrics().widthPixels-
            		(int) (this.getResources().getDisplayMetrics().widthPixels*0.1),
                    this.getResources().getDisplayMetrics().heightPixels-
                    (int) (this.getResources().getDisplayMetrics().heightPixels*0.1), 50);
            /*
             * Move Camera
             */
            supportMap.moveCamera(cu);
            /*
             * Set My Current Location Enable
             */
            supportMap.setMyLocationEnabled(true);
            supportMap.getUiSettings().setMyLocationButtonEnabled(true);
            /*
             * Set Compass Enable
             */
            supportMap.getUiSettings().setCompassEnabled(true);
            /*
             * Set Manual ZoomControl Enable
             */
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

