package com.kojandy.clapperboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import io.realm.Realm;
import io.realm.Sort;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button btnSet = (Button) findViewById(R.id.main_btn_set);
        final Button btnShow = (Button) findViewById(R.id.main_btn_show);
        final Button btnOk = (Button) findViewById(R.id.main_btn_ok);
        final Button btnNext = (Button) findViewById(R.id.main_btn_next);
        final Button btnCancel = (Button) findViewById(R.id.main_btn_cancel);
        final Button btnHistory = (Button) findViewById(R.id.main_btn_history);

        final EditText txtScene = (EditText) findViewById(R.id.main_txt_scene);
        final EditText txtCut = (EditText) findViewById(R.id.main_txt_cut);
        final EditText txtCam = (EditText) findViewById(R.id.main_txt_cam);

        final TextView txtTake = (TextView) findViewById(R.id.main_txt_take);

        txtCut.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                switch (i) {
                    case EditorInfo.IME_ACTION_DONE:
                        btnSet.performClick();
                        break;
                }
                return false;
            }
        });
        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Data data;
                    try {
                        data = Realm.getDefaultInstance().where(Data.class)
                                .equalTo("scene", Integer.parseInt(txtScene.getText().toString()))
                                .equalTo("cut", txtCut.getText().toString())
                                .findAll().sort("take", Sort.DESCENDING).first();
                    } catch (IndexOutOfBoundsException e) {
                        data = null;
                    }
                    if (data == null) {
                        txtTake.setText("1");
                        txtCam.setText("1");
                    } else {
                        txtTake.setText(String.valueOf(data.take + 1));
                        txtCam.setText(String.valueOf(data.camera));
                    }
                    txtScene.setEnabled(false);
                    txtCut.setEnabled(false);
                    btnSet.setEnabled(false);
                } catch (NumberFormatException e) {
                    Toast.makeText(MainActivity.this, "숫자가 뭔지 모름?", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtScene.setEnabled(true);
                txtCut.setEnabled(true);
                btnSet.setEnabled(true);
                txtScene.setText("");
                txtCut.setText("");
                txtTake.setText("0");
                txtCam.setText("");
            }
        });
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!btnSet.isEnabled()) {
                    Intent it = new Intent(MainActivity.this, ShowActivity.class);
                    it.putExtra("num", txtScene.getText().toString() + "-"
                            + txtCut.getText().toString() + "-" + txtTake.getText().toString());
                    it.putExtra("camera", Integer.parseInt(txtCam.getText().toString()));
                    startActivity(it);
                } else Toast.makeText(MainActivity.this, "Set 눌러", Toast.LENGTH_SHORT).show();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!btnSet.isEnabled()) {
                    Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            Data data = realm.createObject(Data.class);
                            data.scene = Integer.parseInt(txtScene.getText().toString());
                            data.cut = txtCut.getText().toString();
                            data.take = Integer.parseInt(txtTake.getText().toString());
                            data.camera = Integer.parseInt(txtCam.getText().toString());
                            data.isOk = true;
                            data.note = "";
                        }
                    });
                    btnCancel.performClick();
                } else Toast.makeText(MainActivity.this, "Set 눌러", Toast.LENGTH_SHORT).show();
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!btnSet.isEnabled()) {
                    Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            Data data = realm.createObject(Data.class);
                            data.scene = Integer.parseInt(txtScene.getText().toString());
                            data.cut = txtCut.getText().toString();
                            data.take = Integer.parseInt(txtTake.getText().toString());
                            data.camera = Integer.parseInt(txtCam.getText().toString());
                            data.isOk = false;
                            data.note = "";
                        }
                    });
                    txtTake.setText(String.valueOf(Integer.parseInt(txtTake.getText().toString()) + 1));
                } else Toast.makeText(MainActivity.this, "Set 눌러", Toast.LENGTH_SHORT).show();
            }
        });
        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, HistoryActivity.class));
            }
        });
    }
}
