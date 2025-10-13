package com.green.blue.red.controller;

import com.green.blue.red.domain.Address;
import com.green.blue.red.domain.AddressImage;
import com.green.blue.red.dto.AddressDTO;
import com.green.blue.red.dto.PageRequestDTO;
import com.green.blue.red.dto.PageResponseDTO;
import com.green.blue.red.repository.AddressRepository;
import com.green.blue.red.service.AddressService;
import com.green.blue.red.util.CustomFileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/address")
public class AddressController {

    private final CustomFileUtil fileUtil;
    private final AddressService service;
    private final AddressRepository repository;
    private final ModelMapper mapper;


//    @GetMapping("/list")
//    public PageResponseDTO<AddressDTO> getList(PageRequestDTO pageRequestDTO) {
//        log.info("address controller: getList : {}",pageRequestDTO);
//        return service.getList(pageRequestDTO);
//    }

    @PostMapping("/")
    public Map<String, Long> register(@ModelAttribute AddressDTO addressDTO) {
        log.info("address controller: register : {}",addressDTO);
        List<MultipartFile> files = addressDTO.getFiles();
        List<String> uploadFileNames = fileUtil.saveFiles(files);
        log.info("address controller: uploadFileNames : {}", uploadFileNames);
        addressDTO.setUploadFileNames(uploadFileNames);
        Long ano = service.register(addressDTO);
        return Map.of("result",ano);
    }

    @GetMapping("/list")
    public ResponseEntity<List<AddressDTO>> list(PageRequestDTO dto) {
        log.info("address controller: getList : {}",dto);

        Pageable pageable = PageRequest.of(
                dto.getPage()-1,
                dto.getSize(),
                Sort.by("ano").descending());

        Page<Object[]> result = repository.selectList(pageable);

//        List<AddressDTO> addressDtoList = result.get().map(i -> {
//
//            Address a = (Address) i[0];
//            AddressImage ai = (AddressImage) i[1];
//            AddressDTO addDTO = mapper.map(i, AddressDTO.class);
//            addDTO.setUploadFileNames(List.of(ai.getFileName()));
//            return addDTO;
//        }).toList();



        List<AddressDTO> addressDtoList = result.get().map((i) -> {

            Address a = (Address) i[0];

            a.getAno();

            AddressImage ai = (AddressImage) i[1];
            AddressDTO addDTO = mapper.map(i, AddressDTO.class);
            addDTO.setUploadFileNames(List.of(ai.getFileName()));
            return addDTO;
        }).toList();


        return ResponseEntity.ok(addressDtoList);
    }
}
