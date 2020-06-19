package Tools;

import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GephiLabel {
    @Test
    public void format() throws IOException {
        BufferedReader bf = new BufferedReader(new FileReader("File/output22/part-r-00000"));
        String line = "";
        Map<String,String> label_mapped = new HashMap<>();
        int index = 1;
        List<String> arrayList = new ArrayList<>();
        while ((line = bf.readLine()) != null){
            int index_dollar = line.indexOf("$");
            String label = line.substring(0,index_dollar);
            String res_string = "";
            String target_label;
            if ((target_label = label_mapped.get(label)) != null){
                res_string = line.replace(label,target_label);
            }else {
                label_mapped.put(label,String.valueOf(index));
                res_string = line.replace(label,String.valueOf(index));
                index++;
            }
            arrayList.add(res_string);
        }
        bf.close();
        arrayList.sort((o1, o2) -> {
            int index_dollar1 = o1.indexOf("$");
            int index_dollar2 = o2.indexOf("$");
            int label1 = Integer.parseInt(o1.substring(0,index_dollar1));
            int label2 = Integer.parseInt(o2.substring(0,index_dollar2));
            return label1 - label2;
        });
        BufferedWriter bw = new BufferedWriter(new FileWriter("last_result.txt"));
        for (String str : arrayList){
            int index_t = str.indexOf("\t");
            bw.write(str);
            bw.newLine();
        }
        bw.flush();
        bw.close();
    }
}

