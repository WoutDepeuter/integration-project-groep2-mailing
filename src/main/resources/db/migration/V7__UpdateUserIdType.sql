ALTER TABLE mailing_service.user_mail_preferences DROP COLUMN user_id;
ALTER TABLE mailing_service.user_mail_preferences ADD COLUMN user_id VARCHAR(255) UNIQUE;
ALTER TABLE mailing_service.user_mail_preferences ADD COLUMN first_name VARCHAR(255);
ALTER TABLE mailing_service.user_mail_preferences ADD COLUMN last_name VARCHAR(255);
ALTER TABLE mailing_service.user_mail_preferences RENAME mail_user;