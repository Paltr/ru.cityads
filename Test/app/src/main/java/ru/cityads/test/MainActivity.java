package ru.cityads.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.jakewharton.rxbinding.view.RxView;

import ru.cityads.test.activities.AdActivity;

public final class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button button = (Button)findViewById(R.id.test_button);
        RxView.clicks(button)
            .subscribe(view -> testAdActivity());
    }

    private void testAdActivity()
    {
        final Intent intent = new Intent(MainActivity.this, AdActivity.class);
        startActivity(intent);
    }
}
