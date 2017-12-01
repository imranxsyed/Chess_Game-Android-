package com.example.ptafo.chessgame42;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;



public class Replay extends Activity {

    Board gameBoard;
    RecordMoves record;
    GameUndo gameUndo;
    GameActivity gameActivity;
    Button next;
    Button prev;
    int currIndex;
    ArrayList<Move> moves;

    private void showToast(String message){
        Toast toast=Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, -30, 50);
        toast.show();
    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.replay_board);

        String filename = getIntent().getExtras().getString("filename");

        try{
            this.gameActivity = GameRead.readGames(this.getApplicationContext());
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        record = gameActivity.getFile(filename);
        moves = record.getMoves();
        gameUndo = new GameUndo();
        currIndex = 0;
        gameBoard = new Board();

        drawBoard();

        prev = (Button) findViewById(R.id.prevMove);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    undo();

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        });

        next = (Button) findViewById(R.id.nextMove);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (hasNext()) {
                    Move move = moves.get(currIndex++);
                    move(move);
                }else{
                    showToast("Reached end of Game");
                }
            }
        });
    }
    protected boolean hasNext(){
        if(moves ==null ||currIndex==moves.size()||moves.get(currIndex)==null){
            return false;
        }else{
            return true;
        }
    }
    protected void undo(){
        if(gameBoard.moveNumber>0 && currIndex>=0){
            gameBoard.undo();
            Board recovState = gameUndo.undoMove();
            //this.gameBoard = recovState;
            Move currMove = moves.get(--currIndex);
            drawBoard();
        }else{
            showToast("There is nothing to undo");
        }

    }

    private void move(Move move){
        String moveFrom = move.fromIndex;
        String moveTo = move.toIndex;

        gameBoard.movePiece(moveFrom, moveTo, move.promotion);
        drawBoard();
    }
    protected void drawBoard(){
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Symbola.ttf");
        ChessPiece holdPiece = null;
        int i,j;

        for(i = 0; i<gameBoard.board.length; i++){
            for(j = 0; j<gameBoard.board.length; j++){
                holdPiece = gameBoard.board[i][j];

                TextView textView = (TextView) findViewById(getCellID(i+1,j+1));;
                if(holdPiece!=null){
                    textView.setTypeface(font);
                    textView.setTextColor(Color.BLACK);
                    String type = holdPiece.getType();

                    if(type.compareToIgnoreCase("king")==0){
                        if(holdPiece.getColor().compareToIgnoreCase("white")==0) {
                            textView.setText(R.string.whiteking);
                        }else{
                            textView.setText(R.string.blackking);
                        }
                    }else if(type.compareToIgnoreCase("queen")==0){
                        if(holdPiece.getColor().compareToIgnoreCase("white")==0) {
                            textView.setText(R.string.whitequeen);
                        }else{
                            textView.setText(R.string.blackqueen);
                        }
                    }else if(type.compareToIgnoreCase("rook")==0){
                        if(holdPiece.getColor().compareToIgnoreCase("white")==0) {
                            textView.setText(R.string.whiterook);
                        }else{
                            textView.setText(R.string.blackrook);
                        }
                    }else if(type.compareToIgnoreCase("bishop")==0){
                        if(holdPiece.getColor().compareToIgnoreCase("white")==0) {
                            textView.setText(R.string.whitebioshop);
                        }else{
                            textView.setText(R.string.blackbioshop);
                        }
                    }else if(type.compareToIgnoreCase("knight")==0){
                        if(holdPiece.getColor().compareToIgnoreCase("white")==0) {
                            textView.setText(R.string.whiteknight);
                        }else{
                            textView.setText(R.string.blackknight);
                        }
                    }else if(type.compareToIgnoreCase("pawn")==0){
                        if(holdPiece.getColor().compareToIgnoreCase("white")==0) {
                            textView.setText(R.string.whitepawn);
                        }else{
                            textView.setText(R.string.blackpawn);
                        }
                    }
                    holdPiece.selected=false;
                    if(holdPiece.getColor().compareToIgnoreCase("white")==0){
                        textView.setShadowLayer(3.0f, 0.0f, 0.0f, Color.WHITE);
                    }
                    textView.setTag(holdPiece.getLocation());
                }
                else{
                    textView.setBackgroundColor(00000000);
                    textView.setShadowLayer(0.0f, 0.0f, 0.0f, Color.WHITE);
                    textView.setTypeface(font);
                    textView.setText("\u2588");
                    textView.setTextColor(00000000);

                }
            }
        }
    }
    private static int getCellID(int row, int colm){
        if(colm ==1){
            switch(row){
                case 1:
                    return R.id.A8;
                case 2:
                    return R.id.A7;
                case 3:
                    return R.id.A6;
                case 4:
                    return R.id.A5;
                case 5:
                    return R.id.A4;
                case 6:
                    return R.id.A3;
                case 7:
                    return R.id.A2;
                case 8:
                    return R.id.A1;
            }

        }else if(colm ==2){
            switch(row){
                case 1:
                    return R.id.B8;
                case 2:
                    return R.id.B7;
                case 3:
                    return R.id.B6;
                case 4:
                    return R.id.B5;
                case 5:
                    return R.id.B4;
                case 6:
                    return R.id.B3;
                case 7:
                    return R.id.B2;
                case 8:
                    return R.id.B1;
            }
        }else if(colm== 3){
            switch(row){
                case 1:
                    return R.id.C8;
                case 2:
                    return R.id.C7;
                case 3:
                    return R.id.C6;
                case 4:
                    return R.id.C5;
                case 5:
                    return R.id.C4;
                case 6:
                    return R.id.C3;
                case 7:
                    return R.id.C2;
                case 8:
                    return R.id.C1;
            }
        }else if(colm == 4){
            switch(row){
                case 1:
                    return R.id.D8;
                case 2:
                    return R.id.D7;
                case 3:
                    return R.id.D6;
                case 4:
                    return R.id.D5;
                case 5:
                    return R.id.D4;
                case 6:
                    return R.id.D3;
                case 7:
                    return R.id.D2;
                case 8:
                    return R.id.D1;
            }
        }else if(colm == 5){
            switch(row){
                case 1:
                    return R.id.E8;
                case 2:
                    return R.id.E7;
                case 3:
                    return R.id.E6;
                case 4:
                    return R.id.E5;
                case 5:
                    return R.id.E4;
                case 6:
                    return R.id.E3;
                case 7:
                    return R.id.E2;
                case 8:
                    return R.id.E1;
            }
        }else if(colm ==6){
            switch(row){
                case 1:
                    return R.id.F8;
                case 2:
                    return R.id.F7;
                case 3:
                    return R.id.F6;
                case 4:
                    return R.id.F5;
                case 5:
                    return R.id.F4;
                case 6:
                    return R.id.F3;
                case 7:
                    return R.id.F2;
                case 8:
                    return R.id.F1;
            }
        }else if(colm ==7){
            switch(row){
                case 1:
                    return R.id.G8;
                case 2:
                    return R.id.G7;
                case 3:
                    return R.id.G6;
                case 4:
                    return R.id.G5;
                case 5:
                    return R.id.G4;
                case 6:
                    return R.id.G3;
                case 7:
                    return R.id.G2;
                case 8:
                    return R.id.G1;
            }
        }else if(colm ==8){
            switch(row){
                case 1:
                    return R.id.H8;
                case 2:
                    return R.id.H7;
                case 3:
                    return R.id.H6;
                case 4:
                    return R.id.H5;
                case 5:
                    return R.id.H4;
                case 6:
                    return R.id.H3;
                case 7:
                    return R.id.H2;
                case 8:
                    return R.id.H1;
            }

        }
        return 0;
    }
}
