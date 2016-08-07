package com.kojandy.clapperboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText txtData = (EditText) findViewById(R.id.main_txt_data);
        final EditText txtMemo = (EditText) findViewById(R.id.main_txt_memo);

        findViewById(R.id.main_btn_history).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, HistoryActivity.class));
            }
        });
        findViewById(R.id.main_btn_show).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MainActivity.this, ShowActivity.class);
                it.putExtra("text", ((TextView) findViewById(R.id.main_txt_data)).getText().toString());
                startActivity(it);
            }
        });
        findViewById(R.id.main_btn_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = txtData.getText().toString();
                final String[] parsed = text.split("-");
                Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        Data data = realm.createObject(Data.class);
                        data.scene = Integer.parseInt(parsed[0]);
                        data.cut = parsed[1];
                        data.take = Integer.parseInt(parsed[2]);
                        if (parsed.length == 4) data.camera = Integer.parseInt(parsed[3]);
                        else data.camera = 1;
                        data.note = txtMemo.getText().toString();
                        data.isOk = false;
                    }
                });
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(parsed[0]).append("-").append(parsed[1]).append("-").append(Integer.parseInt(parsed[2]) + 1);
                if (parsed.length == 4)
                    stringBuilder.append("-").append(parsed[3]);
                txtData.setText(stringBuilder.toString());
                txtMemo.setText("");
            }
        });
        findViewById(R.id.main_btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = txtData.getText().toString();
                final String[] parsed = text.split("-");
                Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        Data data = realm.createObject(Data.class);
                        data.scene = Integer.parseInt(parsed[0]);
                        data.cut = parsed[1];
                        data.take = Integer.parseInt(parsed[2]);
                        if (parsed.length == 4) data.camera = Integer.parseInt(parsed[3]);
                        else data.camera = 1;
                        data.note = txtMemo.getText().toString();
                        data.isOk = true;
                    }
                });
                txtData.setText("");
                txtMemo.setText("");
            }
        });
    }
}
