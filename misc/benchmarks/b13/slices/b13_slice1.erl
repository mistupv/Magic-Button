% line=31 var=NewI oc=1 offset=1244 col=9
%-----------------------------------------------------------------------------------------
%-----------------------------------------------------------------------------------------
%-- b13_s31NewI.erl
%--
%-- DATE:        2016 
%-- SLICING CRITERION: (31,NewI)/(Node 41)    
%-- COPYRIGHT:   Bencher: Suite of Benchmarks for Secuential Erlang 
%--              (University Polytechnic of Valencia)
%--              http://www.dsic.upv.es/~jsilva/slicing/bencher/         
%-- DESCRIPTION
%-- This program is a theoric result of applying static backward slicing to variable NewI 
%-- at line 31 of the bench13.erl file.
%-----------------------------------------------------------------------------------------
%-----------------------------------------------------------------------------------------
-module(b13).
-export([main/0]).

main() -> 
  I = 1,
  _ = while(undef,I,11).

while(_,I,Top) ->
  if Top /= 0 ->
        NewI = increment(I),
        while(undef,NewI,Top-1)
  end.

add(A,B) -> A+B.
increment(A) -> add(A,1).

