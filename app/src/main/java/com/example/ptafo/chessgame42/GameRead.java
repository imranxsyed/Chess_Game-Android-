package com.example.ptafo.chessgame42;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;



public class GameRead {

    private static final String FILENAME = "savedGames.chessApp";

    private GameRead(){};
    public static GameActivity readGames(Context content) throws ClassNotFoundException,IOException{
        ObjectInputStream ois = null;
        FileInputStream ins = null;
        try{
            ins = content.openFileInput(FILENAME);
            ois = new ObjectInputStream(ins);
        }catch(IOException e){
            return null;
        }
        return (GameActivity)ois.readObject();
    }

    public static void saveGames(GameActivity activity, Context content) throws Exception{
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;

        try{
            content.deleteFile(FILENAME);

            fos = content.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(activity);
            oos.flush();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}
