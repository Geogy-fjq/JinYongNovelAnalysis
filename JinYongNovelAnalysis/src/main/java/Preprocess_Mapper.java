import java.io.*;
import java.util.*;

import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.library.DicLibrary;
import org.ansj.splitWord.analysis.DicAnalysis;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class Preprocess_Mapper extends Mapper<LongWritable, Text, Text, IntWritable> {
    //使用哈希表存储小说人物名字
    Set<String> name = new HashSet<String>();
    @Override
    protected void setup(Context context) throws IOException {
        //获取金庸小说中所有出现过的人物的名字（使用ansj_seg的自定义词典功能）
        //File file = new File("File/nameFile/jinyong_all_person.txt");
        File file = new File("hdfs://172.16.29.88:9000/data/jinyong_novel/nameFile/jinyong_all_person.txt");
        FileReader fileReader = new FileReader(file);
        BufferedReader br = new BufferedReader(fileReader);
        String line = br.readLine();
        //将人物名称存入哈希表
        while (line != null){
            DicLibrary.insert(DicLibrary.DEFAULT, line);
            name.add(line);
            line = br.readLine();
        }
    }
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

        // 将maptask传给我们的文本内容先转换成String
        String line = value.toString();
        //使用ansj_seg中的分析功能，对小说段落进行分析提取出现的人物名字
        Result result = DicAnalysis.parse(line);
        //使用列表存储提出出来的人物名字
        List<Term> term = result.getTerms();
        //定义StringBuilder对象进行每一行结果的记录
        StringBuilder stringBuilder = new StringBuilder();
        //将每一个段落出现的人名写入一行中
        for (Term terms : term) {
            if (name.contains(terms.getName())) {
                stringBuilder.append(terms.getName() + " ");
            }
        }
        //如果该行非空，将值传入reducer中
        String res =stringBuilder.length() > 0? stringBuilder.toString().substring(0,stringBuilder.length()-1):"";
        context.write(new Text(res),new IntWritable(1));
    }

}