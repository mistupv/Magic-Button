% line=22 var=Deposits oc=1 offset=1045 col=3
%-----------------------------------------------------------------------------------------
%-----------------------------------------------------------------------------------------
%-- b8_s22Deposits.erl
%--
%-- DATE:        2016 
%-- SLICING CRITERION: (22,Deposits)/(Node 9)
%-- COPYRIGHT: 	 Bencher: Suite of Benchmarks for Secuential Erlang 
%--              (University Polytechnic of Valencia)
%--              http://www.dsic.upv.es/~jsilva/slicing/bencher/
%-- DESCRIPTION
%-- This program is a theoric result of applying static backward slicing to variable 
%-- Deposits at line 22 of the bench8.erl file.
%-----------------------------------------------------------------------------------------
%-----------------------------------------------------------------------------------------

-module(b8).
-export([main/2]).

main(Account,Who) -> 
	
	{Deposits, _} = recount(Who, Account).

recount(Person, List) -> tailrecount(Person,List,[],undef).

tailrecount(_,[],LDeposits, _) -> {LDeposits,undef};
tailrecount(Person,[H|T],LDeposits,_) -> 
	case H of
		{Mov, Amount, Person} when Mov == deposit andalso Amount >300 ->
			tailrecount(Person,T,[H|LDeposits],undef);
		_ -> tailrecount(Person,T,LDeposits,undef)
	end.

