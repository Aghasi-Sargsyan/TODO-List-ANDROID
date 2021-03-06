package com.example.aghasi.todolist;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.aghasi.todolist.items.TodoItem;
import com.example.aghasi.todolist.util.Const;
import com.example.aghasi.todolist.util.RepeatPeriod;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.widget.Toast.makeText;

public class SecondActivity extends AppCompatActivity {
    private Button mButtonSave;
    private EditText mEditTitle, mEditDescription;
    private TextView mTextDateTime, mTextPriority;
    private CheckBox mCheckBoxReminder, mCheckBoxRepeat;
    private RadioGroup mRadioGroupPeriod;
    private ImageView mImageUp, mImageDown;
    private RepeatPeriod mRepeatPeriod;
    private int mPriorityCounter, mItemPosition;
    private boolean mIsEditButton;
    private Date mDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        mPriorityCounter = 0;
        mRepeatPeriod = null;
        mDate = Calendar.getInstance().getTime();
        mIsEditButton = false;

        idInitialization();

        mTextDateTime.setText(new SimpleDateFormat(Const.SECOND_DATE_FORMAT).format(mDate));

        itemEditor();

        mButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mIsEditButton) {
                    mButtonSave.setText(Const.SAVE_BUTTON_NAME);
                    mIsEditButton = false;
                    fieldsEnabler();
                } else {
                    if (mEditTitle.getText() != null && !mEditTitle.getText().toString().isEmpty()) {

                        Intent intent = new Intent();
                        intent.putExtra(Const.TODO_ITEM_KEY, itemCreator());
                        intent.putExtra(Const.ITEM_POSITION_KEY, mItemPosition);
                        setResult(RESULT_OK, intent);
                        finish();

                    } else {
                        Toast toast = makeText(SecondActivity.this, "Title is empty", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            }
        });

        mTextDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseDateAndTime();
            }
        });

        mCheckBoxReminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    compoundButton.setTextColor(getResources().getColor(R.color.green));
                } else {
                    compoundButton.setTextColor(getResources().getColor(R.color.checkbox_color));
                }
            }
        });
        mCheckBoxRepeat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    mRadioGroupPeriod.setVisibility(View.VISIBLE);
                    mRadioGroupPeriod.check(R.id.radioButton_second_daily);
                    mRepeatPeriod = RepeatPeriod.DAILY;
                    compoundButton.setTextColor(getResources().getColor(R.color.green));
                } else {
                    mRadioGroupPeriod.setVisibility(View.GONE);
                    mRepeatPeriod = null;
                    compoundButton.setTextColor(getResources().getColor(R.color.checkbox_color));
                }
            }
        });

        View.OnClickListener upDownButtonsListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.image_second_up:
                        mPriorityCounter++;
                        break;
                    case R.id.image_second_down:
                        if (mPriorityCounter != 0) {
                            mPriorityCounter--;
                        }
                        break;
                }
                mTextPriority.setText(Const.PRIORITY_TEXT_NAME + " " + mPriorityCounter);
            }
        };

        mImageUp.setOnClickListener(upDownButtonsListener);
        mImageDown.setOnClickListener(upDownButtonsListener);

    }

    private void idInitialization() {
        mButtonSave = findViewById(R.id.button_second_save);
        mEditTitle = findViewById(R.id.edit_second_title);
        mEditDescription = findViewById(R.id.edit_second_description);
        mTextDateTime = findViewById(R.id.text_second_date);
        mTextPriority = findViewById(R.id.text_second_priority);
        mCheckBoxReminder = findViewById(R.id.checkbox_second_reminder);
        mCheckBoxRepeat = findViewById(R.id.checkbox_second_repeat);
        mRadioGroupPeriod = findViewById(R.id.radioGroup_second_period);
        mImageUp = findViewById(R.id.image_second_up);
        mImageDown = findViewById(R.id.image_second_down);
    }

    private void repeatPeriodChecker() {
        switch (mRadioGroupPeriod.getCheckedRadioButtonId()) {
            case R.id.radioButton_second_daily:
                mRepeatPeriod = RepeatPeriod.DAILY;
                break;
            case R.id.radioButton_second_weekly:
                mRepeatPeriod = RepeatPeriod.WEEKLY;
                break;
            case R.id.radioButton_second_monthly:
                mRepeatPeriod = RepeatPeriod.MONTHLY;
                break;
        }
    }

    private TodoItem itemCreator() {
        String title = mEditTitle.getText().toString();
        String description = mEditDescription.getText().toString();
        Date date = mDate;
        return new TodoItem(title, description, date);
    }

    private void itemEditor() {
        Intent intent = getIntent();
        TodoItem item = (TodoItem) intent.getSerializableExtra(Const.TODO_ITEM_KEY);
        if (item != null) {
            mIsEditButton = true;
            mButtonSave.setText(Const.EDIT_BUTTON_NAME);
            fieldDisabler();
            mItemPosition = intent.getIntExtra(Const.ITEM_POSITION_KEY, 0);
            mEditTitle.setText(item.getTitle());
            mEditDescription.setText(item.getDescription());

            mDate = item.getDate();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Const.SECOND_DATE_FORMAT);
            mTextDateTime.setText(simpleDateFormat.format(mDate));

        }
    }

    private void chooseDateAndTime() {
        final Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(this, R.style.SpinnerDate, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(year, monthOfYear, dayOfMonth);
                new TimePickerDialog(SecondActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);

                        mDate = calendar.getTime();

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Const.SECOND_DATE_FORMAT);
                        mTextDateTime.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE)).show();

    }

    private void fieldDisabler() {
        mEditTitle.setFocusable(false);
        mEditDescription.setFocusable(false);
        mCheckBoxReminder.setEnabled(false);
        mCheckBoxRepeat.setEnabled(false);
        mTextDateTime.setEnabled(false);
        mImageUp.setEnabled(false);
        mImageDown.setEnabled(false);
    }

    private void fieldsEnabler() {
        mEditTitle.setFocusable(true);
        mEditTitle.setFocusableInTouchMode(true);
        mEditDescription.setFocusable(true);
        mEditDescription.setFocusableInTouchMode(true);
        mCheckBoxReminder.setEnabled(true);
        mCheckBoxRepeat.setEnabled(true);
        mTextDateTime.setEnabled(true);
        mImageUp.setEnabled(true);
        mImageDown.setEnabled(true);
    }
}

