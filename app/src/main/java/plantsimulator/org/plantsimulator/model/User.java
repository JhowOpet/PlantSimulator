package plantsimulator.org.plantsimulator.model;

import android.net.Uri;

/**
 * Created by opet on 27/09/2018.
 */

public class User {

    private String nome;
    private Uri uriFoto;

    public User() {
    }

    public User(String nome, Uri uriFoto) {
        this.nome = nome;
        this.uriFoto = uriFoto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Uri getUriFoto() {
        return uriFoto;
    }

    public void setUriFoto(Uri uriFoto) {
        this.uriFoto = uriFoto;
    }

}
