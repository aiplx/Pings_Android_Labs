package algonquin.cst2335.lian0122;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class SecondActivity extends AppCompatActivity {

    ImageView profileImage;
    private final static String TAG ="SecondActivity";
    ActivityResultLauncher<Intent> cameraResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult()
            ,new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if (result.getResultCode() == Activity.RESULT_OK) {

                        try {
                            Intent data = result.getData();
                            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                            profileImage.setImageBitmap(thumbnail);
                            // save the bitmap object
                            FileOutputStream fOut = null;
                            try { fOut = openFileOutput("ProfilePicture.png", Context.MODE_PRIVATE);
                                thumbnail.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                                fOut.flush();
                                fOut.close();
                            }
                            catch (FileNotFoundException e)
                            { e.printStackTrace();
                            }
                        }
                        catch(Exception e){
                            e.printStackTrace();
                            Log.w(TAG, "Can't output PNG");
                        }
                    }
                    else if(result.getResultCode() == Activity.RESULT_CANCELED)
                        Log.i(TAG, "User refused to capture a picture.");
                }
            } );

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Intent fromPrevious = getIntent();
        String emailAddress = fromPrevious.getStringExtra("EmailAddress");
        TextView tv_email = findViewById(R.id.textView_secondActivity);
        tv_email.setText("Welcome back " + emailAddress); // set the second activity's top as the value input in Main activity

//        make phone call
        Button btC_N = findViewById(R.id.button_callNumber);
        btC_N.setOnClickListener(clk->{
            // get the phone number
            EditText et_phone = findViewById(R.id.editText_Phone);
            String phoneNumber = et_phone.getText().toString();
            Intent call = new Intent(Intent.ACTION_DIAL);
            call.setData(Uri.parse("tel:" + phoneNumber));
            startActivity(call);
        });

        // change profile picture
        Button btC_P = findViewById( R.id.button_changePic);
        btC_P.setOnClickListener(view -> {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                cameraResult.launch(takePictureIntent);
            }
        });

        //Picture container
        profileImage = findViewById( R.id.imageView_profile);

        // check if the ProfilePicture.png is saved, if so, then retrieve the file
        File file = new File(getFilesDir(), "ProfilePicture.png");
        if (file.exists()) {
            Bitmap theImage = BitmapFactory.decodeFile(file.getAbsolutePath());
            profileImage.setImageBitmap(theImage);
        }

    }
}