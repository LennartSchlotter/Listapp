CREATE TABLE IF NOT EXISTS lists (
  id BIGSERIAL PRIMARY KEY,
  owner_id BIGINT NOT NULL,
  title VARCHAR(255) NOT NULL,
  description TEXT,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
  updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
  CONSTRAINT fk_lists_owner FOREIGN KEY (owner_id)
    REFERENCES users (id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_lists_owner_id ON lists (owner_id);
