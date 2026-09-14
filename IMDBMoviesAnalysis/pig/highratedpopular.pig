infiles = LOAD 'C:/Ashfak/data/imdb_movies.csv' USING PigStorage(',') AS (
    title_id:chararray, title:chararray, title_type:chararray, year_str:chararray, 
    genres:chararray, runtime_str:chararray, rating_str:chararray, votes_str:chararray, 
    director_id:chararray, director_name:chararray
);

clean_data = FILTER infiles BY title_id != 'Title_ID' AND rating_str MATCHES '^[0-9]+(\\.[0-9]+)?$' AND votes_str MATCHES '^[0-9]+$';

typed_data = FOREACH clean_data GENERATE title, (float)rating_str AS rating, (int)votes_str AS votes, (int)year_str AS year;

-- High rating (>= 8.0) and high popularity/votes (>= 10,000 votes)
popular_hits = FILTER typed_data BY rating >= 8.0 AND votes >= 10000;

ordered_hits = ORDER popular_hits BY votes DESC, rating DESC;
top20_popular_hits = LIMIT ordered_hits 20;

STORE top20_popular_hits INTO 'HighRatedPopularOutput' USING PigStorage('|');