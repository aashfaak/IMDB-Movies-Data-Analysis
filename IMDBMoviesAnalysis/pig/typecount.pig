infiles = LOAD 'C:/Ashfak/data/imdb_movies.csv' USING PigStorage(',') AS (
    title_id:chararray, title:chararray, title_type:chararray, year_str:chararray, 
    genres:chararray, runtime_str:chararray, rating_str:chararray, votes_str:chararray, 
    director_id:chararray, director_name:chararray
);

clean_data = FILTER infiles BY title_id != 'Title_ID' AND title_type IS NOT NULL;

grouped_type = GROUP clean_data BY title_type;
type_summary = FOREACH grouped_type GENERATE group AS title_type, COUNT(clean_data) AS total_titles;

ordered_type = ORDER type_summary BY total_titles DESC;

STORE ordered_type INTO 'TypeCountOutput' USING PigStorage('|');