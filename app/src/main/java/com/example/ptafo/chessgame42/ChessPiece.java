package com.example.ptafo.chessgame42;

/**
 * Created by ptafo on 4/23/2017.
 */

import java.io.Serializable;

/**
 * This is the abstract class which will be extended by each chess piece type class
 * Each piece has a color (String), location(String), hasMoved (boolean), name(String), type (String)
 * Those mentioned above will be set by the constructers initially when creating the table in the chess class.
 * Additionally each subclass of this class will implement the following function: move, check.
 * @author Pedro Cruz
 *
 */
public abstract class ChessPiece implements Serializable{

    private static final long serialVersionUID = -4877050010818828391L;
    String[] column = {"a","b","c","d","e","f","g","h"};
    String[] rows = {"1","2","3","4","5","6","7","8"};
    boolean el_pessant = false;
    boolean promotion =false;
    String prom;
    /**
     * @color
     * Color is the color of the piece when created by the subclass, it is either white or black.
     * @location
     * Location: is the most current location indicating where a piece is in the board.
     * @hasMoved
     * hasMoved: is a boolean variable indicating if the piece has moved from initial position or not. It will be set to true when the piece has moved successfully.
     * @name
     * Name: is the name which will be printed on the board, ex. black King = bK.
     * @type
     * Type:  will be used to indicated what type of piece it is. Useful for comparing.
     * @other
     * X and Y are the coordinate of the current piece in the table. X is for the rows and Y is the columns.
     * @el_pessant
     *  Variable used for Pawn instance to determine its vulnerability to be takes out when opposite color is replaced one block behing it
     *  @column
     *  Determines the Horizontal position of an instance
     *  @rows
     *  Determines the Vertical position of an instance

     * @author Pedro Cruz
     */
    public String color;
    public String location;
    boolean hasMoved = false;
    public String name;
    public String type;
    public int x,y;
    public boolean selected = false;
    /**
     * @author Pedro Cruz
     * @param x is the rows of the board 1 - 8
     * @param y is the columns of the board a - h
     */
    /**
     * @author Pedro Cruz
     * @return the color of this piece
     */
    public String getColor(){
        return color;
    }
    /**
     * Will return true or false depending if the current piece is able to move or not, to the location set by the player.
     * @param board from chess class
     * @param toX row this piece will go to
     * @param toY column this piece will go to
     * @return boolean
     * @author Pedro Cruz
     */
    public abstract boolean move(ChessPiece[][]board, int toX, int toY);
    /**
     * Will check if after successful move of current piece if it will put the opponent's  piece in check
     * @param board
     * @return boolean
     * @author Pedro Cruz
     */
    public abstract boolean check(ChessPiece[][]board);

    /**Will be used to print the location of the piece onto the board each time the board is updated
     * @author Pedro Cruz
     */
    public String getLocation(){
        return location;
    }
    /**
     * Prints out the name of the piece
     * @author Pedro Cruz
     */
    public void setLocation(String location){
        this.location = location;
    }

    public String getName(){
        return  name;
    }
    /**
     * This method will return the type which is the piece, used for comparisons.
     * @return String
     */
    public String getType(){
        return type;
    }
    /**
     * Every piece will has the capability of moving as long as it passed legal moves. The generic moves are no out of board, and the specific rules of this (current) piece will be set by the the move method.
     * @param board from the chess class
     * @param to String of coordinated indicating the next move.
     * @return Boolean: True if successful or False and message print if failed
     * @author Pedro Crux
     */
    public boolean movePiece(ChessPiece[][] board, String to, String possiblePromotion){
        if(to.length()!=2 ){
            return false;
        }
        int ty = to.charAt(0)-97;
        //row
        int tx = to.charAt(1)-49;

        this.prom = possiblePromotion;
        //board is upside down to how out board is made so it converts the value given by user
        //to a usable value for our array
        tx =  (tx-7)*(-1);

        //checks for out of boundary entry
        if(tx>7 ||tx< 0 || ty>7 || ty<0){
            return false;
        }


        ChessPiece[][] tempBoard = board;
        //if move is legal this will changes the current location of the piece
        if(this.move(tempBoard, tx,ty)){
            int oldX = this.x;
            int oldY = this.y;

            this.y = ty;
            this.x = tx;
            this.location = to;
            if(board[tx][ty]!=null){
                board[tx][ty].location = "null";
            }

            board[tx][ty]= board[oldX][oldY];
            board[oldX][oldY]=null;
            return true;
        }
        //checks if King is in check
        return false;

    }

}