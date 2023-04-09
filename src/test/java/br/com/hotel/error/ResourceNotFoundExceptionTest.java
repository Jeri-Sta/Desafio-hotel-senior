package br.com.hotel.error;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ResourceNotFoundExceptionTest {

	@Test
	void testResourceNotFoundException() {
		try {
			throw new ResourceNotFoundException("Teste");
		} catch(ResourceNotFoundException e) {
			assertEquals(e.getMessage(), "Teste");
		}
	}

}
