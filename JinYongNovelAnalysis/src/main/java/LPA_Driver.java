import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;


public class LPA_Driver {
    public static void main(String[] args) throws Exception {
        for (int i = 13;i < 23; i++) {
            //读取环境中的hadoop默认配置
            Configuration conf = new Configuration();
            Job job = Job.getInstance(conf);
            /*job.setJar("/home/hadoop/wc.jar");*/
            job.setJarByClass(LPA_Driver.class);
            job.setMapperClass(LPA_Mapper.class);
            job.setReducerClass(LPA_Reducer.class);
            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(Text.class);
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(Text.class);

            //指定job的输入原始文件所在目录
            //FileInputFormat.setInputPaths(job, new Path("File/output"+ (i-1) +"/part-r-00000"));
            FileInputFormat.setInputPaths(job, new Path("hdfs://172.16.29.88:9000/data/jinyong_novel/output"+ (i-1) +"/part-r-00000"));
            //指定job的输出结果所在目录
            //FileOutputFormat.setOutputPath(job, new Path("File/output" + i));
            FileOutputFormat.setOutputPath(job, new Path("hdfs://172.16.29.88:9000/data/jinyong_novel/output" + i));

            //将job中配置的相关参数，以及job所用的java类所在的jar包，提交给yarn去运行
            /*job.submit();*/
            boolean res = job.waitForCompletion(true);
        }
        System.exit(0);
    }
}
