% line=29 var=BS oc=1 offset=1428 col=14
%-----------------------------------------------------------------------------------------
%-----------------------------------------------------------------------------------------
%-- b12_s29BS.erl
%--
%-- DATE:        2016 
%-- SLICING CRITERION: (29,BS)/(Node 73)    
%-- COPYRIGHT:   Bencher: Suite of Benchmarks for Secuential Erlang 
%--              (University Polytechnic of Valencia)
%--              http://www.dsic.upv.es/~jsilva/slicing/bencher/         
%-- DESCRIPTION
%-- This program is a theoric result of applying static backward slicing to variable BS at
%-- line 29 of the bench12.erl file.
%-----------------------------------------------------------------------------------------
%-----------------------------------------------------------------------------------------

-module(b12).
-export([main/3]).
 
main(_,B,_) ->
    BT = to_ternary(B), 
    BS = to_string(BT),
    [undef,{BS,undef}|undef].

to_string(T) -> [to_char(X) || X <- T].

to_char(-1) -> $-;
to_char(0) -> $0;
to_char(_) -> $+.
   
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
