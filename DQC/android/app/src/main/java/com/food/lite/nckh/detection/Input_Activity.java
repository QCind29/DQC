package com.food.lite.nckh.detection;

import static java.sql.DriverManager.println;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.food.lite.nckh.detection.env.Utils;
import com.food.lite.nckh.detection.sqlite.CateDB;

import org.tensorflow.lite.examples.detection.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Input_Activity extends AppCompatActivity {
    CateDB cateDB;
    EditText cateName, cateType, cateUnit, cateCost, labelID;
    Button cateSave, btnCam, btnGal;
    ImageView imgView;
    private Uri selectedImageUri;
    private Bitmap imgBit;

    String name, type, unit;
    Integer cost, id;


    private static final int PICK_IMAGE_REQUEST = 1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cateDB = new CateDB(this);
        setContentView(R.layout.input_acivity);

        cateName = findViewById(R.id.inputName);
        cateType = findViewById(R.id.inputType);
        cateUnit = findViewById(R.id.inputUnit);
        cateCost = findViewById(R.id.inputCost);
        labelID = findViewById(R.id.inputID);

        cateSave= findViewById(R.id.inputSave);
        btnCam= findViewById(R.id.btCam);
        btnGal= findViewById(R.id.btGal);



        imgView = findViewById(R.id.inputImg);

        btnGal.setOnClickListener(v -> openGallery());
        btnCam.setOnClickListener(v -> openCamera());



        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
                Toast.makeText(Input_Activity.this, "Array Byte " + imgBit, Toast.LENGTH_SHORT).show();

            }
        });


        cateSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                byte[] img = bitmapToByteArray(imgBit);
                name = cateName.getText().toString();
                type = cateType.getText().toString();
                unit= cateUnit.getText().toString();
                cost = Integer.parseInt(cateCost.getText().toString());
                id = Integer.parseInt(labelID.getText().toString());
                save(name,id,type,unit,cost,img);
                cateName.getText().clear();
                cateType.getText().clear();
                cateUnit.getText().clear();
                cateCost.getText().clear();
                labelID.getText().clear();
                imgView.setImageResource(0); // Clear the ImageView

            }
        });
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, 3);
    }

    public void openCamera(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 2);
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
//            selectedImageUri = data.getData();
//            imgView.setImageURI(selectedImageUri);
//            imgBit = convertImageUriToBitmap(this,selectedImageUri);
//        }
//    }
protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

    if (resultCode == RESULT_OK) {
        if (requestCode == 2) {
            imgBit = (Bitmap) data.getExtras().get("data");
            imgView.setImageBitmap(imgBit);
//            imgBit = imgCam;
        } else if (requestCode == 3) {
            selectedImageUri = data.getData();
            imgView.setImageURI(selectedImageUri);
            imgBit = convertImageUriToBitmap(this, selectedImageUri);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }}

    private void save(String name,Integer id,String type, String unit,Integer cost,byte[] img) {
        try{
//            byte[] img = bitmapToByteArray(imgBit);
//            name = cateName.getText().toString();
//            type = cateType.getText().toString();
//            unit= cateUnit.getText().toString();
//            cost = Integer.parseInt(cateCost.getText().toString());
//            id = Integer.parseInt(labelID.getText().toString());

            cateDB.addItem(name,id,type,unit,cost,img);


        } catch (Exception e) {
            throw new RuntimeException(e);
        }




    }


    public static byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    public static byte[] getBytes(Bitmap bitmap) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }


//
//

//
public static byte[] convertImageUriToByteArray(Context context, Uri imageUri) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(imageUri);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }
    public Bitmap convertImageUriToBitmap(Context context, Uri imageUri) {
        try {
            // Get the InputStream from the content resolver
            InputStream inputStream = context.getContentResolver().openInputStream(imageUri);

            // Decode the InputStream into a Bitmap
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            // Close the InputStream
            if (inputStream != null) {
                inputStream.close();
            }

            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

//
}
