package com.avizway.Springdemo.controller;

import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@org.springframework.stereotype.Controller
public class Controller {
	
	/*
	 * @GetMapping("/") public String hello() { return "Hello Aviz"; }
	 */
	
	@Value("${welcome.message}")
    private String message;
	
    @GetMapping("/")
    public String main(Model model) {
        model.addAttribute("message", message);
        return "welcome"; //view
    }
	

}
