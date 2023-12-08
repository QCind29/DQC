package com.food.lite.nckh.detection;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
//import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.food.lite.nckh.detection.adapter.CateAdapter;
import com.food.lite.nckh.detection.customview.OverlayView;
import com.food.lite.nckh.detection.env.ImageUtils;
import com.food.lite.nckh.detection.env.Logger;
import com.food.lite.nckh.detection.env.Utils;
import com.food.lite.nckh.detection.sqlite.CateDB;
import com.food.lite.nckh.detection.tflite.Classifier;
import com.food.lite.nckh.detection.tflite.YoloV5Classifier;
import com.food.lite.nckh.detection.tracking.MultiBoxTracker;

import org.tensorflow.lite.examples.detection.R;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;


public class DetectGallery extends AppCompatActivity {
    public static final float MINIMUM_CONFIDENCE_TF_OD_API = 0.3f;
    public static final int TF_OD_API_INPUT_SIZE = 640;
    private static final boolean TF_OD_API_IS_QUANTIZED = false;
    private static final String TF_OD_API_MODEL_FILE = "best-fb160().tflite";
    private static final String TF_OD_API_LABELS_FILE = "file:///android_asset/label100c3.txt";

    //private  Classifier detector;
    private YoloV5Classifier detector;


    CateDB cateDB;

    RecyclerView rcv;

     CateAdapter cateAdapter;

    ImageView imgHinh;
    Button btnCamera, btnChoose, btnList, btnAdd;
    TextView txtResult, txtSen;
    int imgSize = 640;


    private static final boolean MAINTAIN_ASPECT = true;
    final Integer sensorOrientation = 90; //edit

    private Matrix frameToCropTransform;
    private Matrix cropToFrameTransform;
    private MultiBoxTracker tracker;
    private OverlayView trackingOverlay;

    protected int previewWidth = 0;
    protected int previewHeight = 0;

    //private Bitmap sourceBitmap;
    private Bitmap cropBitmap;

    ProgressDialog loading;

    //////////////////////////////////////////////////////////



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_detect);
///////////////////////////////////////////////////////////////////


        rcv = (RecyclerView) findViewById(R.id.idRecycle1);

        rcv.setLayoutManager(new LinearLayoutManager(this));
        rcv.setHasFixedSize(true);

        cateDB = new CateDB(this);


        imgHinh = (ImageView) findViewById(R.id.imageViewHinh);
        btnCamera = (Button) findViewById(R.id.btnOpenCam);
        btnChoose = (Button) findViewById(R.id.btnChooseImg);
        btnList = (Button) findViewById(R.id.btList) ;
        txtResult = (TextView) findViewById(R.id.result);
        btnAdd = (Button) findViewById(R.id.btAdd);


        btnAdd.setOnClickListener(v -> startActivity(new Intent(DetectGallery.this,Input_Activity.class)));
        btnCamera.setOnClickListener(v -> openCamera());

        btnChoose.setOnClickListener(v -> openGallery());


        btnList.setOnClickListener(v -> startActivity(new Intent(DetectGallery.this,ListTV.class)));//Realtime



        try {
            detector =
                    YoloV5Classifier.create(
                            getAssets(),
                            TF_OD_API_MODEL_FILE,
                            TF_OD_API_LABELS_FILE,
                            TF_OD_API_IS_QUANTIZED,
                            TF_OD_API_INPUT_SIZE);
        } catch (final IOException e) {
            e.printStackTrace();
            //LOGGER.e(e, "Exception initializing classifier!");
            Toast toast =
                    Toast.makeText(
                            getApplicationContext(), "Classifier could not be initialized", Toast.LENGTH_SHORT);
            toast.show();
            finish();
        }
    }


    public void openCamera(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 2);
    }
    public void openGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 3);
    }

    private static final Logger LOGGER = new Logger();

    public void classifyImage(Bitmap img){
        final List<Classifier.Recognition> results = detector.recognizeImage(img);
        //final Canvas canvas = new Canvas(img);
        final Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2.0f);

        final List<Classifier.Recognition> mappedRecognitions =
                new LinkedList<Classifier.Recognition>();

        String s = "";
        String maxClass = "";
        float maxconf = 0;
        for (final Classifier.Recognition result : results) {
            final RectF location = result.getLocation();
            if (location != null && result.getConfidence() >= MINIMUM_CONFIDENCE_TF_OD_API) {
                //canvas.drawRect(location, paint);
                cropToFrameTransform.mapRect(location);
                result.setLocation(location);
                mappedRecognitions.add(result);
            }
//            float confidences = result.getConfidence();
            if(result.getConfidence() > MINIMUM_CONFIDENCE_TF_OD_API) {
                if (result.getConfidence() > maxconf) {
                    maxClass = result.getTitle();
                    maxconf = result.getConfidence();
                }
            }
        }
        String sx = String.format("%s", maxClass);
        s = s.concat(sx);
        tracker.trackResults(mappedRecognitions, new Random().nextInt());
        trackingOverlay.postInvalidate();
        trackingOverlay.setVisibility(View.VISIBLE);
        imgHinh.setImageBitmap(img);
        if(maxClass != "") {
            String name = cateDB.getCateName(Integer.parseInt(s));
            txtResult.setText(name);

            rcv = (RecyclerView) findViewById(R.id.idRecycle1);
            rcv.setLayoutManager(new LinearLayoutManager(this));
            rcv.setHasFixedSize(true);
            cateAdapter = new CateAdapter(this,cateDB.getCate(Integer.parseInt(maxClass)));
            rcv.setAdapter(cateAdapter);
//
//


        } else {
            txtResult.setText("Không thể nhận dạng  " + s);

        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == 2) {
                Bitmap imgCam = (Bitmap) data.getExtras().get("data");
                int dimension = Math.min(imgCam.getWidth(), imgCam.getHeight());
                imgCam = ThumbnailUtils.extractThumbnail(imgCam, dimension, dimension);

                cropBitmap = Utils.processBitmap(imgCam, TF_OD_API_INPUT_SIZE);
                imgHinh.setImageBitmap(imgCam);

                loading = new ProgressDialog(this);
                loading.setMessage("Đang nhận dạng");
                loading.show();
                initBox();
                imgCam = Bitmap.createScaledBitmap(imgCam, imgSize, imgSize, false);
//
                Bitmap finalImgCam = imgCam;
                new CountDownTimer(2000,1000) {
                    public void onFinish() {
                        classifyImage(finalImgCam);
                        loading.dismiss();
                    }

                    public void onTick(long millisUntilFinished) {

                    }
                }.start();

            } else if (requestCode == 3) {
                Uri dat = data.getData();
                Bitmap imgChoose = null;
                try {
                    imgChoose = MediaStore.Images.Media.getBitmap(this.getContentResolver(), dat);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                cropBitmap = Utils.processBitmap(imgChoose, TF_OD_API_INPUT_SIZE);
                imgHinh.setImageBitmap(imgChoose);
                loading = new ProgressDialog(this);
                loading.setMessage("Đang nhận dạng");
                loading.show();
                initBox();
//
                imgChoose = Bitmap.createScaledBitmap(imgChoose, imgSize, imgSize, false);
                Bitmap finalImgChoose = imgChoose;
                new CountDownTimer(2000,1000) {
                    public void onFinish() {
                        classifyImage(finalImgChoose);
                        loading.dismiss();
                    }

                    public void onTick(long millisUntilFinished) {

                    }
                }.start();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initBox() {
        previewHeight = TF_OD_API_INPUT_SIZE;
        previewWidth = TF_OD_API_INPUT_SIZE;
        frameToCropTransform =
                ImageUtils.getTransformationMatrix(
                        previewWidth, previewHeight,
                        TF_OD_API_INPUT_SIZE, TF_OD_API_INPUT_SIZE,
                        sensorOrientation, MAINTAIN_ASPECT);

        cropToFrameTransform = new Matrix();
        frameToCropTransform.invert(cropToFrameTransform);

        tracker = new MultiBoxTracker(this);
        trackingOverlay = findViewById(R.id.tracking_overlay);
        trackingOverlay.addCallback(
                canvas -> tracker.draw(canvas));

        tracker.setFrameConfiguration(TF_OD_API_INPUT_SIZE, TF_OD_API_INPUT_SIZE, sensorOrientation);

        try {
            detector =
                    YoloV5Classifier.create(
                            getAssets(),
                            TF_OD_API_MODEL_FILE,
                            TF_OD_API_LABELS_FILE,
                            TF_OD_API_IS_QUANTIZED,
                            TF_OD_API_INPUT_SIZE);
            Log.d("YoloV5Classifier",  "model loaded successfully: " + TF_OD_API_MODEL_FILE);
            //detector.useGpu();
        } catch (final IOException e) {
            e.printStackTrace();
            LOGGER.e(e, "Exception initializing classifier!");
            Toast toast =
                    Toast.makeText(
                            getApplicationContext(), "Classifier could not be initialized", Toast.LENGTH_SHORT);
            toast.show();
            finish();
        }
    }
}

