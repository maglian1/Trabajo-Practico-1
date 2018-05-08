package ar.edu.unlam.tallerweb1.persistencia;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.unlam.tallerweb1.SpringTest;
import ar.edu.unlam.tallerweb1.modelo.Barrio;
import ar.edu.unlam.tallerweb1.modelo.Direccion;
import ar.edu.unlam.tallerweb1.modelo.Farmacia;

public class TestTP extends SpringTest{
	
//PUNTO 2: Hacer con junit un test que busque todas las farmacias de turno los días martes.
    
    @Test
    @Transactional  @Rollback(true)
    public void FarmaciasLosMartes(){
    	
		Farmacia farmacia = new Farmacia();
    	
		//Se le carga un valor al campo DiaDeTurno, de la tabla Farmacia
		farmacia.setDiaDeTurno ("Martes");
    	getSession().save(farmacia);
    	
    	
    	//Se crea la restriccion
    	List <Farmacia> list=
    	getSession().createCriteria(Farmacia.class)
    	.add(Restrictions.eq("diaDeTurno","Martes"))
    	.list();
    
    	//Se recorre la lista y muestra las farmacias que atiendan los dias martes
    	for(Farmacia farm: list){
    
    		assertThat(farm.getDiaDeTurno()).isEqualTo("Martes");
    	}
	}
    
    //PUNTO 3: Hacer con junit un test que busque todas las farmacias de una calle.
    
    @Test
    @Transactional  @Rollback(true)
    public void FarmaciasDeUnaCalle(){
    	
    	//Se crea una direccion y se le asigna una calle (Cada objeto direccion equivale a una calle)
    	Direccion direccion = new Direccion();
    	direccion.setCalle("Arieta");
    	getSession().save(direccion);
    	
    	//Se crea una farmacia y se le asigna el objeto direccion (es la unica forma para asignarle una calle a una farmacia)
    	Farmacia farmaciaA = new Farmacia();
    	farmaciaA.setNombre("Farmacia A");
    	farmaciaA.setDireccion(direccion);
    	getSession().save(farmaciaA);
    	
    	//Se crea una segunda farmacia para asegurarnos que funciona el test
    	Farmacia farmaciaB = new Farmacia();
    	farmaciaB.setNombre("Farmacia B");
    	farmaciaB.setDireccion(direccion);
    	getSession().save(farmaciaB);
    	
    	//Va a traer las farmacias con el objeto "direccion"
    	List <Farmacia> farm = getSession().createCriteria(Farmacia.class)
    	.add(Restrictions.eq("direccion", direccion))
    	.list();
    	
    	//El test va a corroborar si la cantidad de farmacias con dicha direccion es 2
    	assertThat(farm.size()).isEqualTo(2);
    	
    }
    
    //PUNTO 4: - Hacer con junit un test que busque todas las farmacias de un barrio
    
    @Test
    @Transactional  @Rollback(true)
    public void FarmaciasDeUnBarrio() {
    	
    	Barrio barrio = new Barrio();
    	barrio.setNombre("Luzuriaga");
    	getSession().save(barrio);
    	
    	Direccion direccion = new Direccion();
    	direccion.setCalle("Marcon");
    	direccion.setBarrio(barrio);
    	getSession().save(direccion);
    	
    	Farmacia farmacia = new Farmacia();
    	farmacia.setNombre("FarmaciaA");
    	farmacia.setDireccion(direccion);
    	getSession().save(farmacia);
    	
    	List <Farmacia> farm =
    			getSession().createCriteria(Farmacia.class)
    			.createAlias("direccion.barrio", "BarrioA")
    			.add(Restrictions.eq("BarrioA.nombre","Luzuriaga"))
    			.list();
    	
    	assertThat(farm.size()).isEqualTo(1);
    	
    }

}
