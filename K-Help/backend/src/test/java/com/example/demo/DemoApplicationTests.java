package com.example.demo;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Disabled("Requires PostgreSQL from docker compose (docker compose up -d)")
class DemoApplicationTests {

	@Test
	void contextLoads() {
	}

}
