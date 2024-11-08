package data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.github.cdimascio.dotenv.Dotenv;
import postgresqlconnection.SqlConnection;

public class DProducto {
    Dotenv dotenv = Dotenv.configure().load();
    String dbUser = dotenv.get("DB_USER");
    String dbPassword = dotenv.get("DB_PASSWORD");
    String dbHost = dotenv.get("DB_HOST");
    String dbPort = dotenv.get("DB_PORT");
    String dbName = dotenv.get("DB_NAME");

    public static final String[] HEADERS = {"ID", "NOMBRE", "DESCRIPCION", "PRECIO", "STOCK"};

    private final SqlConnection connection;

    public DProducto() {
        connection = new SqlConnection(dbUser, dbPassword, dbHost, dbPort, dbName);
    }

    // Método para guardar un producto sin proveedor_id y con stock inicial de 0
    public void guardar(String nombre, String descripcion, double precio) {
        try {
            String query = "INSERT INTO productos(nombre, descripcion, precio, stock) VALUES (?, ?, ?, 0)";
            PreparedStatement ps = connection.connect().prepareStatement(query);
            ps.setString(1, nombre);
            ps.setString(2, descripcion);
            ps.setDouble(3, precio);

            if (ps.executeUpdate() == 0) {
                System.err.println("Class DProducto.java dice: Ocurrió un error al insertar un producto guardar()");
                throw new SQLException();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DProducto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Método para modificar un producto sin proveedor_id
    public void modificar(int id, String nombre, String descripcion, float precio) {
        try {
            String query = "UPDATE productos SET nombre=?, descripcion=?, precio=? WHERE id=?";
            PreparedStatement ps = connection.connect().prepareStatement(query);
            ps.setString(1, nombre);
            ps.setString(2, descripcion);
            ps.setFloat(3, precio);
            ps.setInt(4, id);

            if (ps.executeUpdate() == 0) {
                System.err.println("Class DProducto.java dice: Ocurrió un error al modificar un producto modificar()");
                throw new SQLException();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DProducto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    // Método para eliminar un producto por ID
    public void eliminar(int id) {
        try {
            String query = "DELETE FROM productos WHERE id=?";
            PreparedStatement ps = connection.connect().prepareStatement(query);
            ps.setInt(1, id);

            if (ps.executeUpdate() == 0) {
                System.err.println("Class DProducto.java dice: Ocurrió un error al eliminar un producto eliminar()");
                throw new SQLException();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DProducto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Método para listar todos los productos
    public List<String[]> listar() {
        List<String[]> productos = new ArrayList<>();
        try {
            String query = "SELECT * FROM productos ORDER BY id";
            PreparedStatement ps = connection.connect().prepareStatement(query);
            ResultSet set = ps.executeQuery();
            while (set.next()) {
                productos.add(new String[]{
                        set.getString("id"),
                        set.getString("nombre"),
                        set.getString("descripcion"),
                        set.getString("precio"),
                        set.getString("stock")
                });
            }
        } catch (SQLException ex) {
            Logger.getLogger(DProducto.class.getName()).log(Level.SEVERE, null, ex);
        }
        return productos;
    }

    // Método para ver los detalles de un producto específico
    public String[] ver(int id) {
        String[] producto = null;
        try {
            String query = "SELECT * FROM productos WHERE id=?";
            PreparedStatement ps = connection.connect().prepareStatement(query);
            ps.setInt(1, id);

            ResultSet set = ps.executeQuery();
            if (set.next()) {
                producto = new String[]{
                        set.getString("id"),
                        set.getString("nombre"),
                        set.getString("descripcion"),
                        set.getString("precio"),
                        set.getString("stock")
                };
            }
        } catch (SQLException ex) {
            Logger.getLogger(DProducto.class.getName()).log(Level.SEVERE, null, ex);
        }
        return producto;
    }

    // Método de ayuda actualizado sin proveedor_id
    public List<String[]> ayuda() {
        List<String[]> ayudas = new ArrayList<>();

        String registrar = "producto agregar [nombre; descripcion; precio]";
        String modificar = "producto modificar [id; nombre; descripcion; precio; stock]";
        String eliminar = "producto eliminar [id]";
        String mostrar = "producto mostrar";
        String ver = "producto ver [id]";

        ayudas.add(new String[]{String.valueOf(1), "Registrar PRODUCTO", registrar});
        ayudas.add(new String[]{String.valueOf(2), "Modificar PRODUCTO", modificar});
        ayudas.add(new String[]{String.valueOf(3), "Eliminar PRODUCTO", eliminar});
        ayudas.add(new String[]{String.valueOf(4), "Mostrar PRODUCTOS", mostrar});
        ayudas.add(new String[]{String.valueOf(5), "Ver PRODUCTO", ver});

        return ayudas;
    }

    public ArrayList<String[]> listarGrafica() {
        ArrayList<String[]> productos = new ArrayList<>();
        try {
            String query = """
                    SELECT u.nombre AS proveedor, COUNT(p.id) AS total_productos, SUM(p.stock) AS stock_total
                    FROM productos p
                    JOIN usuarios u ON p.proveedor_id = u.id
                    GROUP BY u.nombre
                    ORDER BY total_productos DESC;""";
            PreparedStatement ps = connection.connect().prepareStatement(query);
            ResultSet set = ps.executeQuery();
            while(set.next()) {
                productos.add(new String[] {
                        set.getString("proveedor"),
                        set.getString("total_productos"),
                });
            }
        } catch (SQLException ex) {
            Logger.getLogger(DProducto.class.getName()).log(Level.SEVERE, null, ex);
        }
        return productos;
    }

    public void desconectar() {
        connection.closeConnection();
    }
}
