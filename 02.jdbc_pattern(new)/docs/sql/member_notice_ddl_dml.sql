-- master : member table
-- 테이블레벨 단일컬럼 식별키 제약 지정
-- 1단계
create table member (
	member_id varchar2(30),
	member_pw varchar2(20) not null,
	name varchar2(20),
	mobile varchar2(13) not null,
	email varchar2(30) not null,
	entry_date varchar2(10) not null,
	grade varchar2(1) not null,
	mileage number(6),
	manager varchar2(20),
	CONSTRAINT PK_MEMBER_ID PRIMARY KEY (MEMBER_ID)
);

-- detail : notice table
-- 게시글 테이블레벨 참조키 지정 	
CREATE table NOTICE (
	notice_no number(8),
    title varchar2(30) not null,
    contents varchar2(500),
    MEMBER_ID varchar2(30),
    write_date date not null,
    hit_count number(10),
    CONSTRAINT PK_NOTICE_notice_no PRIMARY KEY (notice_no),
	constraint fk_memberid foreign key (MEMBER_ID) references member(member_id)
);

-- 2단계 : 제약 추가 변경
-- master : member table
create table member (
	member_id varchar2(30),
	member_pw varchar2(20) not null,
	name varchar2(20),
	mobile varchar2(13) not null,
	email varchar2(30) not null,
	entry_date varchar2(10) not null,
	grade varchar2(1) not null,
	mileage number(6),
	manager varchar2(20)
);

-- constraint add : pk
alter table member
add constraint pk_memberid primary key (member_id);

-- constraint add : mobile 중복불가, NULL 허용
alter table member 
add constraint UK_mobile unique (mobile);

-- detail : notice table
CREATE table NOTICE (
	notice_no number(8),
    title varchar2(30) not null,
    contents varchar2(500),
    MEMBER_ID varchar2(30),
    write_date date not null,
    hit_count number(10)
);

-- constraint add : pk
alter table notice
add CONSTRAINT PK_NOTICE_notice_no PRIMARY KEY (notice_no);

-- constraint add : fk
alter table notice
add constraint fk_memberid foreign key (MEMBER_ID) references member(member_id);

























