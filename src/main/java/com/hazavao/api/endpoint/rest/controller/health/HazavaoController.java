package com.hazavao.api.endpoint.rest.controller.health;


import com.hazavao.api.service.HazavaoService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@AllArgsConstructor
public class HazavaoController {

    private final HazavaoService hazavaoService;

    @GetMapping("/hazavao")
    public Map<String, String> getDefinition(@RequestParam("teny") String teny) {
        try {
            String definition = hazavaoService.getDefinition(teny);
            return Map.of(
                    "teny", teny,
                    "dikanteny", definition,
                    "status", "success"
            );
        } catch (Exception e) {
            return Map.of(
                    "teny", teny,
                    "dikanteny", "Tsy hita ny dikanteny",
                    "status", "error",
                    "error", e.getMessage()
            );
        }
    }
}