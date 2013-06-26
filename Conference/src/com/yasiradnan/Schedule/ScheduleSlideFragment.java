package com.yasiradnan.Schedule;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.yasiradnan.conference.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class ScheduleSlideFragment extends Fragment {
	static final List<ScheduleItem> dayOne = new ArrayList<ScheduleItem>();
	static final List<ScheduleItem> dayTwo = new ArrayList<ScheduleItem>();
	static final List<ScheduleItem> dayThree = new ArrayList<ScheduleItem>();
	ListView lv1;
	ListView lv2;
	final static String ARG_PAGE = "page";
	private static ViewPager pager;
	private static int pageNumber;
	final static int PAGE_NUM = 3;
	
	private void jsonData()
	{
		try{
			  BufferedReader jsonReader = new BufferedReader (new InputStreamReader(this.getResources().openRawResource(R.raw.program)));
				StringBuilder jsonBuilder = new StringBuilder();
				for (String line = null; (line = jsonReader.readLine()) != null;) {
					jsonBuilder.append(line).append("\n");
				}     

				//Parse Json
				JSONTokener tokener = new JSONTokener(jsonBuilder.toString());
				JSONArray jsonArray = new JSONArray(tokener);
				
				
				for (int index = 0; index < jsonArray.length(); index++) 
				{
					//Set both values into the listview
					Log.e("Taga", String.valueOf(index));
					JSONObject jsonObject = jsonArray.getJSONObject(index);
					String getDate = jsonObject.getString("date");
					/**/
					if(dayOne.isEmpty())
					{
						if("12.09.2012".equalsIgnoreCase(getDate))
				            {
							JSONArray Onejson = new JSONArray(jsonObject.getString("events"));
							
							for(int i =0 ; i<Onejson.length();i++)
							{
								JSONObject jsonObject1 = (JSONObject) Onejson.get(i);
								String time = jsonObject1.getString("time");
								Log.d("KEY Time", time );
								
					            String type= jsonObject1.getString("type"); 
					            Log.d("KEY Type", type );
					           
					            String title= jsonObject1.getString("title");
					            Log.d("KEY Title",title);
					           
					            int typeId = jsonObject1.getInt("type_id");
					           // aa.add(new ScheduleItem(time, title, typeId));
					           dayOne.add(new ScheduleItem(time, title, typeId, getDate));
					            //Getting nesting Events
					            
					            /**/
					            if(typeId ==0)
					            {
					            	JSONArray twoJson = new JSONArray(jsonObject1.getString("events"));
					            
					            	for(int j=0; j< twoJson.length() ; j++){
					            	JSONObject  anotherEvent = (JSONObject) twoJson.get(j);
						          
						            int typeEventId = anotherEvent.getInt("type_id");
						  
					            	if(typeEventId == 1)
					            	{
					            		String EventInfo= anotherEvent.getString("info");
					            		String EventType = anotherEvent.getString("type"); 
					            		String EventTitle= anotherEvent.getString("title");
					            		String Eventtime = anotherEvent.getString("time");
					            		Log.d("Key_match", EventInfo);
					            		dayOne.add(new ScheduleItem(Eventtime, EventTitle, EventInfo, typeEventId,getDate));
					            	
					            	}
					            	else
					            	{
					            		String EventType = anotherEvent.getString("type"); 
						            	String EventTitle= anotherEvent.getString("title");
						            	String Eventtime = anotherEvent.getString("time");
						            	dayOne.add(new ScheduleItem(Eventtime, EventTitle, typeEventId,getDate));
					            	}
					           
					             }
					         }
					    }
				            	
				            }//end of 12
					}//end of flag
					if(dayTwo.isEmpty())
					{
				        if("13.09.2012".equalsIgnoreCase(getDate))
				        	{
				        	JSONArray Twojson = new JSONArray(jsonObject.getString("events"));
							
							for(int i =0 ; i<Twojson.length();i++)
							{
								JSONObject jsonObject1 = (JSONObject) Twojson.get(i);
								String time = jsonObject1.getString("time");
								Log.d("KEY Time", time );
								
					            String type= jsonObject1.getString("type"); 
					            Log.d("KEY Type", type );
					           
					            String title= jsonObject1.getString("title");
					            Log.d("KEY Title",title);
					           
					            int typeId = jsonObject1.getInt("type_id");
					           // aa.add(new ScheduleItem(time, title, typeId));
					          dayTwo.add(new ScheduleItem(time, title, typeId, getDate));
					            //Getting nesting Events
					            
					            /**/
					            if(typeId ==0)
					            {
					            	JSONArray twoJson = new JSONArray(jsonObject1.getString("events"));
					            
					            	for(int j=0; j< twoJson.length() ; j++){
					            	JSONObject  anotherEvent = (JSONObject) twoJson.get(j);
						          
						            int typeEventId = anotherEvent.getInt("type_id");
						  
					            	if(typeEventId == 1)
					            	{
					            		String EventInfo= anotherEvent.getString("info");
					            		String EventType = anotherEvent.getString("type"); 
					            		String EventTitle= anotherEvent.getString("title");
					            		String Eventtime = anotherEvent.getString("time");
					            		Log.d("Key_match", EventInfo);
					            		dayTwo.add(new ScheduleItem(Eventtime, EventTitle, EventInfo, typeEventId,getDate));
					            	
					            	}
					            	else
					            	{
					            		String EventType = anotherEvent.getString("type"); 
						            	String EventTitle= anotherEvent.getString("title");
						            	String Eventtime = anotherEvent.getString("time");
						            	dayTwo.add(new ScheduleItem(Eventtime, EventTitle, typeEventId,getDate));
					            	}
					           
					             }
					         }
					    }
				            	
				            	
				            }//end of 123
					}//end of flag
					if(dayThree.isEmpty())
					{
						
					
						if("14.09.2012".equalsIgnoreCase(getDate))
				         	{
				        	 JSONArray Threejson = new JSONArray(jsonObject.getString("events"));
								
								for(int i =0 ; i<Threejson.length();i++)
								{
									JSONObject jsonObject1 = (JSONObject) Threejson.get(i);
									String time = jsonObject1.getString("time");
									Log.d("KEY Time", time );
									
						            String type= jsonObject1.getString("type"); 
						            Log.d("KEY Type", type );
						           
						            String title= jsonObject1.getString("title");
						            Log.d("KEY Title",title);
						           
						            int typeId = jsonObject1.getInt("type_id");
						           // aa.add(new ScheduleItem(time, title, typeId));
						           dayThree.add(new ScheduleItem(time, title, typeId, getDate));
						            //Getting nesting Events
						            
						            /**/
						            if(typeId ==0)
						            {
						            	JSONArray twoJson = new JSONArray(jsonObject1.getString("events"));
						            
						            	for(int j=0; j< twoJson.length() ; j++){
						            	JSONObject  anotherEvent = (JSONObject) twoJson.get(j);
							          
							            int typeEventId = anotherEvent.getInt("type_id");
							  
						            	if(typeEventId == 1)
						            	{
						            		String EventInfo= anotherEvent.getString("info");
						            		String EventType = anotherEvent.getString("type"); 
						            		String EventTitle= anotherEvent.getString("title");
						            		String Eventtime = anotherEvent.getString("time");
						            		Log.d("Key_match", EventInfo);
						            		dayThree.add(new ScheduleItem(Eventtime, EventTitle, EventInfo, typeEventId,getDate));
						            	
						            	}
						            	else
						            	{
						            		String EventType = anotherEvent.getString("type"); 
							            	String EventTitle= anotherEvent.getString("title");
							            	String Eventtime = anotherEvent.getString("time");
							            	dayThree.add(new ScheduleItem(Eventtime, EventTitle, typeEventId,getDate));
						            	}
						           
						             }
						         }
						    }
				            	
				            	
				            }//
					}//flag
				    }
				
			}//for index
			catch (Exception e) {
			Log.getStackTraceString(e);
		}
	}
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.schedule, container, false);
        
        jsonData();
		
        switch (pageNumber) {
		case 0:
			((TextView) rootView.findViewById(R.id.tvDay)).setText(dayOne.get(pageNumber).getDate().toString());
			final ListView list = (ListView) rootView.findViewById(R.id.list);
			BinderData bindingData = new BinderData(this.getActivity(), dayOne);
			list.setAdapter(bindingData);
			//
			//
			
			list.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> a, View v, int position,
						long id) {
					if (dayOne.get(position).getItemType() == 0
							|| dayOne.get(position).getItemType() == 3
							|| dayOne.get(position).getItemType() == 2)
						return;
					Intent intent = new Intent(ScheduleSlideFragment.this.getActivity(),
							ContentExtended.class);
					intent.putExtra("title", dayOne.get(position).getTitle());
					intent.putExtra("content", dayOne.get(position).getContent());
					startActivity(intent);
				}
			});
			break;
		case 1:
			((TextView) rootView.findViewById(R.id.tvDay)).setText(dayTwo.get(pageNumber).getDate().toString());
			final ListView listOne = (ListView) rootView.findViewById(R.id.list);
			BinderData bindingDataOne = new BinderData(this.getActivity(),dayTwo);
			listOne.setAdapter(bindingDataOne);
			//
			//
			listOne.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> a, View v, int position,
						long id) {
					if (dayTwo.get(position).getItemType() == 0
							||dayTwo.get(position).getItemType() == 3
							|| dayTwo.get(position).getItemType() == 2)
						return;
					Intent intent = new Intent(ScheduleSlideFragment.this.getActivity(),
							ContentExtended.class);
					intent.putExtra("title", dayTwo.get(position).getTitle());
					intent.putExtra("content", dayTwo.get(position).getContent());
					startActivity(intent);
				}
			});
			break;
		case 2:
			((TextView) rootView.findViewById(R.id.tvDay)).setText(dayThree.get(pageNumber).getDate().toString());
			final ListView listTwo = (ListView) rootView.findViewById(R.id.list);
			BinderData bindingDataTwo = new BinderData(this.getActivity(), dayThree);
			listTwo.setAdapter(bindingDataTwo);
			//
			//
			listTwo.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> a, View v, int position,
						long id) {
					if (dayThree.get(position).getItemType() == 0
							|| dayThree.get(position).getItemType() == 3
							|| dayThree.get(position).getItemType() == 2)
						return;
					Intent intent = new Intent(ScheduleSlideFragment.this.getActivity(),
							ContentExtended.class);
					intent.putExtra("title", dayThree.get(position).getTitle());
					intent.putExtra("content", dayThree.get(position).getContent());
					startActivity(intent);
				}
			});
			break;
		default:
			break;
		}

		ImageButton ibLeft = (ImageButton)rootView.findViewById(R.id.ibLeft);
		if(pageNumber == 0)
			ibLeft.setVisibility(View.INVISIBLE);
		
		else
			ibLeft.setVisibility(View.VISIBLE);
		
		ibLeft.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View v) {
	        	if(pager.getCurrentItem() > 0)
	        		pager.setCurrentItem(pager.getCurrentItem() - 1, true);
	        }
		});
		
		ImageButton ibRight = (ImageButton)rootView.findViewById(R.id.ibRight);
		if(pageNumber+1 == PAGE_NUM)
			ibRight.setVisibility(View.INVISIBLE);
		else
			ibRight.setVisibility(View.VISIBLE);
		
		ibRight.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View v) {
	        	if(pager.getCurrentItem() < PAGE_NUM)
	        		pager.setCurrentItem(pager.getCurrentItem() + 1, true);
	        }
		});
        
        return rootView;
    }

	public static Fragment create(int position) {
		Fragment fragment = new ScheduleSlideFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_PAGE, position);
		fragment.setArguments(args);
		return fragment;
	}
	
	public static Fragment create(int position, ViewPager _pager) {
		pageNumber = position;
		pager = _pager;
		Fragment fragment = new ScheduleSlideFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_PAGE, position);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		pageNumber = getArguments().getInt(ARG_PAGE);
	}

}
