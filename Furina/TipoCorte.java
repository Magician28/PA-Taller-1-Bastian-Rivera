// Tipos de corte y precios
public enum TipoCorte {
    CORTO(10000), MEDIO(20000), LARGO(30000);

    private int precio;

    TipoCorte(int precio) {
        this.precio = precio;
    }

    public int getPrecio() {
        return precio;
    }
}
