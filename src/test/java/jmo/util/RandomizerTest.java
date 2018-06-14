package jmo.util;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;

import org.junit.Test;

public class RandomizerTest {

	Randomizer r;

	@Test
	public void getInt_zeroSeed_zero() {
		r = new RandomizerDecorator(0L);
		assertEquals(0, r.getInt());
	}

	@Test
	public void getByte_zeroSeed_zero() {
		r = new RandomizerDecorator(0L);
		assertEquals(0, r.getByte());
	}

	@Test
	public void getLong_zeroSeed_zero() {
		r = new RandomizerDecorator(0L);
		assertEquals(0, r.getLong());
	}

	@Test
	public void getShort_zeroSeed_zero() {
		r = new RandomizerDecorator(0L);
		assertEquals(0, r.getShort());
	}

	@Test
	public void getChar_zeroSeed_zero() {
		r = new RandomizerDecorator(0L);
		assertEquals(0, r.getChar(), 0);
	}

	@Test
	public void getFloat_zeroSeed_zero() {
		r = new RandomizerDecorator(0L);
		assertEquals(0, r.getFloat(), 0);
	}

	@Test
	public void getDouble_zeroSeed_zero() {
		r = new RandomizerDecorator(0L);
		assertEquals(0, r.getDouble(), 0);
	}
	
	@Test
	public void getString_zeroSeed_aaaaaaaa() {
		r = new RandomizerDecorator(0L);
		assertEquals("aaaaaaaa", r.getString(8));
	}

	class RandomizerDecorator extends Randomizer {
		public RandomizerDecorator(Long... longs) {
			this(new FakeGenerator(Arrays.asList(longs)));
		}

		public RandomizerDecorator(Random r) {
			super(r);
		}
	}

	class FakeGenerator extends Random {
		private static final long serialVersionUID = 1L;

		final Collection<Long> data;
		private Iterator<Long> it;

		public FakeGenerator(Collection<Long> data) {
			super();
			this.data = Collections.unmodifiableCollection(data);
			this.it = data.iterator();
		}

		@Override
		protected int next(int bits) {
			return (int) next();
		}

		private long next() {
			if (it.hasNext()) {
				return it.next();
			} else {
				it = data.iterator();
				return next();
			}
		}
	}
}
