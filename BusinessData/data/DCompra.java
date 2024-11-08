package data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import io.github.cdimascio.dotenv.Dotenv;
import postgresqlconnection.SqlConnection;

public class DCompra {
    Dotenv dotenv = Dotenv.configure().load();
    String dbUser = dotenv.get("DB_USER");
    String dbPassword = dotenv.get("DB_PASSWORD");
    String dbHost = dotenv.get("DB_HOST");
    String dbPort = dotenv.get("DB_PORT");
    String dbName = dotenv.get("DB_NAME");

    public static final String[] HEADERS
            = {"ID", "PROVEEEDOR_ID", "PRODUCTO_ID", "CANTIDAD", "FECHA_COMPRA", "MONTO_TOTAL"};

    private final SqlConnection connection;

    public DCompra() {
        connection = new SqlConnection(dbUser, dbPassword, dbHost, dbPort, dbName);
    }

    public void guardar(int usuario_id, int producto_id, int cantidad, String fecha_compra, double monto_total) throws SQLException, ParseException {

        String query = "INSERT INTO compras(usuario_id,producto_id,cantidad,fecha_compra,monto_total)"
                + " values(?,?,?,?,?)";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setInt(1, usuario_id);
        ps.setInt(2, producto_id);
        ps.setInt(3, cantidad);
        ps.setString(4, fecha_compra);
        ps.setDouble(5, monto_total);

        if(ps.executeUpdate() == 0) {
            System.err.println("Class DCompra.java dice: "
                    + "Ocurrio un error al insertar una compra guardar()");
            throw new SQLException();
        }
    }

    public void editar(int id, int usuario_id, int producto_id, int cantidad, String fecha_compra, double monto_total) throws SQLException {
        String query = "UPDATE compras SET usuario_id=?, producto_id=?, cantidad=?, fecha_compra=?, monto_total=? WHERE id=?";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setInt(1, usuario_id);
        ps.setInt(2, producto_id);
        ps.setInt(3, cantidad);
        ps.setString(4, fecha_compra);
        ps.setDouble(5, monto_total);
        ps.setInt(6, id);
        if(ps.executeUpdate() == 0) {
            System.err.println("Class DCompra.java dice: "
                    + "Ocurrio un error al editar una compra editar()");
            throw new SQLException();
        }
    }

    public void eliminar(int id) throws SQLException {
        String query = "DELETE FROM compras WHERE id=?";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setInt(1, id);
        if(ps.executeUpdate() == 0) {
            System.err.println("Class DCompra.java dice: "
                    + "Ocurrio un error al eliminar una compra eliminar()");
            throw new SQLException();
        }
    }

    public List<String[]> listar() throws SQLException {
        String query = "SELECT * FROM compras";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        List<String[]> compras = new ArrayList<>();
        while(rs.next()) {
            String[] compra = new String[6];
            compra[0] = rs.getString("id");
            compra[1] = rs.getString("usuario_id");
            compra[2] = rs.getString("producto_id");
            compra[3] = rs.getString("cantidad");
            compra[4] = rs.getString("fecha_compra");
            compra[5] = rs.getString("monto_total");
            compras.add(compra);
        }
        return compras;
    }

    public List<String[]> listarGrafica() throws SQLException {
        ArrayList<String[]> compras = new ArrayList<>();
        String query = """
                SELECT u.nombre AS proveedor, COUNT(c.id) AS total_compras
                FROM compras c
                JOIN usuarios u ON c.usuario_id = u.id
                GROUP BY u.nombre
                ORDER BY total_compras DESC;""";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            compras.add(new String[] {
                rs.getString("proveedor"),
                rs.getString("total_compras")
            });
        }
        return compras;
    }

    public String[] ver(int id) throws SQLException {
        String query = "SELECT * FROM compras WHERE id=?";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        String[] compra = new String[6];
        if(rs.next()) {
            compra[0] = rs.getString("id");
            compra[1] = rs.getString("usuario_id");
            compra[2] = rs.getString("producto_id");
            compra[3] = rs.getString("cantidad");
            compra[4] = rs.getString("fecha_compra");
            compra[5] = rs.getString("monto_total");
        }
        return compra;
    }

    public List<String[]> ayuda() throws SQLException {
        List<String[]> ayudas = new ArrayList<>();

        String registrar = "compra agregar [usuario_id; producto_id; cantidad; fecha_compra; monto_total]";
        String modificar = "compra modificar [id; usuario_id; producto_id; cantidad; fecha_compra; monto_total]";
        String eliminar = "compra eliminar [id]";
        String mostrar = "compra mostrar";
        String ver = "compra ver [id]";
        String reporte = "compra reporte";

        ayudas.add(new String[]{String.valueOf(1), "Registrar COMPRA", registrar});
        ayudas.add(new String[]{String.valueOf(2), "Modificar COMPRA", modificar});
        ayudas.add(new String[]{String.valueOf(3), "Eliminar COMPRA", eliminar});
        ayudas.add(new String[]{String.valueOf(4), "Mostrar COMPRAS", mostrar});
        ayudas.add(new String[]{String.valueOf(5), "Ver COMPRA", ver});
        ayudas.add(new String[]{String.valueOf(6), "Reporte COMPRAS", reporte});
        
        return ayudas;
    }

    public void desconectar() {
        connection.closeConnection();
    }
}
