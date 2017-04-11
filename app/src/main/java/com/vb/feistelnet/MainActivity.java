package com.vb.feistelnet;

import android.os.AsyncTask;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final String ENCRYPT = "ENCRYPT";
    private static final String DECRYPT = "DECRYPT";

    ProgressBar mLoadingBar;
    RelativeLayout mContent;
    EditText mEncryptText;
    EditText mDecryptText;
    Button mEncryptButton;
    Button mDecryptButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoadingBar = (ProgressBar) findViewById(R.id.activityMainProgressBar);
        mContent = (RelativeLayout) findViewById(R.id.activityMainContent);

        mEncryptText = (EditText) findViewById(R.id.editTextEncrypt);
        mEncryptText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().length() == 0)
                    mDecryptButton.setEnabled(false);
                else
                    mDecryptButton.setEnabled(true);
            }
        });

        mDecryptText = (EditText) findViewById(R.id.editTextDecrypt);
        mDecryptText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().length() == 0)
                    mEncryptButton.setEnabled(false);
                else
                    mEncryptButton.setEnabled(true);
            }
        });

        mEncryptButton = (Button) findViewById(R.id.buttonEcnrypt);
        mEncryptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading(true);
                new CryptAsyncTask().execute(mDecryptText.getText().toString(), ENCRYPT);
            }
        });

        mDecryptButton = (Button) findViewById(R.id.buttonDecrypt);
        mDecryptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading(true);
                new CryptAsyncTask().execute(mEncryptText.getText().toString(), DECRYPT);
            }
        });

        loading(false);
        if(mEncryptText.getText().toString().length() == 0)
            mDecryptButton.setEnabled(false);
        if(mDecryptText.getText().toString().length() == 0)
            mEncryptButton.setEnabled(false);
    }

    private class CryptAsyncTask extends AsyncTask<String, Object, Pair<String, Boolean>>
    {
        @Override
        protected Pair<String, Boolean> doInBackground(String... args) throws IllegalArgumentException {
            if(args.length != 2)
                return null;

            boolean crypt;
            if(args[1].equals(ENCRYPT))
                crypt = true;
            else
                if(args[1].equals(DECRYPT))
                    crypt = false;
                else
                    return null;

            String cryptResult = null;
            try {
                FeistelNet feistelNet = new FeistelNet();
                cryptResult = feistelNet.crypt(args[0], crypt);
            } catch (Exception e) {
                return new Pair<>(e.toString(), crypt);
            }

            return new Pair<>(cryptResult, crypt);
        }

        @Override
        protected void onPostExecute(Pair<String, Boolean> result) {
            loading(false);

            if(result.second)
                mEncryptText.setText(result.first);
            else
                mDecryptText.setText(result.first);
        }
    }

    private void loading(boolean isLoading)
    {
        if(isLoading)
        {
            mLoadingBar.setVisibility(View.VISIBLE);
            mContent.setVisibility(View.GONE);
        }
        else
        {
            mLoadingBar.setVisibility(View.GONE);
            mContent.setVisibility(View.VISIBLE);
        }
    }
}
