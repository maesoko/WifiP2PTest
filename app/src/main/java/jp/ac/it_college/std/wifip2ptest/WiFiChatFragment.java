package jp.ac.it_college.std.wifip2ptest;


import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class WiFiChatFragment extends ListFragment implements View.OnClickListener{

    private List<String> messages = new ArrayList<>();
    private TextView chatLine;
    private ChatManager chatManager;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setListAdapter(new ChatMessageAdapter(getActivity(), android.R.id.text1, messages));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        view.findViewById(R.id.btn_send).setOnClickListener(this);
        chatLine = (TextView) view.findViewById(R.id.txtChatLine);
        return view;
    }

    public void pushMessage(String readMessage) {
        messages.add(readMessage);
        ((ChatMessageAdapter) getListAdapter()).notifyDataSetChanged();
    }

    private void sendMessage() {
        if (chatManager != null) {
            chatManager.write(chatLine.getText().toString().getBytes());
            pushMessage("Me: " + chatLine.getText().toString());
            chatLine.setText("");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send:
                sendMessage();
                break;
        }
    }

    public void setChatManager(ChatManager chatManager) {
        this.chatManager = chatManager;
    }

    public class ChatMessageAdapter extends ArrayAdapter<String> {

        List<String> messages = null;

        public ChatMessageAdapter(Context context, int textViewResourceId,
                                  List<String> messages) {
            super(context, textViewResourceId, messages);
            this.messages = messages;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getActivity()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(android.R.layout.simple_list_item_1, null);
            }
            String message = WiFiChatFragment.this.messages.get(position);
            if (message != null && !message.isEmpty()) {
                TextView nameText = (TextView) v
                        .findViewById(android.R.id.text1);

                if (nameText != null) {
                    nameText.setText(message);
                    if (message.startsWith("Me: ")) {
                        nameText.setTextAppearance(getActivity(),
                                R.style.normalText);
                    } else {
                        nameText.setTextAppearance(getActivity(),
                                R.style.boldText);
                    }
                }
            }
            return v;
        }
    }

}
