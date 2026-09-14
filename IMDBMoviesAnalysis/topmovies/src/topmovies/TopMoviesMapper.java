package topmovies;

import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class TopMoviesMapper
        extends Mapper<Object, Text, DoubleWritable, Text> {

    private final DoubleWritable ratingKey = new DoubleWritable();
    private final Text titleValue = new Text();

    @Override
    public void map(Object key, Text value, Context context)
            throws IOException, InterruptedException {

        String line = value.toString();

        // Skip header
        if (line.startsWith("Title_ID")) {
            return;
        }

        String[] fields = line.split("\t", -1);

        // Need at least 8 columns
        if (fields.length < 8) {
            return;
        }

        String titleId = fields[0].trim();
        String title = fields[1].trim();
        String rating = fields[6].trim();
        String votes = fields[7].trim();

        if (rating.equals("\\N") || rating.equals("Unknown")
                || title.equals("\\N") || title.isEmpty()) {
            return;
        }

        try {
            double ratingValue = Double.parseDouble(rating);

            String output =
                    titleId + "\t" + title + "\tRating=" + ratingValue
                    + "\tVotes=" + votes;

            ratingKey.set(ratingValue);
            titleValue.set(output);

            context.write(ratingKey, titleValue);

        } catch (NumberFormatException e) {
            // Ignore invalid records
        }
    }
}