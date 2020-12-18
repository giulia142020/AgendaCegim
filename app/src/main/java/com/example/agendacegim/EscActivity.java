package com.example.agendacegim;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class EscActivity extends AppCompatActivity {
Button aluno,professor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esc);
        aluno = findViewById(R.id.alunoBtn);
        professor = findViewById(R.id.profBtn);

        aluno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(EscActivity.this, CadastroAlunoActivity.class);
                startActivity(i);            }
        });
        professor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(EscActivity.this, CadastroProfessorActivity.class);
                startActivity(i);
            }
        });

    }
}