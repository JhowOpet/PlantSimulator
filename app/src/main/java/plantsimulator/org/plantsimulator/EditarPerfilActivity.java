package plantsimulator.org.plantsimulator;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import plantsimulator.org.plantsimulator.model.User;

public class EditarPerfilActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void editarPerfilUsuario(String userId, String nome, Uri uriFoto) {
        User user = new User(nome, uriFoto);

        mDatabase.child("users").child(userId).setValue(user);
    }

    public void sair(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
