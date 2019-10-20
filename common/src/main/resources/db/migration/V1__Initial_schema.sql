create table users
(
    username varchar(50)  not null,
    password varchar(100) not null,
    enabled  boolean      not null default true,
    primary key (username)
);

create table authorities
(
    id        integer      not null auto_increment,
    username  varchar(100) not null,
    authority varchar(50)  not null,
    primary key (id),
    foreign key (username) references users (username)
);

create table Account
(
    id      varchar(50) not null,
    owner   varchar(50) not null,
    balance double      not null,
    created timestamp   not null,
    ended   timestamp   null,
    primary key (id),
    foreign key (owner) references users (username)
);

create table Card
(
    id                 varchar(50)  not null,
    cardNumber         integer      not null,
    status             varchar(100) not null,
    sequenceNumber     integer      not null,
    cardHolder         varchar(50)  not null,

    #discriminator
    cardType           varchar(20)  not null,

    #CreditCard specific
    monthlyLimit       double       null,

    #DebitCard specific
    atmLimitValue      double       null,
    atmLimitPeriodUnit varchar(100) null,
    posLimitValue      double       null,
    posLimitPeriodUnit varchar(100) null,
    contactless        boolean      null,

    primary key (id),
    foreign key (cardHolder) references users (username)
);

create table Poa
(
    id             varchar(50)  not null,
    grantor        varchar(50)  not null,
    grantee        varchar(50)  not null,
    account        varchar(100) not null,
    direction      varchar(20)  not null,
    authorizations varchar(200) not null,
    primary key (id),
    foreign key (grantor) references users (username),
    foreign key (grantee) references users (username)
);

    create table Poa_Card
(
    poa_id  varchar(50) not null,
    card_id varchar(50) not null,
    primary key (poa_id, card_id),
    foreign key (poa_id) references Poa (id),
    foreign key (card_id) references Card (id)
);
