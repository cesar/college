%{
	#include <stdio.h>
	#include <stdlib.h>
	#include <time.h>
%}

%option noyywrap

%%

(and)|(as)|(assert)|(break)|(class)|(continue)|(def)|(del)|(elif)|(else)|(except)|(exec)|(finally)|(for)|(from)|(global)|(if)|(import)|(in)|(is)|(lambda)|(not)|(or)|(pass)|(print)|(raise)|(return)|(try)|(while)|(with)|(yield) fprintf(yyout,"\nKEYWORD %s\t", yytext);

[a-zA-Z|\_][a-zA-Z0-9|\_]* fprintf(yyout,"\nIDENTIFIER %s\t", yytext);

((r|u|ur|R|U|UR|Ur|uR|b|B|br|Br|bR|BR)?((''')([^\\'''])*(''')|\"\"\"([^\\\"\"\"])*\"\"\"|'(([^\\\n\'])|\\.)*'|\"(([^\\\n"])|\\.)*\"))   fprintf(yyout,"\nLITERAL %s\t", yytext);

[0-9][0-9a-zA-Z|\.|\+|\-]* fprintf(yyout,"\nLITERAL %s\t", yytext);

\+|\-|\*|\*\*|\/|\/\/|\%|\<\<|\>\>|\&|\||\^|\~|\<|\>|\<\=|\>\=|\=\=|\!\=|\<\> fprintf(yyout,"\nOPERATOR %s\t", yytext);

\(|\)|\[|\]|\{|\}|\@|\,|\:|\.|\`|\=|\;|\+\=|\-\=|\*\=|\/\=|\/\/\=|\%\=|\&\=|\|\=|\^\=|\>\>\=|\<\<\=|\*\*\= fprintf(yyout,"\nDELIMITER %s\t", yytext);

\s* fprintf(yyout,"\nINDENT %s\t", yytext);

\#.* 

\n fprintf(yyout, "\nNEWLINE ", yytext);

%%

int main(){
clock_t start, finish;
start = clock();

yyin = fopen("input.txt", "r");
yyout = fopen("outputFlex.txt", "w");
yylex(); 
finish = clock();
double timeToComplete = (double) (finish - start)/CLOCKS_PER_SEC;
printf("\n\nTotal Time %f",timeToComplete);
}
