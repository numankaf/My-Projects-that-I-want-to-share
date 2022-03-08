This is a sample IMBD system Project written in Java language using OOP principles.

All data input files (people and films) will be processed according to the commands which
will be given in a commands file. The command file contains 12 types of commands whose
definitions and formats are given below.
1. A user can rate a film so that film will be saved to his/her rate list. Rating score must
be between 1 and 10 integers.
RATE<tab><USERID><tab><FILMID><tab><RATINGPOINT>
2. It’s possible to add a new Feature Film to the system.
ADD<tab>FEATUREFILM<tab><ID><tab><TITLE><tab><LANGUAGE><tab> <DIRECTOR1ID,..., DIRECTORnID><tab><LENGTH><tab><COUNTRY><tab> <PERFORMER1ID,...,PERFORMERnID> <tab><GENRE1,...,GENREn><tab> <RELEASEDATE><tab><WRITER1ID,...,WRITERnID><tab> <BUDGET>
3. Details of a film are displayed by using below command.
VIEWFILM<tab><FILMID>
4. A user can list all films which he/she rated so far.
LIST<tab>USER<tab><USERID><tab>RATES
5. A user can edit a film which he/she rated before.
EDIT<tab>RATE<tab><USERID><tab><FILMID><tab><NEWRATINGPOINT>
6. A user can remove one of his/her rated films.
REMOVE<tab>RATE<tab><USERID><tab><FILMID>
7. List all the TV Series in the system.
LIST<tab>FILM<tab>SERIES
8. List all the films from a specified country.
LIST<tab>FILMS<tab>BY<tab>COUNTRY<tab><COUNTRY>
9. List all the films released before a specified year.
LIST<tab>FEATUREFILMS<tab>BEFORE<tab><YEAR>
Programming Assignment 2 6
BBM104: Introduction to Programming Laboratory II (Spring 2021)
10. List all the films released after a specified year.
LIST<tab>FEATUREFILMS<tab>AFTER<tab><YEAR>
11. List all the films in descending order and categorized according to film rating degrees.
LIST<tab>FILMS<tab>BY<tab>RATE<tab>DEGREE
12. List all the artists from a specified country and display in a categorized order.
LIST<tab>ARTISTS<tab>FROM<tab><COUNTRY>