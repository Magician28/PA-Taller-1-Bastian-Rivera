import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;

public class Peluqueria {
    private Persona[] clientes;
    private int cantidadClientes;
    private Atencion[] historial;
    private int cantidadAtenciones;
    private int siguienteId;

    public Peluqueria() {
        clientes = new Persona[100];
        historial = new Atencion[500];
        cantidadClientes = 0;
        cantidadAtenciones = 0;
        siguienteId = 1;
        cargarHistorial();
        cargarClientes();
    }

    public Persona[] getClientes() { return clientes; }
    public int getCantidadClientes() { return cantidadClientes; }
    public Atencion[] getHistorial() { return historial; }
    public int getCantidadAtenciones() { return cantidadAtenciones; }

    public boolean agregarCliente(Persona persona) {
        if (cantidadClientes >= clientes.length) {
            return false;
        }
        clientes[cantidadClientes] = persona;
        cantidadClientes++;
        return true;
    }

    public boolean agregarAtencion(Atencion atencion) {
        if (cantidadAtenciones >= historial.length) {
            return false;
        }
        historial[cantidadAtenciones] = atencion;
        cantidadAtenciones++;
        return true;
    }

    public Persona buscarPersona(String rut) {
        for (int i = 0; i < cantidadClientes; i++) {
            if (clientes[i].getRut().equalsIgnoreCase(rut)) {
                return clientes[i];
            }
        }
        return null;
    }

    public boolean idExiste(int id) {
        for (int i = 0; i < cantidadClientes; i++) {
            if (clientes[i].buscarMascota(id) != null) {
                return true;
            }
        }
        return false;
    }

    public int nuevoId() {
        while (idExiste(siguienteId)) {
            siguienteId++;
        }
        return siguienteId++;
    }

    private TipoPelo leerPelo(String texto) {
        return TipoPelo.valueOf(texto.trim().toUpperCase().replace(" ", "_"));
    }

    private void cargarHistorial() {
        try (BufferedReader br = new BufferedReader(new FileReader("historial.txt"))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length == 5 && cantidadAtenciones < historial.length) {
                    String rut = datos[0].trim();
                    int id = Integer.parseInt(datos[1].trim());
                    TipoCorte corte = TipoCorte.valueOf(datos[2].trim().toUpperCase());
                    int total = Integer.parseInt(datos[3].trim());
                    boolean exitoso = Boolean.parseBoolean(datos[4].trim());
                    historial[cantidadAtenciones] = new Atencion(rut, id, corte, total, !exitoso);
                    cantidadAtenciones++;
                }
            }
        } catch (IOException e) {
            // Si no existe, comienza vacio.
        } catch (Exception e) {
            System.out.println("No se pudo leer alguna linea de historial.txt.");
        }
    }

    private void cargarClientes() {
        try (BufferedReader br = new BufferedReader(new FileReader("Clientes.txt"))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length < 3 || cantidadClientes >= clientes.length) continue;

                String nombre = limpiar(datos[0]);
                String rut = limpiar(datos[1]);
                int cantidad = Integer.parseInt(datos[2].trim());
                Persona persona = new Persona(nombre, rut);
                clientes[cantidadClientes] = persona;
                cantidadClientes++;

                for (int i = 0; i < cantidad; i++) {
                    String lineaMascota = br.readLine();
                    if (lineaMascota == null) break;
                    String[] m = lineaMascota.split(",");
                    if (m.length >= 5) {
                        int id = Integer.parseInt(m[0].trim());
                        String nombreMascota = limpiar(m[1]);
                        String especie = limpiar(m[2]);
                        int edad = Integer.parseInt(m[3].trim());
                        TipoPelo pelo = leerPelo(limpiar(m[4]));
                        persona.agregarMascota(new Mascota(id, nombreMascota, especie, edad, pelo));
                        if (id >= siguienteId) siguienteId = id + 1;
                    }
                }
            }
        } catch (IOException e) {
            // Si no existe, comienza sin clientes.
        } catch (Exception e) {
            System.out.println("No se pudo leer correctamente Clientes.txt.");
        }
    }

    private String limpiar(String texto) {
        return texto.trim().replace("\"", "").replace("“", "").replace("”", "");
    }

    public void guardarArchivos() {
        try (PrintWriter pw = new PrintWriter(new FileWriter("historial.txt"))) {
            for (int i = 0; i < cantidadAtenciones; i++) {
                pw.println(historial[i].aArchivo());
            }
        } catch (IOException e) {
            System.out.println("No se pudo guardar historial.txt.");
        }

        try (PrintWriter pw = new PrintWriter(new FileWriter("Clientes.txt"))) {
            for (int i = 0; i < cantidadClientes; i++) {
                Persona persona = clientes[i];
                pw.println("\"" + persona.getNombre() + "\"," + persona.getRut()
                        + "," + persona.getCantidadMascotas());
                for (int j = 0; j < persona.getCantidadMascotas(); j++) {
                    Mascota mascota = persona.getMascotas()[j];
                    pw.println(mascota.getId() + ",\"" + mascota.getNombre()
                            + "\",\"" + mascota.getEspecie() + "\","
                            + mascota.getEdad() + ",\"" + peloArchivo(mascota.getPelo()) + "\"");
                }
            }
        } catch (IOException e) {
            System.out.println("No se pudo guardar Clientes.txt.");
        }
    }

    private String peloArchivo(TipoPelo pelo) {
        if (pelo == TipoPelo.PELAO) return "pelao";
        return pelo.toString().substring(0, 1) + pelo.toString().substring(1).toLowerCase();
    }
}
