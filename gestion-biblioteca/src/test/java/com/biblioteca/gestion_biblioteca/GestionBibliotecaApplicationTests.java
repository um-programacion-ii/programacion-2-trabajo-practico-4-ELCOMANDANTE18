package com.biblioteca.gestion_biblioteca;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
class GestionBibliotecaApplicationTests {

	@Test
	void contextLoads() {
	}

}