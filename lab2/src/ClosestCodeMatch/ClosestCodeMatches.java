package ClosestCodeMatch;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import CodeFile.CodeFile;

public class ClosestCodeMatches implements ClosestCode{
	
	private String res;
	
	private HashMap<String, HashMap<String, Double>> freqAll;  // for problem 4
	
	public ClosestCodeMatches(ArrayList<String> urls, boolean removeComments) throws FileNotFoundException {
		match(urls, removeComments);
	}
	
	public void match(ArrayList<String> urls, boolean removeComments) throws FileNotFoundException {
		freqAll = new HashMap<String, HashMap<String, Double>>();
		int n=urls.size();
		CodeFile[] cfs = new CodeFile[n];
		for(int i=0;i<n;i++) 
			cfs[i] = new CodeFile(urls.get(i),removeComments);
		HashMap<Integer,Double> closest = new HashMap<Integer, Double>();
		for(int i=0;i<n;i++) {
			HashMap<String,Double> temp = new HashMap<String, Double>();
			for(int j=i+1;j<n;j++) {
				double similarity = 0.0;
				similarity = cfs[i].CosSimilarity(cfs[j].getFreq());
				temp.put(urls.get(j), similarity);
				if(!closest.containsKey(i)) closest.put(i,similarity);
				else closest.put(i,similarity+closest.get(i));
				if(!closest.containsKey(j)) closest.put(j,similarity);
				else closest.put(j,similarity+closest.get(j));
			}
			if(temp.size()!=0)freqAll.put(urls.get(i), temp);
		}
		double max = 0.0;
		for(int i:closest.keySet()) {
			if(closest.get(i)>max) {
				max=closest.get(i);
				res = urls.get(i);
			}
		}
	}
	
	public HashMap<String, HashMap<String, Double>> getFreq(){
		return freqAll;
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		Scanner sc = new Scanner(System.in);
		System.out.println("How many docs would you want to compare?");
		int n = Integer.parseInt(sc.nextLine());
		ArrayList<String> urls = new ArrayList<String>();
		for(int i=0;i<n;i++) {
			System.out.println(String.valueOf(i+1)+".");
			urls.add(sc.nextLine());
		}
		
//		ArrayList<String> urls = new ArrayList<String>();
//		urls.add("src/CodeFile/data/sample1.code");
//		urls.add("src/CodeFile/data/sample2.code");
//		urls.add("src/CodeFile/CodeFile.java");
//		urls.add("src/ClosestCodeMatch/ClosestCodeMatch.java");
		ClosestCodeMatches ccms = new ClosestCodeMatches(urls, false);
		System.out.println(ccms.res + " is the closet codefile for each codefiles");
	}
}
