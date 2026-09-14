package topmovies;

import java.io.IOException;
import java.util.PriorityQueue;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class TopMoviesReducer
        extends Reducer<DoubleWritable, Text, DoubleWritable, Text> {

    private PriorityQueue<MovieRecord> topMovies;

    private static final int TOP_N = 10;

    @Override
    protected void setup(Context context) {

        topMovies = new PriorityQueue<MovieRecord>(
            TOP_N,
            (a, b) -> Double.compare(a.rating, b.rating)
        );
    }

    @Override
    public void reduce(DoubleWritable key,
                       Iterable<Text> values,
                       Context context)
            throws IOException, InterruptedException {

        for (Text value : values) {

            MovieRecord movie =
                    new MovieRecord(key.get(), value.toString());

            topMovies.offer(movie);

            if (topMovies.size() > TOP_N) {
                topMovies.poll();
            }
        }
    }

    @Override
    protected void cleanup(Context context)
            throws IOException, InterruptedException {

        MovieRecord[] results =
                topMovies.toArray(new MovieRecord[0]);

        java.util.Arrays.sort(
            results,
            (a, b) -> Double.compare(b.rating, a.rating)
        );

        for (MovieRecord movie : results) {

            context.write(
                new DoubleWritable(movie.rating),
                new Text(movie.info)
            );
        }
    }

    private static class MovieRecord {

        double rating;
        String info;

        MovieRecord(double rating, String info) {
            this.rating = rating;
            this.info = info;
        }
    }
}