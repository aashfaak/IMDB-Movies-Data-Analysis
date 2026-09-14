package yearcount;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class YearMapper
        extends Mapper<LongWritable, Text, Text, IntWritable> {

    private final Text yearKey = new Text();
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

        // Need at least 4 columns
        if (fields.length < 4) {
            return;
        }

        // Column 3 = Year
        String year = fields[3].trim();

        if (year.equals("\\N") || year.equals("Unknown")
                || year.isEmpty()) {
            return;
        }

        try {
            Integer.parseInt(year);

            yearKey.set(year);
            context.write(yearKey, one);

        } catch (NumberFormatException e) {
            // Ignore invalid years
        }
    }
}