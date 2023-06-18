package ejecutable;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import controlador.ControladorServer;
import modelo.HiloServidor;
import modelo.ListaClientes;
import modelo.SocketCliente;
import util.Constante;

public class Servidor {
	private ServerSocket server = null;
	private ListaClientes clientes;
	private ControladorServer controlador;
	private ArrayList<String> historial = new ArrayList<String>();

	// ------------------EJECUTABLE--------------------//
	public static void main(String[] args) {
		try {
			new ControladorServer();

		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}

	// ------------------METODOS--------------------//
	public void iniciarServidor() throws IOException {

		server = new ServerSocket(Constante.PUERTO_SERVIDOR);
		clientes = new ListaClientes();
	}

	// mientras que el servidor este abierto, recibe conexiones de usuarios y las
	// almacena
	public void recibirClientes() {
		notificar("Servidor iniciado\n");
		while (!server.isClosed()) {
			try {
				Socket socket = server.accept();
				HiloServidor thread = new HiloServidor(socket, this);
				thread.start();
			} catch (IOException e) {
				if (!server.isClosed())
					e.printStackTrace();
			}
		}
	}

	public void meterCliente(String user, Socket socket) {
		clientes.add(user, socket);
		notificar(user + " inicio sesion\n");
		controlador.actualizarConectados();
	}

	public void closeServer() {
		try {
			// cierra todos los sockets
			for (HashMap.Entry<String, SocketCliente> entry : clientes.getMapaClientes().entrySet()) {
				entry.getValue().getSocket().close();
			}
			notificar("Cerrando servidor...\n");
			server.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void quitarCliente(String cliente) {
		clientes.remove(cliente);
		notificar(cliente + " finalizo su sesion\n");
		controlador.actualizarConectados();
	}

	public void notificar(String notificacion) {
		this.historial.add(notificacion);
		controlador.notificar();
	}

	// ------------------GETTERS--------------------//
	public ListaClientes getClientes() {
		return clientes;
	}

	public Socket getSocketCliente(String cliente) {
		return clientes.get(cliente).getSocket();
	}

	public ServerSocket getServer() {
		return server;
	}

	public int getClientesConectados() {
		return clientes.getClientesConectados();
	}

	public void setControlador(ControladorServer controlador) {
		this.controlador = controlador;
	}

	public ArrayList<String> getHistorial() {
		return historial;
	}

}
