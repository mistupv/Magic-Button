% line=25 var=D oc=1 offset=1195 col=2
%-----------------------------------------------------------------------------------------
%-----------------------------------------------------------------------------------------
%-- b6_s24D.erl
%--
%-- DATE:        2016 
%-- SLICING CRITERION: Variable D (LINE 24)         
%-- DESCRIPTION
%-- This program is a theoric result of applying static backward slicing to variable D at
%-- line 24 of the bench6.erl file.
%-----------------------------------------------------------------------------------------
%-----------------------------------------------------------------------------------------

-module(b6).
-export([tuples/2]).

tuples(A,B) ->
	D=ht(B,A).

ht({_,_,Z},{A,B})-> 
	case undef of
		_ -> A+Z*B
	end.
