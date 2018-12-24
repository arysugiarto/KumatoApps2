package com.example.arysugiarto.kumatoapps;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.arysugiarto.kumatoapps.AlarmService.AlarmPreference;
import com.example.arysugiarto.kumatoapps.AlarmService.AlarmReceiver;
import com.example.arysugiarto.kumatoapps.db.NoteHelper;
import com.example.arysugiarto.kumatoapps.entity.Note;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FormAddUpdateActivity extends AppCompatActivity
        implements View.OnClickListener {
    EditText edtTitle, edtDescription;
    Button btnSubmit;

    public static String EXTRA_NOTE = "extra_note";
    public static String EXTRA_POSITION = "extra_position";

    private boolean isEdit = false;
    public static int REQUEST_ADD = 100;
    public static int RESULT_ADD = 101;
    public static int REQUEST_UPDATE = 200;
    public static int RESULT_UPDATE = 201;
    public static int RESULT_DELETE = 301;

    private Note note;
    private int position;
    private NoteHelper noteHelper;

    private TextView tvOneTimeDate, tvOneTimeTime;
    private EditText edtOneTimeMessage;
    private Button btnOneTimeDate, btnOneTimeTime, btnOneTime;
    private Calendar calOneTimeDate, calOneTimeTime;
    private AlarmReceiver alarmReceiver;
    private AlarmPreference alarmPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_add_update);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        tvOneTimeDate = (TextView) findViewById(R.id.tv_one_time_alarm_date);
        tvOneTimeTime = (TextView) findViewById(R.id.tv_one_time_alarm_time);
        edtOneTimeMessage = (EditText) findViewById(R.id.edt_one_time_alarm_message);
        btnOneTimeDate = (Button) findViewById(R.id.btn_one_time_alarm_date);
        btnOneTimeTime = (Button) findViewById(R.id.btn_one_time_alarm_time);
        btnOneTime = (Button) findViewById(R.id.btn_set_one_time_alarm);
        btnOneTimeDate.setOnClickListener(this);
        btnOneTimeTime.setOnClickListener(this);
        btnOneTime.setOnClickListener(this);

        edtTitle = (EditText) findViewById(R.id.edt_title);
        edtDescription = (EditText) findViewById(R.id.edt_description);
        btnSubmit = (Button) findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(this);

        noteHelper = new NoteHelper(this);
        noteHelper.open();

        note = getIntent().getParcelableExtra(EXTRA_NOTE);

        if (note != null) {
            position = getIntent().getIntExtra(EXTRA_POSITION, 0);
            isEdit = true;
        }

        String actionBarTitle = null;
        String btnTitle = null;

        if (isEdit) {
            actionBarTitle = "Ubah";
            btnTitle = "Update";
            edtTitle.setText(note.getTitle());
            edtDescription.setText(note.getDescription());
        } else {
            actionBarTitle = "Tambah";
            btnTitle = "Simpan";
        }

        getSupportActionBar().setTitle(actionBarTitle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnSubmit.setText(btnTitle);

        calOneTimeDate = Calendar.getInstance();
        calOneTimeTime = Calendar.getInstance();


        alarmPreference = new AlarmPreference(this);
        alarmReceiver = new AlarmReceiver();
        if (!TextUtils.isEmpty(alarmPreference.getOneTimeDate())) {
            setOneTimeText();
        }


    }

    private void setOneTimeText() {
        tvOneTimeTime.setText(alarmPreference.getOneTimeTime());
        tvOneTimeDate.setText(alarmPreference.getOneTimeDate());
        edtOneTimeMessage.setText(alarmPreference.getOneTimeMessage());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (noteHelper != null) {
            noteHelper.close();
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_set_one_time_alarm) {
            String title = edtTitle.getText().toString().trim();
            String description = edtDescription.getText().toString().trim();
            boolean isEmpty = false;
            /*
            Jika fieldnya masih kosong maka tampilkan error
             */
            if (TextUtils.isEmpty(title)) {
                isEmpty = true;
                edtTitle.setError("Field can not be blank");
            }
            if (!isEmpty) {
                Note newNote = new Note();
                newNote.setTitle(title);
                newNote.setDescription(description);

                Intent intent = new Intent();

                /*
                Jika merupakan edit setresultnya UPDATE, dan jika bukan maka setresultnya ADD
                 */
                if (isEdit) {
                    newNote.setDate(note.getDate());
                    newNote.setId(note.getId());
                    noteHelper.update(newNote);

                    intent.putExtra(EXTRA_POSITION, position);
                    setResult(RESULT_UPDATE, intent);
                    finish();
                } else {
                    newNote.setDate(getCurrentDate());
                    noteHelper.insert(newNote);

                    setResult(RESULT_ADD);
                    finish();
                }
            }
        }

        if (view.getId() == R.id.btn_one_time_alarm_date) {
            final Calendar currentDate = Calendar.getInstance();
            new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                    calOneTimeDate.set(year, monthOfYear, dayOfMonth);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    tvOneTimeDate.setText(dateFormat.format(calOneTimeDate.getTime()));
                }
            }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
        } else if (view.getId() == R.id.btn_one_time_alarm_time) {
            final Calendar currentDate = Calendar.getInstance();
            new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                    calOneTimeTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calOneTimeTime.set(Calendar.MINUTE, minute);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
                    tvOneTimeTime.setText(dateFormat.format(calOneTimeTime.getTime()));
                }
            }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), true).show();
        } else if (view.getId() == R.id.btn_set_one_time_alarm) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String oneTimeDate = dateFormat.format(calOneTimeDate.getTime());
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            String oneTimeTime = timeFormat.format(calOneTimeTime.getTime());
            String oneTimeMessage = edtOneTimeMessage.getText().toString();
            alarmPreference.setOneTimeDate(oneTimeDate);
            alarmPreference.setOneTimeMessage(oneTimeMessage);
            alarmPreference.setOneTimeTime(oneTimeTime);
            alarmReceiver.setOneTimeAlarm(this, AlarmReceiver.TYPE_ONE_TIME,
                    alarmPreference.getOneTimeDate(),
                    alarmPreference.getOneTimeTime(),
                    alarmPreference.getOneTimeMessage());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isEdit) {
            getMenuInflater().inflate(R.menu.menu_form, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                showAlertDialog(ALERT_DIALOG_DELETE);
                break;
            case android.R.id.home:
                showAlertDialog(ALERT_DIALOG_CLOSE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        showAlertDialog(ALERT_DIALOG_CLOSE);
    }

    final int ALERT_DIALOG_CLOSE = 10;
    final int ALERT_DIALOG_DELETE = 20;

    /*
    Konfirmasi dialog sebelum proses batal atau hapus
    close = 10
    delete = 20
     */
    private void showAlertDialog(int type) {
        final boolean isDialogClose = type == ALERT_DIALOG_CLOSE;
        String dialogTitle = null, dialogMessage = null;

        if (isDialogClose) {
            dialogTitle = "Batal";
            dialogMessage = "Apakah anda ingin membatalkan perubahan pada form?";
        } else {
            dialogMessage = "Apakah anda yakin ingin menghapus item ini?";
            dialogTitle = "Hapus Note";
        }

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle(dialogTitle);
        alertDialogBuilder
                .setMessage(dialogMessage)
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (isDialogClose) {
                            finish();
                        } else {
                            noteHelper.delete(note.getId());
                            Intent intent = new Intent();
                            intent.putExtra(EXTRA_POSITION, position);
                            setResult(RESULT_DELETE, intent);
                            finish();
                        }
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    private String getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();

        return dateFormat.format(date);
    }
}
