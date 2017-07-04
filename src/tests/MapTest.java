package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Vector;

import org.junit.Test;
import org.newdawn.slick.Graphics;

import control.InputObject;
import display.Camera;
import model.Serializer;
import plateau.Character;
import plateau.Checkpoint;
import plateau.Plateau;
import render.RenderEngine;
import ressources.Map;

public class MapTest {

	@Test
	public void test() {
		Plateau plateau = Map.createPlateau("test01", "maptests");
		plateau.update(new Vector<InputObject>());
		assertEquals(plateau.characters.size(),1);
		assertEquals(plateau.buildings.size(),2);
	}
	
	@Test
	public void testIdDifferentes() {
		Plateau plateau = Map.createPlateau("test02", "maptests");
		plateau.update(new Vector<InputObject>());
		//plateau.print();
		for(int i=0; i<5; i++){
			System.out.println(i);
			assertEquals(plateau.characters.get(i).id,i+6);
		}
	}

	@Test
	public void testPlateauEqual() {
		Plateau plateau1 = Map.createPlateau("test03", "maptests");
		Plateau plateau2 = Map.createPlateau("test03", "maptests");
		for(int i=0; i<1000; i++){
			plateau1.update(new Vector<InputObject>());
			plateau2.update(new Vector<InputObject>());
			assertEquals(plateau1, plateau2);
		}
	}
	
	@Test
	public void testDeplacementVersEnnemi() {
		Plateau plateau = Map.createPlateau("test04", "maptests");
		plateau.update(new Vector<InputObject>());
		float f = plateau.getById(7).x;
		float f1 = f;
		for(int i=0; i<1000; i++){
			plateau.update(new Vector<InputObject>());
			if(plateau.characters.size()<3){
				break;
			}
		} 
		f = plateau.getById(7).x;
		assertFalse(f==f1);
	}
	

	@Test
	public void testMoveToward() {
		Plateau plateau = Map.createPlateau("test01", "maptests");
		plateau.update(new Vector<InputObject>());
		Character c =(Character) plateau.characters.get(0);
		float xObjectif = 10;
		float yObjectif = 90;
		Checkpoint checkpoint = new Checkpoint(xObjectif, yObjectif, plateau);
		c.setTarget(checkpoint, plateau);
		for(int i=0; i<5000; i++){
			plateau.update(new Vector<InputObject>());
		}
		assertTrue(Math.abs(c.x - xObjectif)<50);
		assertTrue(Math.abs(c.y - yObjectif)<50);
	}
	
	@Test
	public void testPlateauSerializable() throws ClassNotFoundException, IOException {
		Plateau plateau = Map.createPlateau("test01", "maptests");
		byte[] serializedPlateau = Serializer.serialize(plateau);
		Plateau plateau2 = (Plateau)Serializer.deserialize(serializedPlateau);
	}
	
	@Test
	public void testPlateauSerializableSizeStatic() throws ClassNotFoundException, IOException {
		Plateau plateau = Map.createPlateau("test01", "maptests");
		int refSize = 0;
		for(int i=0; i<10; i++){
			plateau.update(new Vector<InputObject>());
			byte[] serializedPlateau = Serializer.serialize(plateau);
			if(refSize==0){
				refSize=serializedPlateau.length;
			}
			assertTrue(refSize==serializedPlateau.length);
		}
	}
	@Test
	public void testPlateauSerializableDontGrowTooMuch() throws ClassNotFoundException, IOException {
		Plateau plateau = Map.createPlateau("test01", "maptests");
		int refSize = 0;
		for(int i=0; i<100; i++){
			byte[] serializedPlateau = Serializer.serialize(plateau);
			
			if(refSize==0){
				refSize=serializedPlateau.length;
			}
			plateau.update(new Vector<InputObject>());
			plateau.characters.get(0).setTarget(new Checkpoint(100+(float)Math.random(), 100+(float)Math.random(), plateau), plateau);
			assertTrue(2*refSize>serializedPlateau.length);
		}
	}
	
	



}
