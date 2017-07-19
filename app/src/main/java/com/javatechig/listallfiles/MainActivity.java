package com.javatechig.listallfiles;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.listallfiles.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends Activity {
    private ArrayList<File> matchedFileList = new ArrayList<File>();
	private LinearLayout resultView;
	Button btn_search, btn_searchDate, btn_searchSize, btn_searchJpg, btn_searchPng, btn_searchDupFile ,btn_clear;
    EditText et_fileName;
	ScrollView scrollView;
	LinearLayout ll;
	EditText et_startYear, et_startMonth, et_startDay, et_endYear, et_endMonth, et_endDay;
	EditText et_minSize, et_maxSize;

	//View
	private ViewStub stubGrid;
	private ViewStub stubList;
	private ListView listView;
	private GridView gridView;
	private ListViewAdapter listViewAdapter;
	private GridViewAdapter gridViewAdapter;
	private List<Product> productList;
	private int currentViewMode = 0;

	static final int VIEW_MODE_LISTVIEW = 0;
	static final int VIEW_MODE_GRIDVIEW = 1;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		findAllViews();
		setFileViews();

		btn_search.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View view) {
				String strFileName = et_fileName.getText().toString();

				//search
				FileSearcher fileSearcher = new FileSearcher(strFileName);
				matchedFileList = fileSearcher.searchFiles();

				displaySearchResult();
			}
		});

		btn_searchDate.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View view) {
				int startYear = Integer.parseInt(et_startYear.getText().toString().trim());
				int startMonth = Integer.parseInt(et_startMonth.getText().toString().trim());
				int startDay = Integer.parseInt(et_startDay.getText().toString().trim());

				int endYear = Integer.parseInt(et_endYear.getText().toString().trim());
				int endMonth = Integer.parseInt(et_endMonth.getText().toString().trim());
				int endDay = Integer.parseInt(et_endDay.getText().toString().trim());

				Date startDate = setDate(startYear, startMonth, startDay, false);
				Date endDate = setDate(endYear, endMonth, endDay, true);

				//search
				FileSearcher fileSearcher = new FileSearcher(startDate, endDate);
				matchedFileList = fileSearcher.searchFiles();

				displaySearchResult();
			}
		});

		btn_searchSize.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View view) {
				long min_size = Long.parseLong(et_minSize.getText().toString().trim()) * 1024 * 1024; //Convert megabytes to bytes
				long max_size = Long.parseLong(et_maxSize.getText().toString().trim()) * 1024 * 1024; //Convert megabytes to bytes

				//search
				FileSearcher fileSearcher = new FileSearcher(min_size, max_size);
				matchedFileList = fileSearcher.searchFiles();

				displaySearchResult();
			}
		});

		btn_searchJpg.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View view) {
				//search
				FileSearcher fileSearcher = new FileSearcher("jpg");
				matchedFileList = fileSearcher.searchFiles();

				displaySearchResult();
			}
		});

		btn_searchPng.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View view) {
				//search
				FileSearcher fileSearcher = new FileSearcher("png");
				matchedFileList = fileSearcher.searchFiles();

				displaySearchResult();
			}
		});

		btn_searchDupFile.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View view) {
				FileSearcher fileSearcher = new FileSearcher();
				matchedFileList = fileSearcher.searchFiles();

				displaySearchResult();
			}
		});
	}

	public Date setDate(int year, int month, int day, Boolean isEndDate){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month - 1);
		if(isEndDate) day++;
		calendar.set(Calendar.DAY_OF_MONTH, day);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);// for 0 min
		calendar.set(Calendar.SECOND, 0);// for 0 sec
		Date date = new Date(calendar.getTimeInMillis());

		return date;
	}

	public void displaySearchResult(){
		for (int i = 0; i < matchedFileList.size(); i++) {
			TextView textView = new TextView(this);
			textView.setText(matchedFileList.get(i).getName());
			textView.setPadding(5, 5, 5, 5);

			System.out.println(matchedFileList.get(i).getName());

			if (matchedFileList.get(i).isFile())
				resultView.addView(textView);
		}
	}

	public void setFileViews(){
		stubList = (ViewStub) findViewById(R.id.stub_list);
		stubGrid = (ViewStub) findViewById(R.id.stub_grid);

		//Inflate ViewStub before get view

		stubList.inflate();
		stubGrid.inflate();

		listView = (ListView) findViewById(R.id.mylistview);
		gridView = (GridView) findViewById(R.id.mygridview);

		//get list of product
		getProductList();

		//Get current view mode in share reference
		SharedPreferences sharedPreferences = getSharedPreferences("ViewMode", MODE_PRIVATE);
		currentViewMode = sharedPreferences.getInt("currentViewMode", VIEW_MODE_LISTVIEW);//Default is view listview
		//Register item lick
		listView.setOnItemClickListener(onItemClick);
		gridView.setOnItemClickListener(onItemClick);

		switchView();


	}

	public void findAllViews(){
//		resultView = (LinearLayout) findViewById(R.id.view);
//		scrollView = (ScrollView) findViewById(R.id.scrollView);
		btn_search = (Button) findViewById(R.id.activity_main_btn_search);
//		btn_clear = (Button) findViewById(R.id.activity_main_btn_clear);
		btn_searchDate = (Button) findViewById(R.id.activity_main_btn_searchDate);
		btn_searchSize = (Button) findViewById(R.id.activity_main_btn_searchSize);
		btn_searchJpg = (Button) findViewById(R.id.activity_main_btn_searchJpg);
		btn_searchPng = (Button) findViewById(R.id.activity_main_btn_searchPng);
		btn_searchDupFile = (Button) findViewById(R.id.activity_main_btn_searchDupFile);
		et_fileName = (EditText) findViewById(R.id.activity_main_et_fileName);
		ll = (LinearLayout) findViewById(R.id.ll);
		//start & end date
		et_startYear = (EditText) findViewById(R.id.activity_main_et_startYear);
		et_startMonth = (EditText) findViewById(R.id.activity_main_et_startMonth);
		et_startDay = (EditText) findViewById(R.id.activity_main_et_startDay);
		et_endYear = (EditText) findViewById(R.id.activity_main_et_endYear);
		et_endMonth = (EditText) findViewById(R.id.activity_main_et_endMonth);
		et_endDay = (EditText) findViewById(R.id.activity_main_et_endDay);
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
			listViewAdapter = new ListViewAdapter(this, R.layout.list_item, productList);
			listView.setAdapter(listViewAdapter);
		} else {
			gridViewAdapter = new GridViewAdapter(this, R.layout.grid_item, productList);
			gridView.setAdapter(gridViewAdapter);
		}
	}

	public List<Product> getProductList() {
		//pseudo code to get product, replace your code to get real product here
		productList = new ArrayList<>();
		productList.add(new Product(R.drawable.icon_android, "Title 1", "This is description 1"));
		productList.add(new Product(R.drawable.icon_android, "Title 2", "This is description 2"));
		productList.add(new Product(R.drawable.icon_android, "Title 3", "This is description 3"));
		productList.add(new Product(R.drawable.icon_android, "Title 4", "This is description 4"));
		productList.add(new Product(R.drawable.icon_android, "Title 5", "This is description 5"));
		productList.add(new Product(R.drawable.icon_android, "Title 6", "This is description 6"));
		productList.add(new Product(R.drawable.icon_android, "Title 7", "This is description 7"));
		productList.add(new Product(R.drawable.icon_android, "Title 8", "This is description 8"));
		productList.add(new Product(R.drawable.icon_android, "Title 9", "This is description 9"));
		productList.add(new Product(R.drawable.icon_android, "Title 10", "This is description 10"));

		return productList;
	}

	AdapterView.OnItemClickListener onItemClick = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			//Do any thing when user click to item
			Toast.makeText(getApplicationContext(), productList.get(position).getTitle() + " - " + productList.get(position).getDescription(), Toast.LENGTH_SHORT).show();
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
