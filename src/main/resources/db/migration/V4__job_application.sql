CREATE TABLE job_applications (
    id TEXT PRIMARY KEY,
    job_id TEXT NOT NULL REFERENCES jobs(id) ON DELETE CASCADE,
    applicant_id TEXT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    firstname TEXT NOT NULL,  -- First name of the applicant
    lastname TEXT NOT NULL,   -- Last name of the applicant
    phone_number VARCHAR(15) NOT NULL,  -- Contact number
    resume TEXT NOT NULL,
    expected_salary DECIMAL(10,2),
    availability_to_join VARCHAR(20) NOT NULL CHECK (availability_to_join IN ('Immediately', 'Within 15 Days', 'Within 30 Days', 'More than 30 Days')),
    portfolio_link TEXT,
    additional_comments TEXT,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'SHORTLISTED', 'REJECTED', 'HIRED')),
    applied_at BIGINT NOT NULL DEFAULT (extract(epoch from now()) * 1000)
);
