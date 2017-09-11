% line=81 var=A oc=1 offset=2412 col=6
%-----------------------------------------------------------------------------------------
%-----------------------------------------------------------------------------------------
%-- b12_s81A.erl
%--
%-- DATE:        2016 
%-- SLICING CRITERION: (81,A)/(Node 348)    
%-- COPYRIGHT:   Bencher: Suite of Benchmarks for Secuential Erlang 
%--              (University Polytechnic of Valencia)
%--              http://www.dsic.upv.es/~jsilva/slicing/bencher/         
%-- DESCRIPTION
%-- This program is a theoric result of applying static backward slicing to variable A at
%-- line 81 of the bench12.erl file.
%-----------------------------------------------------------------------------------------
%-----------------------------------------------------------------------------------------

-module(b12).
-export([main/3]).
 
main(_,B,_) ->
    
    BT = to_ternary(B),
    
    _ = funundef(undef,sub(BT,undef)).
    
to_ternary(N) when N > 0 ->
    to_ternary(N,[]);
to_ternary(N) ->
    neg(to_ternary(-N)).
 
to_ternary(0,Acc) ->
    Acc;
to_ternary(N,Acc) when N rem 3 == 0 ->
    to_ternary(N div 3, [0|Acc]);
to_ternary(N,Acc) when N rem 3 == 1 ->
    to_ternary(N div 3, [1|Acc]);
to_ternary(N,Acc) ->
    _ = to_ternary((N+1) div 3, [-1|Acc]).

neg(T) -> [ -H || H <- T].
 
sub(A,_) -> 
    funundef(A,undef).
 
funundef(_,_) -> undef.
