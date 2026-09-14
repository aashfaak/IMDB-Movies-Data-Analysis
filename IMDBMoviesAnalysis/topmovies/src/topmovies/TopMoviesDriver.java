package topmovies;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class TopMoviesDriver {

    public static void main(String[] args) throws Exception {

        if (args.length != 2) {

            System.err.println(
                "Usage: TopMoviesDriver <input> <output>"
            );

            System.exit(-1);
        }

        Configuration conf = new Configuration();

        Job job = Job.getInstance(
            conf,
            "Top 10 Highest Rated Titles"
        );

        job.setJarByClass(TopMoviesDriver.class);

        // Mapper
        job.setMapperClass(TopMoviesMapper.class);

        // Reducer
        job.setReducerClass(TopMoviesReducer.class);

        // Mapper output
        job.setMapOutputKeyClass(DoubleWritable.class);
        job.setMapOutputValueClass(Text.class);

        // Final output
        job.setOutputKeyClass(DoubleWritable.class);
        job.setOutputValueClass(Text.class);

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