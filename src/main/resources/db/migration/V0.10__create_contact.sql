CREATE TABLE contact (
    id bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    company text NOT NULL,
    name text,
    surname text,
    website text,
    country text NOT NULL,
    skype text,
    viber bigint,
    whats_app bigint,
    we_chat text,
    linked_in text,
    business_type text NOT NULL,
    comments text
);