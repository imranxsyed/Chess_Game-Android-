package com.example.ptafo.chessgame42;

import java.io.Serializable;


public class GameUndo implements Serializable{

    //private static final ling serialVersionUID =

    private static final long serialVersionUID = 8225728534795140231L;


    private BoardMoves  moves ;

    public GameUndo(){
        moves = new BoardMoves();

    }


    public void addMove(Board board){
        moves.addTail(board);
    }

    public Board undoMove(){
        int size = moves.size();
        if(size>0){
            Board board= moves.undo();
            return board;
        }
        else{
            return null;
        }
    }

    public boolean hasPrev(){
        if(moves.hasPrev()){
            return true;
        }

        return false;
    }

    public Board initializeBoard(){

        Board gameState = moves.initialize();
        return gameState;
    }



}


