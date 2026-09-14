infiles = LOAD 'C:/Ashfak/data/imdb_movies.csv' USING PigStorage(',') AS (
    title_id:chararray, title:chararray, title_type:chararray, year_str:chararray, 
    genres:chararray, runtime_str:chararray, rating_str:chararray, votes_str:chararray, 
    director_id:chararray, director_name:chararray
);

clean_data = FILTER infiles BY title_id != 'Title_ID' AND genres IS NOT NULL AND genres != '';
flattened_genres = FOREACH clean_data GENERATE title_id, FLATTEN(TOKENIZE(genres, ',')) AS genre;

grouped_genre = GROUP flattened_genres BY TRIM(genre);
genre_count = FOREACH grouped_genre GENERATE group AS genre, COUNT(flattened_genres) AS total_movies;

ordered_count = ORDER genre_count BY total_movies DESC;

STORE ordered_count INTO 'GenreCountOutput' USING PigStorage('|');