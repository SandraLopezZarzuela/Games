package com.example.games;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class daoUsuario {
    Context c;
    Usuario u;
    ArrayList<Usuario> lista;
    SQLiteDatabase sql;
    String bd = "BDUsuarios";
    //creamos tabla si no existe en la bbdd
    String tabla = "create table if not exists us (id integer primary key autoincrement, usuario text, pass text, nombre text, ap text, scorePeg integer, score2048 integer)";

    public daoUsuario(Context c) {
        this.c = c;
        sql = c.openOrCreateDatabase(bd, c.MODE_PRIVATE, null);
        sql.execSQL(tabla);
        u = new Usuario();

    }

    public boolean insertUsuario(Usuario u){
        //comprobamos si el usuario existe o no en la base de datos
        //si existe no podemos hacer el insert
        //si no existe hacemos el insert
        if (buscar(u.getUsuario())==0){
            ContentValues cv = new ContentValues();
            cv.put("usuario", u.getUsuario());
            cv.put("pass", u.getPassword());
            cv.put("nombre", u.getNombre());
            cv.put("ap", u.getApellidos());
            cv.put("scorePeg", 32);
            cv.put("score2048", 0);
            return (sql.insert("us", null, cv)>0);

        }else{
            return false;
        }
    }

    public int buscar (String u){
        //Comprobamos si el usuario esta en la base de datos
        int x = 0;
        lista = selectUsuario();
        for (Usuario us:lista){
            if (us.getUsuario().equals(u)){
                x++;
            }
        }
        return x;
    }

    public ArrayList<Usuario> selectUsuario(){
        ArrayList<Usuario> lista = new ArrayList<Usuario>();
        lista.clear();;
        Cursor cr = sql.rawQuery("select * from us", null);
        if (cr!=null&&cr.moveToFirst()){
            do{
                Usuario u = new Usuario();
                u.setId(cr.getInt(0));
                u.setUsuario(cr.getString(1));
                u.setPassword(cr.getString(2));
                u.setNombre(cr.getString(3));
                u.setApellidos(cr.getString(4));
                u.setScorePeg(cr.getInt(5));
                u.setScore2048(cr.getInt(6));
                lista.add(u);

            }while(cr.moveToNext());
        }
        return lista;
    }

    public int login (String u, String p){
        int a = 0;
        Cursor cr = sql.rawQuery("select * from us", null);
        if (cr!=null&&cr.moveToFirst()){
            do{
                //miramos si nuestro usuario esta registrado, comparamos los valores en nuestra bbdd
                if(cr.getString(1).equals(u)&&cr.getString(2).equals(p)) {
                    a++;
                }
            }while(cr.moveToNext());
        }
        return a;
    }

    public Usuario getUsuario(String u, String p){

        lista = selectUsuario();
        for(Usuario us:lista){
            if (us.getUsuario().equals(u)&&us.getPassword().equals(p)){
                return us;
            }
        }
        return null;
    }

    public Usuario getUsuarioById(int id){

        lista = selectUsuario();
        for(Usuario us:lista){
            if (us.getId()== id){
                return us;
            }
        }
        return null;
    }

    public boolean updateUsuario(Usuario u){
        ContentValues cv = new ContentValues();
        cv.put("usuario", u.getUsuario());
        cv.put("pass", u.getPassword());
        cv.put("nombre", u.getNombre());
        cv.put("ap", u.getApellidos());
        //hacemos el update con el valor del id que vamos a buscar
        return (sql.update("us", cv, "id="+u.getId(),null)>0);

    }

    public boolean updateScorePeg(Usuario u){
        ContentValues cv = new ContentValues();
        cv.put("scorePeg", u.getScorePeg());
        //hacemos el update con el valor del id que vamos a buscar
        return (sql.update("us", cv, "id="+u.getId(),null)>0);
    }

    public boolean updateScore2048(Usuario u){
        ContentValues cv = new ContentValues();
        cv.put("score2048", u.getScore2048());
        //hacemos el update con el valor del id que vamos a buscar
        return (sql.update("us", cv, "id="+u.getId(),null)>0);
    }

    public boolean deleteUsuario(int id){
        //instruccion para eliminar nuestro usuario por su id en la bbdd
        return (sql.delete("us", "id="+id, null)>0);
    }
}
