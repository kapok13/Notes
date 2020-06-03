package com.vb.notes.ui.activities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.vb.notes.R;
import com.vb.notes.data.Note;
import com.vb.notes.data.NoteRepository;
import com.vb.notes.ui.presenters.MainActivityPresenter;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements IMainActivity{

    private static final int INTERNET_REQUEST_CODE = 1;
    private static final int NETWORT_STATE_REQUEST_CODE = 2;

    private MainActivityPresenter mMainPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestInternetPermission();
        requestForNetworkStatePermission();
        init();
    }

    private void init(){
        ButterKnife.bind(this);
        mMainPresenter = new MainActivityPresenter(this,
                new NoteRepository());

    }

    /*
        при нажатии на кнопку создаем новый элемент для бд и вставляем в базу
            передаем в элемент текущую дату и время в соответствующих форматах
     */
    @OnClick(R.id.add_notes_fab)
    public void onFabClicked(){
        Calendar calendar = Calendar.getInstance();
        String time = new SimpleDateFormat("HH:mm").format(calendar.getTime());
        String date = new SimpleDateFormat("dd-MM-yyyy").format(calendar.getTime());
        Note note = new Note();
        note.setTime(time);
        note.setDate(date);
        mMainPresenter.insertItem(note);
    }

    public void setActionBarTitle(String title){
        getSupportActionBar().setTitle(title);
    }

    // Проверка наличия интернета

    public boolean isNetworkAvailable(Context context){
        if (context == null){
            return false;
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null){

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
                if (capabilities != null){
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)){
                        return true;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)){
                        return true;
                    } else if(capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)){
                        return true;
                    }
                }
            } else {
                try {
                    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                    if (activeNetworkInfo.isConnected() && activeNetworkInfo != null){
                        return true;
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /*
        @requestInternetPermission
        @requestForNetworkStatePermission

        запросы permissions в ui для api > 23
     */

    private void requestInternetPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
                == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.INTERNET}
                    , INTERNET_REQUEST_CODE);
        }
    }

    private void requestForNetworkStatePermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE)
                == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_NETWORK_STATE}
                    , NETWORT_STATE_REQUEST_CODE);
        }
    }

}
