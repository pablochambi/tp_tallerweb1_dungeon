INSERT INTO Usuario
  (id, email, password, rol, activo, nombre, vida, atk, defensa, oro)
VALUES
  (1, 'test@unlam.edu.ar', 'test', 'ADMIN', TRUE,
   'Héroe', 1000, 10, FALSE, 1000),
  (2, 'otro@ejemplo.com', '1234', 'ADMIN', TRUE,
   'Aventurero', 800, 8, FALSE, 500);


INSERT INTO Heroe(id,nombre,nivel,precio,urlImagen)VALUES (1,'Cruzado',1,300,'/imagenes/cruzado.webp');
INSERT INTO Heroe(id,nombre,nivel,precio,urlImagen)VALUES (2,'Vestal',1,200,'/imagenes/Vestal.webp');


INSERT INTO monster (id, nombre, vida, atk, imagen) VALUES
  (5, 'Orco',       60, 10, '&#x1F9DD;&#x200D;&#x2642;&#xFE0F;'),
  (6, 'Mago',       35, 12, '&#x1F9D9;&#x200D;&#x2642;&#xFE0F;'),
  (7, 'Vampiro',    55, 14, '&#x1F9DB;&#x200D;&#x2642;&#xFE0F;'),
  (8, 'Golem',      80,  7, '&#x1F9D9;&#x200D;&#x2640;&#xFE0F;'),
  (9, 'Dragón',     100,20, '&#x1F409;'),
  (10,'Demonio',    70, 18, '&#x1F47F;');

INSERT INTO Item (id, nombre, tipo, precio) VALUES
                                                (1, 'Poción de Vida',  'POCION',    100),
                                                (2, 'Poción de Estrés','POCION',   80),
                                                (3, 'Espada Rota',     'ARMA',  150),
                                                (4, 'Armadura Ligera', 'ARMA', 120);


INSERT INTO Inventario (id, usuario_id) VALUES (1, 1), (2, 2);
UPDATE Usuario SET inventario_id = 1 WHERE id = 1;
UPDATE Usuario SET inventario_id = 2 WHERE id = 2;
