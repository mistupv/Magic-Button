% line=27 var=C oc=1 offset=1058 col=2
%-----------------------------------------------------------------------------------------
%-----------------------------------------------------------------------------------------
%-- b2_s27C.erl
%--
%-- DATE:        2016 
%-- SLICING CRITERION: Variable C (LINE 27)         
%-- DESCRIPTION
%-- This program is a theoric result of applying static backward slicing to variable C at
%-- line 27 of the bench2.erl file.
%-----------------------------------------------------------------------------------------
%-----------------------------------------------------------------------------------------

-module(b7).
-export([main/2]).

main(A,B)->
	if 
	    true -> 
	    	C=f(B,A)
	end,
	C.

f(X,Y) -> 
	case undef of
		_ -> 
			fun(B,E) ->
				B+E 
			end(X,Y)
	end.


