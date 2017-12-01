package com.example.ptafo.chessgame42;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;



public class SerializeMoves {

    static String FILENAME = "gameState";

    private SerializeMoves(){}

    static public Object deepCopy(Object oldObject, Context content) throws Exception{
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;

        try
        {
            FileInputStream fin;


            FileOutputStream fos;
            fos = content.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            // serialize and pass the object
            oos.writeObject(oldObject);
            oos.flush();
            fin = content.openFileInput(FILENAME);
            ois = 	new ObjectInputStream(fin);

        }
        catch(IOException e){
            e.getMessage();
        }
        catch(Exception e)
        {
            System.out.println("Exception in ObjectCloner = " + e);
            throw(e);
        }
        // return the new object
        return (Board)ois.readObject();

    }
}
