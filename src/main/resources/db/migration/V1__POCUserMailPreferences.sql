CREATE TABLE user_mail_preferences
(
    id                 BIGINT   NOT NULL,
    user_id            BIGINT   NULL,
    mail_greeting_type SMALLINT NULL,
    CONSTRAINT pk_usermailpreferences PRIMARY KEY (id)
);

ALTER TABLE user_mail_preferences
    ADD CONSTRAINT uc_usermailpreferences_userid UNIQUE (user_id);