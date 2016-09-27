package br.edu.ufcg.kickzoeira;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import br.edu.ufcg.kickzoeira.activities.ProfileActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.editText);
        password = (EditText) findViewById(R.id.editText2);
    }

    public void handleLogin(View view){



        System.out.println(username.getText().toString());
        System.out.println(password.getText().toString());

        Intent it = new Intent(getApplicationContext(), ProfileActivity.class);
        startActivity(it);
    }
}
