package sv.edu.udb.pizzeria_forkify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import sv.edu.udb.pizzeria_forkify.OrderPizza.Activities.login.LoginActivity;
import sv.edu.udb.pizzeria_forkify.OrderPizza.LandingMenuActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent= new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        }, 1000 );


    }
}