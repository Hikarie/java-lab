package ClosestCodeMatch;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import CodeFile.CodeFile;

public class ClosestCodeMatch implements ClosestCode{
	
	private double max;
	private String a,b;
	
	private ClosestCodeMatch(ArrayList<String> urls,boolean removeComments) throws FileNotFoundException {
		match(urls, removeComments);
	}
	
	public void match(ArrayList<String> urls, boolean removeComments) throws FileNotFoundException {
		int n=urls.size();
		CodeFile[] cfs = new CodeFile[n];
		for(int i=0;i<n;i++) 
			cfs[i] = new CodeFile(urls.get(i), removeComments);
		max=0.0;
		for(int i=0;i<n;i++) {
			for(int j=i+1;j<n;j++) {
				double tmp = 0.0;
				tmp = cfs[i].CosSimilarity(cfs[j].getFreq());
				if(tmp>max) {
					max = tmp;
					a = urls.get(i);
					b = urls.get(j);
				}
			}
		}
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
		
		ClosestCodeMatch ccm = new ClosestCodeMatch(urls, false);
		System.out.println(ccm.a+" and "+ccm.b+" are the closet code match, which similarity is "+String.valueOf(ccm.max));
		
		
		
	}
}
