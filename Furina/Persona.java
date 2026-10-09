public class Persona {
    private String nombre;
    private String rut;
    private Mascota[] mascotas;
    private int cantidadMascotas;

    public Persona(String nombre, String rut) {
        this.nombre = nombre;
        this.rut = rut;
        mascotas = new Mascota[20]; // Maximo simple de mascotas por persona.
        cantidadMascotas = 0;
    }

    public String getNombre() { return nombre; }
    public String getRut() { return rut; }
    public Mascota[] getMascotas() { return mascotas; }
    public int getCantidadMascotas() { return cantidadMascotas; }

    public boolean agregarMascota(Mascota mascota) {
        if (cantidadMascotas >= mascotas.length) {
            return false;
        }
        mascotas[cantidadMascotas] = mascota;
        cantidadMascotas++;
        return true;
    }

    public Mascota buscarMascota(int id) {
        for (int i = 0; i < cantidadMascotas; i++) {
            if (mascotas[i].getId() == id) {
                return mascotas[i];
            }
        }
        return null;
    }
}
