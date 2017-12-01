package com.example.ptafo.chessgame42;


import java.io.Serializable;
import java.util.ArrayList;


public class GameActivity implements Serializable{

    private static final long serialVersionUID = 4733430505225620629L;
    ArrayList<RecordMoves> recordedGames;
    public GameActivity(){
        recordedGames = new ArrayList<RecordMoves>();
    }

    public int size(){
        return recordedGames.size();
    }

    public RecordMoves getFile(String fileName){
        for(RecordMoves file: recordedGames){
            if(file.getFileName().equals(fileName)){
                return file;
            }
        }
        return null;
    }
    public void removeGame(String name){
        int i =0;
        while(i<recordedGames.size()){
            if(recordedGames.get(i).getFileName().compareToIgnoreCase(name)==0){
                recordedGames.remove(i);
                return;
            }
            i++;
        }
    }
}
