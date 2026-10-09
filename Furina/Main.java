import java.util.Scanner;
import java.util.Random;

public class Main {
    static Scanner sc = new Scanner(System.in);
    static Peluqueria peluqueria = new Peluqueria();
    static Random azar = new Random();

    public static void main(String[] args) {
        int opcion = 0;

        do {
            System.out.println("--- patitas y pelos ---");
            System.out.println("1 Registrar persona");
            System.out.println("2 Agregar mascota");
            System.out.println("3 clientes y mascotas");
            System.out.println("4 hacer corte");
            System.out.println("5 historial");
            System.out.println("6 Cerrar programa");

            try {
                System.out.print("Opcion: ");
                opcion = Integer.parseInt(sc.nextLine());

                switch (opcion) {
                    case 1: registrarPersona(); break;
                    case 2: agregarMascota(); break;
                    case 3: consultarClientes(); break;
                    case 4: realizarCorte(); break;
                    case 5: consultarHistorial(); break;
                    case 6:
                        peluqueria.guardarArchivos();
                        System.out.println("Gracias por usar el programa.");
                        break;
                    default: System.out.println("Opcion no valida.");
                }
            } catch (Exception e) {
                System.out.println("Entrada no valida hacelo denuevo5.");
            }
        } while (opcion != 6);
    }

    // Registra una persona y permite agregarle mascotas altiro.
    static void registrarPersona() {
        System.out.print("Nombre del cliente: ");
        String nombre = sc.nextLine();
        System.out.print("RUT: ");
        String rut = sc.nextLine();

        if (peluqueria.buscarPersona(rut) != null) {
            System.out.println("Ese RUT ya esta registrado.");
            return;
        }

        Persona persona = new Persona(nombre, rut);
        peluqueria.agregarCliente(persona);
        System.out.println("Cliente registrado.");

        System.out.print("Desea agregar una mascota? (ingrese s para si y n para no): ");
        String respuesta = sc.nextLine();
        while (respuesta.equalsIgnoreCase("s")) {
            crearMascota(persona);
            System.out.print("Agregar otra mascota? (ingrese s para si y n para no): ");
            respuesta = sc.nextLine();
        }
    }

    static void agregarMascota() {
        System.out.print("RUT del dueño (poner sin punto ni guion): ");
        Persona persona = peluqueria.buscarPersona(sc.nextLine());

        if (persona == null) {
            System.out.println("Primero debe registrar al cliente.");
            return;
        }
        crearMascota(persona);
    }

    static void crearMascota(Persona persona) {
        System.out.print("Nombre de la mascota: ");
        String nombre = sc.nextLine();
        System.out.print("Especie: ");
        String especie = sc.nextLine();
        System.out.print("Edad: ");
        int edad = Integer.parseInt(sc.nextLine());

        if (edad < 0) {
            System.out.println("La edad no puede ser negativa");
            return;
        }

        TipoPelo pelo = pedirPelo();
        int id = peluqueria.nuevoId();
        persona.agregarMascota(new Mascota(id, nombre, especie, edad, pelo));
        System.out.println("Mascota agregada. ID asignado: " + id);
    }

    static TipoPelo pedirPelo() {
        while (true) {
            System.out.print("Largo del pelo (pelao, corto, medio, largo): ");
            String texto = sc.nextLine().trim().toUpperCase().replace(" ", "_");
            try {
                return TipoPelo.valueOf(texto);
            } catch (Exception e) {
                System.out.println("Opcion no valida.");
            }
        }
    }

    static void consultarClientes() {
        if (peluqueria.getCantidadClientes() == 0) {
            System.out.println("No hay clientes registrados.");
            return;
        }

        for (int i = 0; i < peluqueria.getCantidadClientes(); i++) {
            Persona p = peluqueria.getClientes()[i];
            System.out.println(p.getNombre() + " - RUT: " + p.getRut());
        }

        System.out.print("Ingrese el rut a consultar: ");
        Persona persona = peluqueria.buscarPersona(sc.nextLine());
        if (persona == null) {
            System.out.println("No existe ese cliente.");
            return;
        }
        if (persona.getCantidadMascotas() == 0) {
            System.out.println("Este cliente no tiene mascotas.");
            return;
        }
        for (int i = 0; i < persona.getCantidadMascotas(); i++) {
            Mascota m = persona.getMascotas()[i];
            System.out.println(m);
        }
    }

    static void realizarCorte() {
        System.out.print("RUT del cliente: ");
        Persona persona = peluqueria.buscarPersona(sc.nextLine());
        if (persona == null) {
            System.out.println("Cliente no encontrado.");
            return;
        }
        if (persona.getCantidadMascotas() == 0) {
            System.out.println("Este cliente no tiene mascotas.");
            return;
        }

        for (int i = 0; i < persona.getCantidadMascotas(); i++) {
            System.out.println(persona.getMascotas()[i]);
        }
        System.out.print("ID de la mascota: ");
        int id = Integer.parseInt(sc.nextLine());
        Mascota mascota = persona.buscarMascota(id);

        if (mascota == null) {
            System.out.println("Esa mascota no pertenece a este cliente.");
            return;
        }
        if (mascota.getPelo() == TipoPelo.PELAO) {
            System.out.println("Esta mascota no puede recibir cortes.");
            return;
        }

        System.out.println("Cortes disponibles:");
        if (mascota.getPelo().ordinal() >= TipoPelo.CORTO.ordinal())
            System.out.println("1. Corto - $10000");
        if (mascota.getPelo().ordinal() >= TipoPelo.MEDIO.ordinal())
            System.out.println("2. Medio - $20000");
        if (mascota.getPelo().ordinal() >= TipoPelo.LARGO.ordinal())
            System.out.println("3. Largo - $30000");

        System.out.print("Seleccione el corte: ");
        int opcion = Integer.parseInt(sc.nextLine());
        TipoCorte corte;

        if (opcion == 1) corte = TipoCorte.CORTO;
        else if (opcion == 2) corte = TipoCorte.MEDIO;
        else if (opcion == 3) corte = TipoCorte.LARGO;
        else {
            System.out.println("Corte no valido.");
            return;
        }

        if (corte.ordinal() + 1 > mascota.getPelo().ordinal()) {
            System.out.println("Ese corte no es compatible con el pelo actual.");
            return;
        }

        boolean fallo = azar.nextInt(3) == 0; // 1 posibilidad entre 3.
        if (!fallo) {
            mascota.setPelo(TipoPelo.valueOf(corte.toString()));
        }

        Atencion atencion = new Atencion(persona.getRut(), mascota.getId(),
                corte, corte.getPrecio(), fallo);
        peluqueria.agregarAtencion(atencion);

        System.out.println("\n--- COMPROBANTE ---");
        System.out.println("Cliente: " + persona.getNombre() + " | RUT: " + persona.getRut());
        System.out.println("Mascota: " + mascota.getNombre() + " | ID: " + mascota.getId());
        System.out.println("Corte: " + corte);
        System.out.println("Resultado: " + (fallo ? "Fallido" : "Exitoso"));
        System.out.println("Pelo actual: " + mascota.getPelo());
        System.out.println("Total pagado: $" + corte.getPrecio());
    }

    static void consultarHistorial() {
        if (peluqueria.getCantidadAtenciones() == 0) {
            System.out.println("El historial esta vacio.");
            return;
        }
        for (int i = 0; i < peluqueria.getCantidadAtenciones(); i++) {
            Atencion a = peluqueria.getHistorial()[i];
            System.out.println(a);
        }
    }
}
