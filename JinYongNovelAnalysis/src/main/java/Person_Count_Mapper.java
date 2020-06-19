import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public class Person_Count_Mapper extends Mapper<LongWritable, Text, Text, IntWritable>{

    protected void map(LongWritable key, Text values, Context context) throws IOException, InterruptedException {
        //使用哈希表存储人物名字
        Set<String> lineName = new HashSet<String>();
        //将preprocess中获取的结果进行拆解，得到相关人物名字
        String line = values.toString();
        String[] names = line.split(" ");
        //将拆解得到的人名存入哈希表
        lineName.addAll(Arrays.asList(names));
        //遍历哈希表，对所有出现在同一段落的人物进行配对
        for (String firstName:lineName) {
            for (String secondName:lineName) {
                if (firstName.equals(secondName)){
                    continue;
                }else {
                    //将结果已逗号分隔传入reducer
                    context.write(new Text(firstName + "," + secondName), new IntWritable(1));
                }
            }
        }
    }
}
