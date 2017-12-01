package com.example.ptafo.chessgame42;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class priorityQueue {
	
	
	ArrayList<ChessPiece> list = new ArrayList<ChessPiece>();
	List<String> arr =  Arrays.asList("king", "queen","bishop","knight","rook", "pawn");
	
	
	public void enqueue(ChessPiece input){
		
		
		if (list.size()<1){
			
			list.add(input);
			return;
		}
		
		String inputType = input.type.toLowerCase();
		int length = list.size();
		
		for (int i=0 ; i<length; i++){
			
			if (arr.indexOf(inputType)< arr.indexOf(list.get(i).type.toLowerCase())){
				
				list.add(i, input);
				return;
			}
			
		}
		list.add(input);
		
	}
	
	public ChessPiece dequeue(){ // takes out the most prior one
		
		if (list.size()<0){
			
			return null;
		}
		
		return list.remove(0);
	}
	
	public ChessPiece dequeue(ChessPiece input){ // dequeue the desired one
		
		if (list.size()<1){
			
			return null;
		}
		int length = list.size();
		for (int i=0; i<length; i++){
			
			if (list.get(i).type.equalsIgnoreCase(input.type)
					&& list.get(i).getColor().equalsIgnoreCase(input.getColor())){
				
				return list.remove(i);
			}
		}
		
		return null;
	}
	
	public void printQueue(){
		
		int length = list.size();
		
		for (int i=0 ;i< length; i++){
			
			System.out.println(list.get(i).type);
		}
	}
	


}