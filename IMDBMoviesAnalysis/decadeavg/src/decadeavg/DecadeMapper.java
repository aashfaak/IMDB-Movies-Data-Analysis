package decadeavg;

import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class DecadeMapper
        extends Mapper<LongWritable, Text, Text, DoubleWritable> {

    private final Text decadeKey = new Text();
    private final DoubleWritable ratingValue = new DoubleWritable();

    @Override
    public void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {

        String line = value.toString();

        // Skip header
        if (line.startsWith("Title_ID")) {
            return;
        }

        String[] fields = line.split("\t", -1);

        // Need at least 7 columns
        if (fields.length < 7) {
            return;
        }

        String year = fields[3].trim();
        String rating = fields[6].trim();

        if (year.equals("\\N") || year.equals("Unknown")
                || rating.equals("\\N") || rating.equals("Unknown")) {
            return;
        }

        try {
            int yearValue = Integer.parseInt(year);
            double ratingValueDouble = Double.parseDouble(rating);

            int decade = (yearValue / 10) * 10;

            decadeKey.set(decade + "s");
            ratingValue.set(ratingValueDouble);

            context.write(decadeKey, ratingValue);

        } catch (NumberFormatException e) {
            // Ignore invalid records
        }
    }
}