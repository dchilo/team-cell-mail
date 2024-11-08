package validations;

import io.github.cdimascio.dotenv.Dotenv;
import postgresqlconnection.SqlConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class DevolucionValidator {
    Dotenv dotenv = Dotenv.configure().load();
    String dbUser = dotenv.get("DB_USER");
    String dbPassword = dotenv.get("DB_PASSWORD");
    String dbHost = dotenv.get("DB_HOST");
    String dbPort = dotenv.get("DB_PORT");
    String dbName = dotenv.get("DB_NAME");

    private final SqlConnection connection;

    public DevolucionValidator() {
        connection = new SqlConnection(dbUser, dbPassword, dbHost, dbPort, dbName);
    }

    // Método para validar el ID de la venta
    public boolean validarVentaId(String ventaIdStr) {
        try {
            int ventaId = Integer.parseInt(ventaIdStr);
            if (ventaId > 0) {
                return ventaEsValidaEnBD(ventaId);
            }
            return false;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Método para verificar si la venta existe en la BD
    private boolean ventaEsValidaEnBD(int ventaId) {
        String query = "SELECT COUNT(*) FROM ventas WHERE id = ?";
        try (PreparedStatement ps = connection.connect().prepareStatement(query)) {
            ps.setInt(1, ventaId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // Retorna true si la venta existe
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Método para validar el motivo de la devolución
    public static boolean validarMotivo(String motivo) {
        return motivo != null && !motivo.trim().isEmpty();
    }

    // Método para validar la fecha en formato YYYY-MM-DD
    public static boolean validarFecha(String fechaStr) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false); // Deshabilitar la tolerancia de fechas mal formadas
        try {
            dateFormat.parse(fechaStr); // Si falla, lanza ParseException
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    // Método para validar los parámetros de la devolución
    public void validarParametrosDevolucion(List<String> parametros) {
        if (parametros.size() != 3) {
            throw new IllegalArgumentException("Número de parámetros inválido");
        }

        if (!validarVentaId(parametros.get(0))) {
            throw new IllegalArgumentException("ID de la venta no válido");
        }
        if (!validarMotivo(parametros.get(1))) {
            throw new IllegalArgumentException("Motivo de la devolución no válido");
        }
        if (!validarFecha(parametros.get(2))) {
            throw new IllegalArgumentException("Fecha de la devolución no válida. Debe estar en formato YYYY-MM-DD");
        }
    }
}