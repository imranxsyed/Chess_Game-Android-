package com.example.ptafo.chessgame42;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;


public class BoardGamePlay extends Activity {

    Button undo, automatic, draw, resign;
    Board gameBoard;
    protected String moveFrom;
    protected String moveTo;
    protected TextView origin = null;
    protected TextView destiny = null;
    protected String prevOrigin = null;
    protected String prevDestiny = null;
    protected TextView prevPieceSelected = null;
    public GameUndo gameUndo;
    public RecordMoves gameMoves;
    public GameActivity savedGames;
    protected ChessPiece prevPiece;
    protected String promoteMe;
    private boolean gameDraw = true;

    public void onCreate(Bundle saveInstanceState) {

        super.onCreate(saveInstanceState);
        setContentView(R.layout.board_play);
            try{
                GameActivity game;
                game = GameRead.readGames(this.getApplicationContext());
                if(game == null){
                    this.savedGames = new GameActivity();
                    GameRead.saveGames(this.savedGames, this.getApplicationContext());
                }else{
                    this.savedGames = game;
                }
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
        gameMoves = new RecordMoves();
        gameUndo = new GameUndo();
        gameBoard = new Board();

        drawBoard();

        undo = (Button) findViewById(R.id.undoButton);
        undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    undo();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        });
        automatic = (Button) findViewById(R.id.autoButton);
        automatic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<ChessPiece> pieces = new ArrayList<ChessPiece>();

                if (gameBoard.whitesTurn) {
                    ArrayList<ChessPiece> whitePieces = gameBoard.whitePieces;
                    Collections.shuffle(whitePieces);
                    for (ChessPiece piece : whitePieces) {
                        if (piece != null && piece.getLocation().compareToIgnoreCase("null") != 0) {
                            pieces.add(piece);
                        }
                    }

                } else {
                    ArrayList<ChessPiece> blackPieces = gameBoard.blackPieces;
                    Collections.shuffle(blackPieces);
                    for (ChessPiece piece : blackPieces) {
                        if (piece != null && piece.getLocation().compareToIgnoreCase("null") != 0) {
                            pieces.add(piece);
                        }
                    }
                }

                boolean done = false;
                int g = 0;
                Collections.shuffle(pieces);
                while(!done && g<pieces.size()) {
                    ChessPiece piece = pieces.get(g);

                    if (randomMove(piece) == 1) {
                        Move move = new Move(moveFrom, moveTo);
                        addMoveToMoves(move);
                        try {
                            addToUndo();
                        }catch(Exception e){
                            System.out.println(e.getMessage());
                        }
                        drawBoard();
                        done = true;
                    }
                    g++;
                }
                if(!done){
                    showToast("Error, there are no valid moves left");
                }

            }
        });

        draw = (Button) findViewById(R.id.drawButton);
        draw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = "Are you sure that you want to end game in a Draw?";
                dialogMessage(message, 1);
            }


    });

        resign = (Button) findViewById(R.id.resignButton);
        resign.setOnClickListener(new View.OnClickListener() {
                                      @Override public void onClick(View v) {


                                          String message = "Are you sure that you want to resign?";
                                          dialogMessage(message,2);

                                      }
        });

    }
    public void dialogMessage(String message, int caseNum){
        //One of the players is asking for a draw
        switch(caseNum){
        case 1:
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


                        String playerDrawing;

                        if(gameBoard.whitesTurn)
                            playerDrawing = "White";
                        else
                            playerDrawing= "Black";

                        String confirmation = playerDrawing+" team is asking for a draw. Would you like to accept it?";
                        dialogMessage(confirmation,3);
                        dialog.cancel();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
        break;

        //One of the players resigns
        case 2:
        AlertDialog.Builder builderResign = new AlertDialog.Builder(this);
        builderResign.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        String save = "Would you like to save this game?";
                        dialogMessage(save,8);
                        dialog.cancel();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertResign = builderResign.create();
        alertResign.show();
        break;

        //Confirmation of Draw request to the other player
        case 3:
        AlertDialog.Builder builderConfirmation = new AlertDialog.Builder(this);
        builderConfirmation.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String save = "Would you like to save this game?";
                        dialogMessage(save,4);
                        dialog.cancel();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertConfirmation = builderConfirmation.create();
        alertConfirmation.show();
        break;

        //Save game question
        case 4:
        AlertDialog.Builder builderSave = new AlertDialog.Builder(this);
        builderSave.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String messageSave = "Please enter a name for the game: ";
                        dialogMessage(messageSave,5);
                        dialog.cancel();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        gameEnd(3);
                    }
                });
        AlertDialog alertSave = builderSave.create();
        alertSave.show();
        break;


        //File name dialog
        case 5:

        AlertDialog.Builder builderFileName = new AlertDialog.Builder(this);
        builderFileName.setMessage(message);

        final EditText input = new EditText(this);
        builderFileName.setView(input);

        builderFileName.setPositiveButton("Save", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int whichButton) {
                Editable value = input.getText();
                String newGameName = value.toString();

                if(newGameName.equals("")){
                    String messageResign = "Please enter a name for the game";
                    dialogMessage(messageResign,5);
                }

                else{
                    RecordMoves content = BoardGamePlay.this.gameMoves;
                    content.setFileName(value.toString());
                    GameActivity savedContent= BoardGamePlay.this.savedGames;
                    savedContent.recordedGames.add(content);
                    try {
                        GameRead.saveGames(savedGames,BoardGamePlay.this.getApplicationContext());
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }

                    dialog.cancel();
                    gameEnd(3);
                }
            }
        });

        AlertDialog alertFileName = builderFileName.create();
        alertFileName.show();
        break;

        //Notification dialog when one of the players resigns
        case 6:
        AlertDialog.Builder builderNotification = new AlertDialog.Builder(this);
        builderNotification.setMessage(message)
                .setCancelable(false)
                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        BoardGamePlay.this.finish();
                    }
                });
        AlertDialog alertNotification = builderNotification.create();
        alertNotification.show();
        break;

        case 7:
        AlertDialog.Builder builderNotification2 = new AlertDialog.Builder(this);
        builderNotification2.setMessage(message)
                .setCancelable(false)
                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertNotification2 = builderNotification2.create();
        alertNotification2.show();
        break;


        case 8:

        AlertDialog.Builder builderSave2 = new AlertDialog.Builder(this);
        builderSave2.setMessage(message)
                .setCancelable(false)

                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String messageSave = "Please enter a name for the game: ";
                        dialogMessage(messageSave,9);
                        dialog.cancel();
                    }
                })

                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                        gameEnd(6);
                    }
                });

        AlertDialog alertSave2 = builderSave2.create();
        alertSave2.show();
        break;


        case 9:

        AlertDialog.Builder builderFileName2 = new AlertDialog.Builder(this);
        builderFileName2.setMessage(message);

        final EditText input2 = new EditText(this);
        builderFileName2.setView(input2);

        builderFileName2.setPositiveButton("Save", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int whichButton) {

                Editable value = input2.getText();
                String newGameName = value.toString();

                if(newGameName.equals("")){
                    String messageResign = "Please enter a name for the game";
                    dialogMessage(messageResign,5);
                }

                else{
                    RecordMoves content = BoardGamePlay.this.gameMoves;
                    content.setFileName(value.toString());
                    GameActivity save = BoardGamePlay.this.savedGames;
                    save.recordedGames.add(content);
                    try {
                        GameRead.saveGames(savedGames,BoardGamePlay.this.getApplicationContext());
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    dialog.cancel();

                    gameEnd(7);
                }
            }
        });

        AlertDialog alertFileName2 = builderFileName2.create();
        alertFileName2.show();
        break;
    }
    }


    protected void showPromotion(){
        final String[] items  ={"Queen", "Rook", "Bishop", "Knight"};

        AlertDialog.Builder promo = new AlertDialog.Builder(this);
        promo.setTitle("Pawn is Due for a Promotion");
        promo.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int selection) {

                promoteMe = items[selection];
                switch(promoteMe){
                    case "Rook":
                        promoteMe = "r";
                        break;
                    case "Bishop":
                        promoteMe = "b";
                        break;
                    case "Knight":
                        promoteMe = "n";
                        break;
                }
                String hear = gameBoard.movePiece(moveFrom, moveTo, promoteMe);
                if (hear.compareToIgnoreCase("invalid") == 0) {
                    String errorMessage = "Error, invalid move";
                    showToast(errorMessage);
                } else {
                    move(hear);
                    //  prevPiece.setLocation(getLocation(row-1,column-1));
                    prevPiece.selected = false;
                    prevPiece = null;
                    prevPieceSelected = null;
                }
            }
        });
        AlertDialog alert = promo.create();
        alert.setCancelable(false);
        alert.show();
    }

    void addMoveToUndo() throws Exception{
        Board gameState = null;
         gameState = (Board)(SerializeObjects.deepCopy(gameBoard));
        gameUndo.addMove(gameState);
    }

    protected void gameEnd(int status){
        String message ="";
        String title="GAME OVER";
        switch(status){
            case 0:
                title = "CHECKMATE:";
                message ="White Player won";
                break;
            case 1:
                title = "CHECKMATE";
                message = "Black Player won";
                break;
            case 2:
                title = "STALEMATE";
                message = "No winner";
                break;
            case 3:
                title = "DRAW";
                message = "Game Ended in a Draw, NO WINNER";
                break;
            case 4:
                message = "White Player Won";
                break;
            case 5:
                message = "Black Player Won";
                break;
            case 6:
                title = "RESIGN";
                message = "Game Ended in Resign";
                break;
            case 7:
                title = "RESIGN";
                message = "Game Ended in Resign \n Game was SAVED";
                break;
            case 8:
                title = "GAME EXIT";
                message = "Are you sure you want to exit the game?";
                break;
        }
        showEndGameMessage(title, message);
    }
   public void showEndGameMessage(String title,String message){
       if(!title.contains("GAME")){
           if(!gameDraw){
               title = "";
               message = "Game Saved";
           }
           AlertDialog.Builder alert = new AlertDialog.Builder(this);
           alert.setTitle(title)
                   .setMessage(message)
                   .setCancelable(false)
                   .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {

                           dialog.cancel();
                           BoardGamePlay.this.finish();
                       }
                   });
           AlertDialog alertConfirmation = alert.create();
           alertConfirmation.show();
       }else {

           AlertDialog.Builder alert = new AlertDialog.Builder(this);
           alert.setTitle(title)
                   .setMessage(message)
                   .setCancelable(false)
                   .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {

                           dialog.cancel();

                       }
                   });
           AlertDialog alertConfirmation = alert.create();
           alertConfirmation.show();

           AlertDialog.Builder builderConfirmation = new AlertDialog.Builder(this);
           //Would the player like to save game after it ends
           builderConfirmation.setMessage(message)
                   .setCancelable(false)
                   .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                       public void onClick(DialogInterface dialog, int id) {
                           String save = "Would you like to save this game?";
                           dialogMessage(save, 4);
                           dialog.cancel();
                       }
                   })
                   .setNegativeButton("No", new DialogInterface.OnClickListener() {
                       public void onClick(DialogInterface dialog, int id) {
                           dialog.cancel();
                           BoardGamePlay.this.finish();
                       }
                   });
           AlertDialog confirmation = builderConfirmation.create();
           confirmation.show();
       }
   }
    public void backPressed(){
        AlertDialog.Builder confimation = new AlertDialog.Builder(this);
        confimation.setMessage("Are you sure that you want to exit current game?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                        BoardGamePlay.this.finish();
                    }
                });

    }
    private int randomMove(ChessPiece piece){
        int message = 0;

        ChessPiece holdPiece = piece;
        String from = holdPiece.getLocation();
        moveFrom = from;
        int fColumn = from.charAt(0)-97;
        int fRow = from.charAt(1)-49;
        fRow =  (fRow-7)*(-1);
        ArrayList<String> moves = legalMoves(holdPiece);
        Collections.shuffle(moves);
        int i= 0;
        boolean done = false;
        while(i<moves.size() && !done){
            String[]tok;
            String currMove = moves.get(i);
            i++;
            tok = currMove.split(",");
            int row = Integer.parseInt(tok[1])+fRow;
            int colm = Integer.parseInt(tok[0])+fColumn;

            if(row>7 || colm >7 ||row<0 ||colm<0) {
                ;
            }else{

                String moveLocation = getLocation(row, colm);
                moveTo = moveLocation;
                String promo = null;
                boolean promoted = false;
                if (holdPiece.getType().compareToIgnoreCase("pawn") == 0) {
                    if (holdPiece.getColor().compareToIgnoreCase("white") == 0) {
                        if (row == 0) {
                            showPromotion();
                            promoted = true;
                        }
                    } else {
                        if (row == 7) {
                            showPromotion();
                            promoted = true;
                        }
                    }
                }
                if(!promoted) {
                    String returnMessage = gameBoard.movePiece(from, moveLocation, promo);
                    if (returnMessage.compareToIgnoreCase("invalid") != 0) {
                        holdPiece.setLocation(getLocation(row, colm));
                        message = 1;
                        done = true;
                        return message;
                    }
                }
            }
        }



        return message;
    }
    private ArrayList<String> legalMoves(ChessPiece piece){

        ArrayList<String> moves = new ArrayList<String>();
        String type = piece.getType();
        //column(a-h), row(8-1)
        if(type.compareToIgnoreCase("pawn")==0){
            if(piece.getColor().compareToIgnoreCase("black")==0) {
                moves.add("0,1");//move up
                moves.add("0,2");//move up two space
                moves.add("1,1");//moves to right for attacking
                moves.add("-1,1");//moves to left to attack
            }else{
                moves.add("0,-1");//move down
                moves.add("0,-2");//move down two space
                moves.add("1,-1");//moves down right for attacking
                moves.add("-1,-1");//moves down left to attack
            }
        }else if(type.compareToIgnoreCase("king")==0){
            moves.add("1,0");//move up
            moves.add("1,1");//moves to right and up
            moves.add("1,0");//moves to right
            moves.add("2,0");//right castling
            moves.add("-1,1");//moves to left and up
            moves.add("-1,0"); //move left
            moves.add("-2,0");//left casting
            moves.add("0,-1");//move down
            moves.add("1,-1");//moves down right for attacking
            moves.add("-1,-1");//moves down left to attack
        }else if(type.compareToIgnoreCase("knight")==0){
            moves.add("-1,2");//moves up 2 and to the left
            moves.add("1,2");//moves up 2 and to the right
            moves.add("2,1");//moves right two and to the left
            moves.add("-2,1");
            moves.add("2,-1");
            moves.add("-2,-1");
            moves.add("-1,-2");
            moves.add("1,-2");
        }
        else if(type.compareToIgnoreCase("queen")==0){
            int i = 1;
            while(i<8){
                String strn = i+",0";//moves to the right
                moves.add(strn);
                i++;
            }
            i = -1;
            while(i>-8){
                String strn = i+",0";//moves to the left
                moves.add(strn);
                i--;
            }
            i = 1;
            while(i<8){
                String strn = "0,"+i;//moves up
                moves.add(strn);
                i++;
            }
            i = -1;
            while(i>-8){
                String strn = "0,"+i;//moves down
                moves.add(strn);
                i--;
            }
            i = 1;
            //diagnol moves
            int j = 1;
            while(i<8){
                String strn = i+","+j;//moves to the right
                moves.add(strn);
                i++;
                j++;
            }
            i = -1;
            j= -1;
            while(i>-8){
                String strn = i+","+j;//moves to the left and down
                moves.add(strn);
                i--;
                j--;
            }
            i = 1;
            j = -1;
            while(i<8){
                String strn = i+","+j;//moves up and left
                moves.add(strn);
                i++;
                j--;
            }
            i = -1;
            j = -1;
            while(i>-8){
                String strn = j+","+i;//moves down
                moves.add(strn);
                i--;
                j--;
            }
        }else if(type.compareToIgnoreCase("bishop")==0){
            int i = 1;
            int j = 1;
            while(i<8){
                String strn = i+","+j;//moves to the right
                moves.add(strn);
                i++;
                j++;
            }
            i = -1;
            j= -1;
            while(i>-8){
                String strn = i+","+j;//moves to the left and down
                moves.add(strn);
                i--;
                j--;
            }
            i = 1;
            j = -1;
            while(i<8){
                String strn = i+","+j;//moves up and left
                moves.add(strn);
                i++;
                j--;
            }
            i = -1;
            j = -1;
            while(i>-8){
                String strn = j+","+i;//moves down
                moves.add(strn);
                i--;
                j--;
            }
        }
        else {//rook
            int i = 1;
            while(i<8){
                String strn = i+",0";//moves to the right
                moves.add(strn);
                i++;
            }
            i = -1;
            while(i>-8){
                String strn = i+",0";//moves to the left
                moves.add(strn);
                i--;
            }
            i = 1;
            while(i<8){
                String strn = "0,"+i;//moves up
                moves.add(strn);
                i++;
            }
            i = -1;
            while(i>-8){
                String strn = "0,"+i;//moves down
                moves.add(strn);
                i--;
            }
        }
        return moves;
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
                    addlistener(textView, getCellID(i+1,j+1), i+1, j+1);
                }
                else{
                    textView.setBackgroundColor(00000000);
                    textView.setShadowLayer(0.0f, 0.0f, 0.0f, Color.WHITE);
                    textView.setTypeface(font);
                    textView.setText("\u2588");
                    textView.setTextColor(00000000);
                    textView.setTag(getLocation(i,j));
                    addlistener(textView,getCellID(i+1,j+1),i+1,j+1);

                }
            }
        }
    }
    private void addlistener(final TextView textView, final int id, final int row, final int column){

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ChessPiece holdPiece = gameBoard.board[row-1][column-1];
                String possiblePromotion = null;
                boolean promotion = false;
                if (holdPiece != null) {
                    if (gameBoard.whitesTurn) {
                        //deselected piece
                        if (prevPieceSelected != null && prevPiece.getType().compareToIgnoreCase("pawn") == 0) {
                            if (row-1 == 0) {
                                promotion = true;
                            }
                        }
                        if (prevPieceSelected != null && prevPieceSelected == textView) {
                            v.setBackgroundColor(00000000);
                            changefontColor(id, holdPiece);
                            holdPiece.selected = false;
                            origin = null;
                            prevPieceSelected = null;
                            prevPiece = null;
                            moveFrom = "";

                        } else if (prevPieceSelected == null) {
                            if(holdPiece.getColor().compareToIgnoreCase("white") != 0){
                                showToast("Error this is not your piece");
                            }else {
                                //selecting a new origin
                                v.setBackgroundColor(Color.BLACK);
                                v.getBackground().setAlpha(50);
                                changefontColor(id, holdPiece);
                                //selecting origin
                                TextView cell = (TextView) v;
                                holdPiece.selected = true;
                                origin = cell;
                                prevPieceSelected = textView;
                                moveFrom = holdPiece.getLocation();
                                prevPiece = holdPiece;
                            }
                        } else {
                            //moving to a nonempty spot
                            moveTo = holdPiece.getLocation();
                            if (promotion) {
                                showPromotion();
                                possiblePromotion = promoteMe;
                            }else {

                                String message = gameBoard.movePiece(moveFrom, moveTo, possiblePromotion);
                                if (message.compareToIgnoreCase("invalid") == 0) {
                                    //user is making an invalid move
                                    String errorMessage = "Error, invalid move";
                                    showToast(errorMessage);
                                } else {
                                    //valid move
                                    move(message);
                                    // prevPiece.setLocation(getLocation(row-1,column-1));
                                    prevPiece.selected = false;
                                    prevPiece = null;
                                    prevPieceSelected = null;
                                }
                            }
                        }
                    } else {//black turns
                        if (prevPieceSelected != null && prevPiece.getType().compareToIgnoreCase("pawn") == 0) {
                            if ((row-1) == 7) {
                                promotion = true;
                            }
                        }
                        if (prevPieceSelected != null && prevPieceSelected == textView) {
                            v.setBackgroundColor(00000000);
                            changefontColor(id, holdPiece);
                            origin = null;
                            holdPiece.selected = false;
                            prevPieceSelected = null;
                            prevPiece = null;
                            moveFrom = "";
                        } else if (prevPieceSelected == null) {
                            if(holdPiece.getColor().compareToIgnoreCase("black") == 0) {
                                //settting an oring
                                holdPiece.selected = true;
                                v.setBackgroundColor(Color.BLACK);
                                v.getBackground().setAlpha(50);
                                changefontColor(id, holdPiece);
                                TextView cell = (TextView) v;
                                origin = cell;
                                prevPieceSelected = textView;
                                moveFrom = holdPiece.getLocation();
                                prevPiece = holdPiece;
                            }else{
                                showToast("Error, this is not your piece");
                            }
                        } else {
                            //black attacking white
                            moveTo = holdPiece.getLocation();
                            if (promotion) {
                                showPromotion();
                                possiblePromotion = promoteMe;
                            }else {
                                String message = gameBoard.movePiece(moveFrom, moveTo, possiblePromotion);
                                if (message.compareToIgnoreCase("invalid") == 0) {
                                    String eMessage = "Error, invalid move";
                                    showToast(eMessage);
                                } else {
                                    move(message);
                                    //  prevPiece.setLocation(getLocation(row-1,column-1));
                                    prevPiece.selected = false;
                                    prevPiece = null;
                                    prevPieceSelected = null;
                                }
                            }
                        }
                    }

                } else {
                    if (prevPieceSelected != null) {
                        boolean promoFailed = true;
                        if (prevPiece.getType().compareToIgnoreCase("pawn") == 0) {
                            if (gameBoard.whitesTurn) {
                                if (row == 1) {
                                    moveTo = getLocation(row - 1, column - 1);
                                    showPromotion();
                                    promoFailed = false;
                                }
                            } else {
                                if (row == 8) {
                                    moveTo = getLocation(row - 1, column - 1);
                                    showPromotion();
                                    promoFailed = false;
                                }
                            }
                        }
                        if(promoFailed){
                            moveTo = getLocation(row - 1, column - 1);
                            String hear = gameBoard.movePiece(moveFrom, moveTo, possiblePromotion);
                            if (hear.compareToIgnoreCase("invalid") == 0) {
                                String errorMessage = "Error, invalid move";
                                showToast(errorMessage);
                            } else {
                                move(hear);
                                //  prevPiece.setLocation(getLocation(row-1,column-1));
                                prevPiece.selected = false;
                                prevPiece = null;
                                prevPieceSelected = null;
                            }
                        }
                    } else {
                        String errorMessage = "Error, no prior piece was selected";
                        showToast(errorMessage);
                    }
                }
            }
        });
    }

    private void move(String message){

        Move move = new Move(moveFrom, moveTo);
        if(origin!=null) {
            origin.setBackgroundColor(00000000);
        }
        origin = null;
        destiny = null;

        String[]tok;
        tok = message.split("\\s+");
        if(prevPiece!=null && prevPiece.getType().compareToIgnoreCase("pawn")==0){
            if(promoteMe!=null && promoteMe.length()>1){
                move.setPromotion(promoteMe);
                promoteMe = "";
            }
        }
        if(tok.length==1){
            if(tok[0].compareToIgnoreCase("stalemate")==0){
                gameEnd(2);
                return;
            }
        }
        if(tok.length==2){
            if(tok[1].compareTo("Wins")==0){
                if(tok[0].compareTo("White")==0){
                    gameEnd(4);
                }else{
                    gameEnd(5);
                }
                return;
            }
        }

        if(tok.length==3){
            if(tok[2].compareToIgnoreCase("check")==0){
                if(tok[0].compareToIgnoreCase("white")==0){
                    showToast(message);
                }else{
                    showToast(message);
                }
            }else if(tok[2].compareToIgnoreCase("checkmate")==0){
                if(gameBoard.whitesTurn){
                    gameEnd(0);
                }else{
                    gameEnd(1);
                }
                return;
            }

        }
        addMoveToMoves(move);
        try {
            addToUndo();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        drawBoard();
    }
    private void addMoveToMoves(Move move){
        gameMoves.addMove(move);
    }
    private void addToUndo() throws  Exception{
        Board boardState = null;
        boardState = (Board)(SerializeObjects.deepCopy(gameBoard));
        gameUndo.addMove(boardState);
    }

    protected void undo() throws Exception{
        if(gameBoard.moveNumber!=0){
            if(prevOrigin!=null)
                removeTrace(prevOrigin, prevDestiny);
            gameBoard.undo();
            gameMoves.removeLastMove();
            origin = null;
            destiny = null;
            prevPiece = null;
            prevPieceSelected = null;
            moveFrom = null;
            moveTo = null;
            Board recovState = gameUndo.undoMove();
            //this.gameBoard = recovState;
             drawBoard();
        }else{
            showToast("There is nothing to undo");
        }
    }

    private void removeTrace(String mvFro, String mvTo){

    }
    private String getLocation(int row, int colm){
        row = (-1*row)+8;
        String row1 = String.valueOf(row);
        String column = "";
        switch(colm){
            case 0:
                 column= "a";
                break;
            case 1:
                column="b";
                break;
            case 2:
                column="c";
                break;
            case 3:
                column="d";
                break;
            case 4:
                column="e";
                break;
            case 5:
                column="f";
                break;
            case 6:
                column="g";
                break;
            case 7:
                column="h";
                break;
        }

        return column+ row1;
    }

    private void changefontColor(int id, ChessPiece piece){
        if(!piece.selected){
            TextView txt = (TextView)findViewById(id);
            txt.setTextColor(Color.WHITE);
        }else{
            TextView txt = (TextView)findViewById(id);
            txt.setTextColor(Color.BLACK);
        }
    }
    private int getCellID(int row, int colm){
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

    private void showToast(String message){

           Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
           toast.setGravity(Gravity.TOP, -30, 50);
           toast.show();

    }

    public void onBackPressed(){
        gameDraw = false;
        gameEnd(8);

    }
}

