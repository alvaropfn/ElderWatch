package com.alvaropfn.elderwatch;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Alvaro on 21/08/2015.
 */
public class BluetoothController
{
    BluetoothAdapter adapter;
    BluetoothDevice device;
    BluetoothSocket socket;
    InputStream intStream;
    OutputStream outStream;
    Set<BluetoothDevice> paireds;


    public BluetoothController()
    {
        adapter = BluetoothAdapter.getDefaultAdapter();
    }

    public void enable(Activity dad, Context ctx)
    {
        if(!adapter.isEnabled())
        {
            Intent enableBlue = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            dad.startActivityForResult(enableBlue, 0);
            Toast.makeText(ctx,"Bluetooth turned ON", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(ctx,"Bluetooth already ON", Toast.LENGTH_SHORT).show();
        }
    }

    public void disable(Context ctx)
    {
        adapter.disable();
        Toast.makeText(ctx,"Bluetooth turned OFF", Toast.LENGTH_SHORT).show();
    }

    public void fetchDevices()
    {
        paireds = adapter.getBondedDevices();
    }

    public void listDevices(Context ctx, ListView lv)
    {
        fetchDevices();
        if(paireds.size() == 0) return;
        ArrayList list = new ArrayList();

        for (BluetoothDevice btd : paireds)
            list.add(btd.getName() + ": " + btd.getAddress());

        final ArrayAdapter adaptArray = new ArrayAdapter(ctx, android.R.layout.simple_list_item_1, list);
        lv.setAdapter(adaptArray);

        Toast.makeText(ctx, "Showing paired devices", Toast.LENGTH_LONG).show();

    }

    public void connect(Context ctx, String adress)
    {
        for (BluetoothDevice pd : paireds)
        {
            if(pd.getAddress().equals(adress))
            {
                try
                {
                    UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); //Standard SerialPortService ID
                    device = pd;
                    socket = device.createRfcommSocketToServiceRecord(uuid);
                    socket.connect();
                    intStream = socket.getInputStream();
                    outStream = socket.getOutputStream();
                    Toast.makeText(ctx, "connection sucessful: " + pd.getName() , Toast.LENGTH_LONG).show();
                    break;
                }
                catch (IOException e)
                {
                    Toast.makeText(ctx, "connection problem: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    break;
                }
            }
        }

    }

    public void send(Context ctx, String msg)
    {
        msg += "\n";
        try {
            outStream.write(msg.getBytes());
            Toast.makeText(ctx,"sending: " + msg, Toast.LENGTH_SHORT).show();
            System.out.println("sending: " + msg);//debug
        }
        catch (IOException e)
        {
            e.printStackTrace();
            Toast.makeText(ctx,"problems when sending: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void read(Context ctx)
    {
        try
        {
            if(intStream.available() == 0)
            {
                Toast.makeText(ctx, "nothing to read: ", Toast.LENGTH_LONG).show();
                return;
            }
            else
            {
                byte[] buf = new byte[1024];
                intStream.read(buf);
                System.out.println("sending: ");//debug
                Toast.makeText(ctx, "received: " + new String(buf), Toast.LENGTH_SHORT).show();
            }

        }
        catch(IOException e)
        {
            Toast.makeText(ctx, "nothing to read: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }


}
