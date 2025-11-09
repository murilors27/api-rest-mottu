DELETE FROM sensores;

ALTER SEQUENCE sensores_id_seq RESTART WITH 1;

INSERT INTO sensores (localizacao) VALUES
('Setor A - Coluna 1'),
('Setor B - Coluna 2'),
('Setor C - Coluna 3'),
('Setor D - Coluna 4'),
('Setor E - Coluna 5'),
('Setor F - Coluna 6'),
('Setor G - Coluna 7'),
('Setor H - Coluna 8'),
('Setor I - Coluna 9');

SELECT setval('sensores_id_seq', (SELECT MAX(id) + 1 FROM sensores));
