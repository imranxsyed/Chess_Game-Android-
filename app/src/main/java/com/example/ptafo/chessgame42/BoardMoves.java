package com.example.ptafo.chessgame42;

import java.io.Serializable;



public class BoardMoves implements Serializable{
    private static final long serialVersionUID = -1048528699913550091L;

    private MoveNode head ;
    private MoveNode tail = new MoveNode(null);
    private int length = 0;
    public int pointer = 0;

    public BoardMoves(){
        resetLL();
    }

    private void resetLL(){
        head = new MoveNode(null);
        head.setPrev(null);
        head.setNext(tail);
        tail.setPrev(head);
        tail.setNext(null);
    }

    public MoveNode get(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index > length) {
            throw new IndexOutOfBoundsException();
        } else {
            MoveNode cursor = head;
            for (int i = 0; i < index; i++) {
                cursor = cursor.getNext();
            }
            return cursor;
        }
    }

    public void setPoiter(int index){
        this.pointer = index+1;
    }


    public Board getNext(){
        if(pointer==1){
            pointer++;
        }
        if(pointer<=length){
            MoveNode result = get(pointer);
            pointer++;
            return result.getBoard();
        }
        return null;
    }

    public Board getPrev(){
        if(length>0){
            if((pointer-1)==length){
                pointer-=2;
            }
            else{
                pointer--;
            }
            MoveNode result = get(pointer);
            return result.getBoard();
        }

        return null;
    }

    public Board remove(int index) throws IndexOutOfBoundsException {
        if (index == 0) {
            throw new IndexOutOfBoundsException();
        } else {
            MoveNode result = get(index);
            result.getNext().setPrev(result.getPrev());
            result.getPrev().setNext(result.getNext());
            length--;
            return result.getBoard();
        }
    }

    public Board undo(){

        MoveNode prevMove;
        if (length == 1) {
            prevMove = head.getNext();
            resetLL();
            length--;
            return prevMove.getBoard();
        } else {
            return remove(length);
        }
    }

    public void add(int index, Board board) throws IndexOutOfBoundsException {
        MoveNode cursor = get(index);
        MoveNode temp = new MoveNode(board);
        temp.setPrev(cursor);
        temp.setNext(cursor.getNext());
        cursor.getNext().setPrev(temp);
        cursor.setNext(temp);
        length++;
    }

    public void addHead(Board board) {
        MoveNode cursor = head;
        MoveNode temp = new MoveNode(board);
        temp.setPrev(cursor);
        temp.setNext(cursor.getNext());
        cursor.getNext().setPrev(temp);
        cursor.setNext(temp);
        length++;
    }

    public void addTail(Board board) {
        MoveNode cursor = tail.getPrev();
        MoveNode temp = new MoveNode(board);
        temp.setPrev(cursor);
        temp.setNext(cursor.getNext());
        cursor.getNext().setPrev(temp);
        cursor.setNext(temp);
        length++;
    }

    public int size() {
        return length;
    }

    public boolean isEmpty() {
        return length == 0;
    }

    public String toString() {
        StringBuffer result = new StringBuffer();
        result.append("(head) - ");
        MoveNode temp = head;
        while (temp.getNext() != tail) {
            temp = temp.getNext();
            result.append("Move Number: "+ temp.getBoard().moveNumber);
            result.append(" -> ");
        }
        result.append("(tail)");
        return result.toString();
    }

    public boolean hasNext() {
        if(pointer<=length){
            MoveNode result = get(pointer);
            if(result!=null)
                return true;
        }
        return false;
    }

    public boolean hasPrev() {
        int prevPtr = pointer - 1;
        if(prevPtr>=1){
            MoveNode result = get(prevPtr);
            if(result!=null)
                return true;
        }

        return false;
    }

    public Board initialize() {
        setPoiter(0);
        MoveNode result = get(pointer);
        pointer++;
        return result.getBoard();
    }

}

class MoveNode implements Serializable {

    private static final long serialVersionUID = 2409829250935414059L;
    private Board board ;
    private MoveNode prev;
    private MoveNode next;

    MoveNode(Board board) {
        this.board = board;
    }

    MoveNode(Board board, MoveNode prev, MoveNode next) {
        this.board = board;
        setPrev(prev);
        setNext(next);
    }

    void setPrev(MoveNode prev) {
        this.prev = prev;
    }

    void setNext(MoveNode next) {
        this.next = next;
    }

    MoveNode getPrev() {
        return prev;
    }

    MoveNode getNext() {
        return next;
    }

    Board getBoard() {
        return board;
    }



}
