infiles = LOAD 'C:/Ashfak/data/imdb_movies.csv'
USING PigStorage(',')
AS (
    title_id:chararray,
    title:chararray,
    title_type:chararray,
    year:int,
    genres:chararray,
    runtime:float,
    rating:float,
    votes:int,
    director_id:chararray,
    director_name:chararray
);


valid_movies = FILTER infiles BY
    title_id != 'Title_ID' AND
    rating IS NOT NULL AND
    votes IS NOT NULL;


credible_movies = FILTER valid_movies BY
    votes >= 50;


ordered_by_rating = ORDER credible_movies BY
    rating DESC;

-- Top 10
top10_rated = LIMIT ordered_by_rating 10;


final_top10 = FOREACH top10_rated
    GENERATE
    title_id,
    title,
    year,
    genres,
    rating,
    votes;

-- Output
STORE final_top10 INTO 'Top10RatedOutput'
USING PigStorage('|');