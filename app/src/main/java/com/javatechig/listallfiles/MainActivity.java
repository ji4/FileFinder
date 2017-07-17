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

	private File root;
	private ArrayList<File> fileList = new ArrayList<File>();
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
				searchFiles(strFileName);
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
			}
		});
	}

	public void searchFiles(String strFileName){
        //getting SDcard root path
//        root = new File(Environment.getExternalStorageDirectory()
//                .getAbsolutePath());
		root = new File("/storage/emulated/0/Download");
        getfile(root, strFileName);

        for (int i = 0; i < fileList.size(); i++) {
            TextView textView = new TextView(this);
            textView.setText(fileList.get(i).getName());
            textView.setPadding(5, 5, 5, 5);
			textView.setTag("txt");

            System.out.println(fileList.get(i).getName());

            if (fileList.get(i).isFile())
                resultView.addView(textView);
        }
    }

	public ArrayList<File> getfile(File dir, String strFileName) {
		File listFile[] = dir.listFiles();

		if (listFile != null && listFile.length > 0) {
			for (int i = 0; i < listFile.length; i++) {
				if (listFile[i].isDirectory()) //directory
					getfile(listFile[i], strFileName);
				else {  //file
					if(strFileName.length() > 0) { //File Name Inputted
						if(listFile[i].getName().equals(strFileName)) {
							fileList.add(listFile[i]);
						}
					}
					else { //All files
						fileList.add(listFile[i]);
					}
				}
			}
		}
		return fileList;
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
