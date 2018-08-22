package jmo.util;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class FillerTest {

	Filler filler;

	@Before
	public void setup() {
		filler = new Filler();
	}

	@Test
	public void fill_2doubles_success() {
		@SuppressWarnings("unused")
		class TestClass {
			double a = -1;
			Double b = -1D;
			
			public double getA() {
				return a;
			}
			public void setA(double a) {
				this.a = a;
			}
			public Double getB() {
				return b;
			}
			public void setB(Double b) {
				this.b = b;
			}
		}
		
		TestClass tc = new TestClass();
		filler.fill(tc);
		assertEquals(0D, tc.getA(), 0);
		assertEquals(0D, tc.getB(), 0);
	}
	
	@Test(expected=NullPointerException.class)
	public void fill_null_NPE() {
		filler.fill(null);
	}
}
