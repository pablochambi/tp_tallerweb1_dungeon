INSERT INTO Usuario (id, email, password, rol, activo, nombre, oro)
VALUES
  (1, 'test@unlam.edu.ar', 'test', 'ADMIN', TRUE,  'Héroe',      1000),
  (2, 'otro@ejemplo.com',   '1234','ADMIN', TRUE,  'Aventurero',  500);

INSERT INTO heroe
  (id, nombre, nivel, precio, url_imagen, max_vida, atk, defensa_base)
VALUES
  (1, 'Cruzado', 1, 300, '/imagenes/cruzado.webp', 1000, 35, 5),
  (2, 'Vestal',   1, 200, '/imagenes/Vestal.webp',   750, 25, 8),
  (3, 'Cazador', 1, 200 ,'/imagenes/Brigand_Hunter.webp', 800, 30, 10);

INSERT INTO monster (id, nombre, vida, atk, imagen) VALUES
  (5, 'Orco',       60, 10, '&#x1F9DD;&#x200D;&#x2642;&#xFE0F;'),
  (6, 'Mago',       35, 12, '&#x1F9D9;&#x200D;&#x2642;&#xFE0F;'),
  (7, 'Vampiro',    55, 14, '&#x1F9DB;&#x200D;&#x2642;&#xFE0F;'),
  (8, 'Golem',      80,  7, '&#x1F9D9;&#x200D;&#x2640;&#xFE0F;'),
  (9, 'Dragón',     100,20, '&#x1F409;'),
  (10,'Demonio',    70, 18, '&#x1F47F;');

INSERT INTO items (id, nombre, tipo, precio) VALUES
  (1, 'Poción de Vida',  'cura',    100),
  (2, 'Poción de Estrés','estrés',   80),
  (3, 'Espada Rota',     'ataque',  150),
  (4, 'Armadura Ligera', 'defensa', 120);

INSERT INTO game_session
  (id, usuario_id, turno, nivel, active, finished, started_at, ended_at)
VALUES
  (
    1,       -- id
    1,       -- usuario_id (debe existir en Usuario)
    1,       -- turno
    1,       -- nivel
    1,       -- active  (TRUE)
    0,       -- finished (FALSE)
    '2025-06-24 13:00:00',
    NULL
  );

-- estado de monstruos en la mazmorra 1 de la expedición 1
INSERT INTO session_monster
  (session_id, monster_id, vida_actual, expedition_number, dungeon_number, orden)
VALUES
  (1, 5,  60, 1, 1, 1),  -- Orco
  (1, 6,  35, 1, 1, 2),  -- Mago
  (1, 7,  55, 1, 1, 3);  -- Vampiro
