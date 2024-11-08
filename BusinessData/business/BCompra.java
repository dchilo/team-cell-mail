package business;

import data.DCompra;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class BCompra {
    private final DCompra dCompra;
    private final BInventario bInventario;

    public BCompra() {
        dCompra = new DCompra();
        bInventario = new BInventario();
    }

    // Método para guardar la compra y registrar el movimiento en inventario
    public void guardar(List<String> parametros) throws SQLException, ParseException {
        // Guardar la compra en la base de datos
        int proveedorId = Integer.parseInt(parametros.get(0));
        int productoId = Integer.parseInt(parametros.get(1));
        int cantidad = Integer.parseInt(parametros.get(2));
        String fechaCompra = parametros.get(3);
        double montoTotal = Double.parseDouble(parametros.get(4));
        dCompra.guardar(proveedorId, productoId, cantidad, fechaCompra, montoTotal);

        // Registrar un movimiento de "entrada" en el inventario, lo cual incrementará el stock
        registrarMovimientoInventario(productoId, cantidad, fechaCompra);

        dCompra.desconectar();
    }

    // Método para registrar un movimiento de "entrada" en el inventario
    private void registrarMovimientoInventario(int productoId, int cantidad, String fechaMovimiento) throws SQLException, ParseException {
        String tipoMovimiento = "ingreso";

        // Crear el registro de inventario como una "entrada", lo cual aumentará el stock
        List<String> inventarioParametros = List.of(
                String.valueOf(productoId),
                String.valueOf(cantidad),
                tipoMovimiento,
                fechaMovimiento
        );
        bInventario.guardarMovimientoInventario(inventarioParametros);
    }

    public void modificar(List<String> parametros) throws SQLException {
        dCompra.editar(Integer.parseInt(parametros.get(0)), Integer.parseInt(parametros.get(1)), Integer.parseInt(parametros.get(2)), Integer.parseInt(parametros.get(3)), parametros.get(4), Double.parseDouble(parametros.get(5)));
        dCompra.desconectar();
    }

    public void eliminar(List<String> parametros) throws SQLException {
        dCompra.eliminar(Integer.parseInt(parametros.get(0)));
        dCompra.desconectar();
    }

    public ArrayList<String[]> listar() throws SQLException {
        ArrayList<String[]> compras = (ArrayList<String[]>) dCompra.listar();
        dCompra.desconectar();
        return compras;
    }

    public ArrayList<String[]> listarGrafica() throws SQLException {
        ArrayList<String[]> compras = (ArrayList<String[]>) dCompra.listarGrafica();
        dCompra.desconectar();
        return compras;
    }

    public ArrayList<String[]> ver(List<String> parametros) throws SQLException {
        ArrayList<String[]> compras = new ArrayList<>();
        compras.add(dCompra.ver(Integer.parseInt(parametros.get(0))));
        dCompra.desconectar();
        return compras;
    }

    public ArrayList<String[]> ayuda() throws SQLException {
        return (ArrayList<String[]>) dCompra.ayuda();
    }

    public void desconectar() {
        dCompra.desconectar();
    }
}
