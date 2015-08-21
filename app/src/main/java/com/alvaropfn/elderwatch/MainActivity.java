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
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    Button b1, b2, b3, b4, b5, b6;
    private BluetoothAdapter adapter;
    private BluetoothDevice device;
    private Set<BluetoothDevice> paireds;
    ListView lsView;
    /*##################################*/
    private UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    private BluetoothSocket socket = null;
    private InputStream inStream = null;
    private OutputStream outStream = null;
    private int i;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        i = 0 ;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b1 = (Button) findViewById(R.id.button1);
        b2 = (Button) findViewById(R.id.button2);
        b3 = (Button) findViewById(R.id.button3);
        b4 = (Button) findViewById(R.id.button4);
        b5 = (Button) findViewById(R.id.button5);
        b6 = (Button) findViewById(R.id.button6);

        adapter = BluetoothAdapter.getDefaultAdapter();
        lsView = (ListView) findViewById(R.id.listView);
    }

    public void on(View v)
    {
        if(!adapter.isEnabled())
        {
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOn, 1);
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
            list.add(bd.getName() + ": " + bd.getAddress());
        }

        Toast.makeText(getApplicationContext(), "Showing paired devices", Toast.LENGTH_SHORT).show();

        final ArrayAdapter lsAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, list);

        lsView.setAdapter(lsAdapter);
    }
    public void send(View v)
    {
        if(socket == null)
        {
            this.connect(v);
            send(v);
        }
        else
        {
            try
            {
                outStream.write("p".getBytes());
                System.out.println("enviei: " + "p" + i);
                Toast.makeText(getApplicationContext(), "enviei: " + "p" +i, Toast.LENGTH_SHORT).show();
                i++;
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Damn!\nA problem ocurrs: " + e, Toast.LENGTH_LONG).show();
            }
        }
    }

    public void connect(View v)
    {
        String adress = "00:13:12:25:70:84";
        device = adapter.getRemoteDevice(adress);
        try
        {
            socket = device.createRfcommSocketToServiceRecord(MY_UUID);
            socket.connect();
            inStream = socket.getInputStream();
            outStream = socket.getOutputStream();
            Toast.makeText(getApplicationContext(), "Connection sucessfull!", Toast.LENGTH_SHORT).show();
        }
        catch (IOException e)
        {
            Toast.makeText(getApplicationContext(), "A problem ocurrs: " + e, Toast.LENGTH_LONG).show();
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
