package com.javatechig.listallfiles;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.example.listallfiles.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends Activity {

    private ArrayList<File> matchedFileList = new ArrayList<File>();
	private LinearLayout resultView;
	Button btn_search, btn_clear;
	Button btn_searchDate;
    EditText et_fileName;
	ScrollView scrollView;
	LinearLayout ll;
	EditText et_startYear, et_startMonth, et_startDay, et_endYear, et_endMonth, et_endDay;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		findAllViews();

		btn_search.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View view) {
				String strFileName = et_fileName.getText().toString();

				//search
				FileSearcher fileSearcher = new FileSearcher();
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
	}

	public Date setDate(int year, int month, int day, Boolean isEndDate){
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month - 1);
		if(isEndDate) day++;
		calendar.set(Calendar.DAY_OF_MONTH, day);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);// for 0 min
		calendar.set(Calendar.SECOND, 0);// for 0 sec
		System.out.println("calendar.getTime(): "+calendar.getTime());// print 'Mon Mar 28 06:00:00 ALMT 2016'

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

	public void findAllViews(){
		resultView = (LinearLayout) findViewById(R.id.view);
		scrollView = (ScrollView) findViewById(R.id.scrollView);
		btn_search = (Button) findViewById(R.id.activity_main_btn_search);
//		btn_clear = (Button) findViewById(R.id.activity_main_btn_clear);
		btn_searchDate = (Button) findViewById(R.id.activity_main_btn_searchDate);
		et_fileName = (EditText) findViewById(R.id.activity_main_et_fileName);
		ll = (LinearLayout) findViewById(R.id.ll);
		//start date
		et_startYear = (EditText) findViewById(R.id.activity_main_et_startYear);
		et_startMonth = (EditText) findViewById(R.id.activity_main_et_startMonth);
		et_startDay = (EditText) findViewById(R.id.activity_main_et_startDay);
		//end date
		et_endYear = (EditText) findViewById(R.id.activity_main_et_endYear);
		et_endMonth = (EditText) findViewById(R.id.activity_main_et_endMonth);
		et_endDay = (EditText) findViewById(R.id.activity_main_et_endDay);
	}

}
