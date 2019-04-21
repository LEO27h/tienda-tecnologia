package dominio;

import java.util.Date;

public interface GarantiaExtendidaFactory {

    default GarantiaExtendida crearGarantia( Producto producto,
             Date fechaSolicitudGarantia,
             Date fechaFinGarantia,
             double precioGarantia,
             String nombreCliente){
        return new GarantiaExtendida(producto, fechaSolicitudGarantia, fechaFinGarantia, precioGarantia, nombreCliente);
    }
}
