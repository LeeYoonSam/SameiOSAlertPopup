package com.ys.sample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.ys.popup.CustomPopup;
import com.ys.popup.CustomPopup.CustomPopupListener;

public class MainActivity extends ListActivity {

	private static final String ITEM_TITLE = "item_title";
	private static final String ITEM_SUBTITLE = "item_subtitle";
	private static final String ITEM_BTN_TYPE = "item_type";

	// Initialize data
	List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		{
			final Map<String, Object> item = new HashMap<String, Object>();
			item.put(ITEM_TITLE, String.format(getString(R.string.btn_show), "ONE"));
			item.put(ITEM_SUBTITLE, getString(R.string.cancelable));
			item.put(ITEM_BTN_TYPE, Integer.toString(CustomPopup.TYPE_OWN_BTN));
			data.add(item);	
		}

		{
			final Map<String, Object> item = new HashMap<String, Object>();
			item.put(ITEM_TITLE, String.format(getString(R.string.btn_show), "TWO"));
			item.put(ITEM_SUBTITLE, getString(R.string.cancelable));
			item.put(ITEM_BTN_TYPE, Integer.toString(CustomPopup.TYPE_TWO_BTN));
			data.add(item);
		}

		final SimpleAdapter dataAdapter = new SimpleAdapter(this, data, R.layout.cell_base, new String[] {ITEM_TITLE, ITEM_SUBTITLE}, new int[] {R.id.tvTitle, R.id.tvSubTitle});
		setListAdapter(dataAdapter);	

		getListView().setOnItemClickListener(new OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
			{
				int btnType = Integer.parseInt((String)data.get(position).get(ITEM_BTN_TYPE));

				if(btnType == CustomPopup.TYPE_OWN_BTN)
				{
					CustomPopup.createAlert(MainActivity.this, true, btnType, "", (String)data.get(position).get(ITEM_TITLE), "OK", "", null);
					
				}
				else if(btnType == CustomPopup.TYPE_TWO_BTN)
				{
					// set listener
					CustomPopupListener listener = new CustomPopupListener()
					{
						public void onLeftClick(CustomPopup dialogPopup) {
							Toast.makeText(getApplicationContext(), "Click - OK", Toast.LENGTH_SHORT).show();
						};
						
						public void onRightClick(CustomPopup dialogPopup) {
							Toast.makeText(getApplicationContext(), "Click - CANCEL", Toast.LENGTH_SHORT).show();
						};
					};
					
					CustomPopup.createAlert(MainActivity.this, true, btnType, "", (String)data.get(position).get(ITEM_TITLE), "OK", "CANCEL", listener);
				}				
			}
		});
	}
}
