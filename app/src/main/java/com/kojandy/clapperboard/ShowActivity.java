package com.kojandy.clapperboard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.lb.auto_fit_textview.AutoResizeTextView;

public class ShowActivity extends AppCompatActivity {
    int currentCam = 1;
    int finalCam;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        final AutoResizeTextView text = (AutoResizeTextView) findViewById(R.id.show_text);
        String str = getIntent().getStringExtra("text");
        String[] parse = str.split("-");
        if (parse.length == 4) finalCam = Integer.parseInt(parse[3]);
        else finalCam = 1;
        final String num = parse[0] + "-" + parse[1] + "-" + parse[2];
        if (finalCam == 1) text.setText(num);
        else text.setText(num + "-" + currentCam);
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentCam++ < finalCam) {
                    text.setText(num + "-" + currentCam);
                } else finish();
            }
        });
    }
}
