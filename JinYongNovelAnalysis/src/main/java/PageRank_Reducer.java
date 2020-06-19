import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;



public class PageRank_Reducer extends Reducer<Text, Text, Text, Text>{
    int index = 0;
    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        String nameList = "";
        double count = 0;
        for (Text text : values){
            String t = text.toString();
            if (t.charAt(0) == '#'){//若作为该人物，则赋值nameList为#相关人物数据
                nameList = t;
            }else{//若作为相关人物，则累加关联值
                count += Double.parseDouble(t);
            }
        }
        index++;
        //输出（编号$该人物人名，总关联值#相关人物数据）
        context.write(new Text(index+"$"+key.toString()),new Text(String.valueOf(count) + nameList));
    }
}