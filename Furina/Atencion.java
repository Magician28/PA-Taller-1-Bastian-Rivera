public class Atencion {
    private String rutDueno;
    private int idMascota;
    private TipoCorte corte;
    private int totalPagado;
    private boolean fallo;

    public Atencion(String rutDueno, int idMascota, TipoCorte corte,
                    int totalPagado, boolean fallo) {
        this.rutDueno = rutDueno;
        this.idMascota = idMascota;
        this.corte = corte;
        this.totalPagado = totalPagado;
        this.fallo = fallo;
    }

    public String toString() {
        return "RUT: " + rutDueno + " | ID mascota: " + idMascota
                + " | Corte: " + corte + " | Pagado: $" + totalPagado
                + " | Resultado: " + (fallo ? "Fallido" : "Exitoso");
    }

    public String aArchivo() {
        return rutDueno + "," + idMascota + "," + corte + ","
                + totalPagado + "," + (!fallo);
    }
}
