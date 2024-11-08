-- Eliminar tablas si existen
DROP TABLE IF EXISTS Pagos CASCADE;
DROP TABLE IF EXISTS Devoluciones CASCADE;
DROP TABLE IF EXISTS Ventas CASCADE;
DROP TABLE IF EXISTS Inventarios CASCADE;
DROP TABLE IF EXISTS Compras CASCADE;
DROP TABLE IF EXISTS Productos CASCADE;
DROP TABLE IF EXISTS Usuarios CASCADE;

-- Crear tabla Usuarios con los campos solicitados
CREATE TABLE Usuarios (
                          id SERIAL PRIMARY KEY,
                          nombre VARCHAR(100) NOT NULL,
                          email VARCHAR(100),
                          telefono VARCHAR(15),
                          direccion TEXT,
                          tipo VARCHAR(50) CHECK (tipo IN ('administrativo', 'proveedor', 'cliente')) NOT NULL
);

-- Crear tabla Productos sin proveedor_id y con stock predeterminado a 0
CREATE TABLE Productos (
                           id SERIAL PRIMARY KEY,
                           nombre VARCHAR(100) NOT NULL,
                           descripcion TEXT,
                           precio DECIMAL(10, 2) NOT NULL,
                           stock INT DEFAULT 0 NOT NULL
);

-- Crear tabla Compras
CREATE TABLE Compras (
                         id SERIAL PRIMARY KEY,
                         usuario_id INT REFERENCES Usuarios(id),
                         producto_id INT REFERENCES Productos(id),
                         cantidad INT NOT NULL,
                         fecha_compra VARCHAR(10) NOT NULL,
                         monto_total DECIMAL(10, 2) NOT NULL
);

-- Crear tabla Inventarios
CREATE TABLE Inventarios (
                             id SERIAL PRIMARY KEY,
                             producto_id INT REFERENCES Productos(id),
                             cantidad INT NOT NULL,
                             tipo_movimiento VARCHAR(10) CHECK (tipo_movimiento IN ('ingreso', 'salida')) NOT NULL,
                             fecha_movimiento VARCHAR(10) NOT NULL
);

-- Crear tabla Ventas
CREATE TABLE Ventas (
                        id SERIAL PRIMARY KEY,
                        usuario_id INT REFERENCES Usuarios(id),
                        producto_id INT REFERENCES Productos(id),
                        cantidad INT NOT NULL,
                        fecha_venta VARCHAR(10) NOT NULL,
                        direccion_envio TEXT NOT NULL,
                        monto_total DECIMAL(10, 2) NOT NULL
);

-- Crear tabla Devoluciones
CREATE TABLE Devoluciones (
                              id SERIAL PRIMARY KEY,
                              venta_id INT REFERENCES Ventas(id),
                              motivo TEXT NOT NULL,
                              fecha_devolucion VARCHAR(10) NOT NULL
);

-- Crear tabla Pagos
CREATE TABLE Pagos (
                       id SERIAL PRIMARY KEY,
                       venta_id INT REFERENCES Ventas(id),
                       monto DECIMAL(10, 2) NOT NULL,
                       fecha VARCHAR(10) NOT NULL,
                       metodo VARCHAR(50) CHECK (metodo IN ('efectivo', 'transferencia', 'tarjeta', 'qr')) NOT NULL
);

-- Inserción de datos en la tabla Usuarios
INSERT INTO Usuarios (nombre, email, telefono, direccion, tipo) VALUES
                                                                    ('Juan Pérez', 'juan.perez@example.com', '1234567890', 'Calle Falsa 123', 'administrativo'),
                                                                    ('Ana Gómez', 'ana.gomez@example.com', '1234567891', 'Avenida Real 456', 'cliente'),
                                                                    ('Carlos Ruiz', 'carlos.ruiz@example.com', '1234567892', 'Plaza Mayor 789', 'cliente'),
                                                                    ('Luisa Fernández', 'luisa.fernandez@example.com', '1234567893', 'Gran Vía 1010', 'cliente'),
                                                                    ('María López', 'maria.lopez@example.com', '1234567894', 'Camino Largo 1111', 'administrativo'),
                                                                    ('Pedro Morales', 'pedro.morales@example.com', '1234567895', 'Calle Estrecha 1212', 'proveedor'),
                                                                    ('Miguel Sánchez', 'miguel.sanchez@example.com', '1234567896', 'Avenida Ancha 1313', 'cliente'),
                                                                    ('Sofía Hernández', 'sofia.hernandez@example.com', '1234567897', 'Calle Cortada 1414', 'cliente'),
                                                                    ('Laura Castro', 'laura.castro@example.com', '1234567898', 'Plaza Nueva 1515', 'proveedor'),
                                                                    ('Pablo Ortiz', 'pablo.ortiz@example.com', '1234567899', 'Paseo Marítimo 1616', 'administrativo');

-- Inserción de datos en la tabla Productos sin proveedor_id
INSERT INTO Productos (nombre, descripcion, precio, stock) VALUES
                                                               ('iPhone 13', 'Smartphone Apple iPhone 13', 999.99, 50),
                                                               ('Samsung Galaxy S21', 'Smartphone Samsung Galaxy S21', 799.99, 30),
                                                               ('Cargador USB-C', 'Cargador rápido USB-C', 19.99, 100),
                                                               ('Funda iPhone 13', 'Funda protectora para iPhone 13', 15.99, 150),
                                                               ('Auriculares Bluetooth', 'Auriculares inalámbricos', 49.99, 75),
                                                               ('Xiaomi Mi 11', 'Smartphone Xiaomi Mi 11', 699.99, 40),
                                                               ('Protector de Pantalla', 'Protector de pantalla de vidrio templado', 9.99, 200),
                                                               ('Cable Lightning', 'Cable de carga Lightning para iPhone', 12.99, 120),
                                                               ('Batería Externa', 'Batería externa portátil 10000mAh', 29.99, 60),
                                                               ('OnePlus 9', 'Smartphone OnePlus 9', 649.99, 20);

-- Inserción de datos en la tabla Compras
INSERT INTO Compras (usuario_id, producto_id, cantidad, fecha_compra, monto_total) VALUES
                                                                                       (2, 1, 1, '2024-01-10', 999.99),
                                                                                       (3, 3, 2, '2024-01-11', 39.98),
                                                                                       (4, 5, 1, '2024-01-12', 49.99),
                                                                                       (5, 2, 1, '2024-01-13', 799.99),
                                                                                       (6, 4, 10, '2024-01-14', 159.90),
                                                                                       (3, 7, 2, '2024-01-15', 19.98),
                                                                                       (2, 9, 1, '2024-01-16', 29.99),
                                                                                       (8, 6, 1, '2024-01-17', 699.99),
                                                                                       (9, 8, 5, '2024-01-18', 64.95),
                                                                                       (10, 10, 1, '2024-01-19', 649.99);

-- Inserción de datos en la tabla Inventarios
INSERT INTO Inventarios (producto_id, cantidad, tipo_movimiento, fecha_movimiento) VALUES
                                                                                       (1, 50, 'ingreso', '2024-01-01'),
                                                                                       (2, 30, 'ingreso', '2024-01-01'),
                                                                                       (3, 100, 'ingreso', '2024-01-01'),
                                                                                       (4, 150, 'ingreso', '2024-01-01'),
                                                                                       (5, 75, 'ingreso', '2024-01-01'),
                                                                                       (6, 40, 'ingreso', '2024-01-01'),
                                                                                       (7, 200, 'ingreso', '2024-01-01'),
                                                                                       (8, 120, 'ingreso', '2024-01-01'),
                                                                                       (9, 60, 'ingreso', '2024-01-01'),
                                                                                       (10, 20, 'ingreso', '2024-01-01');

-- Inserción de datos en la tabla Ventas
INSERT INTO Ventas (usuario_id, producto_id, cantidad, fecha_venta, direccion_envio, monto_total) VALUES
                                                                                                      (2, 1, 1, '2024-02-01', '123 Calle Falsa', 999.99),
                                                                                                      (3, 2, 1, '2024-02-02', '456 Avenida Real', 799.99),
                                                                                                      (4, 3, 2, '2024-02-03', '789 Plaza Mayor', 39.98),
                                                                                                      (5, 5, 1, '2024-02-04', '1010 Gran Vía', 49.99),
                                                                                                      (6, 7, 1, '2024-02-05', '1111 Camino Largo', 19.99),
                                                                                                      (7, 6, 1, '2024-02-06', '1212 Calle Estrecha', 699.99),
                                                                                                      (8, 4, 3, '2024-02-07', '1313 Avenida Ancha', 47.97),
                                                                                                      (9, 9, 1, '2024-02-08', '1414 Calle Cortada', 29.99),
                                                                                                      (10, 8, 2, '2024-02-09', '1515 Plaza Nueva', 25.98),
                                                                                                      (2, 10, 1, '2024-02-10', '1616 Paseo Marítimo', 649.99);

-- Inserción de datos en la tabla Devoluciones
INSERT INTO Devoluciones (venta_id, motivo, fecha_devolucion) VALUES
                                                                  (1, 'Producto defectuoso', '2024-03-01'),
                                                                  (3, 'No es compatible', '2024-03-02'),
                                                                  (5, 'Falla de fabricación', '2024-03-03'),
                                                                  (7, 'Producto dañado en envío', '2024-03-04'),
                                                                  (9, 'No cumple con las expectativas', '2024-03-05');

-- Inserción de datos en la tabla Pagos
INSERT INTO Pagos (venta_id, monto, fecha, metodo) VALUES
                                                       (1, 999.99, '2024-02-01', 'tarjeta'),
                                                       (2, 799.99, '2024-02-02', 'transferencia'),
                                                       (3, 39.98, '2024-02-03', 'transferencia'),
                                                       (4, 49.99, '2024-02-04', 'tarjeta'),
                                                       (5, 19.99, '2024-02-05', 'efectivo'),
                                                       (6, 699.99, '2024-02-06', 'transferencia'),
                                                       (7, 47.97, '2024-02-07', 'tarjeta'),
                                                       (8, 29.99, '2024-02-08', 'qr'),
                                                       (9, 25.98, '2024-02-09', 'qr'),
                                                       (10, 649.99, '2024-02-10', 'tarjeta');
