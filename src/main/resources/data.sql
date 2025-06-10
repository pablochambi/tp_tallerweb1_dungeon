INSERT INTO Usuario(id, email, password, rol, activo) VALUES(1, 'test@unlam.edu.ar', 'test', 'ADMIN', true);

INSERT INTO jugador (id, nombre, vida, atk, defensa, oro)
  VALUES (1, 'Héroe', 100, 10, FALSE, 1000);

INSERT INTO Heroe (id,nombre,precio,urlImagen)
VALUES (1, 'Cruzado', 300, '/imagenes/cruzado.webp'),
       (2, 'Vestal', 200, '/imagenes/Vestal.webp');

INSERT INTO monster (id, nombre, vida, atk) VALUES
  (1, 'Esqueleto', 30, 5),
  (2, 'Goblin',    50, 8),
  (3, 'Araña',     20, 3),
  (4, 'Zombi',     40, 6);

INSERT INTO items (id, nombre, tipo, precio) VALUES
  (1, 'Poción de Vida',  'cura',    100),
  (2, 'Poción de Estrés','estrés',   80),
  (3, 'Espada Rota',     'ataque',  150),
  (4, 'Armadura Ligera', 'defensa', 120);