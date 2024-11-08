package data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import io.github.cdimascio.dotenv.Dotenv;
import postgresqlconnection.SqlConnection;

public class DDevolucion {
    Dotenv dotenv = Dotenv.configure().load();
    String dbUser = dotenv.get("DB_USER");
    String dbPassword = dotenv.get("DB_PASSWORD");
    String dbHost = dotenv.get("DB_HOST");
    String dbPort = dotenv.get("DB_PORT");
    String dbName = dotenv.get("DB_NAME");

    /*
        -- Crear tabla Devoluciones
        CREATE TABLE Devoluciones (
                                      id SERIAL PRIMARY KEY,
                                      venta_id INT REFERENCES Ventas(id),
                                      motivo TEXT NOT NULL,
                                      fecha_devolucion VARCHAR(10) NOT NULL
        );
     */

    public static final String[] HEADERS
            = {"ID", "VENTA ID", "MOTIVO", "FECHA DEVOLUCION"};

    private final SqlConnection connection;

    public DDevolucion() {
        connection = new SqlConnection(dbUser, dbPassword, dbHost, dbPort, dbName);
    }

    public void guardar(int venta_id, String motivo, String fecha_devolucion) throws SQLException, ParseException {

        String query = "INSERT INTO Devoluciones(venta_id, motivo, fecha_devolucion)"
                + " values(?,?,?)";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setInt(1, venta_id);
        ps.setString(2, motivo);
        ps.setString(3, fecha_devolucion);

        if(ps.executeUpdate() == 0) {
            System.err.println("Class DDevolucion.java dice: "
                    + "Ocurrio un error al insertar una devolucion guardar()");
            throw new SQLException();
        }
    }

    public void modificar(int id, int venta_id, String motivo, String fecha_devolucion) throws SQLException {
        String query = "UPDATE Devoluciones SET venta_id=?, motivo=?, fecha_devolucion=? WHERE id=?";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setInt(1, venta_id);
        ps.setString(2, motivo);
        ps.setString(3, fecha_devolucion);
        ps.setInt(4, id);
        if(ps.executeUpdate() == 0) {
            System.err.println("Class DDevolucion.java dice: "
                    + "Ocurrio un error al editar una devolucion editar()");
            throw new SQLException();
        }
    }

    public void eliminar(int id) throws SQLException {
        String query = "DELETE FROM Devoluciones WHERE id=?";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setInt(1, id);
        if(ps.executeUpdate() == 0) {
            System.err.println("Class DDevolucion.java dice: "
                    + "Ocurrio un error al eliminar una devolucion eliminar()");
            throw new SQLException();
        }
    }

    public List<String[]> listar() throws SQLException {
        String query = "SELECT * FROM Devoluciones";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        List<String[]> devoluciones = new ArrayList<>();
        while(rs.next()) {
            String[] devolucion = new String[4];
            devolucion[0] = rs.getString("id");
            devolucion[1] = rs.getString("venta_id");
            devolucion[2] = rs.getString("motivo");
            devolucion[3] = rs.getString("fecha_devolucion");
            devoluciones.add(devolucion);
        }
        return devoluciones;
    }

    public String[] ver(int id) throws SQLException {
        String query = "SELECT * FROM Devoluciones WHERE id=?";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        if(rs.next()) {
            String[] devolucion = new String[4];
            devolucion[0] = rs.getString("id");
            devolucion[1] = rs.getString("venta_id");
            devolucion[2] = rs.getString("motivo");
            devolucion[3] = rs.getString("fecha_devolucion");
            return devolucion;
        }
        return null;
    }

    public List<String []> listarGrafica() throws SQLException {
        String query = """
                SELECT p.nombre AS producto, COUNT(d.id) AS total_devoluciones
                FROM devoluciones d
                JOIN ventas v ON d.venta_id = v.id
                JOIN productos p ON v.producto_id = p.id
                GROUP BY p.nombre
                ORDER BY total_devoluciones DESC;""";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        List<String[]> devoluciones = new ArrayList<>();
        while(rs.next()) {
            String[] devolucion = new String[2];
            devolucion[0] = rs.getString("producto");
            devolucion[1] = rs.getString("total_devoluciones");
            devoluciones.add(devolucion);
        }
        return devoluciones;
    }

    public List<String[]> ayuda() {
        List<String[]> ayudas = new ArrayList<>();

        String registrar = "devolucion agregar [venta_id; motivo; fecha_devolucion]";
        String modificar = "devolucion modificar [id; venta_id; motivo; fecha_devolucion]";
        String eliminar = "devolucion eliminar [id]";
        String mostrar = "devolucion mostrar";
        String ver = "devolucion ver [id]";
        String reporte = "devolucion reporte";

        ayudas.add(new String[]{String.valueOf(1), "Registrar DEVOLUCION", registrar});
        ayudas.add(new String[]{String.valueOf(2), "Modificar DEVOLUCION", modificar});
        ayudas.add(new String[]{String.valueOf(3), "Eliminar DEVOLUCION", eliminar});
        ayudas.add(new String[]{String.valueOf(4), "Mostrar DEVOLUCIONES", mostrar});
        ayudas.add(new String[]{String.valueOf(5), "Ver DEVOLUCION", ver});
        ayudas.add(new String[]{String.valueOf(6), "Reporte DEVOLUCIONES", reporte});

        return ayudas;
    }

    public void desconectar() {
        connection.closeConnection();
    }

}
