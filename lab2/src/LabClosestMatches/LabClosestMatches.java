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
 *������ codes/lab1/Ŀ¼�´������½ṹ���ļ���֯��
*����Java����ʵϰ-201710001234-xxx-ʵϰ1
*��  ����Java����ʵϰ-20171000123-xxx-ʵϰ1
*��  ��  ����lab1_code
*��  ��      ����rules
*��  ��      ����turtle
*��  ����lab1_code
*��      ����rules
*��      ����turtle
*����Java����ʵϰ-20171001235-xxx-ʵϰһ
*��  ����lab1
*��      ����lab1_code
*��          ����lab1_code
*��              ����bin
*��              ��  ����rules
*��              ��  ����turtle
*��              ����rules
*��              ����turtle
*����Java����ʵϰ-20171001236-xxxx-ʵϰһ
*��  ����rules
*��  ����turtle
*����Java����ʵϰ20171001237-xxxx-ʵϰһ
*    ����Java����ʵϰ20171001237-xxx-ʵϰһ
*       ����Java����ʵϰ20171001237-xxxx-ʵϰһ
*            ����lab1_code
*               ����123
*                ����rules
*                ��  ����bin
*               ����turtle
*                    ����bin
*
*/
public class LabClosestMatches {
	
	/**
	 * �������۸����Ŀ¼�£�ָ���ļ��������ԡ�
	 * Similarity   ��Ŀ¼1                        ��Ŀ¼2
	 * 100%        Java����ʵϰ-201710001234-xxx-ʵϰ1     Java����ʵϰ-201710001235-xxx-ʵϰ1
	 * 89%         Java����ʵϰ-201710001234-xxx-ʵϰ1     Java����ʵϰ-201710001236-xxx-ʵϰ1
	 * ....
	 * @param path ��ҵ�ļ����ڵ�Ŀ¼�����������ǣ�codes/lab1
	 * @param fileNameMatches���������˽��бȽϵ��ļ������ļ�������������ʽ.
	 * �� "DrawableTurtle.java"��"*.java","turtle/*.java"
	 * ���һ����Ŀ¼���ж�������������ļ���������ļ��ϲ���һ���ļ���
	 * 
	 * @param topRate:ȡֵ��Χ��[0,100],������Ƶ���ֵ
	 * 	�Ӹ������������topRate%�����б���
	 * @param removeComments:�Ƿ��Ƴ�ע������	
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * 	 */
	public static void closestCodes(String path, String fileNameMatches,double topRate,boolean removeComments) throws FileNotFoundException, IOException
	{
		// �����ļ���������ԣ����
		File f = new File(path);
		File [] fl = f.listFiles();
		HashMap<Integer, ArrayList<String>> urlMap = new HashMap<Integer, ArrayList<String>>();
		for(int i=0;i<fl.length;i++) {
			ArrayList<String> t = getUrls(fl[i].getAbsolutePath(), fileNameMatches);
			urlMap.put(i, t);
		}
		
		mergeFile(urlMap);	// �ϲ��ļ�
		
		ArrayList<String> urls = new ArrayList<String>();
		for(int i=0;i<fl.length;i++) {
			urls.add("src/LabClosestMatches/lab1-codes-fortest/"+String.valueOf(i));
		}
		ClosestCodeMatches ccms = new ClosestCodeMatches(urls, removeComments);
		HashMap<String, HashMap<String, Double>> freqAll = ccms.getFreq();
		
		printRes(freqAll,fl,topRate);

		System.gc();		// ǿ����������
		for(int i=0;i<fl.length;i++) {
			boolean d = (new File("src/LabClosestMatches/lab1-codes-fortest/"+String.valueOf(i))).delete();
		}
		
	}
	
	private static void printRes(HashMap<String, HashMap<String, Double>> freqAll, File [] fl,double topRate) {
		System.out.println("Similarity                        ��Ŀ¼1                               ��Ŀ¼2");
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
			File [] fl = f.listFiles();	// Ϊ�˵ݹ� ������þ���·�� ���Բ����÷���ֵ��String��list()
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
