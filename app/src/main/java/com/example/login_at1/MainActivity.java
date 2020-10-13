package com.example.login_at1;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;


public class MainActivity extends Activity  {
    Button buttonlogin,buttonsignup,buttoncancel;
    ImageButton click,take;
    EditText editTextentername,editTextpassword;
    private static final int CAMERA_REQUEST = 18;
    TextView textViewattemptsleft;
    ImageView imageView;
    int counter = 3;
    SQLiteDatabase dbase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonlogin = (Button)findViewById(R.id.buttonlogin);
        editTextentername = (EditText)findViewById(R.id.editTextentername);
        editTextpassword = (EditText)findViewById(R.id.editTextpassword);

        imageView = findViewById(R.id.imageView);

        buttoncancel = (Button)findViewById(R.id.buttoncancel);
        textViewattemptsleft = (TextView)findViewById(R.id.textViewattemptsleft);
        textViewattemptsleft.setVisibility(View.GONE);

        click = (ImageButton)findViewById(R.id.click);
        take = (ImageButton) findViewById(R.id.take);

        buttonsignup = (Button)findViewById(R.id.buttonsignup);
        dbase= openOrCreateDatabase("E-Events", Context.MODE_PRIVATE,null);
        buttonsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(), CreateAccountActivity.class);
                startActivity(i);
            }
        });

        buttonlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if(editTextentername.getText().toString().isEmpty() || editTextpassword.getText().toString().isEmpty()){
                    showMessage("Error","Please Enter all Values");
                }

                else{

                    Cursor c = dbase.rawQuery("SELECT username,name,password,email_id FROM users WHERE username='"+editTextentername.getText().toString()+"';",null);
                    if (c.moveToFirst()) {
                        // Displaying record if found 
                        //String u  = c.getString(0);
                        String p = c.getString(2);
                        Log.d("msg from database", p);
                        Log.d("msg from edittext", editTextpassword.getText().toString());
                        //String mobile = c.getString(2);
                        if(p.equals(editTextpassword.getText().toString()))
                        {
                            Toast.makeText(getApplicationContext(),"Login Successful", Toast.LENGTH_LONG).show();

                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Wrong Credentials",Toast.LENGTH_SHORT).show();

                            textViewattemptsleft.setVisibility(View.VISIBLE);
                            textViewattemptsleft.setBackgroundColor(Color.RED);
                            counter--;
                            textViewattemptsleft.setText(Integer.toString(counter));

                            if (counter == 0) {
                                buttonlogin.setEnabled(false);
                            }
                        }
                    }


                }

                /*if(editTextentername.getText().toString().equals("admin") &&
                        editTextpassword.getText().toString().equals("admin")) {
                    Toast.makeText(getApplicationContext(),
                            "Redirecting...",Toast.LENGTH_SHORT).show();
                }*/


            }
        });

        buttoncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        click.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent,CAMERA_REQUEST);

            }
        } );

        take.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent,3);
                }
        });
    }

    private void showMessage(String error, String please_enter_all_values) {
        AlertDialog.Builder a = new AlertDialog.Builder(this);
        a.setTitle(error);
        a.setCancelable(true);
        a.setMessage(please_enter_all_values);
        a.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == 3&& resultCode == RESULT_OK && data != null && data.getData() != null) {
                Uri imageUri = data.getData();
                ImageView imageView = (ImageView) findViewById(R.id.imageView);
                imageView.setImageURI(imageUri);
            }
            if (requestCode == CAMERA_REQUEST) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                imageView.setImageBitmap(photo);
                Toast.makeText(this,String.valueOf(resultCode), Toast.LENGTH_SHORT).show();
            }

    }


}