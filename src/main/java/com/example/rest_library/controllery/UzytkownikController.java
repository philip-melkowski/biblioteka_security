package com.example.rest_library.controllery;

import com.example.rest_library.DTO.LoginRequestDTO;
import com.example.rest_library.encje.Uzytkownik;
import com.example.rest_library.serwisy.UzytkownikService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/uzytkownicy")
@CrossOrigin(origins = "*")
public class UzytkownikController {
    private final UzytkownikService uzytkownikService;


    // 1. dodaj uzytkownika
    @PostMapping
    public ResponseEntity<?> addUzytkownik(@RequestBody Uzytkownik uzytkownik)
    {

        if(uzytkownikService.existsByUsername(uzytkownik.getUsername()))
        {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("nazwa użytkownika zajęta");
        }
        Uzytkownik savedUzytkownik = uzytkownikService.save(uzytkownik);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUzytkownik);
    }

    // 2. dodaj uzytkownika i zwroc jego adres
    @PostMapping("/dodaj")
    public ResponseEntity<?> addUzytkownik2(@RequestBody Uzytkownik uzytkownik)
    {
        if(uzytkownikService.existsByUsername(uzytkownik.getUsername()))
        {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("nazwa użytkownika zajęta");
        }
        Uzytkownik savedUzytkownik = uzytkownikService.save(uzytkownik);
        URI location = URI.create("/api/uzytkownicy/" + savedUzytkownik.getId());
        return ResponseEntity.created(location).body(savedUzytkownik);
    }


    // 3. znajdz po ID
    @GetMapping("/{id}")
    public ResponseEntity<Uzytkownik> findById(@PathVariable Long id)
    {
        return uzytkownikService.findById(id).map(u -> ResponseEntity.ok(u)).orElse(ResponseEntity.notFound().build());
    }

    // 4, znajdz po nazwie uzytkownika
    @GetMapping("/findByUsername")
    public ResponseEntity<Uzytkownik> findByUsername(@RequestParam String username)
    {
        return uzytkownikService.findByUsername(username).map(u -> ResponseEntity.ok(u)).orElse(ResponseEntity.notFound().build());
    }

    // 5. wszyscy uzytkownicy
    @GetMapping
    public List<Uzytkownik> findAll()
    {
        return uzytkownikService.findAll();
    }

    // 6. logowanie
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest, HttpSession session)
    {
        Optional<Uzytkownik> uzytkownik = uzytkownikService.findByUsername(loginRequest.getUsername());
        if(uzytkownik.isPresent() && uzytkownik.get().getPassword().equals(loginRequest.getPassword()))
        {
            session.setAttribute("username", loginRequest.getUsername());
            return ResponseEntity.ok(uzytkownik.get());
        }
        else
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Zły login lub hasło!");
        }
    }

    // 7. sprawdza jaki user jest zalgowowany
    @GetMapping("/ktoZalogowany")
    public ResponseEntity<String> ktoZalogowany(HttpSession session)
    {
        String login = (String) session.getAttribute("username");
        if(login!=null)
        {
            return ResponseEntity.ok(login);
        }
        else
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Nie zalogowano!");
        }
    }

    // 8. zwraca ID zalogowanego uzytkownika
    @GetMapping("/getUserId")
    public ResponseEntity<?> getUserId(HttpSession session)
    {
        String login = (String) session.getAttribute("username");
        if(login!=null)
        {
            return ResponseEntity.ok(uzytkownikService.findByUsername(login).get().getId());
        }
        else
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Nie zalogowano!");
        }
    }




}
