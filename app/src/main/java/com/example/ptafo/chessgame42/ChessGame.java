package com.example.ptafo.chessgame42;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Created by ptafo on 4/28/2017.
 */

public class ChessGame extends Activity {

    private static final String dirName = "data";
    private static final String fileName = "user.date";
    public GameActivity gameRecord;
    Button newGame;
    Button savedGames;

    public void onCreate(Bundle saveInstanceState){

        super.onCreate(saveInstanceState);
        setContentView(R.layout.main);

        gameRecord = getGameRecords();
        newGame = (Button) findViewById(R.id.newgame);
        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ChessGame.this, BoardGamePlay.class);
                startActivity(intent);
            }
        });
        savedGames = (Button) findViewById(R.id.recordedgame);
        savedGames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChessGame.this, SavedGames.class);
                startActivity(intent);
            }
        });

    }

    private GameActivity getGameRecords(){
        GameActivity record = new GameActivity();

        try{
            record = readGames();
            return record;
        }
        catch(FileNotFoundException e){
            return record;
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        return record;
    }

    private GameActivity readGames() throws FileNotFoundException, IOException, Exception{
        try{
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dirName + File.separator + fileName));
            GameActivity record =(GameActivity)ois.readObject();
            ois.close();
            return record;
        }catch (Exception e){
            GameActivity records = new GameActivity();
            return records;
        }
    }
}
