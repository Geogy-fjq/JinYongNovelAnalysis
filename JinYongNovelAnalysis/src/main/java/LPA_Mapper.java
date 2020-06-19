import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.StringTokenizer;


public class LPA_Mapper extends Mapper<LongWritable, Text, Text, Text>{

    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();
        int index_t = line.indexOf("\t");
        int index_j = line.indexOf("#");
        int index_dollar = line.indexOf("$");
        String PR = line.substring(index_t+1,index_j);
        String name = line.substring(index_dollar+1,index_t);
        String nameList = line.split("#")[1];
        String label = line.substring(0,index_dollar);
        StringTokenizer tokenizer = new StringTokenizer(nameList,";");
        while(tokenizer.hasMoreTokens()){
            String[] element = tokenizer.nextToken().split(":");
            context.write(new Text(element[0]),new Text(label+"#"+name));
        }
        context.write(new Text(name),new Text("#"+nameList));
        context.write(new Text(name),new Text("$"+label));
        context.write(new Text(name),new Text("@"+PR));
    }
}
