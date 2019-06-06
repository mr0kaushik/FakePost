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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.bumptech.glide.Glide;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.luseen.autolinklibrary.AutoLinkMode;
import com.luseen.autolinklibrary.AutoLinkTextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class FacebookWithText extends AppCompatActivity {
    private static final int AM = 0;
    private static final int PM = 1;

    private static final String TAG = TweetWithText.class.getSimpleName();
    public static final int REQUEST_IMAGE = 100;

    AlertDialog alertDialog;
    AutoLinkTextView content;
    CircularImageView profilePic;
    TextView name, dateAndTime;
    TextView textViewComments, textViewShares;
    TextView textComments, textShares;
    ImageView postPrivacyIcon;

    Calendar calendar;
    int hour, minute;
    int mYear, mMonth, mDay;

    String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "June", "July",
            "Aug", "Sept", "Oct", "Nov", "Dec"};

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_with_text);


        final Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).setTitle("Post with Text");
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
        dateAndTime = findViewById(R.id.dateAndTime);
        postPrivacyIcon = findViewById(R.id.postPrivacy);


        content = findViewById(R.id.body);

        textViewComments = findViewById(R.id.textViewComments);
        textViewShares = findViewById(R.id.textViewShares);

        textComments = findViewById(R.id.commentsText);
        textShares = findViewById(R.id.sharesText);

        textViewComments.setText(R.string.number);
        textViewShares.setText(R.string.number);


        //Setting Defaults Value
        String strDateAndTime = "Just Now";
        dateAndTime.setText(strDateAndTime);


        content.addAutoLinkMode(
                AutoLinkMode.MODE_HASHTAG,
                AutoLinkMode.MODE_URL,
                AutoLinkMode.MODE_EMAIL,
                AutoLinkMode.MODE_MENTION);

        content.setHashtagModeColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
        content.setEmailModeColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
        content.setMentionModeColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
        content.setUrlModeColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));



        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(FacebookWithText.this);
                builder.setTitle("Set Name");

                final View customLayout = getLayoutInflater().inflate(R.layout.single_edit_dialog, null);
                builder.setView(customLayout);

                final EditText editTextName = customLayout.findViewById(R.id.editTextValue);
                editTextName.setInputType(InputType.TYPE_CLASS_TEXT);


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

        dateAndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String oldValue = dateAndTime.getText().toString();
                final List<Integer> array = new ArrayList<>();

                AlertDialog.Builder builder = new AlertDialog.Builder(FacebookWithText.this);
                builder.setTitle("Set Date and Time");
                final View customLayout = getLayoutInflater().inflate(R.layout.fb_date_time_selector_layout, null);
                builder.setView(customLayout);

                final TextView tvTime = customLayout.findViewById(R.id.tvTime);
                final TextView tvDate = customLayout.findViewById(R.id.tvDate);
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
                tvTime.setText(strValueOfTime);

                mYear = calendar.get(Calendar.YEAR); // current year
                mMonth = calendar.get(Calendar.MONTH); // current month
                mDay = calendar.get(Calendar.DAY_OF_MONTH); // current day
                final String newDate = mDay + " " + months[mMonth-1] + " " + mYear;
                tvDate.setText(newDate);


                array.add(0, mDay);
                array.add(1, mMonth);
                array.add(2, mYear);
                array.add(3, minute);
                array.add(4, hour);
                if (format.equals("AM")){
                    array.add(5, AM);
                } else {
                    array.add(5, PM);
                }

                tvDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatePickerDialog datePickerDialog;
                        datePickerDialog = new DatePickerDialog(FacebookWithText.this,
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year,
                                                          int monthOfYear, int dayOfMonth) {
                                        String newDate = dayOfMonth + " " + months[monthOfYear] + " " + year;
                                        tvDate.setText(newDate);
                                        array.add(0, dayOfMonth);
                                        array.add(1, monthOfYear);
                                        array.add(2, year);

                                    }
                                }, mYear, mMonth, mDay);
                        datePickerDialog.show();
                    }
                });

                tvTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TimePickerDialog mTimePicker;
                        mTimePicker = new TimePickerDialog(FacebookWithText.this, new TimePickerDialog.OnTimeSetListener() {
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
                                tvTime.setText( strValueOfTime );
                                array.add(3, selectedMinute);
                                array.add(4, selectedHour);

                                if (format.equals("AM")){
                                    array.add(5, AM);
                                } else {
                                    array.add(5, PM);
                                }
                            }
                        }, hour, minute, false);
                        mTimePicker.setTitle("Select Time");
                        mTimePicker.show();
                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setDateAndTime(array);
                    }
                });

                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (oldValue.length()>0){
                            dateAndTime.setText(oldValue);
                        }
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
                AlertDialog.Builder builder = new AlertDialog.Builder(FacebookWithText.this);
                builder.setTitle("Post Body");

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
                        dialog.cancel();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        postPrivacyIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String[] deviceArray = new String[]{"Public", "Friends"};
                String oldValue = postPrivacyIcon.getDrawable().toString();
                Log.i(TAG, "onClick: " +  oldValue);

                final AlertDialog.Builder builder = new AlertDialog.Builder(FacebookWithText.this);
                builder.setTitle("Select Your Device");
                builder.setSingleChoiceItems(deviceArray, -1, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {

                        if (item == 0){
                            postPrivacyIcon.setImageDrawable(content.getResources().getDrawable(R.drawable.ic_world_icon));
                        } else {
                            postPrivacyIcon.setImageDrawable(content.getResources().getDrawable(R.drawable.ic_friends_icon));
                        }
                        alertDialog.dismiss();
                    }
                });
                alertDialog = builder.create();
                alertDialog.show();
            }
        });




        textViewComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(FacebookWithText.this);
                builder.setTitle("Set Comments");

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
                        textViewComments.setText(newValue);
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


        textViewShares.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(FacebookWithText.this);
                builder.setTitle("Set Shares");

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
                        textViewShares.setText(newValue);
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
                Dexter.withActivity(FacebookWithText.this)
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

    private void setDateAndTime(List<Integer> array) {
        String newValue, newFormat;
        calendar = Calendar.getInstance();
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        String format = getTimeFormat(hour);
        if (hour>12){
            hour = hour-12;
        } else if(hour==0){
            hour = 12;
        }

        mYear = calendar.get(Calendar.YEAR); // current year
        mMonth = calendar.get(Calendar.MONTH); // current month
        mDay = calendar.get(Calendar.DAY_OF_MONTH); // current day

        if (array.get(5) == 0){
            newFormat = "AM";
        } else {
            newFormat = "PM";
        }

        if (mDay==array.get(0) && mMonth==array.get(1) && mYear == array.get(2) && minute == array.get(3) && hour == array.get(4) && format.equals(newFormat)){
            newValue = "Just Now";
        }
        //MINUTES
        else if (mDay==array.get(0) && mMonth==array.get(1) && mYear == array.get(2) && minute > array.get(3) && hour == array.get(4) && format.equals(newFormat)) {
            newValue = minute-array.get(3) + "m";
        }
        //HOURS
        else if (mDay==array.get(0) && mMonth==array.get(1) && mYear == array.get(2) && hour > array.get(4) && format.equals(newFormat)) {
            newValue = Math.abs(hour-array.get(4)) + "hr";
        }
        //HOURS with different AM & PM
        else if (mDay==array.get(0) && mMonth==array.get(1) && mYear == array.get(2) && hour > array.get(4) && !format.equals(newFormat)) {
            newValue = 12 - Math.abs(hour-array.get(4)) + "hr";
        }
        //YESTERDAY
        else if (mDay-array.get(0)==1 && mMonth==array.get(1) && mYear == array.get(2)){
            newValue = "Yesterday at " + array.get(4) + ":" + array.get(3) + " " + newFormat;
        }
        else if (mDay==array.get(0) && mMonth==array.get(1) && mYear == array.get(2) && hour == array.get(4)){
            newValue = array.get(0) + " " + months[array.get(1)] + " at " + array.get(4) + ":" + array.get(3) + " " + newFormat;
        }
        else {
            newValue = array.get(0) + " " + months[array.get(1)]  + " " + array.get(2) + " at " + array.get(4) + ":" + array.get(3) + " " + newFormat;
        }
        dateAndTime.setText(newValue);
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

    private void loadProfile(String url) {
        Log.d(TAG, "Image cache path: " + url);

        Glide.with(this).load(url)
                .into(profilePic);
        profilePic.setColorFilter(ContextCompat.getColor(this, android.R.color.transparent));
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
        Intent intent = new Intent(FacebookWithText.this, ImagePicker.class);
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
        Intent intent = new Intent(FacebookWithText.this, ImagePicker.class);
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
                //Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                loadProfile(uri.toString());
            }
        }
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(FacebookWithText.this);
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

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }




}
