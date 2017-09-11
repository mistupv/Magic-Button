% line=24 var=C oc=1 offset=1183 col=2
%-----------------------------------------------------------------------------------------
%-----------------------------------------------------------------------------------------
%-- b6_s23C.erl
%--
%-- DATE:        2016 
%-- SLICING CRITERION: Variable C (LINE 23)         
%-- DESCRIPTION
%-- This program is a theoric result of applying static backward slicing to variable C at
%-- line 47 of the test2.erl file.
%-----------------------------------------------------------------------------------------
%-----------------------------------------------------------------------------------------

-module(b6).
-export([tuples/2]).

tuples(A,B) ->
	C=ft(A,B).

ft({X,Y},{X,_,W}) -> 
	case Y of		 
		W -> gt(undef);
		_ -> gt({undef,undef})
	end.

gt({_,_}) -> 4;
gt(_) -> 16.
