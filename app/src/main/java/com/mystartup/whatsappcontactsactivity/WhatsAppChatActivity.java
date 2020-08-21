package com.mystartup.whatsappcontactsactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class WhatsAppChatActivity extends AppCompatActivity implements View.OnClickListener{
    private ListView mListView;
    private ArrayAdapter mArrayAdapter;
    private ImageView sendButton;
    private EditText sendText;
    private ArrayList<String> mArrayList;
    private String receiver;
    private ArrayList<String> chatUsersName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whats_app_chat);
        receiver = getIntent().getStringExtra("sendTextTo");
        sendText = findViewById(R.id.text_to_send);
        mListView = findViewById(R.id.list_view_whatsapp_chat);
        sendButton = findViewById(R.id.send);
        mArrayList = new ArrayList<>();
        mArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, mArrayList);
        sendButton.setOnClickListener(this);
        chatUsersName = new ArrayList<>();
        chatUsersName.add(ParseUser.getCurrentUser().getUsername());
        chatUsersName.add(receiver);

        try{
            ParseQuery<ParseObject> firstParseQuery = new ParseQuery("Chat");
        firstParseQuery.whereContainedIn("Sender", chatUsersName);
        firstParseQuery.whereContainedIn("Receiver", chatUsersName);
        firstParseQuery.orderByAscending("createdAt");
        firstParseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(objects.size()>0) {
                    if (e == null) {
                        for (ParseObject parseObject : objects) {
                            mArrayList.add(parseObject.get("Sender") + ":" +"\n"+ parseObject.get("Chats") + "");
                        }
                    }

                    mListView.setAdapter(mArrayAdapter);

                }}
        });
        }
        catch (Exception exc){
            Log.i("Error",exc.getMessage());
        }

    }


    @Override
    public void onClick(View view) {
        ParseObject Chat = new ParseObject("Chat");
        Chat.put("Sender",ParseUser.getCurrentUser().getUsername());
        Chat.put("Chats",sendText.getText().toString());
        Chat.put("Receiver",receiver);
        Chat.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null){String str = sendText.getText().toString();
                    mArrayList.add(ParseUser.getCurrentUser().getUsername()+":"+"\n"+str);
                    mArrayAdapter.notifyDataSetChanged();
                    sendText.setText("");
                }
            }
        });

    }
}