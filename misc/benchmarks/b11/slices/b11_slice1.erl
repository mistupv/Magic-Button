% line=28 var=DB oc=1 offset=1127 col=5
%-----------------------------------------------------------------------------------------
%-----------------------------------------------------------------------------------------
%-- b10_s25DB.erl
%--
%-- DATE:        2016 
%-- SLICING CRITERION: (25,DB)/(Node 41)
%-- COPYRIGHT:   Bencher: The Program Slicing Benchmark Suite for Erlang 
%--              (Universitat Politècnica de València)
%--              http://www.dsic.upv.es/~jsilva/slicing/bencher/
%-- DESCRIPTION
%-- This program is a theoric result of applying static backward slicing to variable DB at
%-- line 25 of the bench10.erl file.
%-----------------------------------------------------------------------------------------
%-----------------------------------------------------------------------------------------

-module(b11).
-export([main/1]).

main(L) ->
    _ = calculate(L).

calculate(List) ->
    calculate(List, undef).

calculate([{_,B}|T], _) ->
    DB = divs(B),
    calculate(T, undef).

divs(0) ->
    [];
divs(1) ->
    [1];
divs(N) ->
    [1,N] ++ divisors(2, N, math:sqrt(N)).

divisors(K, _, Q) when K > Q ->
    [];
divisors(K, N, Q) when N rem K =/= 0 ->
    divisors(K + 1, N, Q);
divisors(K, N, Q) when K * K == N ->
    [K] ++ divisors(undef, undef, Q);
divisors(K, N, Q) ->
    [K,N div K] ++ divisors(K + 1, N, Q).
