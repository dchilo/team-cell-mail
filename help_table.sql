\-- Creación de la tabla Help
DROP TABLE IF EXISTS Help;
CREATE TABLE Help (
                      id SERIAL PRIMARY KEY,
                      cu VARCHAR(100) NOT NULL,
                      accion VARCHAR(255) NOT NULL,
                      parametros VARCHAR(255) NOT NULL,
                      ejemplo TEXT
);

-- Inserción de datos en la tabla Help
INSERT INTO Help (cu, accion, parametros, ejemplo) VALUES
-- CU1: Gestionar Usuarios
('CU1: Gestionar Usuarios', 'Listar Usuarios', 'usuario mostrar', 'usuario mostrar'),
('CU1: Gestionar Usuarios', 'Registrar Usuario (tipo solo puede ser "cliente", "administrativo" o "proveedor")', 'usuario agregar [nombre; email; telefono; direccion; tipo]', 'usuario agregar [Juan Pérez; juan.perez@example.com; 1234567890; Calle Falsa 123; administrativo]'),
('CU1: Gestionar Usuarios', 'Modificar Usuario (tipo solo puede ser cliente, administrativo o proveedor)', 'usuario modificar [usuario_id; nombre; email; telefono; direccion; tipo]', 'usuario modificar [1; Juan Pérez; juan.perez@example.com; 1234567890; Calle Falsa 123; administrativo]'),
('CU1: Gestionar Usuarios', 'Eliminar Usuario', 'usuario eliminar [usuario_id]', 'usuario eliminar [1]'),
('CU1: Gestionar Usuarios', 'Ver Usuario', 'usuario ver [usuario_id]', 'usuario ver [1]'),
('CU1: Gestionar Usuarios', 'Reporte de Usuarios', 'usuario reporte', 'usuario reporte'),

-- CU2: Gestionar Productos
('CU2: Gestionar Productos', 'Listar Productos', 'producto mostrar', 'producto mostrar'),
('CU2: Gestionar Productos', 'Registrar Producto (El stock inicia en 0, realizar una compra para aumentar Stock)', 'producto agregar [nombre; descripcion; precio]', 'producto agregar [6; iPhone 13; Smartphone Apple iPhone 13; 999.99; 50]'),
('CU2: Gestionar Productos', 'Modificar Producto', 'producto modificar [proveedor_id; nombre; descripcion; precio]', 'producto modificar [1; 6; iPhone 13; Smartphone Apple iPhone 13 actualizado; 999.99; 45]'),
('CU2: Gestionar Productos', 'Eliminar Producto', 'producto eliminar [producto_id]', 'producto eliminar [1]'),
('CU2: Gestionar Productos', 'Ver Producto', 'producto ver [producto_id]', 'producto ver [1]'),
('CU2: Gestionar Productos', 'Reporte de Productos', 'producto reporte', 'producto reporte'),

-- CU3: Gestionar Compras
('CU3: Gestionar Compras', 'Listar Compras', 'compra mostrar', 'compra mostrar'),
('CU3: Gestionar Compras', 'Registrar Compra (Al registrar una compra se crea un registro de inventario de tipo ingreso, la compra se realiza a un usuario con tipo "proveedor")', 'compra agregar [proveedor_id; producto_id; cantidad; fecha_compra]', 'compra agregar [2; 1; 1; 2024-01-10]'),
('CU3: Gestionar Compras', 'Modificar Compra', 'compra modificar [compra_id; proveedor_id; producto_id; cantidad; fecha_compra]', 'compra modificar [1; 2; 1; 1; 2024-01-11]'),
('CU3: Gestionar Compras', 'Eliminar Compra', 'compra eliminar [compra_id]', 'compra eliminar [1]'),
('CU3: Gestionar Compras', 'Ver Compra', 'compra ver [compra_id]', 'compra ver [1]'),
('CU3: Gestionar Compras', 'Reporte de Compras', 'compra reporte', 'compra reporte'),

-- CU4: Gestionar Inventarios
('CU4: Gestionar Inventarios', 'Listar Inventarios', 'inventario mostrar', 'inventario mostrar'),
('CU4: Gestionar Inventarios', 'Registrar Movimiento (tipo movimiento solo puede ser "ingreso" o "salida")', 'inventario agregar [producto_id; cantidad; tipo_movimiento; fecha_movimiento]', 'inventario agregar [1; 50; ingreso; 2024-01-01]'),
('CU4: Gestionar Inventarios', 'Modificar Movimiento (tipo movimiento solo puede ser "ingreso" o "salida")', 'inventario modificar [inventario_id; producto_id; cantidad; tipo_movimiento; fecha_movimiento]', 'inventario modificar [1; 1; 50; ingreso; 2024-01-01]'),
('CU4: Gestionar Inventarios', 'Eliminar Movimiento', 'inventario eliminar [inventario_id]', 'inventario eliminar [1]'),
('CU4: Gestionar Inventarios', 'Ver Movimiento', 'inventario ver [inventario_id]', 'inventario ver [1]'),
('CU4: Gestionar Inventarios', 'Reporte de Inventarios', 'inventario reporte', 'inventario reporte'),

-- CU5: Gestionar Ventas
('CU5: Gestionar Ventas', 'Listar Ventas', 'venta mostrar', 'venta mostrar'),
('CU5: Gestionar Ventas', 'Registrar Venta "Al registrar una venta se crea un registro de inventario de tipo salida, el usuario debe ser de tipo "cliente"', 'venta agregar [usuario_id; producto_id; cantidad; fecha_venta; direccion_envio]', 'venta agregar [2; 1; 1; 2024-02-01; 123 Calle Falsa]'),
('CU5: Gestionar Ventas', 'Modificar Venta', 'venta modificar [venta_id; usuario_id; producto_id; cantidad; fecha_venta; direccion_envio]', 'venta modificar [1; 2; 1; 1; 2024-02-01; 123 Calle Falsa]'),
('CU5: Gestionar Ventas', 'Eliminar Venta', 'venta eliminar [venta_id]', 'venta eliminar [1]'),
('CU5: Gestionar Ventas', 'Ver Venta', 'venta ver [venta_id]', 'venta ver [1]'),
('CU5: Gestionar Ventas', 'Reporte de Ventas', 'venta reporte', 'venta reporte'),

-- CU6: Gestionar Devoluciones
('CU6: Gestionar Devoluciones', 'Listar Devoluciones', 'devolucion mostrar', 'devolucion mostrar'),
('CU6: Gestionar Devoluciones', 'Registrar Devolución', 'devolucion agregar [venta_id; motivo; fecha_devolucion]', 'devolucion agregar [1; Producto defectuoso; 2024-03-01]'),
('CU6: Gestionar Devoluciones', 'Modificar Devolución', 'devolucion modificar [devolucion_id; venta_id; motivo; fecha_devolucion]', 'devolucion modificar [1; 1; Producto defectuoso; 2024-03-01]'),
('CU6: Gestionar Devoluciones', 'Eliminar Devolución', 'devolucion eliminar [devolucion_id]', 'devolucion eliminar [1]'),
('CU6: Gestionar Devoluciones', 'Ver Devolución', 'devolucion ver [devolucion_id]', 'devolucion ver [1]'),
('CU6: Gestionar Devoluciones', 'Reporte de Devoluciones', 'devolucion reporte', 'devolucion reporte'),

-- CU7: Gestionar Pagos
('CU7: Gestionar Pagos', 'Listar Pagos', 'pago mostrar', 'pago mostrar'),
('CU7: Gestionar Pagos', 'Registrar Pago', 'pago agregar [venta_id; monto; fecha; metodo]', 'pago agregar [1; 999.99; 2024-02-01; tarjeta]'),
('CU7: Gestionar Pagos', 'Modificar Pago', 'pago modificar [pago_id; venta_id; monto; fecha; metodo]', 'pago modificar [1; 1; 999.99; 2024-02-01; tarjeta]'),
('CU7: Gestionar Pagos', 'Eliminar Pago', 'pago eliminar [pago_id]', 'pago eliminar [1]'),
('CU7: Gestionar Pagos', 'Ver Pago', 'pago ver [pago_id]', 'pago ver [1]'),
('CU7: Gestionar Pagos', 'Reporte de Pagos', 'pago reporte', 'pago reporte');
