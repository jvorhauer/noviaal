CREATE TABLE IF NOT EXISTS users
(
    id       UUID PRIMARY KEY,
    email    VARCHAR(1024),
    password VARCHAR(255) NOT NULL,
    nickname VARCHAR(255),
    created  TIMESTAMP WITH TIME ZONE,
    enabled  BOOLEAN,
    role     VARCHAR(12)
);

CREATE TABLE IF NOT EXISTS notes
(
    id      UUID PRIMARY KEY,
    created TIMESTAMP WITH TIME ZONE,
    title   VARCHAR(255) NOT NULL,
    body    TEXT,
    user_id UUID REFERENCES users(id)
);
