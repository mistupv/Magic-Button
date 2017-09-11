% line=28 var=D oc=1 offset=1062 col=2
%-----------------------------------------------------------------------------------------
%-----------------------------------------------------------------------------------------
%-- b2_s28D.erl
%--
%-- DATE:        2016 
%-- SLICING CRITERION: Variable D (LINE 22)         
%-- DESCRIPTION
%-- This program is a theoric result of applying static backward slicing to variable D at
%-- line 22 of the test2.erl file.
%-----------------------------------------------------------------------------------------
%-----------------------------------------------------------------------------------------

-module(b7).
-export([main/2]).
main(A, B)->
	D = h(fun g/2, A, B).

g(X,Y) ->
	fun(A, B) ->
		A + B 
	end(X, Y).  

h(F, A, B) ->  
	F(A, B).
