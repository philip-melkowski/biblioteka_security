package com.example.rest_library.DTO;

import com.example.rest_library.encje.Ksiazka;
import lombok.Getter;

@Getter
public class KsiazkaDTO {
    Long id;
    String tytul;
    int rokWydania;
    String imieAutora;
    String nazwiskoAutora;


    public KsiazkaDTO(Ksiazka ksiazka)
    {
        id = ksiazka.getId();
        tytul = ksiazka.getTytul();
        rokWydania = ksiazka.getRokWydania();
        imieAutora = ksiazka.getAutor().getImie();
        nazwiskoAutora = ksiazka.getAutor().getNazwisko();
    }
}
