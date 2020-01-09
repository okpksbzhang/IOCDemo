package com.htc.iocdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.htc.iocdemo.annotataion.CommonOnClick;
import com.htc.iocdemo.annotataion.CommonOnLongClick;
import com.htc.iocdemo.annotataion.MBindView;
import com.htc.iocdemo.annotataion.MContentView;
import com.htc.iocdemo.annotataion.MOnclick;

@MContentView(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {
    @MBindView(R.id.btn1)
    Button btn1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InjectTool.inject(this);
        btn1.setText("hello yoyo");
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @CommonOnClick({R.id.btn1,R.id.btn2})
    public void show(int viewId){
        switch (viewId){
            case R.id.btn1:
                Toast.makeText(this,"show btn1",Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn2:
                Toast.makeText(this,"show btn2",Toast.LENGTH_SHORT).show();
                break;
                default:break;
        }
    }

    @CommonOnLongClick({R.id.btn1,R.id.btn2})
    public boolean showlong(int viewId){
        switch (viewId){
            case R.id.btn1:
                Toast.makeText(this,"show long click btn1",Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn2:
                Toast.makeText(this,"show long click btn2",Toast.LENGTH_SHORT).show();
                break;
            default:break;
        }
        return false;
    }
}
