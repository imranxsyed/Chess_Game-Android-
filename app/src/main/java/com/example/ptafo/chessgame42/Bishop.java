package com.example.ptafo.chessgame42;


/**
 *
 *
 *
 * This is a SubClass of ChessPiece which represents Bishop from the chess game.
 * It has the ability to move diagonally all the way to the end of of the Vertical or Horizontal coordinates given that the path to the
 * new coordinates is clear.
 * The Bishop replaces the instance of opposite color, If the new coordinates contains an instance of the opposite color.
 *
 *@author Imran Syed.
 *
 */
public class Bishop extends ChessPiece{

    private static final long serialVersionUID = -6246108636126852430L;
    /**
     *  Bishop constructor makes an instance of a Bishop class with the given color and x,y .
     * @param color This is the color that determines which side each instance belongs to.
     * @param x This is the Vertical coordinate in 2D array.
     * @param y This is the Horizontal coordinate in 2D arrays.
     */
    public Bishop(String color, int x , int y){


        this.color = color;
        this.x = x;
        this.y = y;
        this.type = "Bishop";

        this.name = color.equalsIgnoreCase("White")? "wB" : "bB"; // setting the name
        //this.location = column[y]+rows[Math.abs((rows.length-1)-x)]; // setting the location
        this.location = column[y]+rows[Math.abs((rows.length-1)-x)];//setting the location
    }


    /**
     * This is abstract method from  the ChessPiece SuperClass.
     * It is called by the movePiece method to determine weather the new coordinates is appropriate to move this instance to the new coordinates.
     * It makes sure the new coordinates move diagonally, and also the path to the new coordinates does not contain any other piece of either color.
     * @param board  this is the actual board that helps determine the existence of other pieces to the target(x,y) position.
     * @param x this is Vertical coordinate for the new target.
     * @param y this the Horizontal coordinate for the new target.
     * @return boolean returns true if the new coordinates are legal or else returns false.
     */
    public boolean move(ChessPiece[][] board, int x, int y) {


        int differenceX = Math.abs(this.x - x); //difference in cordinate x
        int differenceY = Math.abs(this.y- y); // difference in cordinate y

        if (differenceX != differenceY){

            System.out.println("Illegal Move, try again..");
            return false;
        }

        boolean upRight =false, upLeft = false, downRight = false, downLeft = false;

        upRight = x < this.x && y >this.y ? true : false; // going right up
        upLeft = x < this.x && y < this.y? true : false; // goint left up

        downRight =  x> this.x && y >this.y ? true : false; // going downleft
        downLeft =  x > this.x && y <this.y; // goint downright


        if (upRight){
            directionsSort condition = new directionsSort() {
                @Override
                public boolean apply(int x, int y, int fx, int fy) {
                    if(x>=fx+1 &&y<=fy-1){
                        return true;
                    }
                    return false;
                }
            };
            //directionsSort condition = (cordinatex, cordinatey, finalx, finaly) -> cordinatex >= finalx+1 && cordinatey <=finaly-1 ;

            return filter(board, this.x-1, this.y+1, "-+", condition, x, y);

        }
        else if (upLeft){



            directionsSort condition = new directionsSort() {
                @Override
                public boolean apply(int x, int y, int fx, int fy) {
                    if(x>=fx+1 && y>=fy+1){
                        return true;
                    }
                    return false;
                }
            };
            //directionsSort condition = (cordinatex, cordinatey, finalx, finaly) -> cordinatex >= finalx+1 && cordinatey >=finaly+1 ;

            return filter(board, this.x-1, this.y-1, "--", condition, x, y);

        }

        else if (downRight){

            directionsSort condition = new directionsSort() {
                @Override
                public boolean apply(int x, int y, int fx, int fy) {
                    if(x<=fx-1 && y<=fy-1){
                        return true;
                    }
                    return false;
                }
            };
            //directionsSort condition = (cordinatex, cordinatey, finalx, finaly) -> cordinatex <= finalx-1 && cordinatey <=finaly-1 ;

            return filter(board, this.x+1, this.y+1, "++", condition, x, y);

        }

        else if(downLeft){

            directionsSort condition = new directionsSort() {
                @Override
                public boolean apply(int x, int y, int fx, int fy) {
                    if(x<=fx-1 && y>=fy+1){
                        return true;
                    }
                    return false;
                }
            };
            //directionsSort condition = (cordinatex, cordinatey, finalx, finaly) -> cordinatex <= finalx-1 && cordinatey >=finaly+1 ;

            return filter(board, this.x+1, this.y-1, "+-", condition, x, y);


        }

        System.out.println("Illegal move, try again.");
        return false;

    }


    /**
     * Abstract method from  the SuperClass ChessPiece.
     * It is called by the MovePiece Method to determine weather this instance threatens the King of the opposite color after it has moved to its new coordinates,
     * and notifies the caller appropriately.
     * It checks all possible diagonal ways to check if the King of the opposite color is threatened.
     * @param board This is the actual board which helps determine weather this instance threatens  the King of opposite color.
     * @return boolean returns true if it does threatens the opposite king else returns false.
     */
    public boolean check(ChessPiece[][] board) {

        boolean upRight = false, upLeft = false , downRight = false, downLeft = false;
        int xpath,ypath;




        upLeft = this.x >0 && this.y>0 ? true: false;
        downRight = this.x< 7 && this.y <7? true : false;

        upRight = this.x >0 && this.y<7 ? true :false;
        downLeft = this.x <7 && this.y >0 ;

        if (upLeft){

            xpath = this.x-1;
            ypath = this.y-1;

            while (xpath >= 0 && ypath >= 0){

                if (board[xpath][ypath]== null){}

                else if (!board[xpath][ypath].type.equalsIgnoreCase("king")){  break;}

                else if (!board[xpath][ypath].color.equalsIgnoreCase(this.color)){ return true;}

                xpath --;
                ypath--;
            }

        }

        if (downRight){

            xpath = this.x+1;
            ypath = this.y+1;


            while (xpath <= 7 && ypath <= 7){

                if (board[xpath][ypath]== null){}

                else if (!board[xpath][ypath].type.equalsIgnoreCase("king")){ break;}

                else if (!board[xpath][ypath].color.equalsIgnoreCase(this.color)){ return  true; }

                xpath ++;
                ypath++;
            }

        }

        if (upRight){

            xpath = this.x-1;
            ypath = this.y+1;

            while(xpath >=0 && ypath <=7){

                if (board[xpath][ypath]== null){}

                else if (!board[xpath][ypath].type.equalsIgnoreCase("king")){  break;}

                else if (!board[xpath][ypath].color.equalsIgnoreCase(this.color)){ return true;}

                xpath --;
                ypath++;
            }

        }

        if (downLeft){


            xpath = this.x+1;
            ypath = this.y-1;

            while(xpath <=7 && ypath >=0){

                if (board[xpath][ypath]== null){}

                else if (!board[xpath][ypath].type.equalsIgnoreCase("king")){  break;}

                else if (!board[xpath][ypath].color.equalsIgnoreCase(this.color)){ return true;}

                xpath ++;
                ypath--;

            }
        }

        return false;

    }


    private boolean filter(ChessPiece[][] board, int pathX , int pathY , String sings, directionsSort function, int x, int y){





        while (function.apply(pathX, pathY, x, y)){


            if (board[pathX][pathY] !=null){

                return false;
            }
            pathX = sings.charAt(0) == '+'? pathX +1 : pathX-1;
            pathY = sings.charAt(1) == '+'? pathY+1 : pathY-1;
        }

        return  (board[x][y] == null)
                || (board[x][y]!= null && (!board[x][y].color.equalsIgnoreCase(this.color)));
    }
}

interface directionsSort{


    boolean apply(int x , int y, int fx , int fy);
}