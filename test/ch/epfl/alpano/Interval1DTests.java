package ch.epfl.alpano;

import static org.junit.Assert.*;

import org.junit.Test;

public class Interval1DTests {

	@Test
	public void NewInterval(){
		new Interval1D(-2, 34);
	}
	
	@Test
	public void NewInterval1(){
		new Interval1D(-2, -2);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void NewIntervalWithIllegalArguments(){
		new Interval1D(-2, -3);
	}
	
	@Test
	public void IncludedFrom(){
		Interval1D a = new Interval1D(-2, 34);
		
		assertEquals(-2, a.includedFrom(), 0);
	}
	
	@Test
	public void IncludedTo(){
		Interval1D a = new Interval1D(-2, 34);
		
		assertEquals(34, a.includedTo(), 0);
	}
	
	@Test
	public void ContainsTest(){
		Interval1D a = new Interval1D(-2, 34);
		
		assertTrue(a.contains(0));
	}
	
	@Test
	public void ContainsTest1(){
		Interval1D a = new Interval1D(-2, 34);
		
		assertTrue(a.contains(-2));
	}
	
	@Test
	public void ContainsTest2(){
		Interval1D a = new Interval1D(-2, 34);
		
		assertTrue(a.contains(34));
	}
	
	@Test
	public void ContainsTest3(){
		Interval1D a = new Interval1D(-2, -2);
		
		assertTrue(a.contains(-2));
	}
	
	@Test
	public void ContainsTest4(){
		Interval1D a = new Interval1D(-2, -2);
		
		assertFalse(a.contains(2));
	}
	
	@Test
	public void ContainsTest5(){
		Interval1D a = new Interval1D(-2, 34);
		
		assertFalse(a.contains(40));
	}
	
	@Test
	public void SizeTest(){
		Interval1D a = new Interval1D(-2, -2);
		
		assertEquals(1, a.size());
	}
	
	@Test
	public void SizeTest1(){
		Interval1D a = new Interval1D(-2, 2);
		
		assertEquals(5, a.size());
	}
	
	@Test
	public void IntersectionSize(){
		Interval1D a = new Interval1D(-2, 34);
		Interval1D b = new Interval1D(0, 22);
		
		assertEquals(23, a.sizeOfIntersectionWith(b));
	}
	
	@Test
	public void IntersectionSizeBis(){
		Interval1D a = new Interval1D(-2, 34);
		Interval1D b = new Interval1D(0, 22);
		
		assertTrue(a.sizeOfIntersectionWith(b) == b.sizeOfIntersectionWith(a));
	}
	
	@Test
	public void IntersectionSize1(){
		Interval1D a = new Interval1D(-2, 34);
		Interval1D b = new Interval1D(-2, 34);
		
		assertEquals(37, a.sizeOfIntersectionWith(b));
	}
	
	@Test
	public void IntersectionSize2(){
		Interval1D a = new Interval1D(-2, 34);
		
		assertEquals(37, a.sizeOfIntersectionWith(a));
	}
	
	@Test
	public void IntersectionSize3(){
		Interval1D a = new Interval1D(-2, 34);
		Interval1D b = new Interval1D(-2, -2);
		
		assertEquals(1, a.sizeOfIntersectionWith(b));
	}
	
	@Test
	public void IntersectionSize4(){
		Interval1D a = new Interval1D(-2, 34);
		Interval1D b = new Interval1D(-100, -2);
		
		assertEquals(1, a.sizeOfIntersectionWith(b));
	}
	
	@Test
	public void IntersectionSize5(){
		Interval1D a = new Interval1D(-2, 34);
		Interval1D b = new Interval1D(-100, -3);
		
		assertEquals(0, a.sizeOfIntersectionWith(b));
	}
	
	@Test
	public void BoundingUnion(){
		Interval1D a = new Interval1D(-2, 34);
		
		assertEquals(a, a.boundingUnion(a));
	}
	
	@Test
	public void BoundingUnion1(){
		Interval1D a = new Interval1D(-2, -2);
		Interval1D b = new Interval1D(-2, 12);
		
		assertEquals(b, a.boundingUnion(b));
	}
	
	@Test
	public void BoundingUnion2(){
		Interval1D a = new Interval1D(-2, -2);
		Interval1D b = new Interval1D(10, 12);
		
		assertEquals(new Interval1D(-2, 12), a.boundingUnion(b));
	}
	
	@Test
	public void BoundingUnion3(){
		Interval1D a = new Interval1D(-2, -2);
		Interval1D b = new Interval1D(10, 12);
		
		assertTrue(a.boundingUnion(b).equals(b.boundingUnion(a)));
	}
	
	@Test
	public void CheckUnionable(){
		Interval1D a = new Interval1D(-2, 2);
		Interval1D b = new Interval1D(0, 12);
		
		assertTrue(a.isUnionableWith(b));
	}
	
	@Test
	public void CheckUnionable1(){
		Interval1D a = new Interval1D(-2, 2);
		
		assertTrue(a.isUnionableWith(a));
	}
	
	@Test
	public void CheckUnionable2(){
		Interval1D a = new Interval1D(-2, 2);
		Interval1D b = new Interval1D(3, 12);

		assertTrue(a.isUnionableWith(b));
	}
	
	@Test
	public void CheckUnionable3(){
		Interval1D a = new Interval1D(-2, 2);
		Interval1D b = new Interval1D(4, 12);

		assertFalse(a.isUnionableWith(b));
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void Union(){
		Interval1D a = new Interval1D(-2, 2);
		Interval1D b = new Interval1D(4, 12);
		a.union(b);
	}
	
	@Test 
	public void Union1(){
		Interval1D a = new Interval1D(-2, 2);
		Interval1D b = new Interval1D(0, 12);
		
		assertEquals(new Interval1D(-2, 12), a.union(b));
	}
	
	@Test 
	public void Union2(){
		Interval1D a = new Interval1D(-2, 2);
		
		assertEquals(a, a.union(a));
	}
	
	@Test 
	public void Union3(){
		Interval1D a = new Interval1D(-2, 2);
		Interval1D b = new Interval1D(0, 12);
		
		assertTrue(a.union(b).equals(b.union(a)));
	}
	
	@Test
	public void EqualTest(){
		Interval1D a = new Interval1D(-2, 2);
		
		assertFalse(a.equals(null));
	}
	
	@Test
	public void EqualTest1(){
		Interval1D a = new Interval1D(-2, 2);
		Interval1D b = new Interval1D(-2, 12);

		assertFalse(a.equals(b));
	}
	
	@Test
	public void EqualTest2(){
		Interval1D a = new Interval1D(-2, 2);
		Interval1D b = new Interval1D(-2, 2);

		assertTrue(a.equals(b));
	}
	
	@Test
	public void EqualTest3(){
		Interval1D a = new Interval1D(-2, 2);

		assertTrue(a.equals(a));
	}
	
	@Test
	public void hashTest(){
		Interval1D a = new Interval1D(-2, 2);
		Interval1D b = new Interval1D(-2, 2);

		assertTrue(a.hashCode() == b.hashCode());
	}
	
	@Test
	public void hashTest1(){
		Interval1D a = new Interval1D(-2, 2);

		assertTrue(a.hashCode() == a.hashCode());
	}
	
	@Test
	public void hashTest3(){
		Interval1D a = new Interval1D(-2, 2);
		Interval1D b = new Interval1D(-2, 2);

		assertTrue(a.hashCode() == b.hashCode());
	}
	
	@Test
	public void StringTest(){
		Interval1D a = new Interval1D(-2, 2);
		
		assertEquals("[-2..2]", a.toString());

	}
	
	@Test
	public void StringTest1(){
		Interval1D a = new Interval1D(123, 234);
		
		assertEquals("[123..234]", a.toString());
	}

}
