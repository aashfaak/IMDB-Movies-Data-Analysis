infiles = LOAD 'C:/Ashfak/data/imdb_movies.csv' USING PigStorage(',') AS (
    title_id:chararray, title:chararray, title_type:chararray, year_str:chararray, 
    genres:chararray, runtime_str:chararray, rating_str:chararray, votes_str:chararray, 
    director_id:chararray, director_name:chararray
);

clean_data = FILTER infiles BY title_id != 'Title_ID' AND year_str MATCHES '^[0-9]{4}$' AND rating_str MATCHES '^[0-9]+(\\.[0-9]+)?$';
typed_data = FOREACH clean_data GENERATE (int)year_str AS year, (float)rating_str AS rating;

grouped_year = GROUP typed_data BY year;
year_summary = FOREACH grouped_year GENERATE group AS year, COUNT(typed_data) AS movie_count, ROUND_TO(AVG(typed_data.rating), 2) AS avg_rating;

ordered_year = ORDER year_summary BY year DESC;

STORE ordered_year INTO 'YearAvgOutput' USING PigStorage('|');