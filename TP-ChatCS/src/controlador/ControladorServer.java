package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.BindException;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

import ejecutable.Servidor;
import util.Constante;
import vista.VServer;

@SuppressWarnings("static-access")
public class ControladorServer implements ActionListener {
	VServer vista;
	Servidor servidor;

	// ------------------------CONSTRUCTOR------------------------//
	public ControladorServer() {
		servidor = new Servidor();
		servidor.setControlador(this);
		try {
			servidor.iniciarServidor();
			vista = new VServer();
			vista.settearDatos(String.valueOf(this.servidor.getServer().getInetAddress().getLocalHost()),
					String.valueOf(this.servidor.getServer().getLocalPort()),
					String.valueOf(this.servidor.getClientesConectados()));
			vista.setActionListener(this);
			servidor.recibirClientes();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (BindException e) {
			JOptionPane.showMessageDialog(null, "El servidor ya fue iniciado");
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "No se puede establecer la conexion");
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "Numero invalido de puerto");
		}
	}

	// ------------------------METODOS AVANZADOS------------------------//
	@Override
	public void actionPerformed(ActionEvent e) {
		String comando = e.getActionCommand();
		if (comando.equalsIgnoreCase(Constante.BOTON_APAGAR)) {
			int option = JOptionPane.showConfirmDialog(null,
					"¿Seguro que desea apagar el servidor? Ningun dato quedara almacenado", "Confirmación",
					JOptionPane.YES_NO_OPTION);
			if (option == JOptionPane.YES_OPTION) {
				servidor.closeServer();
				vista.cerrarse();
			}
		}
	}

	public void actualizarConectados() {
		vista.actualizarCant(String.valueOf(servidor.getClientesConectados()));
	}

	// ------------------------SETTERS------------------------//
	public void setVista(VServer vista) {
		this.vista = vista;
	}

	public void setServidor(Servidor servidor) {
		this.servidor = servidor;
	}

	public void notificar() {
		this.vista.actualizarHistorial(servidor.getHistorial());
	}

}
