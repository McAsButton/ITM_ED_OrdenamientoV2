import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class ArbolBinario {

    private Nodo raiz;
    private int criterio;

    public ArbolBinario(Nodo raiz) {
        this.raiz = raiz;
    }

    public ArbolBinario() {
    }

    public int getCriterio() {
        return criterio;
    }

    public void setCriterio(int criterio) {
        this.criterio = criterio;
    }

    public Nodo getRaiz() {
        return raiz;
    }

    public void agregarNodo(Nodo n) {
        if (raiz == null) {
            raiz = n;
            return;
        }
        Nodo actual = raiz;
        Nodo padre;

        while (true) {
            padre = actual;
            if (n.getDocumento().equals(actual.getDocumento())) {
                return;
            } else if (Documento.esMayor(n.getDocumento(), actual.getDocumento(), criterio)) {
                actual = actual.derecho;
                if (actual == null) {
                    padre.derecho = n;
                    return;
                }
            } else {
                actual = actual.izquierdo;
                if (actual == null) {
                    padre.izquierdo = n;
                    return;
                }
            }
        }
    }

    public void mostrar(JTable tbl) {
        String[][] datos = null;

        if (raiz != null) {
            datos = new String[Documento.documentos.size()][Documento.encabezados.length];

            Nodo n = raiz;
            Nodo padre;
            int fila = -1;

            while (n != null) {
                if (n.izquierdo == null) {
                    fila++;
                    datos[fila][0] = n.getDocumento().getApellido1();
                    datos[fila][1] = n.getDocumento().getApellido2();
                    datos[fila][2] = n.getDocumento().getNombre();
                    datos[fila][3] = n.getDocumento().getDocumento();
                    n = n.derecho;
                } else {
                    padre = n.izquierdo;
                    while (padre.derecho != null && padre.derecho != n) {
                        padre = padre.derecho;
                    }
                    if (padre.derecho == null) {
                        padre.derecho = n;
                        n = n.izquierdo;
                    } else {
                        padre.derecho = null;
                        fila++;
                        datos[fila][0] = n.getDocumento().getApellido1();
                        datos[fila][1] = n.getDocumento().getApellido2();
                        datos[fila][2] = n.getDocumento().getNombre();
                        datos[fila][3] = n.getDocumento().getDocumento();
                        n = n.derecho;
                    }
                }
            }
        }
        DefaultTableModel dtm = new DefaultTableModel(datos, Documento.encabezados);
        tbl.setModel(dtm);
    }

    // Metodo para buscar el primer nodo encontrado con el texto buscado
    public int buscarNodo(String texto) {
        return buscarNodoRecursivo(raiz, texto);
    }
    
    private int buscarNodoRecursivo(Nodo nodo, String texto) {
        if (nodo == null) {
            return -1; // No se encontró el nodo
        }
        
        // Compara el texto con el documento del nodo actual
        if (nodo.getDocumento().getNombre().toUpperCase().contains(texto) || 
            nodo.getDocumento().getApellido1().toUpperCase().contains(texto) ||
            nodo.getDocumento().getApellido2().toUpperCase().contains(texto) || 
            nodo.getDocumento().getDocumento().toUpperCase().contains(texto)) {
            // Devuelve el índice del documento encontrado
            return Documento.documentos.indexOf(nodo.getDocumento());
        }
    
        // Busca en el subárbol izquierdo
        int encontrado = buscarNodoRecursivo(nodo.izquierdo, texto);
        if (encontrado != -1) {
            return encontrado; // Se encontró en el subárbol izquierdo
        }
    
        // Busca en el subárbol derecho
        return buscarNodoRecursivo(nodo.derecho, texto);
    }

    // Metodo para filtrar los nodos que contienen el texto buscado
    public List<Nodo> filtrarNodos(String texto) {
        List<Nodo> nodosFiltrados = new ArrayList<>();
        filtrarNodosRecursivo(raiz, texto.toUpperCase(), nodosFiltrados);
        return nodosFiltrados;
    }

    private void filtrarNodosRecursivo(Nodo nodo, String texto, List<Nodo> nodosFiltrados) {
        if (nodo == null) {
            return;
        }

        // Compara el texto con el documento del nodo actual
        if (nodo.getDocumento().getNombre().toUpperCase().contains(texto) || 
            nodo.getDocumento().getApellido1().toUpperCase().contains(texto) ||
            nodo.getDocumento().getApellido2().toUpperCase().contains(texto) || 
            nodo.getDocumento().getDocumento().toUpperCase().contains(texto)) {
            // Agrega el nodo a la lista de nodos filtrados
            nodosFiltrados.add(nodo);
        }

        // Buscar en el subárbol izquierdo y derecho
        filtrarNodosRecursivo(nodo.izquierdo, texto, nodosFiltrados);
        filtrarNodosRecursivo(nodo.derecho, texto, nodosFiltrados);
    }

    // Metodo para mostrar los nodos filtrados en la tabla
    public void mostrarFiltrado(JTable tbl, List<Nodo> nodosFiltrados) {
        String[][] datos = new String[nodosFiltrados.size()][Documento.encabezados.length];
        for (int i = 0; i < nodosFiltrados.size(); i++) {
            datos[i][0] = nodosFiltrados.get(i).getDocumento().getApellido1();
            datos[i][1] = nodosFiltrados.get(i).getDocumento().getApellido2();
            datos[i][2] = nodosFiltrados.get(i).getDocumento().getNombre();
            datos[i][3] = nodosFiltrados.get(i).getDocumento().getDocumento();
        }
        DefaultTableModel dtm = new DefaultTableModel(datos, Documento.encabezados);
        tbl.setModel(dtm);
    }
}
