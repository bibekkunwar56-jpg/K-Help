-- V4: Jobs and Housing schema
-- Pratigya Adhikari (Jobs & Housing Lead) ownership according to AGENT.md

CREATE TABLE jobs (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    employer_id UUID NOT NULL REFERENCES users(id),
    title VARCHAR(200) NOT NULL,
    company_name VARCHAR(150) NOT NULL,
    location VARCHAR(120) NOT NULL,
    visa_requirements VARCHAR(150),
    employment_type VARCHAR(50) NOT NULL DEFAULT 'FULL_TIME',
    salary_range VARCHAR(100),
    description TEXT NOT NULL,
    contact_email VARCHAR(255),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_jobs_location ON jobs(location);
CREATE INDEX idx_jobs_active ON jobs(is_active);
CREATE INDEX idx_jobs_created_at ON jobs(created_at DESC);

CREATE TABLE houses (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    landlord_id UUID NOT NULL REFERENCES users(id),
    title VARCHAR(200) NOT NULL,
    housing_type VARCHAR(50) NOT NULL DEFAULT 'ONE_ROOM',
    location VARCHAR(150) NOT NULL,
    deposit_krw BIGINT NOT NULL,
    monthly_rent_krw INT NOT NULL,
    maintenance_fee_krw INT DEFAULT 0,
    floor_level VARCHAR(30),
    description TEXT NOT NULL,
    contact_phone VARCHAR(50),
    is_available BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_houses_location ON houses(location);
CREATE INDEX idx_houses_available ON houses(is_available);
CREATE INDEX idx_houses_deposit ON houses(deposit_krw);
CREATE INDEX idx_houses_rent ON houses(monthly_rent_krw);
