DELETE FROM auth.verification_token;
ALTER TABLE auth.verification_token ADD COLUMN uuid varchar(255) not null;