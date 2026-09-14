infiles = LOAD 'C:/Ashfak/data/imdb_movies.csv' USING PigStorage(',') AS (
    title_id:chararray, title:chararray, title_type:chararray, year_str:chararray, 
    genres:chararray, runtime_str:chararray, rating_str:chararray, votes_str:chararray, 
    director_id:chararray, director_name:chararray
);

clean_data = FILTER infiles BY title_id != 'Title_ID' AND rating_str MATCHES '^[0-9]+(\\.[0-9]+)?$' AND votes_str MATCHES '^[0-9]+$';

typed_data = FOREACH clean_data GENERATE title_id, title, (float)rating_str AS rating, (int)votes_str AS votes, (int)year_str AS year;
credible_movies = FILTER typed_data BY votes >= 50;

ordered_data = ORDER credible_movies BY rating DESC;
top10 = LIMIT ordered_data 10;

STORE top10 INTO 'Top10RatedOutput' USING PigStorage('|');