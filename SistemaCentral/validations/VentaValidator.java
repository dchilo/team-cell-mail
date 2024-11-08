package validations;

import io.github.cdimascio.dotenv.Dotenv;
import postgresqlconnection.SqlConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class VentaValidator {
    Dotenv dotenv = Dotenv.configure().load();
    String dbUser = dotenv.get("DB_USER");
    String dbPassword = dotenv.get("DB_PASSWORD");
    String dbHost = dotenv.get("DB_HOST");
    String dbPort = dotenv.get("DB_PORT");
    String dbName = dotenv.get("DB_NAME");

    private final SqlConnection connection;

    public VentaValidator() {
        connection = new SqlConnection(dbUser, dbPassword, dbHost, dbPort, dbName);
    }

    // Método para validar el ID del usuario y que sea de tipo "cliente"
    public boolean validarUsuarioId(String usuarioIdStr) {
        try {
            int usuarioId = Integer.parseInt(usuarioIdStr);
            return usuarioId > 0 && usuarioEsClienteEnBD(usuarioId);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Método para verificar si el usuario existe en la BD y es de tipo "cliente"
    private boolean usuarioEsClienteEnBD(int usuarioId) {
        String query = "SELECT COUNT(*) FROM Usuarios WHERE id = ? AND LOWER(tipo) = 'cliente'";
        try (PreparedStatement ps = connection.connect().prepareStatement(query)) {
            ps.setInt(1, usuarioId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Método para validar el ID del producto
    public boolean validarProductoId(String productoIdStr) {
        try {
            int productoId = Integer.parseInt(productoIdStr);
            return productoId > 0 && productoEsValidoEnBD(productoId);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Método para verificar si el producto existe en la BD
    private boolean productoEsValidoEnBD(int productoId) {
        String query = "SELECT COUNT(*) FROM Productos WHERE id = ?";
        try (PreparedStatement ps = connection.connect().prepareStatement(query)) {
            ps.setInt(1, productoId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Método para validar la cantidad de venta
    public static boolean validarCantidad(String cantidadStr) {
        try {
            int cantidad = Integer.parseInt(cantidadStr);
            return cantidad > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Método para validar el monto total de la venta
    public static boolean validarMontoTotal(String montoStr) {
        try {
            float monto = Float.parseFloat(montoStr);
            return monto >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Método para validar la dirección de envío
    public static boolean validarDireccionEnvio(String direccionEnvio) {
        return direccionEnvio != null && !direccionEnvio.trim().isEmpty();
    }

    // Método para validar la fecha en formato YYYY-MM-DD
    public static boolean validarFecha(String fechaStr) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(fechaStr);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    // Método para validar los parámetros de la venta
    public void validarParametrosVenta(List<String> parametros) throws SQLException {
        if (parametros.size() != 5) {
            throw new IllegalArgumentException("Número de parámetros inválido");
        }

        // Orden de validación ajustado para coincidir con el orden de la tabla Ventas
        if (!validarUsuarioId(parametros.get(0))) {
            throw new IllegalArgumentException("ID del usuario no válido o el usuario no es de tipo 'cliente'");
        }
        if (!validarProductoId(parametros.get(1))) {
            throw new IllegalArgumentException("ID del producto no válido");
        }
        if (!validarCantidad(parametros.get(2))) {
            throw new IllegalArgumentException("Cantidad de la venta no válida");
        }
        if (!validarFecha(parametros.get(3))) {
            throw new IllegalArgumentException("Fecha de la venta no válida. Debe estar en formato YYYY-MM-DD");
        }
        if (!validarDireccionEnvio(parametros.get(4))) {
            throw new IllegalArgumentException("Dirección de envío no válida");
        }
    }

    public void desconectar() {
        connection.closeConnection();
    }
}
