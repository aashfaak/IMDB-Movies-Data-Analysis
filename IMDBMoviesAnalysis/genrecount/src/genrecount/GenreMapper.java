package genrecount;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class GenreMapper
        extends Mapper<LongWritable, Text, Text, IntWritable> {

    private final Text genreKey = new Text();
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

        // Need at least 5 columns
        if (fields.length < 5) {
            return;
        }

        // Column 4 = Genres
        String genres = fields[4];

        if (genres.equals("Unknown") || genres.equals("\\N")) {
            return;
        }

        // Example: Action,Adventure,Drama
        String[] genreList = genres.split(",");

        for (String genre : genreList) {

            genre = genre.trim();

            if (!genre.isEmpty()) {
                genreKey.set(genre);
                context.write(genreKey, one);
            }
        }
    }
}