infiles = LOAD 'C:/Ashfak/data/imdb_movies.csv' USING PigStorage(',') AS (
    title_id:chararray, title:chararray, title_type:chararray, year_str:chararray, 
    genres:chararray, runtime_str:chararray, rating_str:chararray, votes_str:chararray, 
    director_id:chararray, director_name:chararray
);

clean_data = FILTER infiles BY title_id != 'Title_ID' AND rating_str MATCHES '^[0-9]+(\\.[0-9]+)?$' AND genres IS NOT NULL;
typed_data = FOREACH clean_data GENERATE (float)rating_str AS rating, FLATTEN(TOKENIZE(genres, ',')) AS genre;

grouped_genre = GROUP typed_data BY TRIM(genre);
genre_avg = FOREACH grouped_genre GENERATE group AS genre, ROUND_TO(AVG(typed_data.rating), 2) AS avg_rating, COUNT(typed_data) AS total_movies;

ordered_avg = ORDER genre_avg BY avg_rating DESC;

STORE ordered_avg INTO 'GenreAvgOutput' USING PigStorage('|');