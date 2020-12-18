package com.example.agendacegim;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class Esc2Activity extends AppCompatActivity {
    ImageButton aluno,professor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esc2);

        professor = findViewById(R.id.profBtn);

        professor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Esc2Activity.this,LoginActivity.class );
                startActivity(i);

            }
        });

        aluno = findViewById(R.id.alunoBtn);

        aluno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Esc2Activity.this,LoginAlunoActivity.class );
                startActivity(i);

            }
        });

    }
}