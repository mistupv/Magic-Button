% line=52 var=A oc=1 offset=1859 col=2
%-----------------------------------------------------------------------------------------
%-----------------------------------------------------------------------------------------
%-- b9_s49A.erl
%--
%-- DATE:        2016 
%-- SLICING CRITERION: (49,A)/(Node 65)
%-- COPYRIGHT: 	 Bencher: Suite of Benchmarks for Secuential Erlang 
%--              (University Polytechnic of Valencia)
%--              http://www.dsic.upv.es/~jsilva/slicing/bencher/
%-- DESCRIPTION
%-- This program is a theoric result of applying static backward slicing to variable A at
%-- line 49 of the bench9.erl file.
%-----------------------------------------------------------------------------------------
%-----------------------------------------------------------------------------------------

-module(b9).
-export([main/2]).
-define(ERROR,failure).
-define(ON,enable).
-define(OFF,disable).

light(N,L) -> if N > 20 andalso L==?ON -> {undef, "Success switching ON"};
				 N =< 20 andalso L==?ON -> {undef, ?ERROR};
  			     true   -> {undef, "Success switching OFF"}
			  end.

stove(P,L) -> if L == ?ON -> {_,Reply} = light(P,L);
				 true -> {_,Reply} = light(undef,undef)
			  end, Reply.

main(N,State) when (N > -480 andalso N =< 520) andalso (State == ?ON orelse State == ?OFF) -> 
	A=stove(N,State).
