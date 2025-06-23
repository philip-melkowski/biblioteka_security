package com.example.rest_library.controllery;


import com.example.rest_library.DTO.KsiazkaDTO;
import com.example.rest_library.encje.Ksiazka;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.example.rest_library.serwisy.KsiazkaService;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/ksiazki")
@RequiredArgsConstructor
public class KsiazkaController {

    private final KsiazkaService ksiazkaService;

    // 1. znajdz wszystkie ksiazki
    @GetMapping
    public List<KsiazkaDTO> findAll()
    {
        return ksiazkaService.findAll().stream().map(KsiazkaDTO::new).collect(Collectors.toList());
    }

    // 2. znajdz ksiazki danego autora, po jego ID
    @GetMapping("/autor/{id}")
    public List<KsiazkaDTO> findByAutorId(@PathVariable Long id)
    {
        return ksiazkaService.findByAutorId(id).stream().map(KsiazkaDTO::new).collect(Collectors.toList());
    }

    // 3. znajdz po dokladnym tytule
    @GetMapping("/tytul")
    public ResponseEntity<KsiazkaDTO> findByTytul(@RequestParam String tytul)
    {

        return ksiazkaService.findByTytul(tytul).map(ksiazka -> ResponseEntity.ok(new KsiazkaDTO(ksiazka))).orElse(ResponseEntity.notFound().build());
    }

    // 4. znajdz ksiazki po roku wydania
    @GetMapping("/rok")
    public List<KsiazkaDTO> findByRokWydania(@RequestParam int rok)
    {
        return ksiazkaService.findByRokWydania(rok).stream().map(KsiazkaDTO::new).collect(Collectors.toList());
    }



    // 5. dodaj ksiazke
    @PostMapping()
    public ResponseEntity<Ksiazka> addKsiazka(@RequestBody Ksiazka ksiazka)
    {

        Ksiazka savedKsiazka = ksiazkaService.save(ksiazka);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedKsiazka);
    }

    // 6. dodaj ksiazke i zwroc jej adres w naglowku
    @PostMapping("/dodajZAdresem")
    public ResponseEntity<Ksiazka> addKsiazka2(@RequestBody Ksiazka ksiazka)
    {

        Ksiazka savedKsiazka = ksiazkaService.save(ksiazka);
        URI location = URI.create("/api/ksiazki/" + savedKsiazka.getId());
        return ResponseEntity.created(location).body(savedKsiazka);


    }

    // 7. usun ksiazke
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKsiazka(@PathVariable Long id)
    {
        ksiazkaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // 8. pokaz ksiazke na podstawie ID
    @GetMapping("/{id}")
    public ResponseEntity<KsiazkaDTO> findById(@PathVariable Long id)
    {
        return ksiazkaService.findById(id).map(k -> ResponseEntity.ok(new KsiazkaDTO(k))).orElse(ResponseEntity.notFound().build());
    }

    // 9. zwroc nieprzeczytane jeszcze
    @GetMapping("/nieprzeczytane")
    public List<KsiazkaDTO> findByUzytkownikUsernameAndPrzeczytaneFalse()
    {
        Authentication authentication = (Authentication) SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return ksiazkaService.findByUzytkownikUsernameAndPrzeczytaneFalse(username).stream().map(KsiazkaDTO::new).collect(Collectors.toList());
    }






}
