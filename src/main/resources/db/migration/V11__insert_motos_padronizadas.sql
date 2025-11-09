DELETE FROM motos;

ALTER SEQUENCE motos_id_seq RESTART WITH 1;

INSERT INTO motos (modelo, cor, identificador_uwb, sensor_id, status, ativo) VALUES
('Honda CG 160 Titan', 'Preta', 'UWB001', 1, 'DISPONIVEL', TRUE),
('Yamaha Factor 150', 'Azul', 'UWB002', 2, 'ALOCADA', TRUE),
('Honda Biz 125', 'Vermelha', 'UWB003', 3, 'MANUTENCAO', TRUE),
('Yamaha YBR 125', 'Branca', 'UWB004', 4, 'DISPONIVEL', TRUE),
('Honda Pop 110i', 'Vermelha', 'UWB005', 5, 'DISPONIVEL', TRUE);

SELECT setval('motos_id_seq', (SELECT MAX(id) + 1 FROM motos));