package business;

import data.DVenta;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class BVenta {
    private final DVenta dVenta;
    private final BInventario bInventario;

    public BVenta() {
        dVenta = new DVenta();
        bInventario = new BInventario();
    }

    // Método para guardar la venta y registrar el movimiento en inventario
    public void guardar(List<String> parametros) throws SQLException, ParseException {
        // Guardar la venta en la base de datos
        int usuarioId = Integer.parseInt(parametros.get(0));
        int productoId = Integer.parseInt(parametros.get(1));
        int cantidad = Integer.parseInt(parametros.get(2));
        String fechaVenta = parametros.get(3);
        String direccionEnvio = parametros.get(4);
        dVenta.guardar(usuarioId, productoId, cantidad, fechaVenta, direccionEnvio);

        // Registrar un movimiento de "salida" en el inventario, lo cual reducirá el stock
        registrarMovimientoInventario(productoId, cantidad, fechaVenta);

        dVenta.desconectar();
    }

    // Método para registrar un movimiento de "salida" en el inventario
    private void registrarMovimientoInventario(int productoId, int cantidad, String fechaMovimiento) throws SQLException, ParseException {
        String tipoMovimiento = "salida";

        // Crear el registro de inventario como una "salida", lo cual reducirá el stock
        List<String> inventarioParametros = List.of(
                String.valueOf(productoId),
                String.valueOf(cantidad),
                tipoMovimiento,
                fechaMovimiento
        );
        System.out.println(inventarioParametros);
        bInventario.guardarMovimientoInventario(inventarioParametros);
    }

    public void modificar(List<String> parametros) throws SQLException, ParseException {
        dVenta.modificar(Integer.parseInt(parametros.get(0)), Integer.parseInt(parametros.get(1)), Integer.parseInt(parametros.get(2)), Integer.parseInt(parametros.get(3)), parametros.get(4), parametros.get(5));
        dVenta.desconectar();
    }

    public void eliminar(List<String> parametros) throws SQLException {
        dVenta.eliminar(Integer.parseInt(parametros.getFirst()));
        dVenta.desconectar();
    }

    public ArrayList<String[]> listar() throws SQLException {
        ArrayList<String[]> ventas = (ArrayList<String[]>) dVenta.listar();
        dVenta.desconectar();
        return ventas;
    }

    public ArrayList<String[]> listarGrafica() throws SQLException {
        return (ArrayList<String[]>) dVenta.listarGrafica();
    }

    public ArrayList<String[]> ayuda() throws SQLException {
        return (ArrayList<String[]>) dVenta.ayuda();
    }
}
