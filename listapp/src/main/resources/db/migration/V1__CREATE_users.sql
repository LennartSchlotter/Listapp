CREATE TABLE IF NOT EXISTS users (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  oauth2_provider VARCHAR(100) NOT NULL,
  oauth2_sub VARCHAR(255) NOT NULL,
  name VARCHAR(64) NOT NULL,
  email VARCHAR(255) NOT NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  deleted BOOLEAN NOT NULL DEFAULT false,
  version BIGINT NOT NULL DEFAULT 0

  CONSTRAINT uq_users_oauth2 UNIQUE (oauth2_provider, oauth2_sub),
  CONSTRAINT uq_users_name UNIQUE (name),
  CONSTRAINT uq_users_email UNIQUE (email)
);