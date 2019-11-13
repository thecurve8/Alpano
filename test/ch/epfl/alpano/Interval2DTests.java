package ch.epfl.alpano;

import static org.junit.Assert.*;

import org.junit.Test;

public class Interval2DTests {

	@Test
	public void NewInterval(){
		new Interval2D(	new Interval1D(-2, 34), new Interval1D(-2, 34));
	}
	
	@Test
	public void NewInterval1(){
		Interval1D a = new Interval1D(-2, 34);
		new Interval2D(a, a);
	}
	
	@Test (expected = NullPointerException.class)
	public void NewIntervalWithnull(){
		Interval1D a = new Interval1D(-2, 34);

		new Interval2D(a, null);
	}
	
	@Test (expected = NullPointerException.class)
	public void NewIntervalWithnull1(){
		Interval1D a = new Interval1D(-2, 34);

		new Interval2D(null, a);
	}
	
	@Test (expected = NullPointerException.class)
	public void NewIntervalWithnull2(){

		new Interval2D(null, null);
	}
	
	@Test
	public void iX(){
		Interval1D a = new Interval1D(-2, 34);
		Interval1D b = new Interval1D(-2, 34);
		
		Interval2D c = new Interval2D(a, b);
		
		assertEquals(a, c.iX());
	}
	
	@Test
	public void iY(){
		Interval1D a = new Interval1D(-2, 34);
		Interval1D b = new Interval1D(-2, 34);
		
		Interval2D c = new Interval2D(a, b);
		
		assertEquals(b, c.iY());
	}
	
	
	@Test
	public void ContainsTest(){
		Interval1D a = new Interval1D(-2, 34);
		Interval1D b = new Interval1D(4, 10);
		
		Interval2D c = new Interval2D(a, b);
		assertTrue(c.contains(-2,4));
	}
	
	@Test
	public void ContainsTest1(){
		Interval1D a = new Interval1D(-2, 34);
		Interval1D b = new Interval1D(4, 10);
		
		Interval2D c = new Interval2D(a, b);
		assertTrue(c.contains(8,8));
	}
	
	@Test
	public void ContainsTest2(){
		Interval1D a = new Interval1D(-2, 34);
		Interval1D b = new Interval1D(4, 10);
		
		Interval2D c = new Interval2D(a, b);
		assertTrue(c.contains(34,10));
	}
	
	@Test
	public void ContainsTest3(){
		Interval1D a = new Interval1D(-2, 34);
		Interval1D b = new Interval1D(4, 10);
		
		Interval2D c = new Interval2D(a, b);
		assertFalse(c.contains(-2,2));
	}
	
	@Test
	public void ContainsTest4(){
		Interval1D a = new Interval1D(-2, 34);
		Interval1D b = new Interval1D(4, 10);
		
		Interval2D c = new Interval2D(a, b);
		assertFalse(c.contains(-3,8));
	}
	
	@Test
	public void TestSize(){
		Interval1D a = new Interval1D(-2, 2);
		Interval1D b = new Interval1D(4, 10);
		
		Interval2D c = new Interval2D(a, b);
		assertEquals(35, c.size());
	}
	
	@Test
	public void TestSizeq(){
		Interval1D a = new Interval1D(-2, -2);
		Interval1D b = new Interval1D(4, 4);
		
		Interval2D c = new Interval2D(a, b);
		assertEquals(1, c.size());
	}
	
	@Test
	public void IntersectionSize(){
		Interval1D a = new Interval1D(-3, -1);
		Interval1D b = new Interval1D(4, 7);
		Interval2D c = new Interval2D(a, b);
		
		Interval1D d = new Interval1D(-2, -1);
		Interval1D e = new Interval1D(3, 6);
		Interval2D f = new Interval2D(d, e);
		
		assertEquals(6, c.sizeOfIntersectionWith(f));
	}
	
	@Test
	public void IntersectionSizeBis(){
		Interval1D a = new Interval1D(-2, 2);
		Interval1D b = new Interval1D(4, 45);
		Interval2D c = new Interval2D(a, b);
		
		Interval1D d = new Interval1D(-22, -2);
		Interval1D e = new Interval1D(43, 48);
		Interval2D f = new Interval2D(d, e);
		
		assertTrue(c.sizeOfIntersectionWith(f) == f.sizeOfIntersectionWith(c));
	}
	
	@Test
	public void IntersectionSize1(){
		Interval1D a = new Interval1D(-2, 2);
		Interval1D b = new Interval1D(4, 7);
		Interval2D c = new Interval2D(a, b);
		
		Interval1D d = new Interval1D(-2, 2);
		Interval1D e = new Interval1D(4, 7);
		Interval2D f = new Interval2D(d, e);
		
		assertEquals(20, c.sizeOfIntersectionWith(f));
	}
	
	@Test
	public void IntersectionSize2(){
		Interval1D a = new Interval1D(-2, 2);
		Interval1D b = new Interval1D(4, 7);
		Interval2D c = new Interval2D(a, b);
		
		assertEquals(20, c.sizeOfIntersectionWith(c));
	}
	
	@Test
	public void IntersectionSize3(){
		Interval1D a = new Interval1D(-2, 2);
		Interval1D b = new Interval1D(4, 7);
		Interval2D c = new Interval2D(a, b);
		
		Interval1D d = new Interval1D(2, 3);
		Interval1D e = new Interval1D(7, 17);
		Interval2D f = new Interval2D(d, e);
		
		assertEquals(1, c.sizeOfIntersectionWith(f));
	}
	
	@Test
	public void IntersectionSize5(){
		Interval1D a = new Interval1D(-2, 2);
		Interval1D b = new Interval1D(4, 7);
		Interval2D c = new Interval2D(a, b);
		
		Interval1D d = new Interval1D(4, 9);
		Interval1D e = new Interval1D(14, 17);
		Interval2D f = new Interval2D(d, e);
		
		assertEquals(0, c.sizeOfIntersectionWith(f));
	}
	
	@Test
	public void BoundingUnion(){
		Interval1D a = new Interval1D(-2, 2);
		Interval1D b = new Interval1D(4, 7);
		Interval2D c = new Interval2D(a, b);
		
		assertEquals(c, c.boundingUnion(c));
	}
	
	@Test
	public void BoundingUnion1(){
		Interval1D a = new Interval1D(-2, -2);
		Interval1D b = new Interval1D(4, 4);
		Interval2D c = new Interval2D(a, b);
		
		Interval1D d = new Interval1D(-2, 9);
		Interval1D e = new Interval1D(4, 17);
		Interval2D f = new Interval2D(d, e);
		
		assertEquals(f, c.boundingUnion(f));
	}
	
	@Test
	public void BoundingUnion2(){
		Interval1D a = new Interval1D(-5, -2);
		Interval1D b = new Interval1D(0, 4);
		Interval2D c = new Interval2D(a, b);
		
		Interval1D d = new Interval1D(-2, 9);
		Interval1D e = new Interval1D(2, 17);
		Interval2D f = new Interval2D(d, e);
		
		Interval1D g = new Interval1D(-5, 9);
		Interval1D h = new Interval1D(0, 17);
		Interval2D expected = new Interval2D(g, h);
		
		assertEquals(expected, c.boundingUnion(f));
	}
	
	@Test
	public void BoundingUnion3(){
		Interval1D a = new Interval1D(-5, -2);
		Interval1D b = new Interval1D(0, 4);
		Interval2D c = new Interval2D(a, b);
		
		Interval1D d = new Interval1D(-2, 9);
		Interval1D e = new Interval1D(2, 17);
		Interval2D f = new Interval2D(d, e);
		
		assertTrue(c.boundingUnion(f).equals(f.boundingUnion(c)));
	}
	
	@Test
	public void CheckUnionable(){
		Interval1D a = new Interval1D(-5, -2);
		Interval1D b = new Interval1D(0, 4);
		Interval2D c = new Interval2D(a, b);
		
		Interval1D d = new Interval1D(-5, 9);
		Interval1D e = new Interval1D(2, 17);
		Interval2D f = new Interval2D(d, e);
		
		assertFalse(c.isUnionableWith(f));
	}
	
	@Test
	public void CheckUnionable1(){
		Interval1D a = new Interval1D(-5, -2);
		Interval1D b = new Interval1D(0, 4);
		Interval2D c = new Interval2D(a, b);
		
		Interval1D d = new Interval1D(-1, 9);
		Interval1D e = new Interval1D(-7, -1);
		Interval2D f = new Interval2D(d, e);
		
		assertFalse(c.isUnionableWith(f));
	}
	
	@Test
	public void CheckUnionable2(){
		Interval1D a = new Interval1D(-5, -2);
		Interval1D b = new Interval1D(0, 4);
		Interval2D c = new Interval2D(a, b);
		
		Interval1D d = new Interval1D(-2, 9);
		Interval1D e = new Interval1D(4, 12);
		Interval2D f = new Interval2D(d, e);
		
		assertFalse(c.isUnionableWith(f));
	}
	
	@Test
	public void CheckUnionable3(){
		Interval1D a = new Interval1D(-5, -2);
		Interval1D b = new Interval1D(0, 4);
		Interval2D c = new Interval2D(a, b);
		
		Interval1D d = new Interval1D(0, 9);
		Interval1D e = new Interval1D(2, 12);
		Interval2D f = new Interval2D(d, e);
		
		assertFalse(c.isUnionableWith(f));
	}
	
	@Test
	public void CheckUnionable4(){
		Interval1D a = new Interval1D(-5, -2);
		Interval1D b = new Interval1D(0, 4);
		Interval2D c = new Interval2D(a, b);
		
		Interval1D d = new Interval1D(-5, -2);
		Interval1D e = new Interval1D(2, 12);
		Interval2D f = new Interval2D(d, e);
		
		assertTrue(c.isUnionableWith(f));
	}
	
	@Test
	public void CheckUnionable5(){
		Interval1D a = new Interval1D(-15, -6);
		Interval1D b = new Interval1D(2, 12);
		Interval2D c = new Interval2D(a, b);
		
		Interval1D d = new Interval1D(-5, -2);
		Interval1D e = new Interval1D(2, 12);
		Interval2D f = new Interval2D(d, e);
		
		assertTrue(c.isUnionableWith(f));
	}
	
	@Test
	public void CheckUnionable6(){
		Interval1D a = new Interval1D(-15, -6);
		Interval1D b = new Interval1D(2, 12);
		Interval2D c = new Interval2D(a, b);
		
		assertTrue(c.isUnionableWith(c));
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void Union(){
		Interval1D a = new Interval1D(-5, -2);
		Interval1D b = new Interval1D(0, 4);
		Interval2D c = new Interval2D(a, b);
		
		Interval1D d = new Interval1D(0, 9);
		Interval1D e = new Interval1D(2, 12);
		Interval2D f = new Interval2D(d, e);
		
		c.union(f);
	}
	
	@Test 
	public void Union1(){
		Interval1D a = new Interval1D(-5, -2);
		Interval1D b = new Interval1D(0, 4);
		Interval2D c = new Interval2D(a, b);
		
		Interval1D d = new Interval1D(-5, -2);
		Interval1D e = new Interval1D(2, 12);
		Interval2D f = new Interval2D(d, e);
		
		Interval1D g = new Interval1D(-5, -2);
		Interval1D h = new Interval1D(0, 12);
		Interval2D expected = new Interval2D(g, h);
		
		assertEquals(expected, c.union(f));
	}
	
	@Test 
	public void Union2(){
		Interval1D a = new Interval1D(-5, -2);
		Interval1D b = new Interval1D(0, 4);
		Interval2D c = new Interval2D(a, b);
		
		assertEquals(c, c.union(c));
	}
	
	@Test 
	public void Union3(){
		Interval1D a = new Interval1D(-5, -2);
		Interval1D b = new Interval1D(0, 4);
		Interval2D c = new Interval2D(a, b);
		
		Interval1D d = new Interval1D(-5, -2);
		Interval1D e = new Interval1D(2, 12);
		Interval2D f = new Interval2D(d, e);
		
		assertTrue(c.union(f).equals(f.union(c)));
	}
	
	@Test
	public void EqualTest(){
		Interval1D a = new Interval1D(-5, -2);
		Interval1D b = new Interval1D(0, 4);
		Interval2D c = new Interval2D(a, b);
		
		assertFalse(c.equals(null));
	}
	
	@Test
	public void EqualTest1(){
		Interval1D a = new Interval1D(-5, -2);
		Interval1D b = new Interval1D(0, 4);
		Interval2D c = new Interval2D(a, b);
		
		Interval1D d = new Interval1D(0, 9);
		Interval2D f = new Interval2D(d, b);
		
		assertFalse(c.equals(f));
	}
	
	@Test
	public void EqualTest2(){
		Interval1D a = new Interval1D(-5, -2);
		Interval1D b = new Interval1D(0, 4);
		Interval2D c = new Interval2D(a, b);
		
		Interval1D d = new Interval1D(-5, -2);
		Interval1D e = new Interval1D(0, 4);
		Interval2D f = new Interval2D(d, e);
		
		assertTrue(c.equals(f));
	}
	
	@Test
	public void EqualTest2bis(){
		Interval1D a = new Interval1D(-5, -2);
		Interval1D b = new Interval1D(0, 4);
		Interval2D c = new Interval2D(a, b);
		
		Interval2D f = new Interval2D(a, b);
		
		assertTrue(c.equals(f));
	}
	
	@Test
	public void EqualTest3(){
		Interval1D a = new Interval1D(-5, -2);
		Interval1D b = new Interval1D(0, 4);
		Interval2D c = new Interval2D(a, b);
		
		assertTrue(c.equals(c));
	}
	
	@Test
	public void hashTest(){
		Interval1D a = new Interval1D(-5, -2);
		Interval1D b = new Interval1D(0, 4);
		Interval2D c = new Interval2D(a, b);
		
		Interval1D d = new Interval1D(-5, -2);
		Interval1D e = new Interval1D(0, 4);
		Interval2D f = new Interval2D(d, e);
		
		assertTrue(c.hashCode() == f.hashCode());
	}
	
	@Test
	public void hashTest1(){
		Interval1D a = new Interval1D(-5, -2);
		Interval1D b = new Interval1D(0, 4);
		Interval2D c = new Interval2D(a, b);
		
		assertTrue(c.hashCode() == c.hashCode());
	}
	
	@Test
	public void hashTest3(){
		Interval1D a = new Interval1D(-5, -2);
		Interval1D b = new Interval1D(0, 4);
		Interval2D c = new Interval2D(a, b);
		
		Interval2D f = new Interval2D(a, b);
		
		assertTrue(c.hashCode() == f.hashCode());
	}
	
	@Test
	public void StringTest(){
		Interval1D a = new Interval1D(-5, -2);
		Interval1D b = new Interval1D(0, 4);
		Interval2D c = new Interval2D(a, b);
		
		assertEquals("[-5..-2]x[0..4]", c.toString());

	}
	
	@Test
	public void StringTest1(){
		Interval1D a = new Interval1D(-123, 123);
		Interval1D b = new Interval1D(0, 456);
		Interval2D c = new Interval2D(a, b);
		
		assertEquals("[-123..123]x[0..456]", c.toString());

	}

}
