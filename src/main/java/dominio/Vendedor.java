package dominio;

import dominio.excepcion.GarantiaExtendidaException;
import dominio.repositorio.RepositorioProducto;
import dominio.repositorio.RepositorioGarantiaExtendida;
import persistencia.builder.ProductoBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.Optional.ofNullable;

public class Vendedor implements  GarantiaExtendidaFactory{

    public static final String EL_PRODUCTO_TIENE_GARANTIA = "El producto ya cuenta con una garantia extendida";
    public static final String EL_PRODUCTO_NO_ES_ASEGURABLE = "El producto no es asegurable";
    private static  final double CONDICIONVALOR = 500000;
    private static  final double MAYORDESCUENTO = 0.20;
    private static  final double MENOR_DESCUENTO = 0.10;

    private RepositorioProducto repositorioProducto;
    private RepositorioGarantiaExtendida repositorioGarantia;

    public Vendedor(RepositorioProducto repositorioProducto, RepositorioGarantiaExtendida repositorioGarantia) {
        this.repositorioProducto = repositorioProducto;
        this.repositorioGarantia = repositorioGarantia;

    }

    private Boolean validarPropiedadesProducto(double valor){
       Predicate<Double> validarValor = i -> i >= CONDICIONVALOR;
       Boolean response = validarValor.test(valor);
       return validarValor.test(valor);
    }

    private Boolean noEsProductoAsegurable(String codigo) {
        int cantidadVocales = 0;
        Predicate<Integer> validador =  i -> i == 3;
        for(int x=0;x<codigo.length();x++) {
            if ((codigo.charAt(x)=='A') || (codigo.charAt(x)=='E') || (codigo.charAt(x)=='I') ||
                    (codigo.charAt(x)=='O') || (codigo.charAt(x)=='U')){
                cantidadVocales++;
            }
        }
            return  validador.test(cantidadVocales);
        }

    public void generarGarantia(String codigo) {
        GarantiaExtendida garantiaExtendida ;

        if(noEsProductoAsegurable(codigo)) {
            throw new GarantiaExtendidaException(EL_PRODUCTO_NO_ES_ASEGURABLE);
        }

        if(tieneGarantia(codigo)){
            throw new GarantiaExtendidaException(EL_PRODUCTO_TIENE_GARANTIA);
        } else {
            Producto producto = repositorioProducto.obtenerPorCodigo(codigo);
            if(validarPropiedadesProducto(producto.getPrecio())){
                double valorGarantia = producto.getPrecio() * MAYORDESCUENTO;
                Date finalDate = sumarDiasAFecha(Calendar.getInstance().getTime() , 200);
                garantiaExtendida = crearGarantia(producto, Calendar.getInstance().getTime(),
                        finalDate , valorGarantia, "Jeiner");
            } else  {
                double valorGarantia = producto.getPrecio() * MENOR_DESCUENTO;
                Date finalDate = sumarDiasAFecha(Calendar.getInstance().getTime() , 100);
                garantiaExtendida = crearGarantia(producto, Calendar.getInstance().getTime(),
                        finalDate , valorGarantia, "Tiberio");
            }

            repositorioGarantia.agregar(garantiaExtendida);
        }
    }
    
    public Date sumarDiasAFecha(Date fecha, int dias){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha); 
        calendar.add(Calendar.DAY_OF_YEAR, dias);  
        return calendar.getTime(); 
  }

    public boolean tieneGarantia(String codigo) {
        Optional<Producto> producto1 = ofNullable(repositorioGarantia.obtenerProductoConGarantiaPorCodigo(codigo));
        return producto1.isPresent();
    }


}
