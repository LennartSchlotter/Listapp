CREATE TABLE IF NOT EXISTS list_items (
  id BIGSERIAL PRIMARY KEY,
  list_id BIGINT NOT NULL,
  position INT NOT NULL DEFAULT 0,
  title VARCHAR(255) NOT NULL,
  description TEXT,
  metadata JSONB,
  image_path VARCHAR(1024),
  created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
  updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
  CONSTRAINT fk_items_list FOREIGN KEY (list_id)
    REFERENCES lists (id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_list_items_list_id_position ON list_items (list_id, position);