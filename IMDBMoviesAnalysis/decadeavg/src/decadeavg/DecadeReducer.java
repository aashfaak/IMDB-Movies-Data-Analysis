package decadeavg;

import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class DecadeReducer
        extends Reducer<Text, DoubleWritable, Text, DoubleWritable> {

    private final DoubleWritable result = new DoubleWritable();

    @Override
    public void reduce(Text key, Iterable<DoubleWritable> values,
                       Context context)
            throws IOException, InterruptedException {

        double sum = 0.0;
        int count = 0;

        for (DoubleWritable value : values) {
            sum += value.get();
            count++;
        }

        if (count > 0) {
            double average = sum / count;

            // Round to 2 decimal places
            average = Math.round(average * 100.0) / 100.0;

            result.set(average);

            context.write(key, result);
        }
    }
}
