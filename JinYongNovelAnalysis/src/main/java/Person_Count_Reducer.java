import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;


public class Person_Count_Reducer extends Reducer<Text, IntWritable, Text, IntWritable>{
    protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        //对人物出现的关联关系（出现在同一段落以及在一个段落中多次出现）的次数进行统计
        int count = 0;
        for (IntWritable value:values) {
            count = count + value.get();
        }
        //返回人物名称对以及出现的次数
        context.write(key, new IntWritable(count));
    }
}
