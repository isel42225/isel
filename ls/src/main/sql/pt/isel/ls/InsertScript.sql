use LS_TEST

insert into Movies values ('M1', 2000, 90)
insert into Movies values ('M2', 2001, 120)
insert into Movies values ('M3', 2003, 30)

insert into Cinemas values ('C1', 'Lisbon')
insert into Cinemas values ('C2', 'Porto')


insert into Theaters values (1,'T1C1', 500, 50, 10)
insert into Theaters values (1,'T2C1', 100, 10, 10)
insert into Theaters values (2,'T1C2', 300, 30, 10)


insert into Sessions values (1, 1, 1, '2018-12-03 12:43:10')
insert into Sessions values (1, 2, 2, '2018-11-23 12:15:10')
insert into Sessions values (2, 3, 1, '2018-11-23 12:15:10')

insert into Tickets values (1, 1, 'A')
insert into Tickets values (2, 1, 'B')
insert into Tickets values (3, 2, 'A')