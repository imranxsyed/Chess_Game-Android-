package com.example.ptafo.chessgame42;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;



public class RecordMoves implements Serializable {

    private static final long serialVersionUID = -1059891522649776112L;
    private String fileName;
    private Calendar date;
    private String strnDate;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy\nHH:mm:ss");
    private ArrayList<Move> moves;
    int ptr;

    public RecordMoves(){
        moves = new ArrayList<Move>();
        setFileName(new String());
        setDate(this.date);
        strnDate = sdf.format(date.getTime());
        ptr = 0;
    }

    public boolean hasNext(){
        if(ptr==moves.size()){
            ptr--;
            return false;
        }
        return true;
    }

    public String toString(){
        int count = 1;
        StringBuffer result = new StringBuffer();
        result.append("Moves: ");
        for(Move move:moves){
            result.append(count+"("+move.fromIndex+"-"+move.toIndex+") ->");


        }

        return result.toString();
    }
    public void setFileName(String strn){
        this.fileName = strn;
    }
    public String getStrnDate(){
        return this.strnDate;
    }

    public Calendar getCalDate(){
        return this.date;
    }

    private void setDate(Calendar date){
        this.date = Calendar.getInstance();
    }

    public String getFileName(){
        return fileName;
    }


    public ArrayList<Move> getMoves(){return moves;}
    public void addMove(Move newMove){
        moves.add(newMove);
    }

    public void removeLastMove(){
        int size = moves.size()-1;
        moves.remove(size);
    }

    public Move getNextMove(){
        Move nextMove = moves.get(ptr);
        ptr++;
        return nextMove;
    }

    public static Comparator<RecordMoves> compareByDate = new Comparator<RecordMoves>() {
        @Override
        public int compare(RecordMoves o1, RecordMoves o2) {
            Calendar date1 = o1.getCalDate();
            Calendar date2 = o2.getCalDate();
            return date1.compareTo(date2);
        }
    };

    public static Comparator<RecordMoves> compareByName = new Comparator<RecordMoves>() {
        @Override
        public int compare(RecordMoves o1, RecordMoves o2) {
            String name1 = o1.getFileName();
            String name2 = o2.getFileName();
            return name1.compareTo(name2);
        }
    };

}
