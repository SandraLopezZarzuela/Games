package com.example.games;

import java.util.Comparator;

public class Usuario {
    int id, scorePeg, score2048;
    String nombre, apellidos, usuario, password;
    int time;

    public Usuario() {

    }

    public Usuario(String nombre, String apellidos, String usuario, String password, Integer time) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.usuario = usuario;
        this.password = password;
        this.time = time;
    }

    public boolean isNull(){
        if (nombre.equals("")&&apellidos.equals("")&&usuario.equals("")&&password.equals("")){
            return false;
        }else {
            return true;
        }
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", scorePeg=" + scorePeg +
                ", score2048=" + score2048 +
                ", nombre='" + nombre + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", usuario='" + usuario + '\'' +
                ", password='" + password + '\'' +
                ", bestScore=" + bestScore +
                '}';
    }

    public Comparator <Usuario> bestScore = new Comparator<Usuario>() {
        @Override
        public int compare(Usuario u, Usuario u2) {
            int sc = u.getScore2048();
            int sc2 = u2.getScore2048();
            return Integer.compare(sc,sc2);
        }
    };

    public Comparator <Usuario> bestScorePeg = new Comparator<Usuario>() {
        @Override
        public int compare(Usuario u, Usuario u2) {
            int sc = u.getScorePeg();
            int sc2 = u2.getScorePeg();
            return Integer.compare(sc,sc2);
        }
    };


    public int getScorePeg() {
        return scorePeg;
    }

    public void setScorePeg(int scorePeg) {
        this.scorePeg = scorePeg;
    }

    public int getScore2048() {
        return score2048;
    }

    public void setScore2048(int score2048) {
        this.score2048 = score2048;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
