package lk.idlsolutions.fan_tasticcompanion;

import android.app.DialogFragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class MainActivity extends AppCompatActivity implements DeviceListFragment.OnBtDeviceSelectListener {

    ImageButton btConnect; // Bluetooth connection button

    TextView btStatus; // Display Bluetooth connection status

    BluetoothAdapter btAdapter; // Bluetooth adapter

    BluetoothArduino btDuino;

    ToggleButton fanStatus ;

    ToggleButton oscillationStatus;

    ToggleButton autoSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        btConnect = (ImageButton) findViewById(R.id.btn_BluetoothConnect);

        btStatus = (TextView) findViewById(R.id.lbl_BluetoothStatus);

        btAdapter = BluetoothAdapter.getDefaultAdapter();

        fanStatus = (ToggleButton) findViewById(R.id.btn_ToggleLED);

        oscillationStatus = (ToggleButton) findViewById(R.id.btn_ToggleOscillation);

        autoSwitch = (ToggleButton) findViewById(R.id.btn_ToggleAutoSwitch);

        fanStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                fanOnOffSelect(isChecked);
            }
        });

        oscillationStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                oscillationOnOffSelect(isChecked);
            }
        });

        autoSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                autoSwitchOnOffSelect(isChecked);
            }
        });

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
        // connect to BT
        btDuino = BluetoothArduino.getInstance(toConnect.getName());
        btDuino.connect();
        btConnect.setBackgroundColor(Color.parseColor("#32cd32"));
        btStatus.setText("Connected to "+toConnect.getName()); // Sets text
    }

    private void fanOnOffSelect(boolean isChecked) {
        if(isChecked){
            btDuino.SendMessage("F1");
        } else{
            btDuino.SendMessage("F0");
        }
    }

    private void oscillationOnOffSelect(boolean isChecked){
        if(isChecked){
            btDuino.SendMessage("O1");
        }else{
            btDuino.SendMessage("O0");
        }
    }

    private void autoSwitchOnOffSelect(boolean isChecked){
        if(isChecked){
            btDuino.SendMessage("A1");
        }else{
            btDuino.SendMessage("A0");
        }
    }



}
