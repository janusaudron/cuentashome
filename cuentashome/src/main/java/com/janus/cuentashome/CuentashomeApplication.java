package com.janus.cuentashome;

import javax.faces.webapp.FacesServlet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan(basePackages = { "com.janus.cuentashome.*" })
@EnableJpaRepositories("com.janus.cuentashome.business.repository")
@EntityScan("com.janus.cuentashome.business.domain")
@EnableScheduling
@ServletComponentScan
public class CuentashomeApplication {

	public static void main(String[] args) {
		SpringApplication.run(CuentashomeApplication.class, args);
	}

	@Bean
    public ServletRegistrationBean servletRegistrationBean() {
        FacesServlet servlet = new FacesServlet();
        return new ServletRegistrationBean(servlet, "*.xhtml");
    }

}
