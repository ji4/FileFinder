package com.javatechig.listallfiles;

import java.io.File;
import java.util.ArrayList;

import com.example.listallfiles.R;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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
    EditText et_fileName;
	ScrollView scrollView;
	LinearLayout ll;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		resultView = (LinearLayout) findViewById(R.id.view);
		scrollView = (ScrollView) findViewById(R.id.scrollView);
		btn_search = (Button) findViewById(R.id.activity_main_btn_search);
//		btn_clear = (Button) findViewById(R.id.activity_main_btn_clear);
		et_fileName = (EditText) findViewById(R.id.activity_main_et_fileName);
		ll = (LinearLayout) findViewById(R.id.ll);



		btn_search.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View view) {
				String strFileName = et_fileName.getText().toString();
				searchFiles(strFileName);
			}
		});

//		btn_clear.setOnClickListener(new Button.OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				resultView.post(new Runnable(){
//					@Override
//					public void run()
//					{
//						resultView.removeAllViews();
//						resultView.invalidate();
//						resultView.requestLayout();
//					}
//				});
//
//			}
//		});

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
				if (listFile[i].isDirectory())
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

}
