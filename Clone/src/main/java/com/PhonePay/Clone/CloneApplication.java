package com.PhonePay.Clone;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.PhonePay.Clone", "com.phonepay.clone", "com.phonepe.clone"})
public class CloneApplication {

	public static void main(String[] args) {
		SpringApplication.run(CloneApplication.class, args);
	}
}


//package com.PhonePay.Clone;
//
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//
//@SpringBootApplication
//public class CloneApplication {
//
//	public static void main(String[] args) {
//		SpringApplication.run(CloneApplication.class, args);
//	}
//
//}
