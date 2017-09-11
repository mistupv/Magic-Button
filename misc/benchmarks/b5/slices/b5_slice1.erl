% line=24 var=C oc=1 offset=1260 col=2
%-----------------------------------------------------------------------------------------
%-----------------------------------------------------------------------------------------
%-- b5_s24C.erl
%--
%-- DATE:        2016 
%-- SLICING CRITERION: Variable C (LINE 24)         
%-- DESCRIPTION
%-- This program is a theoric result of applying static backward slicing to variable C at
%-- line 24 of the test2.erl file.
%-----------------------------------------------------------------------------------------
%-----------------------------------------------------------------------------------------

-module(b5). 

-export([lists/2]).

lists(A, B) ->
    C = fl(A, B).

fl([H1|_], [H2|T2]) ->
    if
        H1 >= 3 ->
            H2;
        true ->
            H1 + gl(T2)
    end.

gl(0) ->
    3;
gl([]) ->
    5;
gl([1|_]) ->
    0;
gl([_|_]) ->
    1;
gl(_) ->
    7.