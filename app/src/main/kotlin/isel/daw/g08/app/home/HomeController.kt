package isel.daw.g08.app.home

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(produces =["application/json"])
class HomeController {

    @GetMapping("/home")
    fun get() : ResponseEntity<HomeResponse> {
        return ResponseEntity.ok(HomeResponse.build())
    }
}