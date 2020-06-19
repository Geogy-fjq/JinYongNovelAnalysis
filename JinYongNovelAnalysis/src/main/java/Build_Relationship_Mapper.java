import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;


public class Build_Relationship_Mapper extends Mapper<LongWritable, Text, Text, Text>{
    protected void map(LongWritable key, Text values, Context context) throws IOException, InterruptedException {
        String line = values.toString();
        //按照“，”号分割成数组（其中第一个元素是第一个人物名称，第二个是第二个人物名称并包含出现的次数，需要在reducer中进一步处理）
        String[] kv = line.split(",");
        //将两个元素传入reducer中，同一个人物的所有有关人物会交给同一个reducer处理
        context.write(new Text(kv[0]), new Text(kv[1]));
    }
}
