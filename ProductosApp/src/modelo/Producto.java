package modelo;

import java.time.LocalDate;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import util.LocalDateAdapter;

public class Producto {

	private final StringProperty descripcion;
	private final IntegerProperty unidades;
	private final DoubleProperty precio_uni;
	private final ObjectProperty<LocalDate> fecha_fab;
		
	public Producto() {
		this(null,0,0,null);
	}

	public Producto(String descripcion, int unidades, double precio_uni, LocalDate fecha) {
		super();
		this.descripcion =new SimpleStringProperty(descripcion);
		this.unidades =new SimpleIntegerProperty(unidades);
		this.precio_uni =new SimpleDoubleProperty(precio_uni);
		this.fecha_fab = new SimpleObjectProperty<LocalDate>(fecha);
	}
	
	public String getDescripcion() {
		return descripcion.get();
	}
	
	public void setDescripcion(String descripcion) {
		this.descripcion.set(descripcion);
	}

	public StringProperty descripcionProperty() {
		return descripcion;
	}
	
	public int getUnidades() {
		return unidades.get();
	}
	
	public void setUnidades(int unidades) {
		this.unidades.set(unidades);
	}

	public IntegerProperty unidadesProperty() {
		return unidades;
	}
	
	public Double getPrecio() {
		return precio_uni.get();
	}
	
	public void setPrecio(Double precio) {
		this.precio_uni.set(precio);
	}

	public DoubleProperty precio_uniProperty() {
		return precio_uni;
	}

	public LocalDate getFecha() {
		return fecha_fab.get();
	}
	
	public void setFecha(LocalDate fecha) {
		this.fecha_fab.set(fecha);
	}

	public ObjectProperty<LocalDate> fecha_fabProperty() {
		return fecha_fab;
	}
	
		
}
