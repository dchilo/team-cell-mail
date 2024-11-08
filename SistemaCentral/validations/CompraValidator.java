package validations;

import io.github.cdimascio.dotenv.Dotenv;
import postgresqlconnection.SqlConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class CompraValidator {
    Dotenv dotenv = Dotenv.configure().load();
    String dbUser = dotenv.get("DB_USER");
    String dbPassword = dotenv.get("DB_PASSWORD");
    String dbHost = dotenv.get("DB_HOST");
    String dbPort = dotenv.get("DB_PORT");
    String dbName = dotenv.get("DB_NAME");

    private final SqlConnection connection;

    public CompraValidator() {
        connection = new SqlConnection(dbUser, dbPassword, dbHost, dbPort, dbName);
    }

    // Método para validar el ID del usuario y que sea de tipo "proveedor"
    public boolean validarUsuarioId(String usuarioIdStr) {
        try {
            int usuarioId = Integer.parseInt(usuarioIdStr);
            if (usuarioId > 0) {
                return usuarioEsProveedorEnBD(usuarioId);
            }
            return false;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Método para verificar si el usuario existe en la BD y es de tipo "proveedor"
    private boolean usuarioEsProveedorEnBD(int usuarioId) {
        String query = "SELECT COUNT(*) FROM usuarios WHERE id = ? AND LOWER(tipo) = 'proveedor'";
        try (PreparedStatement ps = connection.connect().prepareStatement(query)) {
            ps.setInt(1, usuarioId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // Retorna true si el usuario existe y es de tipo "proveedor"
                }
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
            if (productoId > 0) {
                return productoEsValidoEnBD(productoId);
            }
            return false;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Método para verificar si el producto existe en la BD
    private boolean productoEsValidoEnBD(int productoId) {
        String query = "SELECT COUNT(*) FROM productos WHERE id = ?";
        try (PreparedStatement ps = connection.connect().prepareStatement(query)) {
            ps.setInt(1, productoId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // Retorna true si el producto existe
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Método para validar la cantidad de compra
    public static boolean validarCantidad(String cantidadStr) {
        try {
            int cantidad = Integer.parseInt(cantidadStr);
            return cantidad > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Método para validar el monto total de la compra
    public static boolean validarMontoTotal(String montoStr) {
        try {
            float monto = Float.parseFloat(montoStr);
            return monto >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
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

    // Método para validar los parámetros de la compra
    public void validarParametrosCompra(List<String> parametros) {
        if (parametros.size() != 5) {
            throw new IllegalArgumentException("Número de parámetros inválido");
        }

        if (!validarUsuarioId(parametros.get(0))) {
            throw new IllegalArgumentException("ID del usuario no válido o el usuario no es de tipo 'proveedor'");
        }
        if (!validarProductoId(parametros.get(1))) {
            throw new IllegalArgumentException("ID del producto no válido");
        }
        if (!validarCantidad(parametros.get(2))) {
            throw new IllegalArgumentException("Cantidad de la compra no válida");
        }
        if (!validarMontoTotal(parametros.get(4))) {
            throw new IllegalArgumentException("Monto total de la compra no válido");
        }
        if (!validarFecha(parametros.get(3))) {
            throw new IllegalArgumentException("Fecha de compra no válida. Debe estar en formato YYYY-MM-DD");
        }
    }
}