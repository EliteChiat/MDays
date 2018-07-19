package com.zucc.ccm31501396.mdays;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

public class AddSchedule extends AppCompatActivity {
    private SharedPreferences user_sp;
    private EditText title_edit;
    private EditText place_edit;
    private EditText note_edit;
    private TextView date_text;
    private TextView startTime_text;
    private TextView endTime_text;
    private Spinner type_spinner;
    private String[] type_list ={"工作","娱乐","学习",};
    private ArrayAdapter<String> type_adapter;
    private Spinner priority_spinner;
    private Button up_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);

        user_sp = getSharedPreferences("loginInfo",MODE_PRIVATE);
        String username = user_sp.getString("username","");
        title_edit = (EditText) findViewById(R.id.title_edit);
        date_text = (TextView) findViewById(R.id.date_edit);
        type_spinner = (Spinner) findViewById(R.id.stype_spinner);
        startTime_text = (TextView) findViewById(R.id.startTime_text);
        endTime_text = (TextView) findViewById(R.id.endTime_text);
        priority_spinner = (Spinner) findViewById(R.id.priority_spinner);
        place_edit = (EditText) findViewById(R.id.place_edit);
        note_edit = (EditText) findViewById(R.id.note_edit);
        up_button = (Button) findViewById(R.id.up_button);

        type_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,type_list);
        type_spinner.setAdapter(type_adapter);


    }
}
