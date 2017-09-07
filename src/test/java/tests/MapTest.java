package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Vector;

import org.junit.Test;

import control.InputObject;
import model.Serializer;
import multiplaying.Checksum;
import plateau.Character;
import plateau.Checkpoint;
import plateau.Plateau;
import ressources.Map;

public strictfp class MapTest {

	@Test
	public void test() {
		Plateau plateau = Map.createPlateau("test01", "maptests");
		plateau.update(new Vector<InputObject>());
		assertEquals(plateau.getCharacters().size(),1);
		assertEquals(plateau.getBuildings().size(),2);
	}
	
	@Test
	public void testIdDifferentes() {
		Plateau plateau = Map.createPlateau("test02", "maptests");
		plateau.update(new Vector<InputObject>());
		//plateau.print();
		for(int i=0; i<5; i++){
			System.out.println(i);
			assertEquals(plateau.getCharacters().get(i).id,i+6);
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
	
//	@Test
//	public void testDeplacementVersEnnemi() {
//		Plateau plateau = Map.createPlateau("test04", "maptests");
//		plateau.update(new Vector<InputObject>());
//		float f = plateau.getById(7).x;
//		float f1 = f;
//		for(int i=0; i<1000; i++){
//			plateau.update(new Vector<InputObject>());
//			if(plateau.characters.size()<3){
//				break;
//			}
//		} 
//		f = plateau.getById(7).x;
//		assertFalse(f==f1);
//	}
	
//
//	@Test
//	public void testMoveToward() {
//		Plateau plateau = Map.createPlateau("test01", "maptests");
//		plateau.update(new Vector<InputObject>());
//		Character c =(Character) plateau.characters.get(0);
//		float xObjectif = 10;
//		float yObjectif = 90;
//		Checkpoint checkpoint = new Checkpoint(xObjectif, yObjectif, plateau);
//		c.setTarget(checkpoint, plateau);
//		for(int i=0; i<1000; i++){
//			plateau.update(new Vector<InputObject>());
//		}
//		assertTrue(StrictMath.abs(c.x - xObjectif)<50);
//		assertTrue(StrictMath.abs(c.y - yObjectif)<50);
//	}
	
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
			plateau.getCharacters().get(0).setTarget(new Checkpoint(100+(float)StrictMath.random(), 100+(float)StrictMath.random(), plateau), plateau);
			assertTrue(2*refSize>serializedPlateau.length);
		}
	}
	
	
	@Test
	public void testChecksum() throws ClassNotFoundException, IOException {
		Plateau plateau = Map.createPlateau("test01", "maptests");
		plateau.update(new Vector<InputObject>());
		assertTrue(new Checksum(plateau).equals(new Checksum(plateau)));
	}
	@Test
	public void testPlateauSerializableChecksum() throws ClassNotFoundException, IOException {
		Plateau plateau = Map.createPlateau("test01", "maptests");
		plateau.update(new Vector<InputObject>());
		int refSize = 0;
		for(int i=0; i<100; i++){
			byte[] serializedPlateau1 = Serializer.serialize(plateau);
			byte[] serializedPlateau2 = Serializer.serialize(plateau);
			Plateau plateau1 = (Plateau) Serializer.deserialize(serializedPlateau1);
			Plateau plateau2 = (Plateau) Serializer.deserialize(serializedPlateau2);
			assertTrue(new Checksum(plateau1).equals(new Checksum(plateau2)));
		}
	}
	
	@Test
	public void testChecksumRoundDifferentEqual() throws ClassNotFoundException, IOException {
		Plateau plateau = Map.createPlateau("test01", "maptests");
		Checksum check1 = new Checksum(plateau);
		plateau.update(new Vector<InputObject>());
		plateau.getCharacters().get(0).x += 2;
		Checksum check2 = new Checksum(plateau);
		assertTrue(check1.equals(check2));
	}
	@Test
	public void testChecksumEqualDifferentPlateau() throws ClassNotFoundException, IOException {
		Plateau plateau = Map.createPlateau("test01", "maptests");
		plateau.update();
		Checksum check1 = new Checksum(plateau);
		Plateau plateau2 = Map.createPlateau("test01", "maptests");
		plateau2.update();
		Checksum check2 = new Checksum(plateau2);
		assertTrue(check1.equals(check2));
	}
	
	@Test
	public void testChecksumDesynchro() throws ClassNotFoundException, IOException {
		Plateau plateau = Map.createPlateau("test01", "maptests");
		plateau.update();
		Checksum check1 = new Checksum(plateau);
		Plateau plateau2 = Map.createPlateau("test01", "maptests");
		plateau2.update();
		plateau2.getCharacters().get(0).x +=2;
		Checksum check2 = new Checksum(plateau2);
		assertFalse(check1.equals(check2));
	}
	



}
