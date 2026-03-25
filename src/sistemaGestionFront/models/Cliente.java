package sistemaGestionFront.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)

public class Cliente {

    private Long id;

    private String nombre;

    private String telefono;

    private Boolean activo;


    public Cliente(){}


    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id=id;
    }


    public String getNombre(){
        return nombre;
    }

    public void setNombre(String nombre){
        this.nombre=nombre;
    }


    public String getTelefono(){
        return telefono;
    }

    public void setTelefono(String telefono){
        this.telefono=telefono;
    }


    public Boolean getActivo(){
        return activo;
    }

    public void setActivo(Boolean activo){
        this.activo=activo;
    }


    @Override
    public String toString(){

        return nombre;

    }

}