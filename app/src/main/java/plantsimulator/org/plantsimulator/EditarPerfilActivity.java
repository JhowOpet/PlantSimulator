package plantsimulator.org.plantsimulator;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import plantsimulator.org.plantsimulator.model.User;

public class EditarPerfilActivity extends AppCompatActivity {

    private TextView textLogin;
    private EditText editNome;
    private ImageView imgPerfil;
    //private Button btnSalvar;
    //private ProgressBar progressSalvar;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        textLogin = findViewById(R.id.textLogin);
        editNome = findViewById(R.id.editNome);
        //imgPerfil = findViewById(R.id.imgPerfil);

        if (currentUser != null) {
            String email = currentUser.getEmail();
            String uid = currentUser.getUid();

            recuperarDadosUsuario(uid);

            textLogin.setText("Email: " + email);
            //editNome.setText(nome);
            //imgPerfil.set
        }
    }

    public void recuperarDadosUsuario(String uid){

    }

    public void salvarPerfilUsuario(String userId, String nome, Uri uriFoto) {
        User user = new User(nome, uriFoto);

        mDatabase.child("users").child(userId).setValue(user);
    }

    public void sair(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
