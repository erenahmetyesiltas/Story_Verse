-- Email will be UNIQUE
ALTER TABLE `user`
ADD CONSTRAINT unique_email UNIQUE (email);