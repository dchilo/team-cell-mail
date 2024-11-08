package data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import io.github.cdimascio.dotenv.Dotenv;
import postgresqlconnection.SqlConnection;


public class DInventario {
    Dotenv dotenv = Dotenv.configure().load();
    String dbUser = dotenv.get("DB_USER");
    String dbPassword = dotenv.get("DB_PASSWORD");
    String dbHost = dotenv.get("DB_HOST");
    String dbPort = dotenv.get("DB_PORT");
    String dbName = dotenv.get("DB_NAME");

    public static final String[] HEADERS
            = {"ID", "PRODUCTO ID", "CANTIDAD", "TIPO MOVIMIENTO", "FECHA MOVIMIENTO"};

    private final SqlConnection connection;

    public DInventario() {
        connection = new SqlConnection(dbUser, dbPassword, dbHost, dbPort, dbName);
    }

    public void guardar(int producto_id, int cantidad, String tipo_movimiento, String fecha_movimiento) throws SQLException, ParseException {
        String query = "INSERT INTO inventarios(producto_id, cantidad, tipo_movimiento, fecha_movimiento)"
                + " values(?,?,?,?)";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setInt(1, producto_id);
        ps.setInt(2, cantidad);
        ps.setString(3, tipo_movimiento);
        ps.setString(4, fecha_movimiento);

        if(ps.executeUpdate() == 0) {
            System.err.println("Class DInventario.java dice: "
                    + "Ocurrio un error al insertar un inventario guardar()");
            throw new SQLException();
        }
    }

    public void modificar(int id, int producto_id, int cantidad, String tipo_movimiento, String fecha_movimiento) throws SQLException {
        String query = "UPDATE inventarios SET producto_id=?, cantidad=?, tipo_movimiento=?, fecha_movimiento=? WHERE id=?";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setInt(1, producto_id);
        ps.setInt(2, cantidad);
        ps.setString(3, tipo_movimiento);
        ps.setString(4, fecha_movimiento);
        ps.setInt(5, id);
        if(ps.executeUpdate() == 0) {
            System.err.println("Class DInventario.java dice: "
                    + "Ocurrio un error al editar un inventario editar()");
            throw new SQLException();
        }
    }

    public void eliminar(int id) throws SQLException {
        String query = "DELETE FROM inventarios WHERE id=?";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setInt(1, id);
        if(ps.executeUpdate() == 0) {
            System.err.println("Class DInventario.java dice: "
                    + "Ocurrio un error al eliminar un inventario eliminar()");
            throw new SQLException();
        }
    }

    public List<String[]> listar() throws SQLException {
        String query = "SELECT * FROM inventarios";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        List<String[]> inventarios = new ArrayList<>();
        while(rs.next()) {
            String[] inventario = new String[5];
            inventario[0] = rs.getString("id");
            inventario[1] = rs.getString("producto_id");
            inventario[2] = rs.getString("cantidad");
            inventario[3] = rs.getString("tipo_movimiento");
            inventario[4] = rs.getString("fecha_movimiento");
            inventarios.add(inventario);
        }
        return inventarios;
    }

    public List<String[]> listarGrafica() throws SQLException {
        String query = """
                SELECT p.nombre AS producto, SUM(p.stock) AS total_stock
                FROM productos p
                GROUP BY p.nombre
                ORDER BY total_stock DESC;""";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        List<String[]> inventarios = new ArrayList<>();
        while(rs.next()) {
            inventarios.add(new String[] {
                rs.getString("producto"),
                rs.getString("total_stock")
            });
        }
        return inventarios;
    }


    public String[] ver(int id) throws SQLException {
        String[] inventario = null;
        String query = "SELECT * FROM inventarios WHERE id=?";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            inventario = new String[5];
            inventario[0] = rs.getString("id");
            inventario[1] = rs.getString("producto_id");
            inventario[2] = rs.getString("cantidad");
            inventario[3] = rs.getString("tipo_movimiento");
            inventario[4] = rs.getString("fecha_movimiento");
        }
        return inventario;
    }

    public List<String[]> ayuda() throws SQLException {
        List<String[]> ayudas = new ArrayList<>();

        String registrar = "inventario agregar [producto_id; cantidad; tipo_movimiento; fecha_movimiento]";
        String modificar = "inventario modificar [id; producto_id; cantidad; tipo_movimiento; fecha_movimiento]";
        String eliminar = "inventario eliminar [id]";
        String mostrar = "inventario mostrar";
        String ver = "inventario ver [id]";
        String reporte = "inventario reporte";

        ayudas.add(new String[]{String.valueOf(1), "Registrar INVENTARIO", registrar});
        ayudas.add(new String[]{String.valueOf(2), "Modificar INVENTARIO", modificar});
        ayudas.add(new String[]{String.valueOf(3), "Eliminar INVENTARIO", eliminar});
        ayudas.add(new String[]{String.valueOf(4), "Mostrar INVENTARIOS", mostrar});
        ayudas.add(new String[]{String.valueOf(5), "Ver INVENTARIO", ver});
        ayudas.add(new String[]{String.valueOf(6), "Reporte INVENTARIOS", reporte});

        return ayudas;
    }

    // Método para actualizar el stock de un producto en la base de datos
    public void actualizarStock(int productoId, int nuevoStock) throws SQLException {
        String query = "UPDATE productos SET stock = ? WHERE id = ?";

        try (PreparedStatement ps = connection.connect().prepareStatement(query)) {
            ps.setInt(1, nuevoStock);
            ps.setInt(2, productoId);
            int filasActualizadas = ps.executeUpdate();

            if (filasActualizadas == 0) {
                System.err.println("Class DInventario.java dice: "
                        + "Ocurrió un error al actualizar el stock en actualizarStock()");
                throw new SQLException("No se pudo actualizar el stock. Producto no encontrado.");
            }
        }
    }

    // Método para obtener el stock actual de un producto en la base de datos
    public int obtenerStock(int productoId) throws SQLException {
        String query = "SELECT stock FROM productos WHERE id = ?";
        int stock = 0;

        try (PreparedStatement ps = connection.connect().prepareStatement(query)) {
            ps.setInt(1, productoId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    stock = rs.getInt("stock");
                } else {
                    throw new SQLException("Producto no encontrado.");
                }
            }
        }
        return stock;
    }

    public void desconectar(){
        connection.closeConnection();
    }
}
