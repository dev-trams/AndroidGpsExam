package com.ctrls.gpsexam2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    TextView latitudeView;
    TextView longitudeView;
    int PERMISSION_REQUEST_CODE;
    int MENU_SELECT_STATE = 1;
    Handler handler = new Handler();
    LocationManager manager;
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        latitudeView = findViewById(R.id.latitude);
        longitudeView = findViewById(R.id.longitude);
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        switch (MENU_SELECT_STATE) {
            case 1:
                // 현재 위치 검색
                Toast.makeText(MainActivity.this, "검색 시작", Toast.LENGTH_SHORT).show();
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_CODE);
                    return;
                }
                startLocationSearch();
                break;
            case 2:
                // 현재 위치 검색 중지
                stopLocationSearch();
                break;
        }
    }

    private void startLocationSearch() {
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                Toast.makeText(MainActivity.this, "경로 체크", Toast.LENGTH_SHORT).show();
                double latitude = location.getLatitude();
                double hardness = location.getLongitude();
                latitudeView.setText("위도: " + latitude);
                longitudeView.setText("경도: " + hardness);
            }
        };
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    private void stopLocationSearch() {
        if (locationListener != null) {
            manager.removeUpdates(locationListener);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 권한이 허용된 경우
                PERMISSION_REQUEST_CODE = 1;
                startLocationSearch();
            } else {
                // 권한이 거부된 경우
                PERMISSION_REQUEST_CODE = 0;
                Toast.makeText(this, "위치 권한이 거부되었습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.onSearch:
                MENU_SELECT_STATE = 1;
                Toast.makeText(this, "선택된 메뉴코드: " + MENU_SELECT_STATE, Toast.LENGTH_SHORT).show();
                startLocationSearch();
                break;
            case R.id.stopSearch:
                MENU_SELECT_STATE = 2;
                Toast.makeText(this, "선택된 메뉴코드: " + MENU_SELECT_STATE, Toast.LENGTH_SHORT).show();
                stopLocationSearch();
                break;
        }
        item.setChecked(true);
        return true;
    }
}
