% line=34 var=W oc=1 offset=1215 col=2
%-----------------------------------------------------------------------------------------
%-----------------------------------------------------------------------------------------
%-- b14_s30W.erl
%--
%-- DATE:        2016 
%-- SLICING CRITERION: (30,W)/(Node 72)    
%-- COPYRIGHT:   Bencher: Suite of Benchmarks for Secuential Erlang 
%--              (University Polytechnic of Valencia)
%--              http://www.dsic.upv.es/~jsilva/slicing/bencher/         
%-- DESCRIPTION
%-- This program is a theoric result of applying static backward slicing to variable W 
%-- at line 30 of the bench14.erl file.
%-----------------------------------------------------------------------------------------
%-----------------------------------------------------------------------------------------
-module(b14).
-export([main/2]).

main(X, _) ->
    W = g([undef,undef,{X,undef}|undef]).

g(X) ->
    [_,_,{R,_}|_] = X,
    case R of
        [1,3] ->
            21;
        [A,B] ->
            A * B / 9;
        T ->
            T
    end.