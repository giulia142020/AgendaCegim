package com.example.agendacegim;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class CadastroProfessorActivity extends AppCompatActivity {
    EditText mtxt_emailc, mtxt_senhac, mtxt_nomeProfc, mtxt_phonec;
    Button mbtn_Cadastrar;
    RadioButton maternalRb, primeiroPeriodoRb, segundoPeriodoRb,primerioAnoRb, segundoAnoRb, terceiroAnoArb, quartoAnoRb,quintoAnoRb;

    ProgressDialog progressDialog;
    private DatabaseReference mDatabase;


    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_professor);
        mtxt_emailc= findViewById(R.id.txt_emailc);
        mtxt_senhac = findViewById(R.id.txt_senhac);

        mtxt_nomeProfc= findViewById(R.id.txt_nomeProfc);
        mtxt_phonec = findViewById(R.id.txt_phonec);
        maternalRb = findViewById(R.id.maternalRb);
        primeiroPeriodoRb = findViewById(R.id.primeiroPeriodoRb);
        segundoPeriodoRb = findViewById(R.id.segundoPeriodoRb);
        primerioAnoRb = findViewById(R.id.primeiroAnoRb);
        segundoAnoRb = findViewById(R.id.segundoAnoRb);
        terceiroAnoArb = findViewById(R.id.terceiroAnoRb);
        quartoAnoRb = findViewById(R.id.quartoAnoRb);
        quintoAnoRb = findViewById(R.id.quintoAnoRb);


        mbtn_Cadastrar = findViewById(R.id.btn_cadastrar);


        mAuth = FirebaseAuth.getInstance();


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cadastrando...");

        mbtn_Cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mtxt_emailc.getText().toString().trim();
                String password= mtxt_senhac.getText().toString().trim();
                String nomeProf = mtxt_nomeProfc.getText().toString().trim();
                String phone = mtxt_phonec.getText().toString().trim();
                String turma;
                turma = getTurmas();
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    mtxt_emailc.setError("Email inválido");
                    mtxt_emailc.setFocusable(true);
                }
                if(!maternalRb.isChecked() && !primeiroPeriodoRb.isChecked() && !segundoPeriodoRb.isChecked() && !primerioAnoRb.isChecked() && !segundoAnoRb.isChecked() && !terceiroAnoArb.isChecked()  && !quartoAnoRb.isChecked() && !quintoAnoRb.isChecked()){
                    Toast.makeText(CadastroProfessorActivity.this, "Inserir sua turma...", Toast.LENGTH_SHORT).show();

                }
                else if (password.length()<8)
                {
                    mtxt_senhac.setError("A senha precisa ser maior que 8 caracteres");
                    mtxt_senhac.setFocusable(true);
                }

                else {
                        registeProf(nomeProf,email,password,turma,phone);
                }
            }


        });

    }

    private void registeProf( final String nomeProf, String email, String password, final String turma, final String phone) {

        mDatabase = FirebaseDatabase.getInstance().getReference();
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();

                    FirebaseUser user = mAuth.getCurrentUser();

                    String email = user.getEmail();
                    String uid = user.getUid();

                    HashMap<Object,String> hashMap = new HashMap<>();
                    hashMap.put("email", email);
                    hashMap.put("uid", uid);
                    hashMap.put("nomeProf",nomeProf);
                    hashMap.put("turma",turma);
                    hashMap.put("onlineStatus","online");
                    hashMap.put("phone", phone);
                    hashMap.put("image","" );

                    // firebase database instance
                    FirebaseDatabase database = FirebaseDatabase.getInstance();

                    DatabaseReference reference = database.getReference("Professores");

                    reference.child(uid).setValue(hashMap);

                    Toast.makeText(CadastroProfessorActivity.this,"Registrando...\n"+user.getEmail(),Toast.LENGTH_SHORT);
                    startActivity(new Intent(CadastroProfessorActivity.this,MainActivity.class));
                    finish();

                } else {
                    // If sign in fails, display a message to the user.
                    progressDialog.dismiss();
                    Toast.makeText(CadastroProfessorActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();

                }


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(CadastroProfessorActivity.this,""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private String getTurmas() {
        String turma = "";

        if(maternalRb.isChecked()) turma += "Maternal";
        if(primeiroPeriodoRb.isChecked()) turma += "Primeiro Período";
        if(segundoPeriodoRb.isChecked()) turma += "Segundo Periodo";
        if(primerioAnoRb.isChecked()) turma += "Primeiro Ano";
        if(segundoAnoRb.isChecked()) turma += "Segundo Ano";
        if(terceiroAnoArb.isChecked()) turma += "Terceiro Ano";
        if(quartoAnoRb.isChecked()) turma += "Quarto Ano";
        if(quintoAnoRb.isChecked()) turma += "Quinto Ano";

        return turma;
    }
}