package com.example.jayvee.contactlist;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class AddContactActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView iv;
    EditText txtName, txtTel;
    Button btnSave, btnCancel;
    Uri imageUri;
    Intent intent;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.add_layout);

        this.tv = (TextView) this.findViewById(R.id.editTitle);
        this.iv = (ImageView) this.findViewById(R.id.imageView1);
        this.txtName = (EditText) this.findViewById(R.id.editText);
        this.txtTel = (EditText) this.findViewById(R.id.editText2);
        this.btnSave = (Button) this.findViewById(R.id.button);
        this.btnCancel = (Button) this.findViewById(R.id.button2);

        this.tv.setText("");
        this.iv.setOnClickListener(this);
        this.btnSave.setOnClickListener(this);
        this.btnCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch(id) {
            case R.id.imageView1:
                getImage();

                break;

            case R.id.button:
                Intent n = new Intent();
                String name = this.txtName.getText().toString();
                String tel = this.txtTel.getText().toString();

                if(imageUri != null && !name.equals("") && !tel.equals("")) {
                    n.putExtra("image", imageUri);
                    n.putExtra("name", name);
                    n.putExtra("tel", tel);

                    this.setResult(Activity.RESULT_OK, n);
                    Toast.makeText(this, "Added Successfully", Toast.LENGTH_SHORT).show();
                    this.finish();
                } else {
                    Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.button2:
                this.finish();
        }
    }

    public void getImage() {
        intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE}, 30);
            }
        } else {
            this.startActivityForResult(intent, 100);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode) {
            case 30:
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    final Dialog dialog3 = new Dialog(this);
                    dialog3.setContentView(R.layout.permission_layout);

                    TextView t1 = (TextView) dialog3.findViewById(R.id.textView6);
                    TextView t2 = (TextView) dialog3.findViewById(R.id.textView7);
                    t1.setText("Request Permissions");
                    t2.setText("Please allow permissions if you want this application to perform the task.");

                    Button dialogButton = (Button) dialog3.findViewById(R.id.button4);
                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog3.dismiss();
                        }
                    });

                    dialog3.show();
                } else {
                    this.startActivityForResult(intent, 100);
                }

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data != null) {
            imageUri = data.getData();
            try {
                iv.setImageURI(imageUri);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
