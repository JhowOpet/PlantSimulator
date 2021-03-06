package plantsimulator.org.plantsimulator;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;

import me.iwf.photopicker.PhotoPicker;
import plantsimulator.org.plantsimulator.model.User;

public class EditarPerfilActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editEmail;
    private EditText editNome;
    private Button btnSalvar;
    private ProgressBar progressSalvar;
    private ImageView imgPerfil;
    private ArrayList<String> photos;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        editEmail = findViewById(R.id.editEmail);
        editNome = findViewById(R.id.editNome);
        progressSalvar = findViewById(R.id.progressSalvar);
        progressSalvar.setVisibility(ProgressBar.GONE);
        btnSalvar = findViewById(R.id.btnSalvar);
        imgPerfil = findViewById(R.id.imgPerfil);

        btnSalvar.setOnClickListener(this);

        if (currentUser != null) {
            String email = currentUser.getEmail();

            editEmail.setText(email);
            recuperarDadosUsuario(currentUser.getUid().toString());

        }
    }

    public void recuperarDadosUsuario(String uid){

        Query mQuery = mDatabase.child("users").child(uid);
        mQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User usuario;
                usuario = dataSnapshot.getValue(User.class);
                editNome.setText(usuario.getNome());

                Log.e("Get dados usuario: ", "Sucesso.");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Get dados usuario: ", "Falha.");
                Toast.makeText(EditarPerfilActivity.this, "Impossível recuperar dados usuário.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onClick(View view) {
        progressSalvar.setVisibility(ProgressBar.VISIBLE);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        String uid = currentUser.getUid().toString();

        // Salvar email
        salvarDadosAutenticacao(currentUser);

        User user = new User();
        user.setNome(editNome.getText().toString());

        // Salvar foto
        salvarFoto(view, uid, user);

        // Salvar outros: nome e diretório da foto
        salvarDadosUsuario(uid, user);

        progressSalvar.setVisibility(ProgressBar.GONE);
    }

    private void resetForm(){
        photos.clear();
        imgPerfil.setImageResource(0);
    }

    public void salvarDadosAutenticacao(FirebaseUser currentUser){
        currentUser.updateEmail(editEmail.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.e("Salvar email:", "Sucesso!");
                        Toast.makeText(EditarPerfilActivity.this, "Email salvo com sucesso!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Salvar email:", "Falha.");
                        Toast.makeText(EditarPerfilActivity.this, "Não salvou.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void selecionarFoto(View view){
        PhotoPicker.builder()
                .setPhotoCount(1)
                .setShowCamera(true)
                .setShowGif(true)
                .setPreviewEnabled(false)
                .start(this, PhotoPicker.REQUEST_CODE);
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PhotoPicker.REQUEST_CODE) {
            if (data != null) {
                photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                imgPerfil.setImageURI(Uri.parse(photos.get(0)));
            }
        }
    }

    public void salvarFoto(View view, String uid, User user){
        if(photos.size() > 0){
            Uri file = Uri.fromFile(new File(photos.get(0)));
            String[] splitted = photos.get(0).split("/");
            //String filename = splitted[splitted.length-1];

            //long timeStampName = new Timestamp(System.currentTimeMillis()).getTime();

            user.setNomeFoto(uid);

            StorageReference photoRef = mStorageRef.child("images/"+user.getNomeFoto());

            photoRef.putFile(file)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Log.e("Salvar foto:", "Sucesso!");
                            Toast.makeText(EditarPerfilActivity.this, "Foto salva com sucesso!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("Salvar foto:", "Falha.");
                            Toast.makeText(EditarPerfilActivity.this, "Não salvou.", Toast.LENGTH_SHORT).show();
                        }
                    });
            resetForm();
        } else {
            Toast.makeText(this, "Nenhum arquivo carregado.", Toast.LENGTH_SHORT).show();
        }
    }

    public void salvarDadosUsuario(String uid, User user){
        mDatabase.child("users").child(uid).setValue(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.e("Salvar dados user:", "Sucesso!");
                        Toast.makeText(EditarPerfilActivity.this, "Dados salvos com sucesso!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Salvar dados user:", "Falha.");
                        Toast.makeText(EditarPerfilActivity.this, "Não salvou.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void sair(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


}
