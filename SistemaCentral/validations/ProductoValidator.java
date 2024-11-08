package validations;

import io.github.cdimascio.dotenv.Dotenv;
import postgresqlconnection.SqlConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ProductoValidator {
    Dotenv dotenv = Dotenv.configure().load();
    String dbUser = dotenv.get("DB_USER");
    String dbPassword = dotenv.get("DB_PASSWORD");
    String dbHost = dotenv.get("DB_HOST");
    String dbPort = dotenv.get("DB_PORT");
    String dbName = dotenv.get("DB_NAME");

    private final SqlConnection connection;

    public ProductoValidator() {
        connection = new SqlConnection(dbUser, dbPassword, dbHost, dbPort, dbName);
    }

    // Método para validar el nombre del producto
    public static boolean validarNombre(String nombre) {
        return nombre != null && !nombre.trim().isEmpty() && nombre.length() <= 100;
    }

    // Método para validar la descripción del producto
    public static boolean validarDescripcion(String descripcion) {
        return descripcion == null || descripcion.length() <= 255;
    }

    // Método para validar el precio del producto
    public static boolean validarPrecio(String precioStr) {
        try {
            float precio = Float.parseFloat(precioStr);
            return precio >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Método para validar los parámetros del producto sin proveedor_id y sin stock
    public void validarParametrosProducto(List<String> parametros) {
        if (parametros.size() != 3) {
            throw new IllegalArgumentException("Número de parámetros inválido");
        }

        if (!validarNombre(parametros.get(0))) {
            throw new IllegalArgumentException("Nombre del producto no válido");
        }
        if (!validarDescripcion(parametros.get(1))) {
            throw new IllegalArgumentException("Descripción del producto no válida");
        }
        if (!validarPrecio(parametros.get(2))) {
            throw new IllegalArgumentException("Precio del producto no válido");
        }
    }
}
