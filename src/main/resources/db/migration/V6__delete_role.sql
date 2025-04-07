ALTER TABLE users
DROP COLUMN role;
-- 1. Drop the CHECK constraint on user_type
ALTER TABLE users DROP CONSTRAINT IF EXISTS users_user_type_check;

-- 2. Drop the DEFAULT value
ALTER TABLE users ALTER COLUMN user_type DROP DEFAULT;

-- 3. Make the column nullable
ALTER TABLE users ALTER COLUMN user_type DROP NOT NULL;
