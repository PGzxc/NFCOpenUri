package com.pgzxc.nfcopenuri;

import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()), 0);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (nfcAdapter != null)
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
    }

    @Override
    public void onNewIntent(Intent intent) {
        Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        writeNFCTag(detectedTag);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (nfcAdapter != null)
            nfcAdapter.disableForegroundDispatch(this);
    }

    public void writeNFCTag(Tag tag) {
        if (tag == null) {
            return;
        }
        NdefMessage ndefMessage = new NdefMessage(
                new NdefRecord[] { NdefRecord.createUri(Uri
                        .parse("http://www.baidu.com")) });
        int size = ndefMessage.toByteArray().length;
        try{
            Ndef ndef = Ndef.get(tag);
            if(ndef != null)
            {
                ndef.connect();
                if(!ndef.isWritable())
                {
                    return;
                }
                if(ndef.getMaxSize() < size)
                {
                    return;
                }
                ndef.writeNdefMessage(ndefMessage);
                Toast.makeText(this, "ok", Toast.LENGTH_LONG).show();
            }
            else {
                NdefFormatable format = NdefFormatable.get(tag);
                if(format != null)
                {
                    format.connect();
                    format.format(ndefMessage);
                    Toast.makeText(this, "ok", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(this, "formating is failed", Toast.LENGTH_LONG).show();
                }
            }

        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}
