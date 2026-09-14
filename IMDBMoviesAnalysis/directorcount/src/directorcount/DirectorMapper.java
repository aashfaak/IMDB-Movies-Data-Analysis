package directorcount;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class DirectorMapper
        extends Mapper<LongWritable, Text, Text, IntWritable> {

    private final Text directorKey = new Text();
    private final IntWritable one = new IntWritable(1);

    @Override
    public void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {

        String line = value.toString();

        // Skip header
        if (line.startsWith("Title_ID")) {
            return;
        }

        String[] fields = line.split("\t", -1);

        // Need at least 10 columns
        if (fields.length < 10) {
            return;
        }

        // Column 9 = Director_Name
        String director = fields[9].trim();

        // Ignore unknown directors
        if (director.isEmpty()
                || director.equals("Unknown")
                || director.equals("\\N")) {
            return;
        }

        directorKey.set(director);

        context.write(directorKey, one);
    }
}