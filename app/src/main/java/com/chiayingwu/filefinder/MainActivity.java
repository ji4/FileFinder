package com.chiayingwu.filefinder;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.listallfiles.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends Activity {
    private ArrayList<File> m_matchedFileList = new ArrayList<File>();
    private Button m_btn_search, m_btn_searchDupFile;
    private EditText m_et_fileName;
    private EditText m_et_startDate, m_et_endDate;
    private EditText m_et_minSize, m_et_maxSize;

    //-------------file view variables-------------//
    private ViewStub m_stubGrid;
    private ViewStub m_stubList;
    private ListView m_listView;
    private GridView m_gridView;
    private ListViewAdapter m_listViewAdapter;
    private GridViewAdapter m_gridViewAdapter;
    private int m_currentViewMode = 0;

    private static final int VIEW_MODE_LISTVIEW = 0;
    private static final int VIEW_MODE_GRIDVIEW = 1;
    //----------End of file view variables-----------//

    //-------------UI Handler-------------//
    private Handler m_handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Code.MSG_UPDATE_VIEW:
                    File receivedFile = (File) msg.obj;
                    addFilesToAdapter(receivedFile);
                    break;
                case Code.MSG_RESET_VIEW:
                    m_matchedFileList.clear();
                    notifyChangeToAdapter();
                    break;
            }
        }
    };
    //---------End of UI Handler---------//

    private Controller m_controller = new Controller(m_handler);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        initFileViews();

        m_btn_search.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_handler.sendEmptyMessage(Code.MSG_RESET_VIEW);
                List<String> strListInputText = detectEditTextInputStatus();
                m_controller.searchFilesByInput(strListInputText);
            }
        });

		m_btn_searchDupFile.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View view) {
                m_handler.sendEmptyMessage(Code.MSG_RESET_VIEW);
                m_controller.searchDupFiles();
			}
		});


    }

    public List<String> detectEditTextInputStatus() {
        //Add All EditTexts into ArrayList
        ArrayList<EditText> editTextList = new ArrayList<EditText>(
                Arrays.asList(m_et_fileName, m_et_startDate, m_et_endDate, m_et_minSize, m_et_maxSize));

        //Initialize input text list with null
        int iEditTextListSize = editTextList.size();
        List<String> strListInputText = new ArrayList<String>(Arrays.asList(new String[iEditTextListSize]));
        Collections.fill(strListInputText, null);

        //Fill in strListInputText if EditText has text
        int iInputNullCount = 0;
        for (int i = 0; i < iEditTextListSize; i++) {
            String strInputValue = editTextList.get(i).getText().toString().trim();
            if (!strInputValue.matches("")) {//has input text
                strListInputText.set(i, strInputValue);
            } else {
                iInputNullCount++;
            }
        }
        if (iInputNullCount == iEditTextListSize)
            strListInputText = null;

        Log.d("strListInputText", String.valueOf(strListInputText));
        return strListInputText;
    }

    public void findViews() {
        m_btn_search = (Button) findViewById(R.id.activity_main_btn_search);
        m_btn_searchDupFile = (Button) findViewById(R.id.activity_main_btn_searchDupFile);
        m_et_fileName = (EditText) findViewById(R.id.activity_main_et_fileName);
        //start & end date
        m_et_startDate = (EditText) findViewById(R.id.activity_main_et_startDate);
        m_et_endDate = (EditText) findViewById(R.id.activity_main_et_endDate);
        //size
        m_et_minSize = (EditText) findViewById(R.id.activity_main_et_minSize);
        m_et_maxSize = (EditText) findViewById(R.id.activity_main_et_maxSize);
    }

    //----------------Following are file view functions-------------------//
    AdapterView.OnItemClickListener onItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //Do any thing when user click to item
            Toast.makeText(getApplicationContext(), m_matchedFileList.get(position).getName() + " - " + DataConverter.convertTime(m_matchedFileList.get(position).lastModified()), Toast.LENGTH_SHORT).show();
        }
    };

    public void initFileViews() {
        m_stubList = (ViewStub) findViewById(R.id.stub_list);
        m_stubGrid = (ViewStub) findViewById(R.id.stub_grid);

        //Inflate ViewStub before get view

        m_stubList.inflate();
        m_stubGrid.inflate();

        m_listView = (ListView) findViewById(R.id.mylistview);
        m_gridView = (GridView) findViewById(R.id.mygridview);

        m_controller.searchFilesByInput(null); //filled with files

        //get list of files
        setAdapters();

        //Get current view mode in share reference
        SharedPreferences sharedPreferences = getSharedPreferences("ViewMode", MODE_PRIVATE);
        m_currentViewMode = sharedPreferences.getInt("currentViewMode", VIEW_MODE_LISTVIEW);//Default is view listview
        //Register item lick
        m_listView.setOnItemClickListener(onItemClick);
        m_gridView.setOnItemClickListener(onItemClick);

        switchView();
    }

    private void switchView() {

        if (VIEW_MODE_LISTVIEW == m_currentViewMode) {
            //Display listview
            m_stubList.setVisibility(View.VISIBLE);
            //Hide gridview
            m_stubGrid.setVisibility(View.GONE);
        } else {
            //Hide listview
            m_stubList.setVisibility(View.GONE);
            //Display gridview
            m_stubGrid.setVisibility(View.VISIBLE);
        }
        setAdapters();
    }

    private void addFilesToAdapter(File receivedFile) {
        m_matchedFileList.add(receivedFile);
        notifyChangeToAdapter();
    }

    private void notifyChangeToAdapter(){
        if (VIEW_MODE_LISTVIEW == m_currentViewMode) {
            m_listViewAdapter.notifyDataSetChanged();
        } else {
            m_gridViewAdapter.notifyDataSetChanged();
        }
    }

    private void setAdapters() {
        if (VIEW_MODE_LISTVIEW == m_currentViewMode) {
            m_listViewAdapter = new ListViewAdapter(this, R.layout.list_item, m_matchedFileList);
            m_listView.setAdapter(m_listViewAdapter);
        } else {
            m_gridViewAdapter = new GridViewAdapter(this, R.layout.grid_item, m_matchedFileList);
            m_gridView.setAdapter(m_gridViewAdapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_menu_1:
                if (VIEW_MODE_LISTVIEW == m_currentViewMode) {
                    m_currentViewMode = VIEW_MODE_GRIDVIEW;
                } else {
                    m_currentViewMode = VIEW_MODE_LISTVIEW;
                }
                //Switch view
                switchView();
                //Save view mode in share reference
                SharedPreferences sharedPreferences = getSharedPreferences("ViewMode", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("currentViewMode", m_currentViewMode);
                editor.commit();

                break;
        }
        return true;
    }

    //----------------End of file view functions-------------------//

}
