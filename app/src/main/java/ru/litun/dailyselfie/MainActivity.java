package ru.litun.dailyselfie;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    //    public static final String PREF_KEY = "main_prefs";
//    public static final String SELFIES_JSON_KEY = "selfies_json";
    private List<Selfie> selfies = new ArrayList<>();
    private SelfieAdapter adapter;

    private SelfieDatabaseHelper selfieDatabaseHelper;
    private SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //database
        selfieDatabaseHelper = new SelfieDatabaseHelper(this);
        sqLiteDatabase = selfieDatabaseHelper.getWritableDatabase();
        loadFromDatabase();

        //recycler
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
        adapter = new SelfieAdapter(selfies);
        adapter.setListener(new SelfieAdapter.OnClick() {
            @Override
            public void itemClicked(View view, int position) {
                Intent intent = new Intent(MainActivity.this, PictureActivity.class);
                intent.putExtra(PictureActivity.PATH_KEY, selfies.get(position).getPhotoPath());
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(MainActivity.this, view, "picture");
                startActivity(intent, options.toBundle());
            }
        });
        RecyclerView.LayoutManager layoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void loadFromDatabase() {
        Cursor cursor = sqLiteDatabase.query(SelfieDatabaseHelper.DATABASE_TABLE,
                new String[]{SelfieDatabaseHelper.NAME_COLUMN,
                        SelfieDatabaseHelper.PHOTO_PATH_COLUMN},
                null, null,
                null, null, null);

        //cursor.moveToFirst();
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(SelfieDatabaseHelper.NAME_COLUMN));
            String path = cursor.getString(cursor.getColumnIndex(SelfieDatabaseHelper.PHOTO_PATH_COLUMN));
            Selfie selfie = new Selfie(path, name);
            selfies.add(selfie);
        }
        cursor.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_camera) {
            dispatchTakePictureIntent();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            //Bundle extras = data.getExtras();
            //Bitmap imageBitmap = (Bitmap) extras.get("data");
            Selfie selfie = new Selfie(mCurrentPhotoPath);
            String timeStamp = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date());
            selfie.setName(timeStamp);
            selfies.add(selfie);
            adapter.notifyItemInserted(selfies.size());

            //add to database
            ContentValues newValues = new ContentValues();
            newValues.put(SelfieDatabaseHelper.NAME_COLUMN, selfie.getName());
            newValues.put(SelfieDatabaseHelper.PHOTO_PATH_COLUMN, selfie.getPhotoPath());
            sqLiteDatabase.insert(SelfieDatabaseHelper.DATABASE_TABLE, null, newValues);
        }
    }

    private String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String photoFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                photoFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }


    private static final long INITIAL_ALARM = 2 * 60 * 1000L;

    @Override
    protected void onStop() {
        super.onStop();
        // Get the AlarmManager Service
        AlarmManager mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // Create an Intent to broadcast to the AlarmNotificationReceiver
        Intent mNotificationReceiverIntent = new Intent(MainActivity.this,
                AlarmNotificationReceiver.class);

        // Create an PendingIntent that holds the NotificationReceiverIntent
        PendingIntent mNotificationReceiverPendingIntent = PendingIntent.getBroadcast(
                MainActivity.this, 0, mNotificationReceiverIntent, 0);

        // Set repeating alarm
        mAlarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + INITIAL_ALARM,
                INITIAL_ALARM,
                mNotificationReceiverPendingIntent);
    }
}
