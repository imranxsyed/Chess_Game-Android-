package com.example.ptafo.chessgame42;



public class pawn extends ChessPiece {


    private static final long serialVersionUID = 6239180037777798806L;
    /**
     *  Pawn constructor makes an instance of a Pawn class with the given color and x,y coordinates.
     * @param color This is the color that determines which side each instance belongs to.
     * @param x This is the Vertical coordinate in 2D array.
     * @param y This is the Horizontal coordinate in 2D arrays.
     */
    public pawn(String color, int x, int y){
        this.type = "Pawn";
        this.color = color;
        this.x = x;
        this.y = y;

        name = color.equals("white")? "wP": "bP";
        location = color.equals("white")? column[y]+2 : column[y]+ 7;


    }



    /**
     * This is a abstract method from ChessPiece SuperClass.
     * It is called by the movePiece method to determine weather the new coordinates is appropriate to replace this instance to the new position.
     * It checks weather the new coordinates requires the piece to move diagonally, one square ahead, or two squares ahead and notifies the caller
     * appropriately.
     * @param board  this is the actual board that helps determine the existence of other pieces to the target(x,y) position.
     * @param x this is Vertical coordinate for the new target.
     * @param y this the Horizontal coordinate for the new target.
     * @return boolean returns true if the new coordinates are legal or else returns false.
     */
    public boolean move(ChessPiece[][] board, int x, int y) {
        if(this.getColor().compareToIgnoreCase("white")==0){
            if((this.x - x)<1){
                return false;
            }
        }else{
            if((this.x - x)>-1){
                return false;
            }
        }
        boolean oneStepAhead,twoStepAhead, diagonalStep;
        oneStepAhead = false; twoStepAhead = false; diagonalStep = false;
        int cordinateX = Math.abs(this.x - x); // new cooridates for x
        int codinateY = Math.abs(this.y- y);	//new coorditaes for y

        if ( codinateY == 0){

            switch(cordinateX){

                case 1: oneStepAhead = true; break;
                case 2: twoStepAhead = true; break;

            }

        }
        else if (codinateY == 1 && cordinateX == 1){

            diagonalStep = true;

        }else{ // meaning no legal cordintes are found



            return false;
        }

        //checked so far which of three kinds will be using in this step

        boolean output = false;




        if (oneStepAhead){ // if the coordiates matches with one step ahead

            output = board[x][y] == null ; //checking if the square ahead of it is empty

            this.promotion =  (this.color.equalsIgnoreCase("black") && x== 7);  // checking if its black and reaches all the to the white territoyr

            if (!this.promotion){

                this.promotion =  (this.color.equalsIgnoreCase("white") && x == 0); // checking if its white and reaches all the to the black territory
            }
        }



        else if (twoStepAhead){ // if the coordiates matches with two step ahead

            output = (this.hasMoved == false) &&	 (board[x][y]== null); //all condition matches
            el_pessant = true;

        }



        else if (diagonalStep){


            if ((this.color.equalsIgnoreCase("black")&& this.x == 4)|| (this.color.equalsIgnoreCase("white")&& this.x == 3)){//check for el_pessant

                output = ( board[x][y] ==null ) && ((board[this.x][y] != null)&& (board[this.x][y].type.equalsIgnoreCase("Pawn")
                        && (!board[this.x][y].color.equalsIgnoreCase(this.color))&& (board[this.x][y].el_pessant== true)) );

                if (output){

                    board[this.x][y] = null; // el_passant removing that manually since the movePiece method will not do it for you
                }

            } if(!output){ //if el_pessant is not true

                output = (board[x][y] !=null ) && (!(board[x][y].color.equalsIgnoreCase(this.color)));
            }
            this.promotion =  (this.color.equalsIgnoreCase("black") && x == 7);  // checking if its black and reaches all the to the white territoyr

            if (!this.promotion){

                this.promotion =  (this.color.equalsIgnoreCase("white") && x == 0); // checking if its white and reaches all the to the black territory

            }

        }



        if (output){ // if it will move to the new location, we need to do the follwing

            this.hasMoved = true; // make sure we make its first moved to true

            if(this.promotion){

                //ChessPiece promPiece =  new Queen(this.color, x,y);  // initially making it a queen


                if (this.prom == null){

                    board[this.x][this.y]= new Queen(this.color,x,y);
                    return output;
                }

                prom = prom.toLowerCase();
                switch(prom){

                    case "n" :board[this.x][this.y]=( new Knight(this.color, x,y)); break;

                    case "b" : board[this.x][this.y]= (new Bishop(this.color, x,y)); break;

                    case "r" :  board[this.x][this.y]=( new Rook(this.color, x,y)); break;

                    default :  board[this.x][this.y]= new Queen(this.color,x,y); break;
                }


            }

        }


        return output;
    }



    /**
     * Abstract method from SuperClass ChessPiece.
     * It is called by the MovePiece Method to determine weather this instance threatens the King of the opposite color after it has moved to its new coordinates,
     * and notifies the caller appropriately.
     * It checks all possible diagonal ways satisfying the out of restriction for 2D arrays.
     * @param board This is the actual board which helps determine weather this instance threatens the King of opposite color.
     * @return boolean returns true if it does threaten the opposite king or else returns false.
     */
    public boolean check(ChessPiece[][] board) {


        int xCordinate = this.x;
        int yCordinate = this.y;

        if (yCordinate <7 ) {

            /**
             * checking if the posiible target for king is not null
             * it is king
             * it is the opposite color
             */

            if (this.color.equalsIgnoreCase("White") && xCordinate >0){ // checking for white side

                if (board[xCordinate -1][yCordinate +1] != null && board[xCordinate -1][yCordinate +1].type.equalsIgnoreCase("King") &&
                        (!board[xCordinate -1][yCordinate +1].color.equalsIgnoreCase(this.color))){

                    return true;
                }

            }else if (this.color.equalsIgnoreCase("black")&& xCordinate <7){ // checking for black side

                if (board[xCordinate +1][yCordinate +1] != null && board[xCordinate +1][yCordinate +1].type.equalsIgnoreCase("King") &&
                        (!board[xCordinate +1][yCordinate +1].color.equalsIgnoreCase(this.color))){

                    return true;
                }

            }
        }


        if (yCordinate > 0) {

            /**
             * checking if the posiible target for king is not null
             * it is king
             * it is the opposite color
             */


            if (this.color.equalsIgnoreCase("White") && xCordinate <0){


                if (board[xCordinate -1][yCordinate -1] != null && board[xCordinate -1][yCordinate -1].type.equalsIgnoreCase("King") &&
                        (!board[xCordinate -1][yCordinate -1].color.equalsIgnoreCase(this.color))){

                    return true;

                }
            }
            else if (this.color.equalsIgnoreCase("black")&& xCordinate <7){

                if (board[xCordinate +1][yCordinate -1] != null && board[xCordinate +1][yCordinate -1].type.equalsIgnoreCase("King") &&
                        (!board[xCordinate +1][yCordinate -1].color.equalsIgnoreCase(this.color))){

                    return true;
                }

            }

        }
        return false;

    }
}

