/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import io.github.cdimascio.dotenv.Dotenv;
import postgresqlconnection.SqlConnection;

/**
 *
 * @author dchil
 */
public class DPago {
    Dotenv dotenv = Dotenv.configure().load();
    String dbUser = dotenv.get("DB_USER");
    String dbPassword = dotenv.get("DB_PASSWORD");
    String dbHost = dotenv.get("DB_HOST");
    String dbPort = dotenv.get("DB_PORT");
    String dbName = dotenv.get("DB_NAME");

    public static final String[] HEADERS
            = {"ID", "VENTA_ID","MONTO", "FECHA", "METODO"};

    private final SqlConnection connection;

    public DPago() {
        connection = new SqlConnection(dbUser, dbPassword, dbHost, dbPort, dbName);
    }

    public void guardar(int venta_id, double monto, String fecha, String metodo) throws SQLException, ParseException {

        String query = "INSERT INTO pagos(venta_id, monto, fecha, metodo)"
                + " values(?,?,?,?)";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setInt(1, venta_id);
        ps.setDouble(2, monto);
        ps.setString(3, fecha);
        ps.setString(4, metodo);

        if(ps.executeUpdate() == 0) {
            System.err.println("Class DPago.java dice: "
                    + "Ocurrio un error al insertar un pago guardar()");
            throw new SQLException();
        }
    }

    public void modificar(int id, int venta_id, double monto, String fecha, String metodo)
            throws SQLException, ParseException {

        String query = "UPDATE pagos SET venta_id=?, monto=?, fecha=?,"
                + " metodo_pago=?"
                + " WHERE id=?";
        System.out.println(query);
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setInt(1, venta_id);
        ps.setDouble(2, monto);
        ps.setString(3, fecha);
        ps.setString(4, metodo);

        if(ps.executeUpdate() == 0) {
            System.err.println("Class DPago.java dice: "
                    + "Ocurrio un error al modificar un pago modificar()");
            throw new SQLException();
        }
    }

    public void eliminar(int id) throws SQLException {
        String query = "DELETE FROM pagos WHERE id=?";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setInt(1, id);

        if(ps.executeUpdate() == 0) {
            System.err.println("Class DPago.java dice: "
                    + "Ocurrio un error al eliminar un pago eliminar()");
            throw new SQLException();
        }
    }

    public List<String[]> listar() throws SQLException {
        List<String[]> pagos = new ArrayList<>();
        String query = "SELECT * FROM pagos";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ResultSet rs = ps.executeQuery();

        while(rs.next()) {
            String[] pago = new String[5];
            pago[0] = rs.getString("id");
            pago[1] = rs.getString("venta_id");
            pago[2] = rs.getString("monto");
            pago[3] = rs.getString("fecha");
            pago[4] = rs.getString("metodo");

            pagos.add(pago);
        }

        return pagos;
    }

    public List<String[]> listarGrafica() throws SQLException {
        List<String[]> pagos = new ArrayList<>();
        String query = "SELECT metodo, COUNT(*) AS total FROM Pagos GROUP BY metodo;";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            String[] pago = new String[5];
            pago[0] = rs.getString("metodo");
            pago[1] = rs.getString("total");
            pagos.add(pago);
        }

        return pagos;
    }

    public List<String[]> ayuda() throws SQLException {
        List<String[]> ayudas = new ArrayList<>();

        String registrar = "pago agregar [venta_id; monto; fecha; metodo]";
        String modificar = "pago modificar [id; venta_id; monto; fecha; metodo]";
        String eliminar = "pago eliminar [id]";
        String mostrar = "pago mostrar";
        String ver = "pago ver [id]";
        String reporte = "pago reporte";

        ayudas.add(new String[]{String.valueOf(1), "Registrar PAGO", registrar});
        ayudas.add(new String[]{String.valueOf(2), "Modificar PAGO", modificar});
        ayudas.add(new String[]{String.valueOf(3), "Eliminar PAGO", eliminar});
        ayudas.add(new String[]{String.valueOf(4), "Mostrar PAGOS", mostrar});
        ayudas.add(new String[]{String.valueOf(5), "Ver PAGO", ver});
        ayudas.add(new String[]{String.valueOf(6), "Reporte PAGOS", reporte});

        return ayudas;
    }

    public void desconectar() {
        connection.closeConnection();
    }
}
