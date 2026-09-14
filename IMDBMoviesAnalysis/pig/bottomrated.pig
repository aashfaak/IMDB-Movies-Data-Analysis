infiles = LOAD 'C:/Ashfak/data/imdb_movies.csv' USING PigStorage(',') AS (
    title_id:chararray, title:chararray, title_type:chararray, year_str:chararray, 
    genres:chararray, runtime_str:chararray, rating_str:chararray, votes_str:chararray, 
    director_id:chararray, director_name:chararray
);

-- Clean data and cast types safely
clean_data = FILTER infiles BY 
    title_id != 'Title_ID' AND 
    rating_str MATCHES '^[0-9]+(\\.[0-9]+)?$' AND 
    votes_str MATCHES '^[0-9]+$';

typed_data = FOREACH clean_data GENERATE 
    title_id, 
    title, 
    (int)year_str AS year, 
    genres, 
    (float)rating_str AS rating, 
    (int)votes_str AS votes;

-- Ensure minimum vote threshold for credibility
credible_movies = FILTER typed_data BY votes >= 50;

-- Sort by rating ascending (lowest first)
ordered_bottom = ORDER credible_movies BY rating ASC, votes DESC;
bottom20_rated = LIMIT ordered_bottom 20;

STORE bottom20_rated INTO 'Bottom20RatedOutput' USING PigStorage('|');