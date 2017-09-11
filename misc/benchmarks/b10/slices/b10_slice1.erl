% line=58 var=Shown oc=1 offset=2204 col=14
%-----------------------------------------------------------------------------------------
%-----------------------------------------------------------------------------------------
%-- b15_s58Shown.erl
%--
%-- DATE:        2016 
%-- SLICING CRITERION: (58,Shown)/(Node 244)
%-- COPYRIGHT:   Bencher: Suite of Benchmarks for Secuential Erlang 
%--              (University Polytechnic of Valencia)
%--              http://www.dsic.upv.es/~jsilva/slicing/bencher/      
%-- DESCRIPTION
%-- This program is a theoric result of applying static backward slicing to variable 
%-- Shown at line 58 of the bench15.erl file.
%-----------------------------------------------------------------------------------------
%-----------------------------------------------------------------------------------------

-module(b10).
-export([main/4]).
 
perm(N, K) ->
    product(lists:seq(N - K + 1, N)).
 
comb(N, K) ->
    perm(N, K) div product(lists:seq(1, K)).
 
product(List) ->
    lists:foldl(fun(N, Acc) -> N * Acc end, 1, List).
 
main(PFrom,PTo,CFrom,CTo) ->
    IncremP = if (PTo - PFrom >= 10) -> (PTo-PFrom) div 10;
                true -> 1
              end,
    IncremC = if (CTo - CFrom >= 10) -> (CTo-CFrom) div 10;
                true -> 1
              end,
    _=[show_perm({N, N div 3}) || N <- lists:seq(PFrom, PTo, IncremP)],
    
    _=[show_comb({N, N div 3}) || N <- lists:seq(CFrom, CTo, IncremC)].
 
show_perm({N, K}) ->
    show_gen(N, K, undef, fun perm/2).
 
show_comb({N, K}) ->
    show_gen(N, K, undef, fun comb/2).
 
show_gen(N, K, _, Fun) ->
    funundef(undef,[undef, undef, undef, show_big(Fun(N, K), 40)|undef]). 
 
show_big(N, Limit) ->
    StrN = integer_to_list(N),
    case length(StrN) < Limit of
		true -> StrN;        
		_ -> 
            {Shown, _} = lists:split(Limit, StrN)
    end. 

funundef(_,_) -> undef.
