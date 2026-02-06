package com.gateway;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RutaPrincipalController {
    
    @RequestMapping("/")
    public String home() {
        return "Bienvenido al Gateway de Pagos Seguru";
    }
}
