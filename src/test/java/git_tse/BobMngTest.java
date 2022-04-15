package git_tse;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import control.BobMng;

class BobMngTest {

	@Test
	void test() {
		BobMng.setAsBob("test.prenom");
		BobMng.setAsBob("nom.prenom");
		BobMng.setAsBob("test.prenom");

		assertTrue(BobMng.isUserABob("test.prenom"));
		assertFalse(BobMng.isUserABob("test"));

		assertFalse(BobMng.isUserABob("test.pasBob"));

	}
}
