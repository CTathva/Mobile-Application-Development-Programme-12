package com.akash.cp.vtu.vtupartb_4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity implements Base {
    File mFile;
    EditText mEditText;
    private static final int request_code=100;
    private static final int request_code2=101;
    private static final String TAG="MainActivity";
    private static final String FILE_NAME = "example.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        listener();
    }

    @Override
    public void init() {
        mEditText = findViewById(R.id.edit_text);
        mFile = new File(getApplicationContext().getFilesDir(), FILE_NAME);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }

    @Override
    public void listener() {
    }

    public void create(View v) {
        try {
            if(!mFile.exists()) {
                mFile.createNewFile();
                Toast.makeText(this, "File Created", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this, "File already created", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void save(View v) {
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                request_code2);

    }
    public void load(View v) {
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                request_code);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case request_code: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    FileInputStream fis = null;
                    try {
                        fis = openFileInput(FILE_NAME);
                        InputStreamReader isr = new InputStreamReader(fis);
                        BufferedReader br = new BufferedReader(isr);
                        StringBuilder sb = new StringBuilder();
                        String text;
                        while ((text = br.readLine()) != null) {
                            sb.append(text).append("\n");
                        }
                        mEditText.setText(sb.toString().trim());
                        cursorPosition(sb.toString().trim());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (fis != null) {
                            try {
                                fis.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                }
                return;

            }
            case request_code2: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (mFile.exists()) {
                        String text = mEditText.getText().toString();
                        FileOutputStream fos = null;
                        try {
                            fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
                            fos.write(text.getBytes());
                            mEditText.getText().clear();
                            Toast.makeText(this, "Saved to " + getFilesDir() + "/" + FILE_NAME,
                                    Toast.LENGTH_LONG).show();
                            Log.d(TAG,"Path: "+getFilesDir() + "/" + FILE_NAME);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            if (fos != null) {
                                try {
                                    fos.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                    } else {
                        Toast.makeText(this, "Please create file first", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    private void cursorPosition(String str) {
        mEditText.setSelection(str.length());
    }

}