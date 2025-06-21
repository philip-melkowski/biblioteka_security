package com.example.rest_library.controllery;

import com.example.rest_library.DTO.PrzeczytaneDTO;
import com.example.rest_library.encje.Przeczytane;
import com.example.rest_library.encje.Uzytkownik;
import com.example.rest_library.serwisy.PrzeczytaneService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/przeczytane")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class PrzeczytaneController {

    private final PrzeczytaneService przeczytaneService;


    // 1. zwroc wszystkie reordy przeczytane
    @GetMapping
    public List<PrzeczytaneDTO> findAll()
    {
        return przeczytaneService.findAll().stream().map(PrzeczytaneDTO::new).collect(Collectors.toList());
    }

    // 2. zwroc ksiazki przeczytane przez uzytkownika o ID=id
    @GetMapping("/uzytkownik/id/{id}")
    public List<PrzeczytaneDTO> findByUzytkownikId(@PathVariable Long id)
    {
        return przeczytaneService.findByUzytkownikId(id).stream().map(PrzeczytaneDTO::new).collect(Collectors.toList());
    }

    // 3. zwroc ksiazki przeczytane przez uzytkownika o nazwie uzytkownika = username
    @GetMapping("/uzytkownik/username/{username}")
    public List<PrzeczytaneDTO> findByUzytkownikUsername(@PathVariable String username)
    {
        return przeczytaneService.findByUzytkownikUsername(username).stream().map(PrzeczytaneDTO::new).collect(Collectors.toList());
    }

    // 4. zwroc wszystkie rekordy dla danej ksiazki o ID=id
    @GetMapping("/ksiazka/id/{ID}")
    public List<PrzeczytaneDTO> findByKsiazkaId(@PathVariable Long id)
    {
        return przeczytaneService.findByKsiazkaId(id).stream().map(PrzeczytaneDTO::new).collect(Collectors.toList());
    }

    // 5. zwroc rekord uzytkownik - ksiazka na podstawie ich ID
    @GetMapping("/{uzytkownikId}&{ksiazkaId}")
    public ResponseEntity<PrzeczytaneDTO> findByUzytkownikIdAndKsiazkaId(@PathVariable Long uzytkownikId, @PathVariable Long ksiazkaId)
    {
        return przeczytaneService.findByUzytkownikIdAndKsiazkaId(uzytkownikId, ksiazkaId).map(p -> ResponseEntity.ok(new PrzeczytaneDTO(p))).orElse(ResponseEntity.notFound().build());
    }

    // 6. srednia ocen ksiazki
    @GetMapping("/ksiazka/srednia/{id}")
    public ResponseEntity<Double> sredniaOcenKsiazkiById(@PathVariable Long id)
    {
        return ResponseEntity.ok(przeczytaneService.sredniaOcenKsiazkiById(id));
    }

    // 7. srednia ocen uzytkownika
    @GetMapping("/uzytkownik/srednia/{id}")
    public ResponseEntity<Double> sredniaOcenByUzytkownikId(@PathVariable Long id)
    {
        return ResponseEntity.ok(przeczytaneService.sredniaByUzytkownikId(id));
    }

    // 8. dodaj recenzje
    @PostMapping
    public ResponseEntity<?> save(@RequestBody Przeczytane przeczytane)
    {
        if(przeczytaneService.czyUzytkownikPrzeczytalKsiazke(przeczytane.getUzytkownik().getId(), przeczytane.getKsiazka().getId()))
        {
            return ResponseEntity.status(409).body("Uzytkownik juz przeczytal te ksiazke!\n");
        }
        Przeczytane savedPrzeczytane = przeczytaneService.save(przeczytane);
        return ResponseEntity.ok(new PrzeczytaneDTO(savedPrzeczytane));
    }

    @DeleteMapping("/{uzytkownikId}&{ksiazkaId}")
    public ResponseEntity<Void> delete(@PathVariable Long uzytkownikId, @PathVariable Long ksiazkaId)
    {

        przeczytaneService.findByUzytkownikIdAndKsiazkaId(uzytkownikId, ksiazkaId).ifPresent(p -> {
            przeczytaneService.delete(p);
        });
        return ResponseEntity.noContent().build();
    }







}
