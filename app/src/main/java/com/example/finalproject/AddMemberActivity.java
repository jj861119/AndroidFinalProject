package com.example.finalproject;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.UUID;

public class AddMemberActivity extends AppCompatActivity {

    private int ImageNum = 0;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Uri uri;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser;
    private String imageURL;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private byte[] data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);

        mAuth.signInWithEmailAndPassword("jj8611192@gmail.com", "jacky861132");
        currentUser = mAuth.getCurrentUser();
        final ImageView imageView = findViewById(R.id.imageView);
        imageView.setImageResource(R.drawable.default_img);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("myCards");

        //uri = Uri.parse("android.resource://FinalProject/drawable/default_img");

        Resources resources = AddMemberActivity.this.getResources();
        String path = ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + resources.getResourcePackageName(R.drawable.default_img) + "/"
                + resources.getResourceTypeName(R.drawable.default_img) + "/"
                + resources.getResourceEntryName(R.drawable.default_img);
        uri = Uri.parse(path);

        Button AddButton = (Button) findViewById(R.id.AddButton);
        AddButton.setOnClickListener(new Button.OnClickListener(){
        @Override
        public void onClick(View v) {
            final EditText name = findViewById(R.id.name);
            final EditText description = findViewById(R.id.description);
            //Toast.makeText(AddMemberActivity.this, "Add clicked", Toast.LENGTH_LONG).show();
            ImageNum++;
            //Log.i("123","111111111111111111111111111");
            uploadImage();
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.i("imageUrlF",imageURL);
                    FirebaseMember newFirebaseMember = new FirebaseMember(name.getText().toString(),imageURL,description.getText().toString());
                    Log.i("newName",newFirebaseMember.getImageURL());
                    myRef.child(newFirebaseMember.getName()).setValue(newFirebaseMember);

                    finish();
                }
            }, 4000);

        }});


        Button ImageButton = (Button) findViewById(R.id.ImageButton);
        ImageButton.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v) {
                Intent intent = new Intent();
                //開啟Pictures畫面Type設定為image
                intent.setType("image/*");
                //使用Intent.ACTION_GET_CONTENT這個Action                                            //會開啟選取圖檔視窗讓您選取手機內圖檔
                intent.setAction(Intent.ACTION_GET_CONTENT);
                //取得相片後返回本畫面
                startActivityForResult(intent, 1);

                imageView.setDrawingCacheEnabled(true);
                imageView.buildDrawingCache();
                Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                data = baos.toByteArray();
            }
        });

    }

    //取得相片後返回的監聽式
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //當使用者按下確定後
        if (resultCode == RESULT_OK) {
            //取得圖檔的路徑位置
            uri = data.getData();
            //寫log
            //Log.e("uri", uri.toString());
            //抽象資料的接口
            ContentResolver cr = this.getContentResolver();
            FileOutputStream outputStream;
            try {
                //由抽象資料接口轉換圖檔路徑為Bitmap
                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                //取得圖片控制項ImageView
                ImageView imageView = (ImageView) findViewById(R.id.imageView);
                // 將Bitmap設定到ImageView
                imageView.setImageBitmap(bitmap);
                int dimensionInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, getResources().getDisplayMetrics());
                imageView.getLayoutParams().height = dimensionInDp;

                //ByteArrayOutputStream out = new ByteArrayOutputStream();
                //bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                //out.flush();
                // out.close();
                // final byte[] buffer = out.toByteArray();
                // outputStream.write(buffer);
                //outputStream = AddMemberActivity.this.openFileOutput("newImage"+ImageNum+".jpg",
                //        Context.MODE_PRIVATE);
                //Log.i("DIR",getFilesDir().toString());
                //out.writeTo(outputStream);
                //out.close();
                //outputStream.close();
            } catch (FileNotFoundException e) {
                Log.e("Exception", e.getMessage(),e);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadImage() {

        if (currentUser != null) {

            if(uri != null)
            {
                //final ProgressDialog progressDialog = new ProgressDialog(this);
                //progressDialog.setTitle("Uploading...");
                //progressDialog.show();




                final StorageReference ref = storageReference.child("photos/"+ UUID.randomUUID().toString());
                StorageTask storageTask;
                storageTask = ref.putFile(uri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                //progressDialog.dismiss();
                                //Toast.makeText(AddMemberActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                //progressDialog.dismiss();
                                Log.i("Failed","Failed");
                                //Toast.makeText(AddMemberActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                        //.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        //    @Override
                        //    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        //        double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                        //               .getTotalByteCount());
                        //        progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        //    }
                        //});



                Task<Uri> urlTask = storageTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        // Continue with the task to get the download URL
                        return ref.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            imageURL=downloadUri.toString();
                            Log.i("imageUrl",imageURL);

                        } else {
                            // Handle failures
                        }
                    }
                });

            }

        } else {
        }


    }

}

