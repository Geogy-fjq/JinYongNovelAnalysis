import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Build_Relationship_Reducer extends Reducer<Text, Text, Text, NullWritable>{
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        double count = 0;
        //定义StringBuilder对象用于输出对象的存储
        StringBuilder stringBuilder = new StringBuilder();
        //用于存储value
        List<String> list = new ArrayList<String>();
        for (Text value:values) {
            //存储用于计算归一化（计算出现频率）
            list.add(value.toString());
            //将第二个人物名称与出现的次数进行分割
            String[] numValue = value.toString().split("\\s+");
            //计算所有人物出现的总次数
            count += Integer.parseInt(numValue[1]);
        }
        //归一化计算
        for (String text:list) {
            String[] numValue = text.split("\\s+");
            //获得对应人物出现的次数
            double number = Integer.parseInt(numValue[1]);
            //计算出现频率
            double scale = number/count;
            //记录各个结果
            stringBuilder.append(numValue[0] + ":" + String.format("%.4f", scale) + ";");
        }
        //在头部加入对应人物信息
        stringBuilder.insert(0, key.toString() + "\t" + "0.1#");
        String res = stringBuilder.toString().substring(0, stringBuilder.length() - 1);
        context.write(new Text(res), NullWritable.get());
    }
}
