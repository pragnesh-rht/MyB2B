package com.rohit.pragnesh.myb2b.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.rohit.pragnesh.myb2b.R;
import com.rohit.pragnesh.myb2b.utility.AuthUtility;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NewPostActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int CAMERA_REQUEST = 34;
    private final String TAG = "NEOTag";
    int PICK_IMAGE_REQUEST = 33;
    Uri uri;
    ArrayList<String> imagePaths;
    AuthUtility authUtility = AuthUtility.getInstance();
    EditText et_post_title, et_post_desc, et_post_content, et_post_price;
    ImageView imageView1, imageView2, imageView3;
    Button btn_post;
    private String currentPhotoPath;
    private int current_index = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        imagePaths = new ArrayList<>();

        et_post_title = findViewById(R.id.et_post_title);
        et_post_desc = findViewById(R.id.et_post_desc);
        et_post_content = findViewById(R.id.et_post_content);
        et_post_price = findViewById(R.id.et_post_price);

        imageView1 = findViewById(R.id.imageView3);
        imageView2 = findViewById(R.id.imageView4);
        imageView3 = findViewById(R.id.imageView5);

        btn_post = findViewById(R.id.btn_post);

        imageView1.setOnClickListener(this);
        imageView2.setOnClickListener(this);
        imageView3.setOnClickListener(this);
        btn_post.setOnClickListener(this);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1001);
        }
    }

    private void selectImage() {
        new AlertDialog.Builder(this)
                .setMessage("Choose one")
                .setNegativeButton("take picture", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        takePicture();
                    }
                })
                .setPositiveButton("go to gallery", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    public void onClick(DialogInterface dialog, int id) {
                        showFileChooser();
                    }
                }).create().show();
    }

    private void takePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.e(TAG, "ERROR: " + ex);
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST);
            }
        }
    }

    private void showFileChooser() {
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
//        i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(i, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void setImage(ImageView imageView, String imagePath) {
        File f = new File(imagePath);

        Uri contentUri = Uri.fromFile(f);
        try {
            imageView.setImageBitmap(getBitmapFromUri(contentUri));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {

            if (data != null) {
                uri = data.getData();
                currentPhotoPath = getRealPathFromURI(uri);
                imagePaths.add(currentPhotoPath);
            }
        }
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            File f = new File(currentPhotoPath);
            uri = Uri.fromFile(f);
            imagePaths.add(currentPhotoPath);
        }

        ImageView imageView = imageView1;
        switch (current_index) {
            case 0:
                imageView = imageView1;
                break;
            case 1:
                imageView = imageView2;
                break;
            case 2:
                imageView = imageView3;
                break;
        }
        setImage(imageView, imagePaths.get(current_index));
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private String getRealPathFromURI(Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            CursorLoader loader = new CursorLoader(getApplicationContext(), contentUri, proj, null, null, null);
            cursor = loader.loadInBackground();
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            Log.e(TAG, "getRealPathFromURI Exception : " + e.toString());
            return "";
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    public void sendPost() {
        if (!TextUtils.isEmpty(et_post_title.getText()) &&
                !TextUtils.isEmpty(et_post_desc.getText()) &&
                !TextUtils.isEmpty(et_post_content.getText()) &&
                !TextUtils.isEmpty(et_post_price.getText())) {

            String title = et_post_title.getText().toString();
            String desc = et_post_desc.getText().toString();
            String content = et_post_content.getText().toString();
            int price = Integer.parseInt(et_post_price.getText().toString());

            authUtility.sendPost(title, desc, content, price, imagePaths, this);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.btn_post:
                current_index = -1;
                sendPost();
                break;
            case R.id.imageView3:
                current_index++;
                selectImage();
                break;
            case R.id.imageView4:
                current_index++;
                selectImage();
                break;
            case R.id.imageView5:
                current_index++;
                selectImage();
                break;
        }
    }
}
