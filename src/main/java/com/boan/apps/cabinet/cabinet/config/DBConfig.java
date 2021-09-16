package com.boan.apps.cabinet.cabinet.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.util.Objects;


//public class DBConfig {
//
//	@Autowired
//	Environment env;
//@Bean
//public DriverManagerDataSource dataSource() {
//final DriverManagerDataSource dataSource = new DriverManagerDataSource();
//		dataSource.setDriverClassName(Objects.requireNonNull(env.getProperty("driverClassName")));
//		dataSource.setUrl(env.getProperty("url"));
//		dataSource.setUsername(env.getProperty("user"));
//		dataSource.setPassword(env.getProperty("password"));
//		return dataSource;
//		}
//		}
