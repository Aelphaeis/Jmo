package jmo.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import jmo.util.ObjectComparator.Checker;
import jmo.util.ObjectComparator.DefaultChecker;

import org.junit.Before;
import org.junit.Test;

public class ObjectComparatorTest {

	private ObjectComparator<TestClass> comp;
		
	@Before
	public void setup() {
		comp = ObjectComparator.builder(TestClass.class).build();
	}

	@Test
	public void isPropertiesEqual_equalTimestamps_true() {
		TestClass tcA = new TestClass();
		TestClass tcB = new TestClass();
		tcA.setTs(tcA.getTs());
		assertTrue(comp.isPropertiesEqual(tcA, tcB));
	}

	@Test
	public void isPropertiesEqual_equalDifferentTimestamps_False() {
		TestClass tcA = new TestClass();
		TestClass tcB = new TestClass(tcA.getTs().getTime() + 5);
		
		assertFalse(comp.isPropertiesEqual(tcA, tcB));
	}
	
	@Test
	public void isPropertiesEqual_dateOverride_Equal() {
		Map<Class<?>, Checker<?>> override = new HashMap<>();
		override.put(Timestamp.class, new TimestampApproxChecker());
		comp = new ObjectComparator<>(TestClass.class, override);
		TestClass tcA = new TestClass();
		TestClass tcB = new TestClass(tcA.getTs().getTime() + 5);
		
		assertTrue(comp.isPropertiesEqual(tcA, tcB));
		
		tcB = new TestClass(tcA.getTs().getTime() + 1000);
		assertFalse(comp.isPropertiesEqual(tcA, tcB));
	}
	
	@Test
	public void isPropertiesEqual_oneNull_notEqual() {
		TestClass tcA = new TestClass();
		assertFalse(comp.isPropertiesEqual(tcA, null));
		assertFalse(comp.isPropertiesEqual(null, tcA));
	}
	
	@Test
	public void isPropertiesEqual_onePropertyNull_notEqual() {
		TestClass tcA = new TestClass();
		TestClass tcB = new TestClass();
		
		tcA.setTs(null);

		assertFalse(comp.isPropertiesEqual(tcA, tcB));
	}
	
	@Test
	public void isPropertiesEqual_sameProperty_Equal() {
		TestClass tcA = new TestClass(5);
		TestClass tcB = new TestClass();
		tcB.setTs(tcA.getTs());
		assertTrue(comp.isPropertiesEqual(tcA, tcB));
	}
	
	
	@Test
	public void isPropertyEqual_sameObject_equal() {
		TestClass tc = new TestClass();
		
		assertTrue(comp.isPropertiesEqual(tc, tc));
	}


	public static class TestClass {
		private Timestamp ts;
		
		public TestClass() {
			this(System.currentTimeMillis());
		}
		
		public TestClass(long l) {
			setTs(new Timestamp(l));
		}

		public Timestamp getTs() {
			return ts;
		}

		public void setTs(Timestamp ts) {
			this.ts = ts;
		}
	}
	
	public static class TimestampApproxChecker implements Checker<Timestamp> {
		
		private DefaultChecker checker = new DefaultChecker();

		@Override
		public boolean isEqual(Timestamp a, Timestamp b) {
			return checker.isEqual(a, b) || round(a).equals(round(b));
		}

		@Override
		public Class<Timestamp> getCheckedClass() {
			return Timestamp.class;
		}
		
		private static Timestamp round(Date d) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(d);
			if (cal.get(Calendar.MILLISECOND) >= 500 ) {
			  cal.set(Calendar.SECOND, cal.get(Calendar.SECOND) + 1);
			}
			cal.set(Calendar.MILLISECOND, 0);
			return new Timestamp(cal.getTime().getTime());
		}
	}
}
