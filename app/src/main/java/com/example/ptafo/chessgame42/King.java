package com.example.ptafo.chessgame42;


public class King extends ChessPiece {
    /**
     * This is the King piece constructor, taking in the color specific to this location and the coordinated at which the piece will initially be when called.
     * @author Pedro Cruz
     * @param color String
     * @param x int
     * @param y int
     */
    private static final long serialVersionUID = -437852127107894880L;
    public King(String color, int x, int y){
        type = "King";
        if(color =="white"){
            this.color = color;
            this.x = x;
            this.y = y;
            location = "e1";
            name = "wK";
        }else{
            this.color = color;
            this.x = x;
            this.y = y;
            location = "e8";
            name = "bK";
        }
    }
    /**
     *
     * Kings move Piece. Rules specific to the Kings legal moves are defined here and are used to see if the coordinated where the piece will be going to violate or not these rules.
     * @author Pedro Cruz
     */
    public  boolean move(ChessPiece[][] board, int toX, int toY){

        int newX = Math.abs(this.x - toX);
        int newY = Math.abs(this.y - toY);

        //checks for possible castling
        if(this.hasMoved==false && (newY==2) && newX==0){
            char sideToMove;
            //left ? right
            if(this.y>toY){
                if(board[this.x][0]!=null && board[this.x][0].hasMoved==true){
                    return false;
                }
                sideToMove = 'l';
            }else{
                if(board[this.x][7]!=null &&board[this.x][7].hasMoved==true){
                    return false;
                }
                sideToMove = 'r';
            }
            return castling(sideToMove, board);
        }

        if(newX>1 || newY>1){
            return false;
        }else if(this.y ==toY && this.x == toX){
            //System.out.println("Error: Illegal move");
            return false;
        }else{
            //checks if the piece there it its own piece
            if(board[toX][toY]!=null &&board[toX][toY].getColor()==this.getColor()){
                //System.out.println("your piece is there");
                return false;
            }else{
                this.hasMoved = true;
                return true;
            }
        }
    }
    /**
     *
     *
     * This check method checks to see if after successful movement of piece if it puts the opponent in check by looking for the the opponents king obeying the king's move rules.
     * @author Pedro Cruz
     */
    public boolean check(ChessPiece[][] board){

        int tempX = this.x;
        int tempY = this.y;


        //checks left up
        tempX++;
        tempY--;
        if(tempY>-1 && tempX<8){
            ChessPiece tempPiece = board[tempX][tempY];
            if(tempPiece!=null && tempPiece.getType()=="King" && this.getColor()!=tempPiece.getColor()){ return true;}
        }


        //checks left
        tempY = this.y -1;
        tempX = this.x;
        if(tempY>-1){
            ChessPiece tempPiece = board[tempX][tempY];
            if(tempPiece!=null && tempPiece.getType()=="King" && this.getColor()!=tempPiece.getColor()){ return true;}
        }

        //checks left down
        tempY = this.y -1;
        tempX = this.x -1;
        if(tempY>-1 && tempX>-1){
            ChessPiece tempPiece = board[tempX][tempY];
            if(tempPiece!=null && tempPiece.getType()=="King" && this.getColor()!=tempPiece.getColor()){ return true;}
        }



        //checks down
        tempY = this.y;
        tempX = this.x -1;
        if(tempX>-1){
            ChessPiece tempPiece = board[tempX][tempY];
            if(tempPiece!=null && tempPiece.getType()=="King" && this.getColor()!=tempPiece.getColor()){ return true;}
        }


        //checks down right
        tempX = this.x -1;
        tempY = this.y +1;
        if(tempY<8 && tempX>-1){
            ChessPiece tempPiece = board[tempX][tempY];
            if(tempPiece!=null && tempPiece.getType()=="King" && this.getColor()!=tempPiece.getColor()){ return true;}
        }

        //checks right
        tempX = this.x;
        tempY = this.y+1;
        if(tempY<8){
            ChessPiece tempPiece = board[tempX][tempY];
            if(tempPiece!=null && tempPiece.getType()=="King" && this.getColor()!=tempPiece.getColor()){ return true;}
        }


        //checks up right up
        tempX = this.x+1;
        tempY = this.y +1;
        if(tempX<8 && tempY<8){
            ChessPiece tempPiece = board[tempX][tempY];
            if(tempPiece!=null && tempPiece.getType()=="King" && this.getColor()!=tempPiece.getColor()){ return true;}
        }

        //checks up
        tempY = this.y;
        tempX = this.x+1;
        if(tempX<8){
            ChessPiece tempPiece = board[tempX][tempY];
            if(tempPiece!=null && tempPiece.getType()=="King" && this.getColor()!=tempPiece.getColor()){ return true;}
        }
        return false;
    }
    /**
     * This method is used to castle in chess in which the king moves two pieces closer to either rook as long as the rook and the king has not been previously moved. Further rules of violation will be dictated by castlingCheck method. If success boolean true will be return to else false.
     * @param c either 'l' or 'r' for left or right castling
     * @param board chess board
     * @return boolean
     * @author Pedro Cruz
     */
    private boolean castling(char c, ChessPiece[][] board){

        int toX = this.x;
        int toY;
        int count  = this.y;
        if(c == 'l'){
            //left castling
            while(count>0){
                if(castlingCheck(board, count)){
                    return false;
                }
                if(board[this.x][--count]!=null){
                    if(count==0){
                        continue;
                    }
                    return false;
                }
            }
            toY = this.y - 2;
            board[toX][toY+1] = board[toX][0];
            board[toX][0] = null;
            if(this.x==7){
                //left-white castling
                board[toX][toY+1].location = "d1";
                board[toX][toY+1].x = 7;
                board[toX][toY+1].y = 3;
            }else{
                //left-black castling
                board[toX][toY+1].location = "d8";
                board[toX][toY+1].x = 0;
                board[toX][toY+1].y = 3;
            }

            board[toX][toY+1].hasMoved = true;
        }else{
            //right castling
            while(count<7){
                if(castlingCheck(board, count)){
                    return false;
                }
                if(board[this.x][++count]!=null){
                    if(count==7){
                        continue;
                    }
                    return false;
                }
            }

            toY = this.y + 2;
            board[toX][toY-1] = board[toX][toY+1];
            board[toX][toY+1] = null;
            if(this.x==7){
                //white castling
                board[toX][toY-1].location = "f1";
                board[toX][toY-1].x = 7;
                board[toX][toY-1].y = 5;
            }else{
                //black castling
                board[toX][toY-1].location = "f8";
                board[toX][toY-1].x = 0;
                board[toX][toY-1].y = 5;
            }
            board[toX][toY-1].hasMoved=true;
        }

        this.hasMoved = true;
        return true;

    }
    /**
     * This method checks whether in movement along the row space is occupied or in attacking position would signify invalid castling.
     * @param board
     * @param currColumn movement across row either left or right
     * @return boolean
     * @author Pedro Cruz
     */
    private boolean castlingCheck(ChessPiece[][] board, int currColumn){


        ChessPiece tempPiece = null;
        ChessPiece[][] tempBoard = new ChessPiece[8][8];

        //created temp board
        for(int i = 0; i<board.length; i++){
            for(int j =0; j<board.length; j++){
                tempBoard[i][j]= board[i][j];
            }
        }


        tempBoard[this.x][currColumn] = tempBoard[this.x][this.y];

        if(currColumn!=this.y){
            tempBoard[this.x][this.y]=null;
        }

        for(int i=0; i<tempBoard.length; i++){
            for(int j=0; j<tempBoard.length; j++){
                tempPiece = tempBoard[i][j];
                if(tempPiece!=null){
                    if(tempPiece.check(tempBoard)){
                        return true;
                    }
                }
            }
        }
        return false;

    }
}