//package com.javatechig.listallfiles;
//
//import android.app.Activity;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewStub;
//import android.widget.AdapterView;
//import android.widget.GridView;
//import android.widget.ListView;
//import android.widget.Toast;
//
//import com.example.listallfiles.R;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class Main2Activity extends Activity {
//    private ViewStub stubGrid;
//    private ViewStub stubList;
//    private ListView listView;
//    private GridView gridView;
//    private ListViewAdapter listViewAdapter;
//    private GridViewAdapter gridViewAdapter;
//    private List<Product> productList;
//    private int currentViewMode = 0;
//
//    static final int VIEW_MODE_LISTVIEW = 0;
//    static final int VIEW_MODE_GRIDVIEW = 1;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main2);
//
//
//
//    }
//
//
//    private void switchView() {
//
//        if(VIEW_MODE_LISTVIEW == currentViewMode) {
//            //Display listview
//            stubList.setVisibility(View.VISIBLE);
//            //Hide gridview
//            stubGrid.setVisibility(View.GONE);
//        } else {
//            //Hide listview
//            stubList.setVisibility(View.GONE);
//            //Display gridview
//            stubGrid.setVisibility(View.VISIBLE);
//        }
//        setAdapters();
//    }
//
//    private void setAdapters() {
//        if(VIEW_MODE_LISTVIEW == currentViewMode) {
//            listViewAdapter = new ListViewAdapter(this, R.layout.list_item, productList);
//            listView.setAdapter(listViewAdapter);
//        } else {
//            gridViewAdapter = new GridViewAdapter(this, R.layout.grid_item, productList);
//            gridView.setAdapter(gridViewAdapter);
//        }
//    }
//
//    public List<Product> getProductList() {
//        //pseudo code to get product, replace your code to get real product here
//        productList = new ArrayList<>();
//        productList.add(new Product(R.drawable.icon_android, "Title 1", "This is description 1"));
//        productList.add(new Product(R.drawable.icon_android, "Title 2", "This is description 2"));
//        productList.add(new Product(R.drawable.icon_android, "Title 3", "This is description 3"));
//        productList.add(new Product(R.drawable.icon_android, "Title 4", "This is description 4"));
//        productList.add(new Product(R.drawable.icon_android, "Title 5", "This is description 5"));
//        productList.add(new Product(R.drawable.icon_android, "Title 6", "This is description 6"));
//        productList.add(new Product(R.drawable.icon_android, "Title 7", "This is description 7"));
//        productList.add(new Product(R.drawable.icon_android, "Title 8", "This is description 8"));
//        productList.add(new Product(R.drawable.icon_android, "Title 9", "This is description 9"));
//        productList.add(new Product(R.drawable.icon_android, "Title 10", "This is description 10"));
//
//        return productList;
//    }
//
//    AdapterView.OnItemClickListener onItemClick = new AdapterView.OnItemClickListener() {
//        @Override
//        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            //Do any thing when user click to item
//            Toast.makeText(getApplicationContext(), productList.get(position).getTitle() + " - " + productList.get(position).getDescription(), Toast.LENGTH_SHORT).show();
//        }
//    };
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.item_menu_1:
//                if(VIEW_MODE_LISTVIEW == currentViewMode) {
//                    currentViewMode = VIEW_MODE_GRIDVIEW;
//                } else {
//                    currentViewMode = VIEW_MODE_LISTVIEW;
//                }
//                //Switch view
//                switchView();
//                //Save view mode in share reference
//                SharedPreferences sharedPreferences = getSharedPreferences("ViewMode", MODE_PRIVATE);
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.putInt("currentViewMode", currentViewMode);
//                editor.commit();
//
//                break;
//        }
//        return true;
//    }
//}
