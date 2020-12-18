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
import android.widget.TextView;
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

public class LoginAlunoActivity extends AppCompatActivity {
    EditText mtxt_emaill, mtxt_senhal;
    TextView tv_esqueceuSenha;

    Button mbtn_Logar;

    private FirebaseAuth mAuth;

    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_aluno);

        mAuth = FirebaseAuth.getInstance();

        mtxt_emaill = findViewById(R.id.txt_emailc);
        mtxt_senhal = findViewById(R.id.txt_senhac);
        mbtn_Logar = findViewById(R.id.btn_logar);
        tv_esqueceuSenha= findViewById(R.id.recuperarTv);

        mbtn_Logar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mtxt_emaill.getText().toString();
                String passw = mtxt_senhal.getText().toString().trim();

                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    mtxt_emaill.setError("Email Invalido");
                    mtxt_emaill. setFocusable(true);
                }else{
                    loginUser(email, passw);
                }
            }


        });
        tv_esqueceuSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEsqueceuSenha();
            }


        });

        pd =  new ProgressDialog(this);
    }


    private void showEsqueceuSenha() {
    }

    private void loginUser(String email, String passw) {
        pd.setMessage("Entrando...");
        pd.show();
        mAuth.signInWithEmailAndPassword(email, passw)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            pd.dismiss();

                            FirebaseUser user = mAuth.getCurrentUser();

                            if(task.getResult().getAdditionalUserInfo().isNewUser()){

                                String email = user.getEmail();
                                String uid = user.getUid();


                                HashMap<Object,String> hashMap = new HashMap<>();
                                hashMap.put("email", email);
                                hashMap.put("uid", uid);
                                hashMap.put("nomeAluno","");
                                hashMap.put("nomeResponsavel","");
                                hashMap.put("endereco","");
                                hashMap.put("turma","");
                                hashMap.put("onlineStatus","online");
                                hashMap.put("phone", "");
                                hashMap.put("image","" );

                                // firebase database instance
                                FirebaseDatabase database = FirebaseDatabase.getInstance();

                                DatabaseReference reference = database.getReference("Alunos");

                                reference.child(uid).setValue(hashMap);


                            }


                            startActivity(new Intent(LoginAlunoActivity.this, DashboardAlunoActivity.class));
                            finish();


                        }

                        else {
                            pd.dismiss();
                            // If sign in fails, display a message to the user.

                            Toast.makeText(LoginAlunoActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }


                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(LoginAlunoActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}