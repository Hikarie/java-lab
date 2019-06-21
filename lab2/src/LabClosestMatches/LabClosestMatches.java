/**
 * 
 */
package LabClosestMatches;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.*;
import java.util.Map.Entry;

import ClosestCodeMatch.ClosestCodeMatches;
import CodeFile.CodeFile;

/**
 * @author 20171001234 xxx
 *
 *假设在 codes/lab1/目录下存在以下结构的文件组织：
*├─Java课内实习-201710001234-xxx-实习1
*│  ├─Java课内实习-20171000123-xxx-实习1
*│  │  └─lab1_code
*│  │      ├─rules
*│  │      └─turtle
*│  └─lab1_code
*│      ├─rules
*│      └─turtle
*├─Java课内实习-20171001235-xxx-实习一
*│  └─lab1
*│      └─lab1_code
*│          └─lab1_code
*│              ├─bin
*│              │  ├─rules
*│              │  └─turtle
*│              ├─rules
*│              └─turtle
*├─Java课内实习-20171001236-xxxx-实习一
*│  ├─rules
*│  └─turtle
*└─Java课内实习20171001237-xxxx-实习一
*    └─Java课内实习20171001237-xxx-实习一
*       └─Java课内实习20171001237-xxxx-实习一
*            └─lab1_code
*               ├─123
*                ├─rules
*                │  └─bin
*               └─turtle
*                    └─bin
*
*/
public class LabClosestMatches {
	
	/**
	 * 用于评价各相关目录下，指定文件的相似性。
	 * Similarity   子目录1                        子目录2
	 * 100%        Java课内实习-201710001234-xxx-实习1     Java课内实习-201710001235-xxx-实习1
	 * 89%         Java课内实习-201710001234-xxx-实习1     Java课内实习-201710001236-xxx-实习1
	 * ....
	 * @param path 作业文件所在的目录，比如这里是：codes/lab1
	 * @param fileNameMatches：用来过滤进行比较的文件名的文件名或者正则表达式.
	 * 如 "DrawableTurtle.java"，"*.java","turtle/*.java"
	 * 如果一个子目录下有多个符合条件的文件，将多个文件合并成一个文件。
	 * 
	 * @param topRate:取值范围从[0,100],输出控制的阈值
	 * 	从高往低输出高于topRate%相似列表，如
	 * @param removeComments:是否移除注释内容	
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * 	 */
	public static void closestCodes(String path, String fileNameMatches,double topRate,boolean removeComments) throws FileNotFoundException, IOException
	{
		// 计算文件间的相似性，输出
		File f = new File(path);
		File [] fl = f.listFiles();
		HashMap<Integer, ArrayList<String>> urlMap = new HashMap<Integer, ArrayList<String>>();
		for(int i=0;i<fl.length;i++) {
			ArrayList<String> t = getUrls(fl[i].getAbsolutePath(), fileNameMatches);
			urlMap.put(i, t);
		}
		
		mergeFile(urlMap);	// 合并文件
		
		ArrayList<String> urls = new ArrayList<String>();
		for(int i=0;i<fl.length;i++) {
			urls.add("src/LabClosestMatches/lab1-codes-fortest/"+String.valueOf(i));
		}
		ClosestCodeMatches ccms = new ClosestCodeMatches(urls, removeComments);
		HashMap<String, HashMap<String, Double>> freqAll = ccms.getFreq();
		
		printRes(freqAll,fl,topRate);

		System.gc();		// 强制垃圾回收
		for(int i=0;i<fl.length;i++) {
			boolean d = (new File("src/LabClosestMatches/lab1-codes-fortest/"+String.valueOf(i))).delete();
		}
		
	}
	
	private static void printRes(HashMap<String, HashMap<String, Double>> freqAll, File [] fl,double topRate) {
		System.out.println("Similarity                        子目录1                               子目录2");
		final int n = fl.length;
		for(int k=0;k<n*(n-1)/2;k++) {
			String a="",b="";
			double max=0.0;
			for(String i:freqAll.keySet()) {
				HashMap<String, Double> freq=freqAll.get(i);
				for(String j:freq.keySet()) {
					if(freq.get(j)>max) {
						max=freq.get(j);
						a=i;
						b=j;
					}
				}
			}
			if(a!=""&&b!=""&&max>topRate) {
				freqAll.get(a).remove(b);
				if(a!="")a = fl[Integer.parseInt(a.substring(a.length()-1, a.length()))].getName();
				if(b!="")b = fl[Integer.parseInt(b.substring(b.length()-1, b.length()))].getName();
				System.out.println(String.valueOf(max*100)+"%    "+a+"    "+b);
			}
		}
	}
	
	private static void mergeFile(HashMap<Integer, ArrayList<String>> urlMap) throws IOException {
		for(int i:urlMap.keySet()) {
			File outFile = new File("src/LabClosestMatches/lab1-codes-fortest/"+String.valueOf(i));
			final FileChannel outChannel = new FileOutputStream(outFile).getChannel(); 
			urlMap.get(i).forEach(x -> {
				try {
					File f = new File(x);
                    FileChannel fc = new FileInputStream(f).getChannel();   
                    ByteBuffer bb = ByteBuffer.allocate(1024 * 8);  
                    while(fc.read(bb) != -1){  
                        bb.flip();  
                        outChannel.write(bb);  
                        bb.clear();  
                    } 
                    fc.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
			outChannel.close();
		}
	}
	
	public static ArrayList<String> getUrls(String path, String fileNameMatches)throws FileNotFoundException, IOException{
		ArrayList<String> urls = new ArrayList<String>();
		File f = new File(path);
		String h = f.getName();
		if(f.isFile()) {
			if(f.getName().matches(fileNameMatches))
				urls.add(f.getAbsolutePath());
		}
		else {
			File [] fl = f.listFiles();	// 为了递归 这里得用绝对路径 所以不能用返回值是String的list()
			for(int i=0;i<fl.length;i++) {
				ArrayList<String> temp = getUrls(fl[i].getAbsolutePath(), fileNameMatches);
				urls.addAll(temp);
			}
		}
		return urls;
	}
	/**
	 * @param args
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub
		LabClosestMatches.closestCodes("src/LabClosestMatches/lab1-codes-fortest", "DrawableTurtle.java", 0, true);
    }

}
