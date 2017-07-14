package com.javatechig.listallfiles;

import java.io.File;
import java.util.ArrayList;

import com.example.listallfiles.R;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

	private File root;
	private ArrayList<File> fileList = new ArrayList<File>();
	private LinearLayout view;
	Button btn_search;
    EditText et_fileName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		view = (LinearLayout) findViewById(R.id.view);
		btn_search = (Button) findViewById(R.id.activity_main_btn_search);
        et_fileName = (EditText) findViewById(R.id.activity_main_et_fileName);

		btn_search.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View view) {
//                String strFileName = et_fileName.getText().toString();
//				Log.d("strFileName's length", String.valueOf(strFileName.length()));
				searchFiles();
			}
		});



	}

    public void searchFiles(){
        //getting SDcard root path
        root = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath());
        getfile(root);

        for (int i = 0; i < fileList.size(); i++) {
            TextView textView = new TextView(this);
            textView.setText(fileList.get(i).getName());
            textView.setPadding(5, 5, 5, 5);

            System.out.println(fileList.get(i).getName());

//			if (fileList.get(i).isDirectory()) {
//				textView.setTextColor(Color.parseColor("#FF0000"));
//			}

            if (fileList.get(i).isFile())
                view.addView(textView);
        }
    }

	public ArrayList<File> getfile(File dir) {
		File listFile[] = dir.listFiles();
		if (listFile != null && listFile.length > 0) {
			for (int i = 0; i < listFile.length; i++) {

				if (listFile[i].isDirectory()) {
//					fileList.add(listFile[i]);
					getfile(listFile[i]);

				} else {
//                    if(strFileName.length() > 0) //Search for input name
//                        if(listFile[i].getName().equals(strFileName))
//						    fileList.add(listFile[i]);
//                    else { //Search All files
                            fileList.add(listFile[i]);
//                    }
				}

			}
		}
		return fileList;
	}



}
