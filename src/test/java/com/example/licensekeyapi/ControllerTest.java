package com.example.licensekeyapi;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import com.example.licensekeyapi.controllers.Controller;
import static junit.framework.TestCase.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ControllerTest {

	@Test
	public void testGenerate() {
		Controller a = new Controller();
		ResponseEntity<String> resp1 = a.generate("Harry","Potter","C","asd");
		assertEquals(HttpStatus.UNAUTHORIZED, resp1.getStatusCode());

		Controller b = new Controller();
		ResponseEntity<String> resp2 = b.generate("Harry","Potter","opensesame","asd");
		assertEquals(HttpStatus.OK, resp2.getStatusCode());

		assertEquals("168f15dcf3a6ece4d6d1a8a833a542b7", resp2.getBody());
	}

	@Test
	public void testValidate() {
		Controller a = new Controller();
		ResponseEntity<String> resp = a.validate("Harry","Potter","C");
		assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());

		Controller b = new Controller();
		ResponseEntity<String> resp2 = b.validate("Harry","Potter","168f15dcf3a6ece4d6d1a8a833a542b7");
		assertEquals(HttpStatus.NO_CONTENT, resp2.getStatusCode());
	}
}
