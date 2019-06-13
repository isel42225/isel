use LS

drop table Tickets
drop table Sessions
drop table Theaters
drop table Cinemas
drop table Movies


create table Movies 
(
	mid int identity(1,1) not null,
	title nvarchar (50) not null,
	releaseYear int not null,
	duration int not null,
	PRIMARY KEY (mid),
)

create table Cinemas(
	cid int identity(1,1) not null,
	name nvarchar (50) not null,
	city nvarchar (50) not null,
	PRIMARY KEY (cid)	 
)

create table Theaters
(
	tid int identity(1,1) not null,
	cinemas_cid int not null,
	name nvarchar (50) not null,
	available_seats int not null,
	rows int not null,
	seats_per_row int not null,
	Primary key (tid),
	FOREIGN key (cinemas_cid) REFERENCES Cinemas(cid)
)

create table Sessions
(
	sid int identity(1,1) not null,
	cinema_id int not null,
	theater_id int not null,
	movie_id int not null,
	date_time smalldatetime not null, 

	PRIMARY KEY (sid),
	FOREIGN key (theater_id) REFERENCES Theaters(tid),
	FOREIGN KEY (cinema_id) REFERENCES Cinemas(cid),
	FOREIGN key (movie_id) REFERENCES Movies(mid)
)

create table Tickets
(
    tkid int identity(1,1) not null,
    session_id int not null,
    seat_number int not null,
    row nvarchar (50) not null,

    PRIMARY KEY (tkid),
    FOREIGN key (session_id) REFERENCES Sessions(sid),
)










	
	


