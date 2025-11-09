ALTER TABLE sensores
ADD CONSTRAINT chk_sensor_localizacao_format
CHECK (
  localizacao ~* '^Setor [A-Z] - Coluna [0-9]+$'
);

COMMENT ON CONSTRAINT chk_sensor_localizacao_format ON sensores IS
'Garante o formato "Setor <Letra> - Coluna <Número>" (ex: Setor A - Coluna 1)';
