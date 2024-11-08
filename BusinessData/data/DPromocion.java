package data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import io.github.cdimascio.dotenv.Dotenv;
import postgresqlconnection.SqlConnection;


public class DPromocion {
    Dotenv dotenv = Dotenv.configure().load();
    String dbUser = dotenv.get("DB_USER");
    String dbPassword = dotenv.get("DB_PASSWORD");
    String dbHost = dotenv.get("DB_HOST");
    String dbPort = dotenv.get("DB_PORT");
    String dbName = dotenv.get("DB_NAME");

    public static final String[] HEADERS = { "ID",  "PRODUCTO_ID","NOMBRE", "DESCRIPCION", "DESCUENTO" };

    private SqlConnection connection;

    public DPromocion() {
        connection =  new SqlConnection(dbUser, dbPassword, dbHost, dbPort, dbName);
    }

    public void guardar(int producto_id,String nombre, String descripcion, float descuento) {
        try {
            String query = "INSERT INTO promociones(producto_id, nombre, descripcion,descuento)"
                    + " values(?,?,?,?)";
            PreparedStatement ps = connection.connect().prepareStatement(query);
            ps.setInt(1, producto_id);
            ps.setString(2, nombre);
            ps.setString(3, descripcion);
            ps.setFloat(4, descuento);



            if (ps.executeUpdate() == 0) {
                System.err.println("Class DPromocion.java dice: "
                        + "Ocurrio un error al insertar un oferta guardar()");
                throw new SQLException();
            }
        } catch (SQLException e) {
            System.err.println("Class DPromocion.java dice: "
                    + "Ocurrio un error al insertar una promocion guardar()");
        }
    }

    public void modificar(int id,int producto_id, String nombre, String descripcion, float descuento) {
        try {
            String query = "UPDATE promociones SET producto_id=?,nombre=?,descripcion=?, descuento=?"
                    + " WHERE id=?";
            PreparedStatement ps = connection.connect().prepareStatement(query);
            ps.setInt(1, producto_id);
            ps.setString(2, nombre);
            ps.setString(3, descripcion);
            ps.setFloat(4, descuento);
            ps.setInt(5, id);
            if (ps.executeUpdate() == 0) {
                System.err.println("Class DPromocion.java dice: "
                        + "Ocurrio un error al modificar un oferta modificar()");
                throw new SQLException();
            }
        } catch (SQLException e) {
            System.err.println("Class DPromocion.java dice: "
                    + "Ocurrio un error al modificar un oferta modificar()");
        }
    }

    public void eliminar(int id) {
        try {
            String query = "DELETE FROM promociones WHERE id=?";
            PreparedStatement ps = connection.connect().prepareStatement(query);
            ps.setInt(1, id);
            if (ps.executeUpdate() == 0) {
                System.err.println("Class DPromocion.java dice: "
                        + "Ocurrio un error al eliminar un oferta eliminar()");
                throw new SQLException();
            }
        } catch (SQLException e) {
            System.err.println("Class DPromocion.java dice: "
                    + "Ocurrio un error al eliminar un oferta eliminar()");
        }
    }

    public List<String[]> listar() {
        List<String[]> promociones = new ArrayList<>();
        try {
            String query = "SELECT * FROM promociones";
            PreparedStatement ps = connection.connect().prepareStatement(query);
            ResultSet set = ps.executeQuery();
            while (set.next()) {
                promociones.add(new String[] {
                        String.valueOf(set.getInt("id")),
                        String.valueOf(set.getInt("producto_id")),
                        set.getString("nombre"),
                        set.getString("descripcion"),
                        String.valueOf(set.getFloat("descuento"))
                });
            }
        } catch (SQLException e) {
            System.err.println("Class DPromocion.java dice: "
                    + "Ocurrio un error al listar un oferta listar()");
        }
        return promociones;
    }

    public List<String[]> listarGrafica() {
        List<String[]> promociones = new ArrayList<>();
        try {
            String query = "SELECT Promociones.nombre AS promocion, COUNT(Detalleventa.id) AS total_ventas_promocion FROM Promociones LEFT JOIN Productos ON Promociones.producto_id = Productos.id LEFT JOIN Detalleventa ON Productos.id = Detalleventa.producto_id GROUP BY Promociones.nombre;\n";
            PreparedStatement ps = connection.connect().prepareStatement(query);
            ResultSet set = ps.executeQuery();
            while (set.next()) {
                promociones.add(new String[] {
                        set.getString("promocion"),
                        set.getString("total_ventas_promocion")
                });
            }
        } catch (SQLException e) {
            System.err.println("Class DPromocion.java dice: "
                    + "Ocurrio un error al listar un promociones listarGrafica()");
        }
        return promociones;
    }

    public String[] ver(int id) {
        String[] promociones = null;
        try {
            String query = "SELECT * FROM promociones WHERE id=?";
            PreparedStatement ps = connection.connect().prepareStatement(query);
            ps.setInt(1, id);

            ResultSet set = ps.executeQuery();
            if (set.next()) {
                promociones = new String[] {
                        String.valueOf(set.getInt("id")),
                        String.valueOf(set.getInt("producto_id")),
                        set.getString("nombre"),
                        set.getString("descripcion"),
                        String.valueOf(set.getFloat("descuento"))
                };
            }
        } catch (SQLException e) {
            System.err.println("Class DPromocion.java dice: "
                    + "Ocurrio un error al ver un promociones ver()");
        }
        return promociones;
    }

    public List<String[]> ayuda() {
        List<String[]> ayudas = new ArrayList<>();
        ayudas.add(new String[] { "ID", "PRODUCTO_ID", "NOMBRE", "DESCRIPCION", "DESCUENTO" });
        return ayudas;
    }

    public void desconectar() {
        connection.closeConnection();
    }

}
