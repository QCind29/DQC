package com.food.lite.nckh.detection;

import android.os.Bundle;
//import android.widget.SearchView;

//import android.support.v7.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.food.lite.nckh.detection.adapter.CateAdapter;
import com.food.lite.nckh.detection.sqlite.CateDB;

import org.tensorflow.lite.examples.detection.R;

public class ListTV extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CateAdapter cateAdapter;

    CateDB cateDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycle_list_tv);


        //init View
        recyclerView = (RecyclerView) findViewById(R.id.idRecycle);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        cateDB = new CateDB(this);
        cateAdapter = new CateAdapter(this,cateDB.getAll());
        recyclerView.setAdapter(cateAdapter);

    }


//    @Override
//    public boolean onCreateOptionsMenu (Menu menu){
//        getMenuInflater().inflate(R.menu.search_menu, menu);
//        MenuItem item = menu.findItem(R.id.action_search);
//
//
////        MenuItem searchItem = menu.findItem(R.id.action_search);
//        SearchView searchView = (SearchView) item.getActionView();
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//               cateAdapter.getFilter().filter(newText);
//                return false;
//            }
//        });
//        return super.onCreateOptionsMenu(menu);
//    }
}
