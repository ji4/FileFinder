package com.javatechig.listallfiles;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.listallfiles.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends Activity {
    private ArrayList<File> matchedFileList = new ArrayList<>();
	private Button btn_search, btn_searchJpg, btn_searchPng, btn_searchDupFile;
    private EditText et_fileName;
	private LinearLayout ll;
	private EditText et_startDate, et_endDate;
	private EditText et_minSize, et_maxSize;

	//file view
	private ViewStub stubGrid;
	private ViewStub stubList;
	private ListView listView;
	private GridView gridView;
	private ListViewAdapter listViewAdapter;
	private GridViewAdapter gridViewAdapter;
	private int currentViewMode = 0;

	static final int VIEW_MODE_LISTVIEW = 0;
	static final int VIEW_MODE_GRIDVIEW = 1;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		findViews();
		initFileViews();

		btn_search.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View view) {
                //EditTexts ArrayList
				ArrayList<EditText> editTextList = new ArrayList<>(
						Arrays.asList(et_fileName, et_startDate, et_endDate, et_minSize, et_maxSize));

                List<String> textValueList = new ArrayList<>(Arrays.asList(new String[editTextList.size()]));
                Collections.fill(textValueList, null);

				for(int i = 0; i < editTextList.size(); i++){
					String strInputValue = editTextList.get(i).getText().toString().trim();
					if(!strInputValue.matches("")){//has input text
						textValueList.set(i, strInputValue);
					}
                    Log.d("textValueList", String.valueOf(textValueList.get(i)));
				}

				FileSearcher fileSearcher = new FileSearcher(textValueList);
				matchedFileList.clear();
				matchedFileList = fileSearcher.searchFiles();

				setAdapters();
			}
		});


		btn_searchJpg.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View view) {
				//search
				FileSearcher fileSearcher = new FileSearcher("jpg");
				matchedFileList = fileSearcher.searchFiles();

				setAdapters();
			}
		});

		btn_searchPng.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View view) {
				//search
				FileSearcher fileSearcher = new FileSearcher("png");
				matchedFileList = fileSearcher.searchFiles();

				setAdapters();
			}
		});

		btn_searchDupFile.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View view) {
				FileSearcher fileSearcher = new FileSearcher();
				matchedFileList = fileSearcher.searchFiles();

				setAdapters();
			}
		});
	}

	public void initFileViews(){
		stubList = (ViewStub) findViewById(R.id.stub_list);
		stubGrid = (ViewStub) findViewById(R.id.stub_grid);

		//Inflate ViewStub before get view

		stubList.inflate();
		stubGrid.inflate();

		listView = (ListView) findViewById(R.id.mylistview);
		gridView = (GridView) findViewById(R.id.mygridview);

		searchAllFiles();

		//get list of product
		setAdapters();

		//Get current view mode in share reference
		SharedPreferences sharedPreferences = getSharedPreferences("ViewMode", MODE_PRIVATE);
		currentViewMode = sharedPreferences.getInt("currentViewMode", VIEW_MODE_LISTVIEW);//Default is view listview
		//Register item lick
		listView.setOnItemClickListener(onItemClick);
		gridView.setOnItemClickListener(onItemClick);

		switchView();
	}

	public void searchAllFiles(){
        //EditTexts ArrayList
        ArrayList<EditText> editTextList = new ArrayList<>(
                Arrays.asList(et_fileName, et_startDate, et_endDate, et_minSize, et_maxSize));

        List<String> textValueList = new ArrayList<>(Arrays.asList(new String[editTextList.size()]));
        Collections.fill(textValueList, null);


		//search
		FileSearcher fileSearcher = new FileSearcher(textValueList);
		matchedFileList = fileSearcher.searchFiles();
	}

	public void findViews(){
		btn_search = (Button) findViewById(R.id.activity_main_btn_search);
		btn_searchJpg = (Button) findViewById(R.id.activity_main_btn_searchJpg);
		btn_searchPng = (Button) findViewById(R.id.activity_main_btn_searchPng);
		btn_searchDupFile = (Button) findViewById(R.id.activity_main_btn_searchDupFile);
		et_fileName = (EditText) findViewById(R.id.activity_main_et_fileName);
		ll = (LinearLayout) findViewById(R.id.ll);
		//start & end date
		et_startDate = (EditText) findViewById(R.id.activity_main_et_startDate);
		et_endDate = (EditText) findViewById(R.id.activity_main_et_endDate);
		//size
		et_minSize = (EditText) findViewById(R.id.activity_main_et_minSize);
		et_maxSize = (EditText) findViewById(R.id.activity_main_et_maxSize);
	}

	//---------Following are file view functions-------------------

	private void switchView() {

		if(VIEW_MODE_LISTVIEW == currentViewMode) {
			//Display listview
			stubList.setVisibility(View.VISIBLE);
			//Hide gridview
			stubGrid.setVisibility(View.GONE);
		} else {
			//Hide listview
			stubList.setVisibility(View.GONE);
			//Display gridview
			stubGrid.setVisibility(View.VISIBLE);
		}
		setAdapters();
	}

	private void setAdapters() {
		if(VIEW_MODE_LISTVIEW == currentViewMode) {
			listViewAdapter = new ListViewAdapter(this, R.layout.list_item, matchedFileList);
			listView.setAdapter(listViewAdapter);
		} else {
			gridViewAdapter = new GridViewAdapter(this, R.layout.grid_item, matchedFileList);
			gridView.setAdapter(gridViewAdapter);
		}
	}

	AdapterView.OnItemClickListener onItemClick = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			//Do any thing when user click to item
			Toast.makeText(getApplicationContext(), matchedFileList.get(position).getName() + " - " + ListViewAdapter.convertTime(matchedFileList.get(position).lastModified()), Toast.LENGTH_SHORT).show();
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.item_menu_1:
				if(VIEW_MODE_LISTVIEW == currentViewMode) {
					currentViewMode = VIEW_MODE_GRIDVIEW;
				} else {
					currentViewMode = VIEW_MODE_LISTVIEW;
				}
				//Switch view
				switchView();
				//Save view mode in share reference
				SharedPreferences sharedPreferences = getSharedPreferences("ViewMode", MODE_PRIVATE);
				SharedPreferences.Editor editor = sharedPreferences.edit();
				editor.putInt("currentViewMode", currentViewMode);
				editor.commit();

				break;
		}
		return true;
	}


}