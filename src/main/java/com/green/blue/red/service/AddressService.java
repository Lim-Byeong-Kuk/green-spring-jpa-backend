package com.green.blue.red.service;

import com.green.blue.red.dto.AddressDTO;
import com.green.blue.red.dto.PageRequestDTO;
import com.green.blue.red.dto.PageResponseDTO;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface AddressService {
    PageResponseDTO<AddressDTO> getList(PageRequestDTO pageRequestDTO);
    Long register(AddressDTO dto);
    AddressDTO get(Long ano);

}
