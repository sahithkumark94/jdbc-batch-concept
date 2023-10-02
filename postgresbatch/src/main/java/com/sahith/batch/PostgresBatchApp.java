package com.sahith.batch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.System.in;


@SpringBootApplication
@RestController()
public class PostgresBatchApp {

    @Autowired
    JdbcTemplate jdbcTemplate;

    private final static String url = "jdbc:postgresql://localhost:5432/batchdb";
    private final static String user = "postgres";
    private final static String password = "Weclome@123";
    private final static int count = 100000;

    public static void main(String[] args) {
        System.out.println("The System UP");
        SpringApplication.run(PostgresBatchApp.class,args);
    }

    @PostMapping
    public ResponseEntity<?> insertData(){
        StopWatch timer = new StopWatch();
        timer.start();
        List<String> arrOfQuery = getArrOfQuery();
        StringBuilder sql = new StringBuilder("INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS,SALARY,JOIN_DATE) VALUES ");

        for (String query : arrOfQuery) {
            sql.append(query);
        }
        String string = sql.toString();
        System.out.println("The size of query is :" + string);
        jdbcTemplate.batchUpdate(string);
        System.out.println(" completed");

        timer.stop();
        System.out.println("batchUpdate -> Total time in seconds: " + timer.getTotalTimeSeconds());
        return ResponseEntity.status(HttpStatus.OK).body("Success");
    }


    public static List<String> getArrOfQuery(){
        List<String> list = new ArrayList<>();
        for (int i =0; i <= count;i++){
            String random = getSaltString();
            String INSERT_USERS_SQL = null;
            if(i == count){
                INSERT_USERS_SQL = " ("+"'"+random+i+"'"+", 'Paul', 32, 'California', 20000.00,'2001-07-13')";

            }else{
                INSERT_USERS_SQL = " ("+"'"+random+i+"'"+", 'Paul', 32, 'California', 20000.00,'2001-07-13'),";

            }
            list.add(INSERT_USERS_SQL);
        }
        return list;
    }

    public static String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        return salt.toString();

    }

}
