% line=49 var=Year oc=1 offset=2770 col=12
%-----------------------------------------------------------------------------------------
%-----------------------------------------------------------------------------------------
%-- b1_s49Year.erl
%--
%-- DATE:        2016 
%-- SLICING CRITERION: (49,Year)/(Node 392)    
%-- COPYRIGHT:   Bencher: Suite of Benchmarks for Secuential Erlang 
%--              (University Polytechnic of Valencia)
%--              http://www.dsic.upv.es/~jsilva/slicing/bencher/         
%-- DESCRIPTION
%-- This program is a theoric result of applying static backward slicing to variable Year 
%-- at line 49 of the bench1.erl file.
%-----------------------------------------------------------------------------------------
%-----------------------------------------------------------------------------------------
-module(b15).
-export([main/1]).

main(Number)
    when
        Number > 0
        andalso
        Number < 7 ->
    Database =
        [[undef,
          undef,
          undef,
          {undef,
           [{undef,{undef,undef,undef}},
            {undef,{undef,undef,undef}},
            {undef,{undef,undef,undef}},
            {undef,{2017,undef,undef}}]}|
          undef],
         [undef,
          undef,
          undef,
          {undef,
           [{undef,{undef,undef,undef}},{undef,{undef,undef,undef}}]}|
          undef],
         [undef,
          undef,
          undef,
          {undef,
           [{undef,{undef,undef,undef}},
            {undef,{undef,undef,undef}},
            {undef,{undef,undef,undef}}]}|
          undef],
         [undef,
          undef,
          undef,
          {undef,
           [{undef,{undef,undef,undef}},
            {undef,{undef,undef,undef}},
            {undef,{2017,undef,undef}}]}|
          undef],
         [undef,
          undef,
          undef,
          {undef,
           [{undef,{undef,undef,undef}},
            {undef,{undef,undef,undef}},
            {undef,{2017,undef,undef}}]}|
          undef],
         [undef,
          undef,
          undef,
          {undef,
           [{undef,{undef,undef,undef}},
            {undef,{undef,undef,undef}},
            {undef,{undef,undef,undef}},
            {undef,{2017,undef,undef}}]}|
          undef]|
         undef],
    Artist = lists:nth(Number, Database),
    NextConcert = getNextConcert(Artist),
    Info = getConcertLocationAndYear(NextConcert),
    {_,Year} = Info.

getConcerts(Artist) ->
    [_,_,_,{_,Concerts}|_] = Artist,
    Concerts.

getNextConcert(Artist) ->
    ActualDate = date(),
    Concerts = getConcerts(Artist),
    case getNext(Concerts, ActualDate, undef) of
        empty ->
            "No future concerts planned";
        Concert ->
            Concert
    end.

getNext([], _, NextConcert) ->
    NextConcert;
getNext([Concert|Concerts], Actual, _) ->
    {YearA,_,_} = Actual,
    {_,Date} = Concert,
    {YearC,_,_} = Date,
    case {YearA,YearC,undef,undef,undef,undef} of
        {Y,Y,_,_,DA,DC} when DC >= DA ->
            getNext(Concerts, undef, Concert);
        _ ->
            getNext(Concerts, Actual, empty)
    end.

getConcertLocationAndYear("No future concerts planned") ->
    {undef,"Not planned yet"};
getConcertLocationAndYear(Concert) ->
    {_,Date} = Concert,
    {Year,_,_} = Date,
    _ = {undef,Year}.

