
package com.app_services.mr_kaushik.fakepost;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;


import com.bumptech.glide.Glide;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.luseen.autolinklibrary.AutoLinkMode;
import com.luseen.autolinklibrary.AutoLinkTextView;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class TweetWithImage extends AppCompatActivity {

    private static final String TAG = TweetWithImage.class.getSimpleName();
    public static final int REQUEST_IMAGE = 100;


    AutoLinkTextView content;
    AlertDialog alertDialog;
    CircularImageView profilePic;
    TextView name, username;
    TextView textViewTime, textViewDate, textViewDevice;
    TextView textViewReTweet, textViewLikes;
    ImageView verifiedIcon;
    RoundedImageView imageTweet;
    boolean isProfilePic;

    Calendar calendar;
    int hour, minute;
    int mYear, mMonth, mDay;

    String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "June", "July",
            "Aug", "Sept", "Oct", "Nov", "Dec"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_with_image);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).setTitle("Tweet with Image");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        }
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



        profilePic = findViewById(R.id.profilePic);
        name = findViewById(R.id.name);
        verifiedIcon = findViewById(R.id.verified_icon);
        username = findViewById(R.id.username);
        content = findViewById(R.id.body);

        textViewTime = findViewById(R.id.textViewTime);
        imageTweet = findViewById(R.id.image_tweet);

        textViewDate = findViewById(R.id.textViewDate);
        textViewDevice = findViewById(R.id.textViewDevice);
        textViewReTweet = findViewById(R.id.textViewReTweet);
        textViewLikes = findViewById(R.id.textViewLikes);


        textViewReTweet.setText(R.string.number);
        textViewLikes.setText(R.string.number);
//
//        Typeface font = Typeface.createFromAsset(getAssets(), "font_helvetica_neue.ttf");
//        name.setTypeface(font);
//        username.setTypeface(font);
//        content.setTypeface(font);
//        textViewTime.setTypeface(font);
//        textViewDate.setTypeface(font);
//        textViewDevice.setTypeface(font);
//        textViewReTweet.setTypeface(font);
//        textViewLikes.setTypeface(font);



        //Setting Defaults Value
        calendar = Calendar.getInstance();
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        String format = getTimeFormat(hour);
        if (hour>12){
            hour = hour-12;
        } else if(hour==0){
            hour = 12;
        }
        String strValueOfTime = hour + ":" + minute + " " + format;
        textViewTime.setText(strValueOfTime);

        mYear = calendar.get(Calendar.YEAR); // current year
        mMonth = calendar.get(Calendar.MONTH); // current month
        mDay = calendar.get(Calendar.DAY_OF_MONTH); // current day
        final String newDate = mDay + " " + months[mMonth-1] + " " + mYear;
        textViewDate.setText(newDate);



        content.addAutoLinkMode(
                AutoLinkMode.MODE_HASHTAG,
                AutoLinkMode.MODE_PHONE,
                AutoLinkMode.MODE_URL,
                AutoLinkMode.MODE_EMAIL,
                AutoLinkMode.MODE_MENTION);
        content.setHashtagModeColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlueType1));
        content.setEmailModeColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlueType1));
        content.setMentionModeColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlueType1));
        content.setUrlModeColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlueType1));



        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TweetWithImage.this);
                builder.setTitle("Name");

                final View customLayout = getLayoutInflater().inflate(R.layout.single_edit_dialog, null);
                builder.setView(customLayout);

                final EditText editTextName = customLayout.findViewById(R.id.editTextValue);
                editTextName.setInputType(InputType.TYPE_CLASS_TEXT);

                final Switch verified = customLayout.findViewById(R.id.verified);

                verified.setVisibility(View.VISIBLE);
                if (verifiedIcon.isShown()){
                    verified.setChecked(true);
                } else {
                    verified.setChecked(false);
                }
                verified.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked){
                            verifiedIcon.setVisibility(View.VISIBLE);
                        } else {
                            verifiedIcon.setVisibility(View.GONE);
                        }
                    }
                });

                Log.i("Name Value", "onClick: name = " + name.getText().toString());
                if (name.getText().toString().contains("Tap to add name")){
                    editTextName.setText("");
                } else {
                    String oldValue = name.getText().toString();
                    editTextName.setText(oldValue);
                }

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (editTextName.getText().length()>0){
                            name.setText(editTextName.getText());
                        }
                    }
                });

                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TweetWithImage.this);
                builder.setTitle("Username");

                final View customLayout = getLayoutInflater().inflate(R.layout.single_edit_dialog, null);
                builder.setView(customLayout);

                final EditText editTextUserName = customLayout.findViewById(R.id.editTextValue);
                editTextUserName.setInputType(InputType.TYPE_CLASS_TEXT);

                if (username.getText().length()>0){
                    String oldValue = username.getText().toString().split("@")[1];
                    editTextUserName.setText(oldValue);
                }

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (editTextUserName.getText().length()>0){
                            username.setText("@".concat(editTextUserName.getText().toString()));
                        }
                    }
                });

                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TweetWithImage.this);
                builder.setTitle("Tweet Body");

                final View customLayout = getLayoutInflater().inflate(R.layout.edit_text_for_tweet, null);
                builder.setView(customLayout);

                final EditText editTextBody = customLayout.findViewById(R.id.tweetContent);
                editTextBody.setInputType(InputType.TYPE_CLASS_TEXT);
                editTextBody.setSingleLine(false);
                editTextBody.setLines(8);
                editTextBody.setMaxLines(30);
                editTextBody.setGravity(Gravity.START | Gravity.TOP);
                editTextBody.setHorizontalScrollBarEnabled(false);
                editTextBody.setBackgroundColor(Color.TRANSPARENT);

                final TextView textViewCounter = customLayout.findViewById(R.id.counter);

                if (content.getText().length()>0){
                    String oldValue = content.getText().toString();
                    editTextBody.setText(oldValue);
                }

                editTextBody.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        String length = s.length() + "/280";
                        textViewCounter.setText(length);
                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (editTextBody.getText().length()>0){
                            content.setAutoLinkText(editTextBody.getText().toString());
                        }
                    }
                });

                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        content.addAutoLinkMode(AutoLinkMode.MODE_HASHTAG, AutoLinkMode.MODE_EMAIL,
                                AutoLinkMode.MODE_MENTION, AutoLinkMode.MODE_URL);
                        content.setHashtagModeColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlueType1));
                        content.setEmailModeColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlueType1));
                        content.setMentionModeColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlueType1));
                        content.setUrlModeColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlueType1));


                        dialog.cancel();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });



        textViewTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(TweetWithImage.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String format;
                        format = getTimeFormat(selectedHour);

                        if (selectedHour>12){
                            selectedHour = selectedHour-12;
                        } else if(selectedHour==0){
                            selectedHour = 12;
                        } else{
                            Log.i("SelectedHour", "onTimeSet: selected Hour" + selectedHour);
                        }

                        String strValueOfTime = selectedHour + ":" + selectedMinute + " " + format;
                        textViewTime.setText( strValueOfTime );
                    }
                }, hour, minute, false);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        textViewDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog;
                datePickerDialog = new DatePickerDialog(TweetWithImage.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                String newDate = dayOfMonth + " " + months[monthOfYear] + " " + year;
                                textViewDate.setText(newDate);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });


        textViewDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String str = "Twitter for ";
                final String[] deviceArray = new String[]{"Android", "iPhone"};

                int checkedItem;
                String oldValue = textViewDevice.getText().toString();
                if (oldValue.contains("Android")){
                    checkedItem = 0;
                } else {
                    checkedItem = 1;
                }

                final AlertDialog.Builder builder = new AlertDialog.Builder(TweetWithImage.this);
                builder.setTitle("Select Your Device");
                builder.setSingleChoiceItems(deviceArray, checkedItem, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {

                        Log.i("Device Selection", "onClick: Before Selection " + str);
                        String newValue = str + deviceArray[item];
                        textViewDevice.setText(newValue);
                        Log.i("Device Selection", "onClick: After Selection " + str);
                        alertDialog.dismiss();
                    }
                });
                alertDialog = builder.create();
                alertDialog.show();
            }
        });

        textViewReTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TweetWithImage.this);
                builder.setTitle("Retweets");

                final View customLayout = getLayoutInflater().inflate(R.layout.single_edit_dialog, null);
                builder.setView(customLayout);

                final long[] value = new long[1];
                final EditText editTextNumValue = customLayout.findViewById(R.id.editTextValue);


                editTextNumValue.setInputType(InputType.TYPE_CLASS_NUMBER);
                editTextNumValue.setFilters(new InputFilter[] { new InputFilter.LengthFilter(10) });


                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String newValue;
                        value[0] = Long.parseLong(String.valueOf(editTextNumValue.getText()));

                        if (value[0] <= 999) {
                            newValue = editTextNumValue.getText().toString();
                        } else if (value[0] < 9999){
                            String str = editTextNumValue.getText().toString();
                            newValue = str.charAt(0) + "," + str.charAt(1) + str.charAt(2) +str.charAt(3);
                        } else {

                            final String[] units = new String[]{"", "K", "M", "B"};
                            int digitGroups = (int) (Math.log10(value[0]) / Math.log10(1000));
                            newValue = new DecimalFormat("#,##0.#").format(value[0] / Math.pow(1000, digitGroups)) + "" + units[digitGroups];
                        }
                        textViewReTweet.setText(newValue);
                    }
                });

                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        textViewLikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TweetWithImage.this);
                builder.setTitle("Likes");

                final View customLayout = getLayoutInflater().inflate(R.layout.single_edit_dialog, null);
                builder.setView(customLayout);

                final long[] value = new long[1];
                final EditText editTextNumValue = customLayout.findViewById(R.id.editTextValue);

                editTextNumValue.setInputType(InputType.TYPE_CLASS_NUMBER);
                editTextNumValue.setFilters(new InputFilter[] { new InputFilter.LengthFilter(10) });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String newValue;
                        value[0] = Long.parseLong(String.valueOf(editTextNumValue.getText()));
                        if (value[0] <= 999) {
                            newValue = editTextNumValue.getText().toString();
                        } else if (value[0] < 9999){
                            String str = editTextNumValue.getText().toString();
                            newValue = str.charAt(0) + "," + str.charAt(1) + str.charAt(2) +str.charAt(3);
                        } else {

                            final String[] units = new String[]{"", "K", "M", "B"};
                            int digitGroups = (int) (Math.log10(value[0]) / Math.log10(1000));
                            newValue = new DecimalFormat("#,##0.#").format(value[0] / Math.pow(1000, digitGroups)) + "" + units[digitGroups];
                        }
                        textViewLikes.setText(newValue);
                    }
                });

                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });


        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isProfilePic = true;
                Dexter.withActivity(TweetWithImage.this)
                        .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                if (report.areAllPermissionsGranted()) {
                                    showImagePickerOptions();
                                }

                                if (report.isAnyPermissionPermanentlyDenied()) {
                                    showSettingsDialog();
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();

            }
        });

        imageTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isProfilePic = false;
                Dexter.withActivity(TweetWithImage.this)
                        .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                if (report.areAllPermissionsGranted()) {
                                    showImagePickerOptions();
                                }

                                if (report.isAnyPermissionPermanentlyDenied()) {
                                    showSettingsDialog();
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();

            }
        });


        ImagePicker.clearCache(this);

    }



    private void loadProfile(String url) {
        Log.d(TAG, "Image cache path: " + url);
        if (isProfilePic){
            Glide.with(this).load(url)
                    .into(profilePic);
            profilePic.setColorFilter(ContextCompat.getColor(this, android.R.color.transparent));
        } else {
            Glide.with(this).load(url)
                    .into(imageTweet);
            imageTweet.setColorFilter(ContextCompat.getColor(this, android.R.color.transparent));
        }


    }


    public String getTimeFormat(int selectedHour){
        String format;
        if (selectedHour == 0) {
            format = "AM";
        }
        else if (selectedHour == 12) {
            format = "PM";
        }
        else if (selectedHour > 12) {
            format = "PM";
        }
        else {
            format = "AM";
        }
        return format;
    }


    private void showImagePickerOptions() {
        ImagePicker.showImagePickerOptions(this, new ImagePicker.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                launchCameraIntent();
            }

            @Override
            public void onChooseGallerySelected() {
                launchGalleryIntent();
            }
        });
    }

    private void launchCameraIntent() {
        Intent intent = new Intent(TweetWithImage.this, ImagePicker.class);
        intent.putExtra(ImagePicker.INTENT_IMAGE_PICKER_OPTION, ImagePicker.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePicker.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePicker.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePicker.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePicker.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePicker.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePicker.INTENT_BITMAP_MAX_HEIGHT, 1000);

        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void launchGalleryIntent() {
        Intent intent = new Intent(TweetWithImage.this, ImagePicker.class);
        intent.putExtra(ImagePicker.INTENT_IMAGE_PICKER_OPTION, ImagePicker.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePicker.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePicker.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePicker.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                assert data != null;
                Uri uri = data.getParcelableExtra("path");
                loadProfile(uri.toString());
            }
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Do you really want to go back?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user pressed "yes", then he is allowed to exit from application
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user select "No", just cancel this dialog and continue with app
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }



    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(TweetWithImage.this);
        builder.setTitle(getString(R.string.dialog_permission_title));
        builder.setMessage(getString(R.string.dialog_permission_message));
        builder.setPositiveButton(getString(R.string.go_to_settings), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton(getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }
}


