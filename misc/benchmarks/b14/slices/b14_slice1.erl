% line=33 var=V oc=1 offset=1193 col=2
%-----------------------------------------------------------------------------------------
%-----------------------------------------------------------------------------------------
%-- b14_s29V.erl
%--
%-- DATE:        2016 
%-- SLICING CRITERION: (29,V)/(Node 56)    
%-- COPYRIGHT:   Bencher: Suite of Benchmarks for Secuential Erlang 
%--              (University Polytechnic of Valencia)
%--              http://www.dsic.upv.es/~jsilva/slicing/bencher/         
%-- DESCRIPTION
%-- This program is a theoric result of applying static backward slicing to variable V 
%-- at line 29 of the bench14.erl file.
%-----------------------------------------------------------------------------------------
%-----------------------------------------------------------------------------------------
-module(b14).
-export([main/2]).

main(_, _) ->

    V = f(undef) + h(2) + h(undef).

f(_) ->
    7.

h(X) ->
    case X of
        2 ->
            j({2,undef});
        _ ->
            k([4|undef])
    end.

j(A) ->
    {X,_} = A,
    X.

k(B) ->
    [H|_] = B,
    H.
