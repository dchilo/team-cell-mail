package validations;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import io.github.cdimascio.dotenv.Dotenv;
import postgresqlconnection.SqlConnection;

public class UsuarioValidator {

    private final SqlConnection connection;

    public UsuarioValidator() {
        Dotenv dotenv = Dotenv.configure().load();
        String dbUser = dotenv.get("DB_USER");
        String dbPassword = dotenv.get("DB_PASSWORD");
        String dbHost = dotenv.get("DB_HOST");
        String dbPort = dotenv.get("DB_PORT");
        String dbName = dotenv.get("DB_NAME");

        this.connection = new SqlConnection(dbUser, dbPassword, dbHost, dbPort, dbName);
    }

    // Método para validar el nombre
    public static boolean validarNombre(String nombre) {
        String patronNombre = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$";
        return nombre != null && !nombre.trim().isEmpty() && nombre.length() <= 100 && nombre.matches(patronNombre);
    }

    // Método para validar el apellido
    public static boolean validarApellido(String apellido) {
        return apellido != null && !apellido.trim().isEmpty() && apellido.length() <= 100;
    }

    // Método para validar el email
    public static boolean validarEmail(String email) {
        String patronCorreo = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email != null && email.matches(patronCorreo);
    }

    // Método para validar el número de teléfono
    public static boolean validarNumeroTelefono(String telefono) {
        String patronTelefono = "^[0-9]{1,15}$";
        return telefono != null && telefono.matches(patronTelefono);
    }

    // Método para validar el tipo de usuario
    public static boolean validarTipoUsuario(String tipoUsuario) {
        return tipoUsuario != null && (tipoUsuario.equalsIgnoreCase("administrativo")
                || tipoUsuario.equalsIgnoreCase("cliente") || tipoUsuario.equalsIgnoreCase("proveedor"));
    }

    // Método para validar la dirección
    public static boolean validarDireccion(String direccion) {
        return direccion == null || direccion.length() <= 255;
    }

    // Método para verificar si el valor de un campo específico ya existe en la base de datos
    private boolean existeCampoUnico(String campo, String valor) throws SQLException {
        String query = "SELECT COUNT(*) FROM usuarios WHERE " + campo + " = ?";
        try (PreparedStatement ps = connection.connect().prepareStatement(query)) {
            ps.setString(1, valor);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    // Método para validar que el email y teléfono no estén duplicados en la base de datos
    public void validarDuplicados(String email, String telefono) throws SQLException {
        if (existeCampoUnico("email", email)) {
            throw new IllegalArgumentException("El correo ya está registrado con otro usuario");
        }
        if (existeCampoUnico("telefono", telefono)) {
            throw new IllegalArgumentException("El teléfono ya está registrado con otro usuario");
        }
    }

    // Método para validar los parámetros del usuario y verificar duplicados
    public void validarParametrosUsuario(List<String> parametros) throws SQLException {
        if (parametros.size() != 5) {
            throw new IllegalArgumentException("Número de parámetros inválido");
        }
        if (!validarNombre(parametros.get(0))) {
            throw new IllegalArgumentException("Nombre no válido");
        }
        if (!validarEmail(parametros.get(1))) {
            throw new IllegalArgumentException("Formato de correo electrónico no válido");
        }
        if (!validarNumeroTelefono(parametros.get(2))) {
            throw new IllegalArgumentException("Formato de número de teléfono no válido");
        }
        if (!validarDireccion(parametros.get(3))) {
            throw new IllegalArgumentException("Dirección no válida");
        }
        if (!validarTipoUsuario(parametros.get(4))) {
            throw new IllegalArgumentException("El tipo de usuario debe ser 'administrativo', 'cliente' o 'proveedor'");
        }

        // Validar duplicados para email y teléfono
        validarDuplicados(parametros.get(1), parametros.get(2));
    }

    public void desconectar() {
        connection.closeConnection();
    }
}
