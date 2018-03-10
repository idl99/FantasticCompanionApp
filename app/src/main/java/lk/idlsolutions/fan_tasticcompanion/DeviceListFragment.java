package lk.idlsolutions.fan_tasticcompanion;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Ihan on 2/20/2018.
 */

public class DeviceListFragment extends DialogFragment {

    private OnBtDeviceSelectListener onBtDeviceSelectListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.onBtDeviceSelectListener = (OnBtDeviceSelectListener) getActivity();
        }
        catch (final ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + " must implement OnCompleteListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final Set<BluetoothDevice> pairedBt = BluetoothAdapter.getDefaultAdapter().getBondedDevices();

        final List<String> pairedBtNames = new ArrayList<>();

        for(BluetoothDevice device: pairedBt){
            // Getting paired device names and adding to paired devices list
            // Can't use Bluetooth Adapter as it doesn't extend Adapter class
            pairedBtNames.add(device.getName());
        }

        // Using builder to create() and show() dialog fragment
        // Using anonymous array adapter that takes the fragment as the context,
        // default Android List-item layout and default Android-TextView as resources
        // and pairedDevices as data
        builder.setTitle("Select your fan")
                .setAdapter(new ArrayAdapter<>(getActivity(),
                                android.R.layout.simple_list_item_1, android.R.id.text1, pairedBtNames),
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Return the bluetooth device details to main activity
                        // Finish fragment
                        String selected = pairedBtNames.get(which);
                        // From the set, find bluetooth device where name == selected
                        for(BluetoothDevice device: pairedBt){
                            if(device.getName().equals(selected)){
                                onBtDeviceSelectListener.onBtDeviceSelect(device);
                            }
                        }
                    }
                });

        return builder.create();

    }

    public interface OnBtDeviceSelectListener {
        public abstract void onBtDeviceSelect(BluetoothDevice connectTo);
    }

}
