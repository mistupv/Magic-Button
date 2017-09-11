% line=35 var=Z oc=1 offset=1246 col=11
%-----------------------------------------------------------------------------------------
%-----------------------------------------------------------------------------------------
%-- b14_s31Z.erl
%--
%-- DATE:        2016 
%-- SLICING CRITERION: (31,Z)/(Node 88)    
%-- COPYRIGHT:   Bencher: Suite of Benchmarks for Secuential Erlang 
%--              (University Polytechnic of Valencia)
%--              http://www.dsic.upv.es/~jsilva/slicing/bencher/         
%-- DESCRIPTION
%-- This program is a theoric result of applying static backward slicing to variable Z
%-- at line 31 of the bench14.erl file.
%-----------------------------------------------------------------------------------------
%-----------------------------------------------------------------------------------------
-module(b14).
-export([main/2]).

main(X, _) ->
    Z = case X of
            terminate ->
                "the end";
            {A,B} ->
                {[A + B,B - A],3};
            _ ->
                {20 * 3,8}
        end,
    _ = {Z,undef,undef}.
    