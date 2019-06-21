package CodeFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class CodeFile {
    private String url;
    private HashMap<String,Integer> freq;
    
    
    public CodeFile(String url, Boolean removeComments) throws FileNotFoundException {
        this.url = url;
        File f = new File(url);
        Scanner sc = new Scanner(f);
        if(removeComments==false)freq = init(sc);
        else freq = initWithoutComments(sc);
    }
    
    public HashMap<String,Integer> getFreq() {
        return freq;
    }
    
    private HashMap<String,Integer> init(Scanner sc){
        HashMap<String,Integer> freq = new HashMap<String,Integer>();
        while(sc.hasNext()) {
            String key = sc.next();
            if(freq.containsKey(key)) {
                freq.put(key, freq.get(key)+1);
            }
            else
                freq.put(key,1);
        }
        return freq;
    }
    
    private HashMap<String,Integer> initWithoutComments(Scanner sc){
        HashMap<String,Integer> freq = new HashMap<String,Integer>();
        final int NORMAL = 0, ALERT = 1, LINE = 2, BLOCK = 3;
        int status = NORMAL;
        StringBuilder code = new StringBuilder();
        while(sc.hasNext()) {
            String line = sc.nextLine();
            char[] chs = line.toCharArray();
            for(int i=0;i<chs.length;i++) {
                char c = chs[i];
                switch(status){
                    case NORMAL:
                        code.append(c);
                        if(c=='/') status = ALERT;
                        break;
                    case ALERT:
                        if(c=='/') status = LINE;
                        else if(c=='*') status = BLOCK;
                        else {
                            status = NORMAL;
                            code.append(c);
                        }
                        break;
                    case LINE:
                        if(c=='\n') status = NORMAL;
                        break;
                    case BLOCK:
                        if(c=='*'&&i+1 < chs.length) {
                            if(chs[i+1]=='/')
                                status = NORMAL;
                        }
                        break;
                }
            }
        }
        Scanner s = new Scanner(code.toString());
        freq = init(s);
        return freq;
    }
    
    private CodeFile() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the first string:");
        String []a = sc.nextLine().split(" ");
        freq = new HashMap<String,Integer>();
        for(int i=0;i<a.length;i++) {
            String key = a[i];
            if(freq.containsKey(key)) {
                freq.put(key, freq.get(key)+1);
            }
            else
                freq.put(key,1);
        }
    }
    
    public double CosSimilarity(HashMap<String,Integer> f) {
        double nume = 0.0, temp1=0.0, temp2=0.0;
        double i = 0.0,j = 0.0;
        
        for(String id:freq.keySet()) {
            i = freq.get(id);
            temp1 += Math.pow(i, 2);
            if(f.containsKey(id))
                nume += i*f.get(id);
        }
        
        for(String id:f.keySet()) {
            j = f.get(id);
            temp2 += Math.pow(j, 2);
        }
        
        double deno = Math.sqrt(temp1)*Math.sqrt(temp2);
        double res = nume/deno;
        return res;
    }
    
    public static void main(String[] args) throws FileNotFoundException {
        CodeFile cf1 = new CodeFile("src/CodeFile/data/sample1.code",false);
        CodeFile cf2 = new CodeFile("src/CodeFile/data/sample2.code",false);
        System.out.print(cf1.CosSimilarity(cf2.freq));
    }
}
