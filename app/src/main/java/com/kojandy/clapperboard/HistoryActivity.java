package com.kojandy.clapperboard;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class HistoryActivity extends AppCompatActivity {
    private Realm realm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        realm = Realm.getDefaultInstance();

        ListView listView = (ListView) findViewById(R.id.history_list);
        final RealmResults<Data> data = realm.where(Data.class)
                .findAllSorted(new String[]{"scene", "cut", "take"},
                        new Sort[]{Sort.ASCENDING, Sort.ASCENDING, Sort.ASCENDING});
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {
                AlertDialog.Builder alert = new AlertDialog.Builder(HistoryActivity.this);

                alert.setTitle("편집");
                alert.setMessage("메모할 내용을 아래에 입력해서 변경하거나 OK를 토글하거나 데이터를 지울 수 있어");

                final EditText input = new EditText(HistoryActivity.this);
                input.setText(data.get(position).note);
                alert.setView(input);

                alert.setPositiveButton("변경", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        final String value = input.getText().toString();
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                data.get(position).note = value;
                            }
                        });
                    }
                });


                alert.setNegativeButton("토글",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                realm.executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        data.get(position).isOk = !data.get(position).isOk;
                                    }
                                });
                            }
                        });

                alert.setNeutralButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                data.deleteFromRealm(position);
                            }
                        });
                    }
                });

                alert.show();
            }
        });
        listView.setAdapter(new Adapter(this, data));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
