package jhappscode.tienda_libros.vista;

import jhappscode.tienda_libros.modelo.Libro;
import jhappscode.tienda_libros.servicio.LibroServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@Component
public class LibroForm extends JFrame {
    LibroServicio libroServicio;
    private JPanel panel;
    private JTable tablaLibros;
    private JTextField idTexto;
    private JTextField libroTexto;
    private JTextField autorTexto;
    private JTextField precioTexto;
    private JTextField existenciasTexto;
    private JButton agregarButton;
    private JButton modificarButton;
    private JButton eliminarButton;
    private DefaultTableModel tablaModeloLibros;

    @Autowired
    public LibroForm(LibroServicio libroServicio){
        this.libroServicio = libroServicio;
        iniciarForma();
        agregarButton.addActionListener(e -> agregarLibro());

        tablaLibros.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                cargarLibroSeleccionado();
            }
        });
        modificarButton.addActionListener(e -> modificarLibro());
        eliminarButton.addActionListener(e -> eliminarLibro());
    }

    private void iniciarForma(){
         setContentPane(panel);
         setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         setVisible(true);
         setSize(900,700);
         Toolkit toolkit = Toolkit.getDefaultToolkit();
         Dimension tamanioPantalla = toolkit.getScreenSize();
         int x = (tamanioPantalla.width - getWidth()) / 2;
         int y = (tamanioPantalla.height = getHeight()) / 2;
         setLocation(x, y);
    }

    private void agregarLibro(){
        // leer los valores del formulario
        if(libroTexto.getText().equals("")){
            mostrarMensaje("Proporciona el nombre del libro");
            libroTexto.requestFocusInWindow();
            return;
        };
        var nombreLibro = libroTexto.getText();
        var autorLibro = autorTexto.getText();
        var precioLibro = Double.parseDouble(precioTexto.getText());
        var existenciasLibro = Integer.parseInt(existenciasTexto.getText());

        var libro = new Libro(null, nombreLibro, autorLibro, precioLibro, existenciasLibro);

        this.libroServicio.guardarLibro(libro);
        mostrarMensaje("Se guardo el libro...");
        limpiarFormulario();
        listarLibros();
    }

    private void cargarLibroSeleccionado(){
    // los indices de las tablas inican en cero
        var reglon = tablaLibros.getSelectedRow();
        if(reglon != -1){ // Regresa -1 si no se selecciono ningun registro
            String idLibro = tablaLibros.getModel().getValueAt(reglon, 0).toString();
            idTexto.setText(idLibro);
            String nombreLibro = tablaLibros.getModel().getValueAt(reglon, 1).toString();
            libroTexto.setText(nombreLibro);
            String autorLibro = tablaLibros.getModel().getValueAt(reglon, 2).toString();
            autorTexto.setText(autorLibro);
            String precioLibro = tablaLibros.getModel().getValueAt(reglon, 3).toString();
            precioTexto.setText(precioLibro);
            String existencias  = tablaLibros.getModel().getValueAt(reglon, 4).toString();
            existenciasTexto.setText(existencias);
        }
    }

    private void modificarLibro(){
        if (this.idTexto.getText().equals("")){
            mostrarMensaje("Debe seleccionar un registro...");
        }else{
            // Verificamos que el nombre del libro no sea nulo
            if (libroTexto.getText().equals("")){
                mostrarMensaje("Proporciona el nombre del Libro...");
                libroTexto.requestFocusInWindow();
                return;
            }
            // Llenamos el objeto libro a utilizar
            int idLibro = Integer.parseInt(idTexto.getText());
            var nombreLibro = libroTexto.getText();
            var autorLibro = autorTexto.getText();
            var precioLibro = Double.parseDouble(precioTexto.getText());
            var existenciasLibro = Integer.parseInt(existenciasTexto.getText());

            var libro = new Libro(idLibro, nombreLibro, autorLibro, precioLibro, existenciasLibro);
            libroServicio.guardarLibro(libro);
            mostrarMensaje("Se modifico el libro...");
            limpiarFormulario();
            listarLibros();
        }
    }

    private void eliminarLibro(){
        var reglon = tablaLibros.getSelectedRow();

        if(reglon != -1){
            String idLibro = tablaLibros.getModel().getValueAt(reglon, 0).toString();
            var libro = new Libro();
            libro.setIdLibro(Integer.parseInt(idLibro));
            libroServicio.eliminarLibro(libro);
            mostrarMensaje("Libro " + libro + " eliminado.");
            limpiarFormulario();
            listarLibros();
        }
        else {
            mostrarMensaje("No se a seleccionado un libro a eliminar...");
        }
    }

    private void limpiarFormulario(){
        libroTexto.setText("");
        autorTexto.setText("");
        precioTexto.setText("");
        existenciasTexto.setText("");
    }

    private void mostrarMensaje(String mensaje){
        JOptionPane.showMessageDialog(null, mensaje);
    }

    private void createUIComponents() {
        // Creamos el elemento idtexto oculto
        idTexto = new JTextField("");
        idTexto.setVisible(false);

        // TODO: place custom component creation code here
        this.tablaModeloLibros = new DefaultTableModel(0,5){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        String[] cabeceros = {"Id", "Libro", "Titulo", "Autor", "Existencias"};
        tablaModeloLibros.setColumnIdentifiers(cabeceros);
        this.tablaLibros = new JTable(tablaModeloLibros);
        // Evitar que se seleccionen varios registros
        tablaLibros.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listarLibros();
    }

    private void listarLibros(){
        tablaModeloLibros.setRowCount(0);
        var libros = libroServicio.listarLibros();
        libros.forEach((libro)->{
            Object[] renglonLibro = {
                    libro.getIdLibro(),
                    libro.getNombreLibro(),
                    libro.getAutor(),
                    libro.getPrecio(),
                    libro.getExistencias()
            };
            this.tablaModeloLibros.addRow(renglonLibro);
        });
    }
}
