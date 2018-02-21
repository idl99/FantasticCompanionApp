package lk.idlsolutions.fan_tasticcompanion;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ihan on 2/20/2018.
 */

public class DeviceListFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        List<String> pairedDevices = new ArrayList<>();

        for(BluetoothDevice device:
                BluetoothAdapter.getDefaultAdapter().getBondedDevices()){
            // Getting paired device names and adding to paired devices list
            // Can't use Bluetooth Adapter as it doesn't extend Adapter class
            pairedDevices.add(device.getName());
        }


        // Using builder to create() and show() dialog fragment
        // Using anonymous array adapter that takes the fragment as the context,
        // default Android List-item layout and default Android-TextView as resources
        // and pairedDevices as data
        builder.setTitle("Select your fan")
                .setAdapter(new ArrayAdapter<>(getActivity(),
                                android.R.layout.simple_list_item_1, android.R.id.text1, pairedDevices),
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AcceptBtConnection btConnection = new AcceptBtConnection(getActivity());
                        btConnection.start();
                    }
                });

        return builder.create();

    }
}
