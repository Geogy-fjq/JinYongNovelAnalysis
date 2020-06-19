import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;


public class PageRank_Mapper extends Mapper<LongWritable, Text, Text, Text>{
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        //获取输入数据的有效部分
        String line = value.toString();
        int index_dollar = line.indexOf("$");
        if (index_dollar != -1){
            line = line.substring(index_dollar+1);
        }
        //分割数据的有效部分
        int index_t = line.indexOf("\t");
        int index_j = line.indexOf("#");
        double PR = Double.parseDouble(line.substring(index_t+1,index_j));//获取PR
        String name = line.substring(0,index_t);//获取该人物的姓名
        String names = line.substring(index_j+1);//获取该人物的相关人物数据
        //分割该人物的相关人物数据
        for (String name_value:names.split(";")){
            String[] nv = name_value.split(":");
            double relation = Double.parseDouble(nv[1]);//获取相关人物的关系值
            double cal = PR * relation;
            //发送相关人物数据到reduce（相关人物姓名，关联值）
            context.write(new Text(nv[0]),new Text(String.valueOf(cal)));
        }
        //发送该人物数据到reduce（该人物姓名，#相关人物数据）
        context.write(new Text(name),new Text("#"+line.substring(index_j+1)));
    }

}
