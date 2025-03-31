CREATE TABLE jobs (
    id TEXT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    company VARCHAR(255) NOT NULL,
    location VARCHAR(255) NOT NULL,
    salary DECIMAL(10,2) NOT NULL,
    remote BOOLEAN NOT NULL DEFAULT FALSE,
    job_type VARCHAR(50) NOT NULL,  -- Full-time, Part-time, Contract
    required_skills TEXT[],  -- Array of skills
    education_level VARCHAR(100),  -- Bachelor's, Master's, etc.
    language_requirement VARCHAR(100),  -- English, Hindi, etc.
    posted_by TEXT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    created_at BIGINT NOT NULL DEFAULT (extract(epoch from now()) * 1000),
    updated_at BIGINT NOT NULL DEFAULT (extract(epoch from now()) * 1000)
);
