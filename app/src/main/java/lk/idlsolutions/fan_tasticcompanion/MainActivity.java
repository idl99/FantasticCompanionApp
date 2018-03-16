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

    LinearLayout tempPlansHeader;

    ImageButton addPlan;

    ImageButton removePlan;

    LinearLayout tempPlans;

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
        tempPlansHeader = (LinearLayout) findViewById(R.id.linearLayout_tempPlansHeader);
        addPlan = (ImageButton) findViewById(R.id.btn_AddPlan);
        removePlan = (ImageButton) findViewById(R.id.btn_RemovePlan);
        tempPlans = (LinearLayout) findViewById(R.id.linearLayout_tempPlans);

        // Setting listeners for changes in toggle buttons

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

        addPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTemperaturePlan();
            }
        });

        removePlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeTemperaturePlan();
            }
        });

        if(btAdapter == null){
            // Show toast message that bluetooth is not available for mobile device
            Toast.makeText(this, "Bluetooth Adapter not available", Toast.LENGTH_LONG).show();

            // Finish activity
            finish();
        } else{
            if(!btAdapter.isEnabled()){
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

    private void addTemperaturePlan(){
        if(tempPlans.getChildCount()>2)
            Toast.makeText(getApplicationContext(), "You have already created the maximum number of plans",
                    Toast.LENGTH_SHORT).show();
        else{
            LinearLayout plan = new LinearLayout(getApplicationContext());
            plan.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1.0f
            );
            plan.setLayoutParams(param);
            tempPlans.addView(plan);
//            ==================================================
            final Spinner conditionSpinner = new Spinner(getApplicationContext());
            conditionSpinner.setAdapter(new ArrayAdapter<String>(
                    getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,
                    new String[]{">THAN","<THAN"}
            ));
            conditionSpinner.setLayoutParams(new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    0.35f
            ));
            plan.addView(conditionSpinner);
//            ==================================================
            final EditText temp = new EditText(getApplicationContext());
            temp.setHint("TEMP");
            temp.setLayoutParams(new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    0.23f
                ));
            plan.addView(temp);
//            ==================================================
            final Spinner speedSpinner = new Spinner(getApplicationContext());
            speedSpinner.setAdapter(new ArrayAdapter<String>(
                    getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,
                    new String[]{"HIGH","LOW"}
            ));
            speedSpinner.setLayoutParams(new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    0.32f
            ));
            plan.addView(speedSpinner);
//            ==================================================
            final Button setPlan = new Button(getApplicationContext());
            setPlan.setText("\u2713");
            setPlan.setLayoutParams(new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    0.1f
            ));
            setPlan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String condition = conditionSpinner.getSelectedItem().toString().substring(0,1);
                    if(speedSpinner.getSelectedItem().toString()=="HIGH"){
                        btDuino.SendMessage("TP"+condition+
                                temp.getText()+2);
                    } else if(speedSpinner.getSelectedItem().toString()=="LOW"){
                        btDuino.SendMessage("TP"+condition+temp.getText()+1);
                    }

                }
            });
            plan.addView(setPlan);
//            LinearLayout plan = new LinearLayout(getApplicationContext());
//            plan.setOrientation(LinearLayout.HORIZONTAL);
//            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.MATCH_PARENT,
//                    LinearLayout.LayoutParams.MATCH_PARENT,
//                    1.0f
//            );
//            plan.setLayoutParams(param);
//            final EditText temp = new EditText(getApplicationContext());
//            temp.setHint("TEMP");
//            temp.setLayoutParams(new LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.MATCH_PARENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT,
//                    0.3f
//                ));
//            plan.addView(temp);
//            final EditText speed = new EditText(getApplicationContext());
//            speed.setHint("SPEED");
//            speed.setLayoutParams(new LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.MATCH_PARENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT,
//                    0.3f
//            ));
//            plan.addView(speed);
//            final Button setPlan = new Button(getApplicationContext());
//            setPlan.setText("APPLY");
//            setPlan.setLayoutParams(new LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.MATCH_PARENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT,
//                    0.4f
//            ));
//            setPlan.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    btDuino.SendMessage("P01"+temp.getText());
//                    Log.d("test","this works");
//                }
//            });
//            plan.addView(setPlan);
//            tempPlans.addView(plan);
        }
    }

    private void removeTemperaturePlan(){
        tempPlans.removeViewAt(0);
    }

}
