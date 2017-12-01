package com.example.ptafo.chessgame42;



public class Rook extends ChessPiece{
    /**
     * Root constructor taking in color, and coordinated x and y. Also location is set to a default state depending on the color that is sent in and the y coordinate.
     * @param color
     * @param x
     * @param y
     * @author Pedro Cruz
     */
    private static final long serialVersionUID = -834531053611595797L;
    public Rook(String color, int x, int y){
        type = "Rook";
        if(color =="white"){
            this.color = color;
            this.x = x;
            this.y = y;
            if(y == 0){
                location = "a1";
            }else{
                location = "h1";
            }
            name = "wR";
        }else{
            this.color = color;
            this.x = x;
            this.y = y;
            if(y ==0){
                location = "a8";
            }else{
                location = "h8";
            }
            name = "bR";
        }
    }
    /**
     * This is the override abstract method move, from the ChessPiece class. There rules specified on what is a legal move, specific to the Rook piece are implemented here. NOTE: Rook can only move in one direction either left or up or right or down. No diagnal move is allowed.
     * @author Pedro Cruz
     */
    public boolean move(ChessPiece[][] board, int toX, int toY){

        int newX = Math.abs(this.x - toX);
        int newY = Math.abs(this.y - toY);

        if((newX == 0 || newY ==0) &&newX!=newY){
            if(newX==0){
                //movement across columns
                int temp = this.y;
                //move left
                if(temp>toY){
                    temp--;
                    if(temp>-1){
                        while(board[this.x][temp]==null && temp!=toY){
                            temp--;
                        }
                    }
                }else{
                    temp++;
                    if(temp<8){
                        while(board[this.x][temp]==null && temp!=toY){
                            temp++;
                        }
                    }
                }
                if(temp!=toY){
                    //System.out.println("these is a abustructive stucture in path, invalid move");
                    return false;
                }
                if(board[toX][toY]!=null && board[toX][toY].getColor().compareTo(this.getColor())==0){
                    //System.out.println("your piece is there");
                    return false;
                }
                this.hasMoved = true;
                return true;

            }else{
                //movement across rows
                int temp = this.x;
                if(temp>toX){
                    temp--;
                    if(temp>-1){
                        while(board[temp][this.y]==null && temp!=toX){
                            temp--;
                        }
                    }
                }else{
                    temp++;
                    if(temp<8){
                        while(board[temp][this.y]==null && temp!=toX){
                            temp++;
                        }
                    }
                }


                if(temp!=toX){
                    //System.out.println("these is a abustructive stucture in path, invalid move");
                    return false;
                }
                if(board[toX][toY]!=null && board[toX][toY].getColor().compareTo(this.getColor())==0){
                    //System.out.println("your piece is there");
                    return false;
                }
                this.hasMoved = true;
                return true;

            }
        }else{

            return false;
        }
    }
    /**
     * Rook check method. Overriding the check method of the abstract class ChessPiece. Checks if after successful move if the Rook is not in position to attack the opponents king should the opponent not move their king.
     * @author Pedro Cruz
     */
    public boolean check(ChessPiece[][] board){
        int tempX = this.x-1;
        int tempY = this.y;

        //looks to the up for king 0-->8
        while(tempX>-1 && board[tempX][tempY]==null){
            tempX--;
        }
        if(tempX>-1 && board[tempX][tempY].getType().compareTo("King")==0){
            if(board[tempX][tempY]!=null && this.getColor().compareTo(board[tempX][tempY].getColor())!=0){ return true;}
        }

        tempX = this.x+1;
        //checks to the down 8-->1
        while(tempX< 8 && board[tempX][tempY]==null){
            tempX++;
        }
        if(tempX<8 && board[tempX][tempY].getType().compareTo("King")==0){
            if(board[tempX][tempY]!=null && this.getColor().compareTo(board[tempX][tempY].getColor())!=0){ return true;}
        }
        //checks left
        tempX = this.x;
        tempY = this.y-1;
        while(tempY > -1 && board[tempX][tempY]==null){
            tempY--;
        }

        if(tempY>-1 && board[tempX][tempY].getType().compareTo("King")==0){
            if(board[tempX][tempY]!=null && this.getColor().compareTo(board[tempX][tempY].getColor())!=0){ return true;}
        }
        tempX = this.x;
        tempY = this.y+1;
        //checks right
        while(tempY< 8 && board[tempX][tempY]==null){
            tempY++;
        }

        if(tempY<8 && board[this.x][tempY].getType().compareTo("King")==0){
            if(board[tempX][tempY]!=null && this.getColor().compareTo(board[tempX][tempY].getColor())!=0){
                return true;}
        }
        return false;
    }
}