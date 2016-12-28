package bug.pixel.callproblem;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telecom.TelecomManager;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, PERMISSION_REQUEST_CODE);
        }
    }

    public void onClickCallButton(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, PERMISSION_REQUEST_CODE);
            return;
        }

        TelecomManager telecomManager = (TelecomManager) getSystemService(Context.TELECOM_SERVICE);
        Uri uri = Uri.fromParts("tel", getPhoneNumber(), null);
        Bundle extras = new Bundle();
        // Pixel problems happens here.
        // When EXTRA_START_CALL_WITH_SPEAKERPHONE is set to `true`, the phone seems to ignore
        // voice input from the microphone and the other party is not able to hear anything.
        extras.putBoolean(TelecomManager.EXTRA_START_CALL_WITH_SPEAKERPHONE, isStartOnSpeaker());
        telecomManager.placeCall(uri, extras);
    }

    private String getPhoneNumber() {
        return ((TextView) findViewById(R.id.phone_number)).getText().toString();
    }

    private boolean isStartOnSpeaker() {
        return ((CheckBox) findViewById(R.id.start_in_speaker)).isChecked();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Ready to place a call!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Without this permission we can't do anything!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
