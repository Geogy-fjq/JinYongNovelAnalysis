import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.io.Text;

import java.util.*;
import java.io.IOException;


public class LPA_Reducer extends Reducer<Text, Text, Text, Text>{

    Map<String,String> name_label_map = new HashMap<String, String>();

    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        String label = "";
        String nameList = "";
        String pr = "";
        Map<String,String> relation_name_label = new HashMap<String, String>();
        for(Text text:values){
            String str = text.toString();
            if (str.length() > 0 && str.charAt(0) == '$'){
                label = str.replace("$","");
            }else if (str.length() > 0 &&str.charAt(0) == '@'){
                pr = str.replace("@","");
            }else if (str.length() > 0 &&str.charAt(0) == '#'){
                nameList = str.replace("#","");
            }else if (str.length() > 0){
                String[] element = str.split("#");
                relation_name_label.put(element[1],element[0]);//义生  33
            }
        }

        Map<String,Float> label_pr_map = new HashMap<String, Float>();
        StringTokenizer nameList_Tokenizer = new StringTokenizer(nameList,";");
        while(nameList_Tokenizer.hasMoreTokens()){
            String[] name_pr = nameList_Tokenizer.nextToken().split(":");
            Float current_pr = Float.parseFloat(name_pr[1]);// current_pr =0.5000
            String current_label = relation_name_label.get(name_pr[0]);// current_label =程青竹
            Float label_pr;
            if ((label_pr = label_pr_map.get(current_label)) != null){
                label_pr_map.put(current_label,label_pr+current_pr);
            }else{
                label_pr_map.put(current_label,current_pr);
            }
        }


        StringTokenizer tokenizer = new StringTokenizer(nameList,";");
        float maxPr = Float.MIN_VALUE;
        List<String> maxNameList = new ArrayList<String>();
        while (tokenizer.hasMoreTokens()){
            String[] element = tokenizer.nextToken().split(":");
            float tmpPr = label_pr_map.get(relation_name_label.get(element[0]));
            if (maxPr < tmpPr){
                maxNameList.clear();
                maxPr = tmpPr;
                maxNameList.add(element[0]);
            }else if (maxPr == tmpPr){
                maxNameList.add(element[0]);
            }
        }

        Random random = new Random();
        int index = random.nextInt(maxNameList.size());
        String target_name = maxNameList.get(index);
        String target_label = relation_name_label.get(target_name);
        if (name_label_map.get(target_name) != null){
            target_label = name_label_map.get(target_name);
        }else{
            name_label_map.put(key.toString(),target_label);
        }
        if (target_label == null){
            System.out.println();
        }
        context.write(new Text(target_label + "$" + key.toString()),new Text(pr + "#" + nameList));
    }

}
