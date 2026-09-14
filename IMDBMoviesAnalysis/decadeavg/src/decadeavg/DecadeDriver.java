package decadeavg;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class DecadeDriver {

    public static void main(String[] args) throws Exception {

        if (args.length != 2) {
            System.err.println(
                "Usage: DecadeDriver <input> <output>"
            );
            System.exit(-1);
        }

        Configuration conf = new Configuration();

        Job job = Job.getInstance(
            conf,
            "Decade Wise Average Rating"
        );

        job.setJarByClass(DecadeDriver.class);

        // Mapper
        job.setMapperClass(DecadeMapper.class);

        // Reducer
        job.setReducerClass(DecadeReducer.class);

        // Mapper output
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(DoubleWritable.class);

        // Final output
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(DoubleWritable.class);

        // Input
        FileInputFormat.addInputPath(
            job,
            new Path(args[0])
        );

        // Output
        FileOutputFormat.setOutputPath(
            job,
            new Path(args[1])
        );

        System.exit(
            job.waitForCompletion(true) ? 0 : 1
        );
    }
}