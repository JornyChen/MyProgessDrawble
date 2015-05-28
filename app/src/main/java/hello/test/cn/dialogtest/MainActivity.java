package hello.test.cn.dialogtest;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends Activity {
    private Button btn_test;
    private Button btn_start;
    private Button btn_end;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final MyView myView = (MyView) findViewById(R.id.myView);

        btn_start = (Button) findViewById(R.id.btn_start);
        btn_test = (Button) findViewById(R.id.btn_test);
        btn_end = (Button) findViewById(R.id.btn_end);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myView.startCheck();
            }
        });
        btn_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myView.finishCheck();
            }
        });
        btn_test.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                myView.startAnimator();
            }
        });

    }


}
