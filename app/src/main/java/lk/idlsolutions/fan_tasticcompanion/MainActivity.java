package lk.idlsolutions.fan_tasticcompanion;

import android.app.DialogFragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements DeviceListFragment.OnBtDeviceSelectListener {

    ImageButton btConnect; // Bluetooth connection button

    TextView btStatus; // Display Bluetooth connection status

    BluetoothAdapter btAdapter; // Bluetooth adapter

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        btConnect = (ImageButton) findViewById(R.id.btn_BluetoothConnect);

        btStatus = (TextView) findViewById(R.id.lbl_BluetoothStatus);

        btAdapter = BluetoothAdapter.getDefaultAdapter();

        if(btAdapter == null){
            // Show toast message that bluetooth is not available for mobile device
            Toast.makeText(this, "Bluetooth Adapter not available", Toast.LENGTH_LONG).show();

            // Finish activity
            finish();

        } else{

            if(btAdapter.isEnabled()){
            } else{
                // Ask user to turn bluetooth on
                Intent turnBtOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(turnBtOn,1);
            }
        }

        btConnect.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                DialogFragment fragment = new DeviceListFragment();
                fragment.show(getFragmentManager(),"Select BT fan");
            }
        });

    }

    @Override
    public void onBtDeviceSelect(BluetoothDevice toConnect) {
        // Connect to BT
        btStatus.setText("TBC to "+toConnect.getName()); // Sets text
    }

}
