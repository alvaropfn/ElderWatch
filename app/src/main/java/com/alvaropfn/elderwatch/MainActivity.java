package com.alvaropfn.elderwatch;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    Button b1, b2, b3, b4;
    private BluetoothAdapter adapter;
    private BluetoothDevice device;
    private Set<BluetoothDevice> paireds;
    ListView lsView;
    /*##################################*/
    UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    private BluetoothSocket socket;
    private InputStream instream = null;
    private OutputStream outstream = null;
    int i = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b1 = (Button) findViewById(R.id.button1);
        b2 = (Button) findViewById(R.id.button2);
        b3 = (Button) findViewById(R.id.button3);
        b4 = (Button) findViewById(R.id.button4);

        adapter = BluetoothAdapter.getDefaultAdapter();
        lsView = (ListView) findViewById(R.id.listView);
    }

    public void on(View v)
    {
        if(!adapter.isEnabled())
        {
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOn, 0);
            Toast.makeText(getApplicationContext(), "Turn on", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Alredy on", Toast.LENGTH_SHORT).show();
        }
    }

    public void off(View v)
    {
        adapter.disable();
        Toast.makeText(getApplicationContext(), "Turned off", Toast.LENGTH_SHORT).show();
    }

    public void visible(View v)
    {
        Intent getVisible = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        startActivityForResult(getVisible, 0);
    }

    public void list(View v)
    {
        paireds = adapter.getBondedDevices();
        ArrayList list = new ArrayList();

        for (BluetoothDevice bd : paireds)
        {
            list.add(bd.getName() + "mac: " + bd.getAddress());
        }

        Toast.makeText(getApplicationContext(), "Showing paired devices", Toast.LENGTH_SHORT).show();

        final ArrayAdapter lsAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, list);

        lsView.setAdapter(lsAdapter);
    }

    public  void send(View v) {
        if(socket == null)
        {
            connect(v);
            this.send(v);
        }
        else
        {
            try
            {
                String msg = "p";
                System.out.println("p" + i);
                outstream.write(msg.getBytes());
            }
            catch (IOException e)
            {
                Toast.makeText(getApplicationContext(), "Fail while send msg: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }

        }
    }

    public void connect(View v)
    {
        String adress = "00:13:12:25:70:84";

        try
        {
            device = adapter.getRemoteDevice(adress);
            socket = device.createRfcommSocketToServiceRecord(uuid);
            socket.connect();
            instream = socket.getInputStream();
            outstream = socket.getOutputStream();
        }
        catch(IOException e)
        {
            Toast.makeText(getApplicationContext(), "A error ocurr: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

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


}
