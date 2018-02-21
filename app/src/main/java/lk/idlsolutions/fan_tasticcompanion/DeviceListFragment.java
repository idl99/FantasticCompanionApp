package lk.idlsolutions.fan_tasticcompanion;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import java.util.List;
import java.util.Set;

/**
 * Created by Ihan on 2/20/2018.
 */

public class DeviceListFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Select your fan")
                .setItems(new CharSequence[]{"Huawei EVAL-19", "Samsung Galaxy S5", "HC05"},
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        return builder.create();

    }
}
