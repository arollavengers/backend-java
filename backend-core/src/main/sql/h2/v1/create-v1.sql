create table streams
(
  stream_id      varchar(36) not null,
  stream_version integer not null,
  constraint streams_pkey primary key (stream_id)
);

create table stream_events
(
  stream_id      varchar(36) not null,
  event_id       integer not null,
  event_data     text,
  constraint streamevts_pkey primary key (stream_id, event_id),
  constraint streamevts_fk1  foreign key (stream_id) references streams(stream_id)
);

create index streamevts_streamid on stream_events (stream_id);
