ALTER TABLE users
    ADD COLUMN user_type VARCHAR(20) NOT NULL DEFAULT 'jobseeker' CHECK (user_type IN ('jobseeker', 'hr')),
    ADD COLUMN company_name VARCHAR(255),
    ADD COLUMN company_website VARCHAR(255),
    ADD COLUMN industry VARCHAR(100),
    ADD COLUMN company_location VARCHAR(255);
