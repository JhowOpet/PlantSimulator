package plantsimulator.org.plantsimulator.model;

import android.net.Uri;

import java.net.URI;

/**
 * Created by opet on 27/09/2018.
 */

public class User {

    private String nome;
    private URI uriFoto;

    public User(String nome, Uri uriFoto) {
    }

    public User(String nome, URI uriFoto) {
        this.nome = nome;
        this.uriFoto = uriFoto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public URI getUriFoto() {
        return uriFoto;
    }

    public void setUriFoto(URI uriFoto) {
        this.uriFoto = uriFoto;
    }

}
