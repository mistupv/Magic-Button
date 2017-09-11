% line=25 var=Abb oc=1 offset=1284 col=2
%-----------------------------------------------------------------------------------------
%-----------------------------------------------------------------------------------------
%-- b4_s26Abb.erl
%--
%-- DATE:        2016 
%-- SLICING CRITERION: (26,Abb)/(Node 15)
%-- COPYRIGHT: 	 Bencher: Suite of Benchmarks for Secuential Erlang 
%--              (University Polytechnic of Valencia)
%--              http://www.dsic.upv.es/~jsilva/slicing/bencher/   
%-- DESCRIPTION
%-- This program is a theoric result of applying static backward slicing to variable Abb 
%-- at line 26 of the bench4.erl file.
%-----------------------------------------------------------------------------------------
%-----------------------------------------------------------------------------------------

-module(b4).
-export([main/2]).

main(Elements,Temp) ->
	Res = templessthan(Temp,Elements),
	Abb = abbreviation(Elements,Res).

templessthan(Temp,Elem) -> 
	[begin
		{Name,undef} 
	end
	||{Name,_,T} <- Elem, T > Temp].

abbreviation(Elements,List) -> 
	[Abb||{RN,_} <- List, {Name,Abb,_} <- Elements, RN == Name andalso length(atom_to_list(Abb)) == 1].
