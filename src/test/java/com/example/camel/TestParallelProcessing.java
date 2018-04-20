package com.example.camel;

import org.apache.camel.CamelContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestParallelProcessing {
	
	@Autowired
    protected CamelContext camelContext;

	@Test
	public void test_split() {
		
		long start = System.currentTimeMillis();
		
		camelContext.createProducerTemplate().requestBody("direct:split", getHugeString());
		
		long end = System.currentTimeMillis();
		
		// Duration(ms): 100396			ExecutorService		corePoolSize=10
		// Duration(ms): 10092			ExecutorService		corePoolSize=150
		// Duration(ms): 4104			ExecutorService		corePoolSize=300
		// Duration(ms): 2166			ExecutorService		corePoolSize=500
		// Duration(ms): 1202			ExecutorService		corePoolSize=1000
		
		System.out.println("Duration(ms): " + (end - start) );
	}
	
	private String getHugeString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 1000; i++) {
			sb.append(i + ",");
		}
		return sb.toString();
	}

}



