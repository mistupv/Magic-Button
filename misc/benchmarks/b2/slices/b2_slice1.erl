% line=21 var=C oc=1 offset=991 col=2
%-----------------------------------------------------------------------------------------
%-----------------------------------------------------------------------------------------
%-- b11_s21C.erl
%--
%-- DATE:        2016 
%-- SLICING CRITERION: Variable C (LINE 21)         
%-- DESCRIPTION
%-- This program is a theoric result of applying static backward slicing to variable C at
%-- line 28 of the test1.erl file.
%-----------------------------------------------------------------------------------------
%-----------------------------------------------------------------------------------------

-module(b2).
-export([lists/2]).

lists(A,B) -> 
	C=fl(A,B).

fl([H1|_],[H2|T2]) ->
	if	H1 >= 3 -> 
			H2;
		true -> 
			[H|_] = T2,
			H1-H  
	end.
