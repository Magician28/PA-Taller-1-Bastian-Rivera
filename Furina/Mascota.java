public class Mascota {
    private int id;
    private String nombre;
    private String especie;
    private int edad;
    private TipoPelo pelo;

    public Mascota(int id, String nombre, String especie, int edad, TipoPelo pelo) {
        this.id = id;
        this.nombre = nombre;
        this.especie = especie;
        this.edad = edad;
        this.pelo = pelo;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getEspecie() { return especie; }
    public int getEdad() { return edad; }
    public TipoPelo getPelo() { return pelo; }

    public void setPelo(TipoPelo pelo) {
        this.pelo = pelo;
    }

    public String toString() {
        return "ID: " + id + " | " + nombre + " | " + especie
                + " | Edad: " + edad + " | Pelo: " + pelo;
    }
}
