package com.example.jayvee.contactlist;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ListView lv;
    ItemAdapter adapter;
    ArrayList<Contact> list = new ArrayList<>();
    ContactDatabase db;
    AdapterView.AdapterContextMenuInfo info;
    Intent call, sendSMS;
    Dialog dialog4, dialog5;

    // Context Menu -> Edit Views
    ImageView editImage;
    EditText editName;
    EditText editPhone;
    Uri editImageUri;
    String pastNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.lv = (ListView) this.findViewById(R.id.listView1);

        this.db = new ContactDatabase(this);
        this.list = this.db.getAllContact();

        this.adapter = new ItemAdapter(this, list);
        this.lv.setAdapter(adapter);
        this.registerForContextMenu(lv);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int selected = item.getItemId();

        switch(selected) {
            case R.id.edit:
                editItem();

                break;

            case R.id.call:
                callPhone();

                break;

            case R.id.view:
                viewItem();

                break;

            case R.id.send:
                sendMessage();

                break;

            case R.id.delete:
                deleteItem();

                break;
        }

        return super.onContextItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.contextmenu, menu);
        info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle(list.get(info.position).getName());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent in = new Intent(this, AddContactActivity.class);
        this.startActivityForResult(in, 0);

        return super.onOptionsItemSelected(item);
    }

    public void editItem() {
        dialog5 = new Dialog(this);
        dialog5.setContentView(R.layout.add_layout);

        LinearLayout lay = (LinearLayout) dialog5.findViewById(R.id.linearLayout);
        editImage = (ImageView) dialog5.findViewById(R.id.imageView1);
        editName = (EditText) dialog5.findViewById(R.id.editText);
        editPhone = (EditText) dialog5.findViewById(R.id.editText2);
        TextView editTitle = (TextView) dialog5.findViewById(R.id.editTitle);
        Button save = (Button) dialog5.findViewById(R.id.button);
        Button cancel = (Button) dialog5.findViewById(R.id.button2);

        ViewGroup.LayoutParams params = lay.getLayoutParams();
        params.height = 600;
        params.width = 500;
        lay.setLayoutParams(params);
        lay.setBackgroundColor(Color.rgb(43, 91, 96));

        pastNum = list.get(info.position).getTel();
        editTitle.setText(String.format("Edit %s", list.get(info.position).getName()));
        editImage.setImageURI(list.get(info.position).getImageUri());
        editName.setText(list.get(info.position).getName());
        editPhone.setText(list.get(info.position).getTel());
        save.setText("Save");

        editImageUri = list.get(info.position).getImageUri();
        editImage.setOnClickListener(this);
        save.setOnClickListener(this);
        cancel.setOnClickListener(this);

        dialog5.show();
    }

    public void viewItem() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.view_layout);

        ImageView img = (ImageView) dialog.findViewById(R.id.imageView2);
        img.setImageURI(list.get(info.position).getImageUri());

        TextView nm = (TextView) dialog.findViewById(R.id.textView3);
        TextView tl = (TextView) dialog.findViewById(R.id.textView4);
        TextView title = (TextView) dialog.findViewById(R.id.textView5);
        nm.setText(String.format("Name: %s" ,list.get(info.position).getName()));
        tl.setText(String.format("Phone: %s" ,list.get(info.position).getTel()));
        title.setText(list.get(info.position).getName());

        Button dialogButton = (Button) dialog.findViewById(R.id.button3);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void deleteItem() {
        dialog4 = new Dialog(this);
        dialog4.setContentView(R.layout.delete_layout);

        TextView ttl = (TextView) dialog4.findViewById(R.id.textView8);
        TextView mess = (TextView) dialog4.findViewById(R.id.textView9);
        Button btnYes = (Button) dialog4.findViewById(R.id.button5);
        Button btnNo = (Button) dialog4.findViewById(R.id.button6);

        ttl.setText("Delete Confirmation");
        mess.setText(String.format("Do you really want to delete %s on the list/database?", list.get(info.position).getName()));

        btnYes.setOnClickListener(this);
        btnNo.setOnClickListener(this);

        dialog4.show();
    }

    public void callPhone() {
        String phone = list.get(info.position).getTel();
        Uri uriPhone = Uri.parse("tel:" +phone);
        call = new Intent(Intent.ACTION_CALL, uriPhone);

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{

                        Manifest.permission.CALL_PHONE}, 10);
            }
        } else {
            this.startActivity(call);
        }
    }

    public void sendMessage() {
        sendSMS = new Intent(Intent.ACTION_VIEW);
        sendSMS.putExtra("address", list.get(info.position).getTel());
        sendSMS.putExtra("sms_body","");
        sendSMS.setType("vnd.android-dir/mms-sms");

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{
                        Manifest.permission.SEND_SMS}, 20);
            }
        } else {
            this.startActivity(sendSMS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case 10:
                if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    final Dialog dialog2 = new Dialog(this);
                    dialog2.setContentView(R.layout.permission_layout);

                    TextView t1 = (TextView) dialog2.findViewById(R.id.textView6);
                    TextView t2 = (TextView) dialog2.findViewById(R.id.textView7);
                    t1.setText("Request Permissions");
                    t2.setText("Please allow permissions if you want this application to perform the task.");

                    Button dialogButton = (Button) dialog2.findViewById(R.id.button4);
                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog2.dismiss();
                        }
                    });

                    dialog2.show();
                } else {
                    this.startActivity(call);
                }

                break;
            case 20:
                if(ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                    final Dialog dialog1 = new Dialog(this);
                    dialog1.setContentView(R.layout.permission_layout);

                    TextView t1 = (TextView) dialog1.findViewById(R.id.textView6);
                    TextView t2 = (TextView) dialog1.findViewById(R.id.textView7);
                    t1.setText("Request Permissions");
                    t2.setText("Please allow permissions if you want this application to perform the task.");

                    Button dialogButton = (Button) dialog1.findViewById(R.id.button4);
                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog1.dismiss();
                        }
                    });

                    dialog1.show();
                } else {
                    this.startActivity(sendSMS);
                }

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK) {
            if(requestCode == 0) {
                Bundle b = data.getExtras();

                Uri imageUri = b.getParcelable("image");
                String name = b.getString("name");
                String tel = b.getString("tel");

                this.db.addContact(imageUri.toString(), name, tel);
                this.list.add(new Contact(imageUri, name, tel));
                this.adapter.notifyDataSetChanged();
            } else if(requestCode == 200) {
                if(data != null) {
                    editImageUri = data.getData();
                    try {
                        editImage.setImageURI(editImageUri);
                    } catch(Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch(id) {
            case R.id.imageView1:
                Intent in = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                this.startActivityForResult(in, 200);
                break;

            case R.id.button:
                String setname = editName.getText().toString();
                String setphone = editPhone.getText().toString();

                list.get(info.position).setImageUri(editImageUri);
                list.get(info.position).setName(setname);
                list.get(info.position).setTel(setphone);

                db.deleteContact(pastNum);
                db.addContact(editImageUri.toString(), setname, setphone);
                adapter.notifyDataSetChanged();

                Toast.makeText(this, String.format("%s has been edited", setname), Toast.LENGTH_SHORT).show();
            case R.id.button2:
                dialog5.dismiss();
                break;

            case R.id.button5:
                String toastMessage = String.format("%s was successfully deleted from the list/database.", list.get(info.position).getName());
                String phoneNum = list.get(info.position).getTel();
                list.remove(info.position);
                db.deleteContact(phoneNum);
                adapter.notifyDataSetChanged();

                Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();
            case R.id.button6:
                dialog4.dismiss();

                break;
        }
    }
}
