package lk.idlsolutions.fan_tasticcompanion;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.*;
import android.util.Log;
import android.widget.Toast;

import java.io.*;
import java.util.UUID;

public class AcceptBtConnection extends Thread{

    // TODO implementation to be fixed

    private final Context CONTEXT;
    private final BluetoothServerSocket MY_SERVER_SOCKET;
    private final String APP_NAME = "Fantastic Companion"; // TODO
    private final UUID UUID = new UUID(01,01); // TODO

    public AcceptBtConnection(Context context) {

        this.CONTEXT = context;

        // Use a temporary object that is later assigned to MY_SERVER_SOCKET
        // because MY_SERVER_SOCKET is final.
        BluetoothServerSocket tmp = null;

        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();

        try {
            // MY_UUID is the app's UUID string, also used by the client code.
            tmp = btAdapter.listenUsingRfcommWithServiceRecord(APP_NAME,UUID);
        } catch (IOException e) {
            Log.e("tag", "Socket's listen() method failed", e);
        }
        MY_SERVER_SOCKET = tmp;
    }

    public void run() {
        BluetoothSocket socket = null;
        // Keep listening until exception occurs or a socket is returned.
        while (true) {
            try {
                socket = MY_SERVER_SOCKET.accept();
            } catch (IOException e) {
                Log.e("tag", "Socket's accept() method failed", e);
                break;
            }

            if (socket != null) {
                // A connection was accepted. Perform work associated with
                // the connection in a separate thread.
                manageMyConnectedSocket(socket);
                try {
                    MY_SERVER_SOCKET.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    // Closes the connect socket and causes the thread to finish.
    public void cancel() {
        try {
            MY_SERVER_SOCKET.close();
        } catch (IOException e) {
            Log.e("tag", "Could not close the connect socket", e);
        }
    }

    public void manageMyConnectedSocket(BluetoothSocket socket){
        Toast.makeText(this.CONTEXT,socket.getRemoteDevice().getName(),Toast.LENGTH_LONG).show();
    }

}
