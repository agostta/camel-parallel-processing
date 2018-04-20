package com.example.camel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.ValueBuilder;
import org.springframework.stereotype.Component;


@Component
public class ParallelProcessingRoute extends RouteBuilder{

	@Override
	public void configure() throws Exception {

		ValueBuilder tokenize = body().tokenize(",");

		MyBean math = new MyBean();

		ExecutorService executorService = new ThreadPoolExecutor(
			  100, 110, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
		
		from("direct:split")
			.split(tokenize).parallelProcessing().executorService(executorService)
				.to("direct:processTask");
		
		from("direct:processTask")
			.bean(math, "sum")
			.log("body: ${body}");
		
	}

	public class MyBean{
		public String sum(Exchange e) throws InterruptedException {
			String body = e.getIn().getBody(String.class);
			
			Thread.sleep(1000);
			
			return "body: " + body;
		}
	}
	
	
	
}
