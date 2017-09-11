% line=21 var=C oc=1 offset=1022 col=2
%-----------------------------------------------------------------------------------------
%-----------------------------------------------------------------------------------------
%-- B3_s21C.erl
%--
%-- DATE:        2016 
%-- SLICING CRITERION: Variable C (LINE 21)         
%-- DESCRIPTION
%-- This program is a theoric result of applying static backward slicing to variable C at
%-- line 39 of the test1.erl file.
%-----------------------------------------------------------------------------------------
%-----------------------------------------------------------------------------------------

-module(b3).
-export([tuples/2]).

tuples(A,_) -> 
	C=ft(A,undef).

ft({X1,X2},_) ->
	if	X1 > X2 ->  
			_ = 5;
		true ->  
			_ = X2
	end.
