% line=20 var=C oc=1 offset=949 col=2 
%-----------------------------------------------------------------------------------------
%-----------------------------------------------------------------------------------------
%-- b7_s20C.erl
%--
%-- DATE:        2016 
%-- SLICING CRITERION: Variable C (LINE 20)         
%-- DESCRIPTION
%-- This program is a theoric result of applying static backward slicing to variable C at
%-- line 20 of the bench7.erl file.
%-----------------------------------------------------------------------------------------
%-----------------------------------------------------------------------------------------

-module(b1).
-export([numbers/2]).

numbers(A,_) -> 
	C=fn(A,undef).

fn(X,_) ->
	if	X>5 -> X; 
		true -> 
			X+2
	end.