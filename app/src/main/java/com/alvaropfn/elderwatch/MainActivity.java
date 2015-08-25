package com.alvaropfn.elderwatch;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;


public class MainActivity extends Activity {
    Button on_btn, off_btn, send_btn, list_btn, connect_btn;
    BluetoothController bc;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        on_btn = (Button) findViewById(R.id.on_btn);
        off_btn = (Button) findViewById(R.id.off_btn);
        send_btn = (Button) findViewById(R.id.send_btn);
        list_btn = (Button) findViewById(R.id.list_btn);
        connect_btn = (Button) findViewById(R.id.connect_btn);

        lv = (ListView) findViewById(R.id.listView);

        bc = new BluetoothController();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void turnOn(View view)
    {
        bc.enable(this, this.getApplicationContext());
    }

    public void turnOff(View view)
    {
        bc.disable(this.getApplicationContext());
    }

    public  void connect(View view)
    {
        String adress = "00:13:12:25:70:84";
        bc.connect(getApplicationContext(), adress);
    }

    public void send(View view)
    {
        bc.send(getApplicationContext(), "a");
        bc.read(getApplicationContext());
    }

    public void clickItem(View view)
    {

    }

    public void listDevices(View view)
    {
        bc.listDevices(getApplicationContext(), lv);
    }
}
