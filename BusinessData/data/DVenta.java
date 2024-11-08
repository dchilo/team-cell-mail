package data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import io.github.cdimascio.dotenv.Dotenv;
import postgresqlconnection.SqlConnection;


public class DVenta {
    /*
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

     */

    Dotenv dotenv = Dotenv.configure().load();
    String dbUser = dotenv.get("DB_USER");
    String dbPassword = dotenv.get("DB_PASSWORD");
    String dbHost = dotenv.get("DB_HOST");
    String dbPort = dotenv.get("DB_PORT");
    String dbName = dotenv.get("DB_NAME");

    public static final String[] HEADERS = {"ID", "USUARIO_ID", "PRODUCTO_ID", "CANTIDAD", "FECHA_VENTA", "DIRECCION_ENVIO", "MONTO_TOTAL"};

    private final SqlConnection connection;

    public DVenta() {
        connection = new SqlConnection(dbUser, dbPassword, dbHost, dbPort, dbName);
    }

    // Método para obtener el precio de un producto por ID
    private float obtenerPrecioProducto(int productoId) throws SQLException {
        String query = "SELECT precio FROM productos WHERE id = ?";
        try (PreparedStatement ps = connection.connect().prepareStatement(query)) {
            ps.setInt(1, productoId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getFloat("precio");
            } else {
                throw new SQLException("Producto no encontrado");
            }
        }
    }

    // Método para guardar una venta calculando el monto_total automáticamente
    public void guardar(int usuario_id, int producto_id, int cantidad, String fecha_venta, String direccion_envio) throws SQLException, ParseException {
        // Obtener el precio del producto
        float precio = obtenerPrecioProducto(producto_id);
        // Calcular el monto total
        float monto_total = precio * cantidad;

        String query = "INSERT INTO ventas(usuario_id, producto_id, cantidad, fecha_venta, direccion_envio, monto_total) VALUES(?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.connect().prepareStatement(query)) {
            ps.setInt(1, usuario_id);
            ps.setInt(2, producto_id);
            ps.setInt(3, cantidad);
            ps.setString(4, fecha_venta);
            ps.setString(5, direccion_envio);
            ps.setFloat(6, monto_total);

            if (ps.executeUpdate() == 0) {
                System.err.println("Class DVenta.java dice: Ocurrio un error al guardar una venta guardar()");
                throw new SQLException();
            }
        }
    }

    public void modificar(int id, int usuario_id, int producto_id, int cantidad, String fecha_venta, String direccion_envio) throws SQLException, ParseException {
        // Obtener el precio del producto
        float precio = obtenerPrecioProducto(producto_id);
        // Calcular el monto total
        float monto_total = precio * cantidad;

        String query = "UPDATE ventas SET usuario_id=?, producto_id=?, cantidad=?, fecha_venta=?, direccion_envio=?, monto_total=? WHERE id=?";
        try (PreparedStatement ps = connection.connect().prepareStatement(query)) {
            ps.setInt(1, usuario_id);
            ps.setInt(2, producto_id);
            ps.setInt(3, cantidad);
            ps.setString(4, fecha_venta);
            ps.setString(5, direccion_envio);
            ps.setFloat(6, monto_total);
            ps.setInt(7, id);

            if (ps.executeUpdate() == 0) {
                System.err.println("Class DVenta.java dice: Ocurrio un error al modificar una venta modificar()");
                throw new SQLException();
            }
        }
    }

    public void eliminar(int id) throws SQLException {

        String query = "DELETE FROM ventas WHERE id=?";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setInt(1, id);

        if(ps.executeUpdate() == 0) {
            System.err.println("Class DVenta.java dice: "
                    + "Ocurrio un error al eliminar una venta eliminar()");
            throw new SQLException();
        }
    }

    public List<String[]> listar() {
        List<String[]> ventas = new ArrayList<>();
        try {
            String query = "SELECT * FROM ventas";
            PreparedStatement ps = connection.connect().prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                ventas.add(new String[] {
                        rs.getString("id"),
                        rs.getString("usuario_id"),
                        rs.getString("producto_id"),
                        rs.getString("cantidad"),
                        rs.getString("fecha_venta"),
                        rs.getString("direccion_envio"),
                        rs.getString("monto_total")
                });
            }
        } catch (SQLException e) {
            System.err.println("Class DVenta.java dice: "
                    + "Ocurrio un error al listar las ventas listar()");
        }
        return ventas;
    }

    public List<String []> listarGrafica() throws SQLException {
        ArrayList<String[]> ventas = new ArrayList<>();
        String query = """
                SELECT u.nombre AS proveedor, COUNT(c.id) AS total_compras
                FROM compras c
                JOIN usuarios u ON c.usuario_id = u.id
                GROUP BY u.nombre
                ORDER BY total_compras DESC
                LIMIT 10;""";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            ventas.add(new String[] {
                    rs.getString("proveedor"),
                    rs.getString("total_compras")
            });
        }
        return ventas;
    }

    public List<String[]> ayuda() {
        List<String[]> ayudas = new ArrayList<>();

        String registrar = "venta agregar [usuario_id; producto_id; cantidad; fecha_venta; direccion_envio]";
        String modificar = "venta modificar [id; usuario_id; producto_id; cantidad; fecha_venta; direccion_envios]";
        String eliminar = "venta eliminar [id]";
        String mostrar = "venta mostrar";
        String ver = "venta ver [id]";
        String reporte = "venta reporte";

        ayudas.add(new String[]{String.valueOf(1), "Registrar VENTA", registrar});
        ayudas.add(new String[]{String.valueOf(2), "Modificar VENTA", modificar});
        ayudas.add(new String[]{String.valueOf(3), "Eliminar VENTA", eliminar});
        ayudas.add(new String[]{String.valueOf(4), "Mostrar VENTAS", mostrar});
        ayudas.add(new String[]{String.valueOf(5), "Ver VENTA", ver});
        ayudas.add(new String[]{String.valueOf(6), "Reporte VENTAS", reporte});

        return ayudas;
    }

    public void desconectar() {
        connection.closeConnection();
    }
}
