INSERT INTO users (id, username, password, created_at, unlocked, enabled)
VALUES (nextval('users_id_seq'), 'hello@axgr.dev', '$2a$10$IwPAtQ5y3FMxByigKzy4ou49fqFl4.A4QUNHYfcxsP/a7p1FdjP8i',
        current_timestamp, true, true);

INSERT INTO authorities (id, username, authority)
VALUES (nextval('authorities_id_seq'), 'hello@axgr.dev', 'ALL_ACCESS');
