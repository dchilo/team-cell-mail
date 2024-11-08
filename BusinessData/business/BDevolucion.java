package business;

import data.DDevolucion;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class BDevolucion {
    private final DDevolucion dDevolucion;

    public BDevolucion() {
        dDevolucion = new DDevolucion();
    }

    public void guardar(List<String> parametros) throws SQLException, ParseException {
        dDevolucion.guardar(Integer.parseInt(parametros.get(0)), parametros.get(1), parametros.get(2));
        dDevolucion.desconectar();
    }

    public void modificar(List<String> parametros) throws SQLException {
        dDevolucion.modificar(Integer.parseInt(parametros.get(0)), Integer.parseInt(parametros.get(1)), parametros.get(2), parametros.get(3));
        dDevolucion.desconectar();
    }

    public void eliminar(List<String> parametros) throws SQLException {
        dDevolucion.eliminar(Integer.parseInt(parametros.get(0)));
        dDevolucion.desconectar();
    }

    public ArrayList<String[]> listar() throws SQLException {
        ArrayList<String[]> devoluciones = (ArrayList<String[]>) dDevolucion.listar();
        dDevolucion.desconectar();
        return devoluciones;
    }

    public ArrayList<String[]> listarGrafica() throws SQLException {
        ArrayList<String[]> devoluciones = (ArrayList<String[]>) dDevolucion.listarGrafica();
        dDevolucion.desconectar();
        return devoluciones;
    }

    public ArrayList<String[]> ver(List<String> parametros) throws SQLException {
        ArrayList<String[]> devoluciones = new ArrayList<>();
        devoluciones.add(dDevolucion.ver(Integer.parseInt(parametros.getFirst())));
        dDevolucion.desconectar();
        return devoluciones;
    }

    public ArrayList<String[]> ayuda() throws SQLException {
        return (ArrayList<String[]>) dDevolucion.ayuda();
    }

}
