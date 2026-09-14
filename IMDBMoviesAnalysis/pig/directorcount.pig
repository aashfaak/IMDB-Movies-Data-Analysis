infiles = LOAD 'C:/Ashfak/data/imdb_movies.csv' USING PigStorage(',') AS (
    title_id:chararray, title:chararray, title_type:chararray, year_str:chararray, 
    genres:chararray, runtime_str:chararray, rating_str:chararray, votes_str:chararray, 
    director_id:chararray, director_name:chararray
);

clean_data = FILTER infiles BY title_id != 'Title_ID' AND director_name IS NOT NULL AND director_name != '' AND director_name != '\\N';

grouped_director = GROUP clean_data BY director_name;
director_summary = FOREACH grouped_director GENERATE group AS director_name, COUNT(clean_data) AS total_movies;

ordered_directors = ORDER director_summary BY total_movies DESC;
top_directors = LIMIT ordered_directors 20;

STORE top_directors INTO 'DirectorCountOutput' USING PigStorage('|');