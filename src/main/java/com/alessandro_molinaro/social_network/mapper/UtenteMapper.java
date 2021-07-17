package com.alessandro_molinaro.social_network.mapper;

import com.alessandro_molinaro.social_network.dto.UtenteDTO;
import com.alessandro_molinaro.social_network.entity.Utente;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UtenteMapper {

    UtenteDTO toDto(Utente utente);
}
