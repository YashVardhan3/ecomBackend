package xa.sh.ecom.ecom.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import xa.sh.ecom.ecom.Service.GraphTokenService;


@RestController
@RequestMapping("/api")
public class TokenController {
    
    @Autowired
    private GraphTokenService gTokenSer;

    @GetMapping("/oToken")
    public ResponseEntity<Map<String, String>> getToken() {
        String token = gTokenSer.getAccessToken();
        return ResponseEntity.ok(Map.of("access_token", token));
    }
    
}
