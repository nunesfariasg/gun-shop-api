-- 1. Clientes (IDs 1 a 3)
INSERT INTO customers (name, cpf, birth_date, cr_number) VALUES
                                                             ('Carlos Eduardo Silva', '11122233344', '1988-04-12', 'CR-102030'), -- ID 1: Válido (+25 anos), já terá arma 9mm
                                                             ('Mariana Costa', '55566677788', '1995-09-23', 'CR-405060'),        -- ID 2: Válido (+25 anos), sem armas registradas ainda
                                                             ('Lucas Mendes', '99988877766', '2004-02-10', 'CR-708090');         -- ID 3: Menor de 25 anos (ideal para testar bloqueio de idade)

-- 2. Armas (IDs 1 a 4)
INSERT INTO weapons (model, brand, caliber, type, price, stock) VALUES
                                                                    ('G19 Gen5', 'Glock', '9mm', 'PISTOL', 6800.00, 5),         -- ID 1: Estoque normal
                                                                    ('T4 5.56', 'Taurus', '5.56 NATO', 'RIFLE', 12500.00, 2),   -- ID 2: Estoque baixo
                                                                    ('RT 85S', 'Taurus', '.38 SPL', 'REVOLVER', 4200.00, 0),    -- ID 3: Estoque zerado (ideal para testar bloqueio de estoque)
                                                                    ('Imbel MD1', 'Imbel', '.380 ACP', 'PISTOL', 5100.00, 4);   -- ID 4: Estoque normal

-- 3. Munições (IDs 1 a 4)
INSERT INTO ammunitions (brand, caliber, quantity_per_box, price, stock) VALUES
                                                                             ('CBC NTA', '9mm', 50, 320.00, 30),         -- ID 1: Compatível com a arma ID 1
                                                                             ('CBC Magtech', '5.56 NATO', 20, 280.00, 15), -- ID 2: Compatível com a arma ID 2
                                                                             ('CBC Gold', '.38 SPL', 50, 350.00, 10),    -- ID 3: Calibre sem dono cadastrado ainda
                                                                             ('CBC Treino', '.380 ACP', 50, 290.00, 1);  -- ID 4: Apenas 1 caixa em estoque

-- 4. Vendas Históricas (ID 1)
-- Carlos (ID 1) já comprou uma Glock 9mm (ID 1) anteriormente.
-- Isso habilita ele a comprar munição 9mm (ID 1) nos seus testes, mas impede de comprar 5.56 ou .38.
INSERT INTO sales (customer_id, sale_date, total_amount, status) VALUES
    (1, '2026-10-10 14:30:00', 6800.00, 'APPROVED');

-- 5. Itens da Venda Histórica
INSERT INTO sale_items (sale_id, weapon_id, ammunition_id, quantity, unit_price) VALUES
    (1, 1, NULL, 1, 6800.00);