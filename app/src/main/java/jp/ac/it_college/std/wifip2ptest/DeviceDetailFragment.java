package jp.ac.it_college.std.wifip2ptest;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager.ConnectionInfoListener;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import jp.ac.it_college.std.wifip2ptest.DeviceListFragment.DeviceActionListener;

public class DeviceDetailFragment extends Fragment implements View.OnClickListener, ConnectionInfoListener{

    private WifiP2pDevice device;
    private ProgressDialog progressDialog;
    private WifiP2pInfo info;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_device_detail, container, false);
        view.findViewById(R.id.btn_connect).setOnClickListener(this);
        view.findViewById(R.id.btn_disconnect).setOnClickListener(this);

        return view;
    }

    private void wifiConnect() {
        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = device.deviceAddress;
        config.wps.setup = WpsInfo.PBC;

        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        progressDialog = ProgressDialog.show(getActivity(), "Press back to cancel",
                "Connecting to :" + device.deviceAddress, true, true, new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        ((DeviceActionListener) getActivity()).cancelDisconnect();
                    }
                });

        ((DeviceActionListener) getActivity()).connect(config);
    }

    private void wifiDisconnect() {
        ((DeviceActionListener) getActivity()).disconnect();
    }

    public void showDetails(WifiP2pDevice device) {
        this.device = device;
        getView().setVisibility(View.VISIBLE);
        TextView view = (TextView) getView().findViewById(R.id.device_address);
        view.setText(device.deviceAddress);
        view = (TextView) getView().findViewById(R.id.device_info);
        view.setText(device.toString());
    }

    public void resetViews() {
        getView().findViewById(R.id.btn_connect).setVisibility(View.VISIBLE);
        TextView view = (TextView) getView().findViewById(R.id.device_address);
        view.setText(R.string.empty);
        view = (TextView) getView().findViewById(R.id.device_info);
        view.setText(R.string.empty);
        view = (TextView) getView().findViewById(R.id.group_owner);
        view.setText(R.string.empty);
        view = (TextView) getView().findViewById(R.id.status_text);
        view.setText(R.string.empty);
        this.getView().setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_connect:
                wifiConnect();
                break;
            case R.id.btn_disconnect:
                wifiDisconnect();
                break;
        }
    }

    @Override
    public void onConnectionInfoAvailable(WifiP2pInfo info) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        this.info = info;
        getView().setVisibility(View.VISIBLE);

        // The owner IP is now known.
        TextView view = (TextView) getView().findViewById(R.id.group_owner);
        view.setText(getResources().getString(R.string.group_owner_text)
                + ((info.isGroupOwner) ? getResources().getString(R.string.yes)
                : getResources().getString(R.string.no)));

        if (info.groupFormed && info.isGroupOwner) {

        } else if (info.groupFormed) {
            // The other device acts as the client. In this case, we enable the
            // get file button.
            ((TextView) getView().findViewById(R.id.status_text)).setText(getResources()
                    .getString(R.string.client_text));
        }
    }
}
