package com.example.ptafo.chessgame42;

import java.io.Serializable;
import java.util.ArrayList;



public class Board implements Serializable{

    private static final long serialVersionUID = -3971191419261581603L;

    public ChessPiece whiteKing;
    public ChessPiece blackKing;
    public ChessPiece pieceToMove;
    public ChessPiece[][] board;
    public boolean end;
    public int whiteInCheck  =0;
    public int blackInCheck  =0;
    public String currPosition;
    public int prevPieceSelectedID = 0;
    public ChessPiece prePieceSelected = null;
    public final int rows  =8;
    public final int colms = 8;
    public String player1 = "white", player2 = "black";
    public int moveNumber;
    public boolean whitesTurn;
    private ArrayList<ChessPiece[][]> prevBoards;
    public ArrayList<ChessPiece> whitePieces;
    public ArrayList<ChessPiece> blackPieces;

    protected Board(){
        whitePieces = new ArrayList<ChessPiece>();
        blackPieces = new ArrayList<ChessPiece>();
        prevBoards = new ArrayList<ChessPiece[][]>();
        createTable();
        end = false;
        whitesTurn = true;
        moveNumber = 0;

    }

   //return 0 if white wins
    //return 1 if black wins
    //retruns -1 o/w
    protected int gameOver() throws Exception{
        if(whitesTurn) {
            if (blackKing.location == "null") {
                return 0;
            }
        }else {
            if (whiteKing.location == "null") {
                return 1;
            }
        }
        return -1;
    }
    //returns -10 if white wins, -11 if black wins
    //returns 0 for whites turn, returns 1 if  black turn
    public ChessPiece getChessPiece(int i, int j){
        return board[i][j];
    }
    public String movePiece(String moveFrom, String moveTo, String possiblePromotion){
        setPreviousBoard();
            String message = "";
            if(whitesTurn){
                checkElPessant("white");
            }else{
                checkElPessant("black");
            }
            String warning = "";
            if(whitesTurn){
                if(validPiece(board, moveFrom, whiteKing)){
                    if(pieceToMove.movePiece(board, moveTo,possiblePromotion)){
                        if(pieceToMove.check(board)){
                            blackInCheck = 1;

                        }else{
                            blackInCheck = 0;
                        }

                        if(blackInCheck==1){
                            if(CheckMate(blackKing, board, blackInCheck)){
                                message = "Black in Checkmate";
                            }else{
                                message = "Black in Check";
                            }
                        }else{
                            message = "valid";
                        }
                        whitesTurn = false;
                    }else{
                        message = "invalid";
                        warning = "piece is not a valid piece in board";
                    }
                }
            }else{
                if(validPiece(board, moveFrom, blackKing)){
                    if(pieceToMove.movePiece(board, moveTo,possiblePromotion)){
                        if(pieceToMove.check(board)){
                            whiteInCheck = 1;
                        }else{
                            whiteInCheck = 0;
                        }

                        if(whiteInCheck==1){
                            if(CheckMate(whiteKing,board,whiteInCheck)){
                                message = "White in Checkmate";
                            }else{
                                message = "White in Check";
                            }
                        }else{
                            message=  "valid";
                        }

                        whitesTurn = true;
                    }else{
                        message = "invalid";
                        warning = "piece is not a valid black piece";
                    }
                }
            }

           if(end==true){
               if(blackInCheck==0 && whiteInCheck==0){
                   message = "Stalemate";
               }
           }
           if(message.compareToIgnoreCase("invalid")!=0){
               moveNumber++;
           }
           if(message.compareToIgnoreCase("invalid")==0){
               if(prevBoards!=null && prevBoards.size()>0){
                   prevBoards.remove(prevBoards.size()-1);
               }
           }
           if(whiteKing!=null && whiteKing.getLocation().compareTo("null")==0){
               message = "Black Wins";
           }
           if(blackKing!=null && blackKing.getLocation().compareTo("null")==0){
               message = "White Wins";
           }

        return message;

    }

    public void setPreviousBoard(){
        ChessPiece[][] previousBoard = new ChessPiece[8][8];
        for(int i = 0; i<8; i++){
            for(int j= 0; j<8; j++){

                if(board[i][j]!=null){
                    String type = board[i][j].getType().toLowerCase();
                    String color = board[i][j].getColor();
                    String location = board[i][j].getLocation();
                    int fColumn = location.charAt(0)-97;
                    int fRow = location.charAt(1)-49;
                    fRow =  (fRow-7)*(-1);
                    switch(type){
                        case "king":
                            previousBoard[i][j] = new King(color,i, j);
                            previousBoard[i][j].setLocation(getLocation(fRow,fColumn));
                            break;
                        case "queen":
                            previousBoard[i][j] = new Queen(color,i, j);
                            previousBoard[i][j].setLocation(getLocation(fRow,fColumn));
                            break;
                        case "pawn":
                            previousBoard[i][j] = new pawn(color,i, j);
                            previousBoard[i][j].setLocation(getLocation(fRow,fColumn));
                            break;
                        case "knight":
                            previousBoard[i][j] = new Knight(color,i, j);
                            previousBoard[i][j].setLocation(getLocation(fRow,fColumn));
                            break;
                        case "rook":
                            previousBoard[i][j] = new Rook(color,i, j);
                            previousBoard[i][j].setLocation(getLocation(fRow,fColumn));
                            break;
                        case "bishop":
                            previousBoard[i][j] = new Bishop(color,i, j);
                            previousBoard[i][j].setLocation(getLocation(fRow,fColumn));
                            break;

                    }
                }else{
                    previousBoard[i][j] = board[i][j];
                }
            }
        }
        prevBoards.add(previousBoard);
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
    public void undo(){
        if(prevBoards==null || prevBoards.size()==0 ||moveNumber==0){return;}
        ChessPiece[][] previousBoard = prevBoards.remove(prevBoards.size()-1);
        whitePieces.clear();
        blackPieces.clear();
        for(int i = 0 ; i<8; i++){
            for(int j = 0; j<8; j++){
                board[i][j]= previousBoard[i][j];
                if(board[i][j]!=null){
                    board[i][j].setLocation(getLocation(i,j));
                    if(board[i][j].getColor().compareToIgnoreCase("white")==0){
                        whitePieces.add(board[i][j]);
                    }else{
                        blackPieces.add(board[i][j]);
                    }
                    if(board[i][j].getType().compareToIgnoreCase("king")==0){
                        if(board[i][j].getColor().compareToIgnoreCase("white")==0){
                            whiteKing = board[i][j];
                        }else{
                            blackKing = board[i][j];
                        }
                    }
                }
            }
        }

        if(moveNumber!=0){
            moveNumber--;
        }
        if(moveNumber%2==0 ||moveNumber==0 && moveNumber!=1 ){
            whitesTurn=true;
        }else{
            whitesTurn=false;
        }
    }

    /**
     * This method exhausts all possible options that next player can do to their king
     * in order to avoid checkmate if the count ==8 this means that no matter where the king goes
     * they will lose
     *
     * king has at most 8 possible options
     * @author Pedro Cruz
     * @param king opposite king
     * @param board
     */
    public boolean CheckMate(ChessPiece king, ChessPiece[][] board, int inCheck){


        int tempX = king.x;
        int tempY = king.y;
        int checkCount = 0;
        int availableSpace = 8;

        ChessPiece oppKing;
        if(king.getColor().compareTo("white")==0){
            oppKing = blackKing;
        }else{
            oppKing = whiteKing;
        }


        ChessPiece tempPiece = null;
        ChessPiece[][] tempBoard = new ChessPiece[8][8];

        for(int i = 0; i<board.length; i++){
            for(int j =0; j<board.length; j++){
                tempBoard[i][j]= board[i][j];
            }
        }

        //down and to the left
        tempX++;
        tempY--;
        if(tempY>-1 && tempX<8){
            ChessPiece tempKing = tempBoard[king.x][king.y];
            if(tempBoard[tempX][tempY]==null||tempBoard[tempX][tempY].getColor().compareTo(king.getColor())!=0){
                tempBoard[tempX][tempY] = tempKing;
                tempBoard[king.x][king.y] = null;
                tempBoard[oppKing.x][oppKing.y] = null;
                boolean stop = false;

                for(int i=0; i<board.length &&stop!=true; i++){
                    for(int j=0; j<board.length; j++){
                        tempPiece = tempBoard[i][j];
                        if(tempPiece!=null){
                            if(tempPiece.check(tempBoard)){
                                checkCount++;
                                stop = true;
                                break;
                            }
                        }
                    }
                }
                tempBoard[oppKing.x][oppKing.y] = oppKing;
                tempBoard[tempX][tempY]=board[tempX][tempY];
                tempBoard[king.x][king.y]= tempKing;
            }else{
                availableSpace--;
            }
        }else{
            availableSpace--;
        }

        //checks left
        tempX = king.x;
        tempY = king.y -1;

        if(tempY>-1){
            ChessPiece tempKing = tempBoard[king.x][king.y];
            if(tempBoard[tempX][tempY]==null||tempBoard[tempX][tempY].getColor().compareTo(king.getColor())!=0){
                tempBoard[tempX][tempY] = tempKing;
                tempBoard[king.x][king.y] = null;
                tempBoard[oppKing.x][oppKing.y] = null;
                boolean stop = false;

                for(int i=0; i<board.length && stop!=true; i++){
                    for(int j=0; j<board.length; j++){
                        tempPiece = tempBoard[i][j];
                        if(tempPiece!=null){
                            if(tempPiece.check(tempBoard)){
                                checkCount++;
                                stop = true;
                                break;
                            }
                        }
                    }
                }
                tempBoard[oppKing.x][oppKing.y] = oppKing;
                tempBoard[tempX][tempY]=board[tempX][tempY];
                tempBoard[king.x][king.y]= tempKing;
            }else{
                availableSpace--;
            }
        }else{
            availableSpace--;
        }



        //checks left up
        tempY = king.y -1;
        tempX = king.x -1;
        if(tempY>-1 && tempX>-1){
            ChessPiece tempKing = tempBoard[king.x][king.y];
            if(tempBoard[tempX][tempY]==null||tempBoard[tempX][tempY].getColor().compareTo(king.getColor())!=0){
                tempBoard[tempX][tempY] = tempKing;
                tempBoard[king.x][king.y] = null;
                tempBoard[oppKing.x][oppKing.y] = null;
                boolean stop = false;

                for(int i=0; i<board.length && stop!=true; i++){
                    for(int j=0; j<board.length; j++){
                        tempPiece = tempBoard[i][j];
                        if(tempPiece!=null){
                            if(tempPiece.check(tempBoard)){
                                checkCount++;
                                stop = true;
                                break;
                            }
                        }
                    }
                }
                tempBoard[oppKing.x][oppKing.y] = oppKing;
                tempBoard[tempX][tempY]=board[tempX][tempY];
                tempBoard[king.x][king.y]= tempKing;
            }else{
                availableSpace--;
            }
        }else{
            availableSpace--;
        }




        //checks up
        tempY = king.y;
        tempX = king.x -1;
        if(tempX>-1){
            ChessPiece tempKing = tempBoard[king.x][king.y];
            if(tempBoard[tempX][tempY]==null||tempBoard[tempX][tempY].getColor().compareTo(king.getColor())!=0){
                tempBoard[tempX][tempY] = tempKing;
                tempBoard[king.x][king.y] = null;
                tempBoard[oppKing.x][oppKing.y] = null;
                boolean stop=  false;

                for(int i=0; i<board.length && stop!=true; i++){
                    for(int j=0; j<board.length; j++){
                        tempPiece = tempBoard[i][j];
                        if(tempPiece!=null){
                            if(tempPiece.check(tempBoard)){
                                checkCount++;
                                stop = true;
                                break;
                            }
                        }
                    }
                }
                tempBoard[oppKing.x][oppKing.y] = oppKing;
                tempBoard[tempX][tempY]=board[tempX][tempY];
                tempBoard[king.x][king.y]= tempKing;
            }else{
                availableSpace--;
            }
        }else{
            availableSpace--;
        }


        //checks up right
        tempX = king.x -1;
        tempY = king.y +1;
        if(tempY<8 && tempX>-1){
            ChessPiece tempKing = tempBoard[king.x][king.y];
            if(tempBoard[tempX][tempY]==null||tempBoard[tempX][tempY].getColor().compareTo(king.getColor())!=0){
                tempBoard[tempX][tempY] = tempKing;
                tempBoard[king.x][king.y] = null;
                tempBoard[oppKing.x][oppKing.y] = null;
                boolean stop = false;

                for(int i=0; i<board.length && stop!=false; i++){
                    for(int j=0; j<board.length; j++){
                        tempPiece = tempBoard[i][j];
                        if(tempPiece!=null){
                            if(tempPiece.check(tempBoard)){
                                checkCount++;
                                stop = true;
                                break;
                            }
                        }
                    }
                }
                tempBoard[oppKing.x][oppKing.y] = oppKing;
                tempBoard[tempX][tempY]=board[tempX][tempY];
                tempBoard[king.x][king.y]= tempKing;
            }else{
                availableSpace--;
            }
        }else{
            availableSpace--;
        }



        //checks right
        tempX = king.x;
        tempY = king.y+1;
        if(tempY<8){
            ChessPiece tempKing = tempBoard[king.x][king.y];
            if(tempBoard[tempX][tempY]==null||tempBoard[tempX][tempY].getColor().compareTo(king.getColor())!=0){
                tempBoard[tempX][tempY] = tempKing;
                tempBoard[king.x][king.y] = null;
                tempBoard[oppKing.x][oppKing.y] = null;
                boolean stop = false;


                for(int i=0; i<board.length && stop!= true; i++){
                    for(int j=0; j<board.length; j++){
                        tempPiece = tempBoard[i][j];
                        if(tempPiece!=null){
                            if(tempPiece.check(tempBoard)){
                                checkCount++;
                                stop = true;
                                break;
                            }
                        }
                    }
                }
                tempBoard[oppKing.x][oppKing.y] = oppKing;
                tempBoard[tempX][tempY]=board[tempX][tempY];
                tempBoard[king.x][king.y]= tempKing;
            }else{
                availableSpace--;
            }
        }else{
            availableSpace--;
        }




        //checks down and to the right
        tempX = king.x+1;
        tempY = king.y +1;
        if(tempX<8 && tempY<8){
            ChessPiece tempKing = board[king.x][king.y];
            if(tempBoard[tempX][tempY]==null||tempBoard[tempX][tempY].getColor().compareTo(king.getColor())!=0){
                tempBoard[tempX][tempY] = tempKing;
                tempBoard[king.x][king.y] = null;
                tempBoard[oppKing.x][oppKing.y] = null;
                boolean stop = false;

                for(int i=0; i<board.length && stop!=true; i++){
                    for(int j=0; j<board.length; j++){
                        tempPiece = tempBoard[i][j];
                        if(tempPiece!=null){
                            if(tempPiece.check(tempBoard)){
                                stop = true;
                                checkCount++;
                                break;
                            }
                        }
                    }
                }
                tempBoard[oppKing.x][oppKing.y] = oppKing;
                tempBoard[tempX][tempY]=board[tempX][tempY];
                tempBoard[king.x][king.y]= tempKing;
            }else{
                availableSpace--;
            }
        }else{
            availableSpace--;
        }




        //checks down
        tempX = king.x+1;
        tempY = king.y;
        if(tempX<8){
            ChessPiece tempKing = board[king.x][king.y];
            if(tempBoard[tempX][tempY]==null||tempBoard[tempX][tempY].getColor().compareTo(king.getColor())!=0){

                tempBoard[tempX][tempY] = tempKing;
                tempBoard[king.x][king.y] = null;
                tempBoard[oppKing.x][oppKing.y] = null;
                boolean stop = false;

                for(int i=0; i<board.length && stop!= true; i++){
                    for(int j=0; j<board.length; j++){
                        tempPiece = tempBoard[i][j];
                        if(tempPiece!=null){
                            if(tempPiece.check(tempBoard)){
                                checkCount++;
                                stop = true;
                                break;
                            }
                        }
                    }
                }
                tempBoard[oppKing.x][oppKing.y] = oppKing;
                tempBoard[tempX][tempY]=board[tempX][tempY];
                tempBoard[king.x][king.y]= tempKing;
            }else{
                availableSpace--;
            }
        }else{
            availableSpace--;
        }




        if(checkCount==availableSpace && checkCount!=0){
            if(inCheck==0){
                end = true;
                return true;
            }
            if(king.getColor().compareToIgnoreCase("white")==0){
                end = true;
                whiteKing = null;
            }else{
                blackKing = null;
                end = true;
            }
            return true;
        }
        return false;
    }

    /**
     * Created a board by generatting a double array of ChessPiece setting the pieces by calling the subsets in the correct position. All empty/unoccupied pieces are set to null.
     * @return
     * @author Pedro Cruz
     */
    private void createTable(){
        board= new ChessPiece[8][8];

        for(int i= 0; i< board.length ;i++ ){

            for(int j= 0; j< board[i].length; j++){
                if(i==1){
                    //fill black pawns

                    board[i][j]= new pawn("black",1,j);
                }else if(i==6){
                    //fills white pawns
                    board[i][j] = new pawn("white",6,j);
                }else{
                    board[i][j]= null;
                }
            }

        }

        board[0][0] = new Rook("black", 0,0);
        board[0][1] = new Knight("black",0,1);
        board[0][2] = new Bishop("black",0,2);
        board[0][3] = new Queen("black", 0,3);
        board[0][4] = new King("black",0,4);
        blackKing = board[0][4];
        board[0][5] = new Bishop("black",0,5);
        board[0][6] = new Knight("black",0,6);
        board[0][7] = new Rook("black",0,7);

        board[7][0] = new Rook("white",7,0);
        board[7][1] = new Knight("white",7,1);
        board[7][2] = new Bishop("white",7,2);
        board[7][3] = new Queen("white",7,3);
        board[7][4] = new King("white",7,4);
        whiteKing = board[7][4];
        board[7][5] = new Bishop("white",7,5);
        board[7][6] = new Knight("white",7,6);
        board[7][7] = new Rook("white",7,7);

        for(int i = 0; i<board.length; i++){
            whitePieces.add(board[6][i]);
            blackPieces.add(board[1][i]);
        }
        for(int i = 0; i<board.length; i++ ){
            whitePieces.add(board[7][i]);
            blackPieces.add(board[0][i]);
        }

    }

    public ArrayList<ChessPiece> getPieces(String color){
        if(color.compareToIgnoreCase("white")==0){
            return whitePieces;
        }else{
            return blackPieces;
        }
    }
    /**
     * Checks if the piece that the player is trying to move belongs to them.
     * @param board
     * @param moveFrom
     * @param king
     * @return boolean
     * @author Pedro Cruz
     */

    private boolean validPiece(ChessPiece[][] board, String moveFrom, ChessPiece king){

        //column
        if(moveFrom.length()!=2){
            return false;
        }
        int ty = moveFrom.charAt(0)-97;
        //row
        int tx = moveFrom.charAt(1)-49;

        //board is upside down to how out board is made so it converts the value given by user
        //to a usable value for our array
        tx =  (tx-7)*(-1);

        //checks for out of boundary entry
        if(tx>7 ||tx< 0 || ty>7 || ty<0){
            return false;
        }

        if(board[tx][ty]==null){
            return false;
        }

        if(board[tx][ty].getColor()!= king.getColor()){
            return false;
        }
        pieceToMove = board[tx][ty];
        return true;
    }

    /**
     *
     * this method checks el_pessant values and sets equals to false depending whose turn it is (parameters)
     * @param color
     */
    private void checkElPessant(String color){

        for (int i = 0 ; i< board[0].length; i ++){

            for (int j = 0 ; j < board[i].length; j++){


                if (board[i][j]!=null
                        && board[i][j].type.equalsIgnoreCase("Pawn")
                        && board[i][j].color.equalsIgnoreCase(color)){

                    board[i][j].el_pessant = false;
                }
            }
        }


    }

    ChessPiece returnKing(String color){
        if(color.compareToIgnoreCase("white")==0){
            return whiteKing;
        }else{
            return blackKing;
        }
    }

}
