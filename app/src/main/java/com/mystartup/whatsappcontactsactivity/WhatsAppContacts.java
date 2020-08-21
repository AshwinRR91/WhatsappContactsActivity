package com.mystartup.whatsappcontactsactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class WhatsAppContacts extends AppCompatActivity {
    private ArrayList<String>mArrayList;
    private ListView mListView;
    private ArrayAdapter mArrayAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whats_app_contacts);
        mArrayList = new ArrayList<>();
        mListView = findViewById(R.id.list_view_whatsapp_chat);
        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(WhatsAppContacts.this,WhatsAppChatActivity.class);
                intent.putExtra("sendTextTo",mArrayList.get(i)+"");
                startActivity(intent);
            }
        });
        mArrayAdapter = new ArrayAdapter(WhatsAppContacts.this,android.R.layout.simple_list_item_1,mArrayList);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    ParseQuery<ParseUser>parseQuery = ParseUser.getQuery();
                    parseQuery.whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername());
                    parseQuery.findInBackground(new FindCallback<ParseUser>() {
                        @Override
                        public void done(List<ParseUser> objects, ParseException e) {
                            for(ParseUser mParseUser:objects){
                                mArrayList.add(mParseUser.getUsername()+"");
                            }
                            mListView.setAdapter(mArrayAdapter);

                        }
                    });
                }
                catch (Exception e){
                    Log.i("ParseQueryException",e.getMessage());
                }
            }
        }).start();

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    ParseQuery<ParseUser> mParseQuery = ParseUser.getQuery();
                    mParseQuery.whereNotContainedIn("username", mArrayList);
                    mParseQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
                    mParseQuery.findInBackground(new FindCallback<ParseUser>() {
                        @Override
                        public void done(List<ParseUser> objects, ParseException e) {
                            if (objects.size() > 0) {
                                for (ParseUser mParseUser : objects) {
                                    mArrayList.add(mParseUser.getUsername() + "");
                                }
                                mArrayAdapter.notifyDataSetChanged();
                                if (mSwipeRefreshLayout.isRefreshing()) {
                                    mSwipeRefreshLayout.setRefreshing(false);
                                }
                            } else {
                                if (mSwipeRefreshLayout.isRefreshing()) {
                                    mSwipeRefreshLayout.setRefreshing(false);
                                }
                            }
                        }

                    });
                }
                catch (Exception exc){

                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.logout){

            ParseUser.logOutInBackground(new LogOutCallback() {
                @Override
                public void done(ParseException e) {
                    if(e!=null){
                        Toast.makeText(WhatsAppContacts.this,"Logout error"+e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                    Intent intent = new Intent(WhatsAppContacts.this, MainActivity.class);
                    startActivity(intent);
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }
}