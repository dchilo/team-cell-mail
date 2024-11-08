package business;

import  data.DInventario;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


public class BInventario {
    private final DInventario dInventario;

    public BInventario() {
        dInventario = new DInventario();
    }

    // Método para manejar la lógica de actualizar stock según el tipo de movimiento
    public void procesarMovimientoInventario(int productoId, int cantidad, String tipoMovimiento) throws SQLException {
        int stockActual = dInventario.obtenerStock(productoId);

        int nuevoStock = tipoMovimiento.equalsIgnoreCase("ingreso") ? stockActual + cantidad : stockActual - cantidad;

        // Solo aplicamos la lógica de negocio aquí, sin validaciones
        if (nuevoStock < 0) {
            throw new IllegalArgumentException("El stock resultante no puede ser negativo");
        }

        dInventario.actualizarStock(productoId, nuevoStock);
    }

    // Método para guardar un nuevo registro en el inventario
    public void guardarMovimientoInventario(List<String> parametros) throws SQLException, ParseException {
        int productoId = Integer.parseInt(parametros.get(0));
        int cantidad = Integer.parseInt(parametros.get(1));
        String tipoMovimiento = parametros.get(2);
        String fechaMovimiento = parametros.get(3);

        procesarMovimientoInventario(productoId, cantidad, tipoMovimiento);

        // Registrar el movimiento en el inventario
        dInventario.guardar(productoId, cantidad, tipoMovimiento, fechaMovimiento);
    }

    // Otros métodos CRUD para manejar inventario en caso de ser necesario
    public void modificar(List<String> parametros) throws SQLException {
        dInventario.modificar(Integer.parseInt(parametros.get(0)), Integer.parseInt(parametros.get(1)), Integer.parseInt(parametros.get(2)), parametros.get(3), parametros.get(4));
        dInventario.desconectar();
    }

    public void eliminar(List<String> parametros) throws SQLException {
        dInventario.eliminar(Integer.parseInt(parametros.get(0)));
        dInventario.desconectar();
    }

    public ArrayList<String[]> listar() throws SQLException {
        ArrayList<String[]> inventarios = (ArrayList<String[]>) dInventario.listar();
        dInventario.desconectar();
        return inventarios;
    }

    public ArrayList<String[]> listarGrafica() throws SQLException {
        ArrayList<String[]> inventarios = (ArrayList<String[]>) dInventario.listarGrafica();
        dInventario.desconectar();
        return inventarios;
    }

    public ArrayList<String[]> ver(List<String> parametros) throws SQLException {
        ArrayList<String[]> inventarios = new ArrayList<>();
        inventarios.add(dInventario.ver(Integer.parseInt(parametros.get(0))));
        dInventario.desconectar();
        return inventarios;
    }

    public ArrayList<String[]> ayuda() throws SQLException {
        return (ArrayList<String[]>) dInventario.ayuda();
    }
}