package com.green.blue.red.service;

import com.green.blue.red.domain.Address;
import com.green.blue.red.domain.AddressImage;
import com.green.blue.red.dto.AddressDTO;
import com.green.blue.red.dto.PageRequestDTO;
import com.green.blue.red.dto.PageResponseDTO;
import com.green.blue.red.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService{

    private final AddressRepository repository;


    @Override
    public PageResponseDTO<AddressDTO> getList(PageRequestDTO pageRequestDTO) {

        Pageable pageable = PageRequest.of(
                pageRequestDTO.getPage()-1,
                pageRequestDTO.getSize(),
                Sort.by("ano").descending());

        Page<Object[]> objects = repository.selectList(pageable);

        List<AddressDTO> addressDTOList = objects.get().map(o -> {
            Address address = (Address) o[0];
            AddressImage addressImage = (AddressImage) o[1];

            AddressDTO dto = AddressDTO.builder()
                    .ano(address.getAno())
                    .city(address.getCity())
                    .gu(address.getGu())
                    .dong(address.getDong())
                    .name(address.getName())
                    .build();

            String fileName = addressImage.getFileName();
            dto.setUploadFileNames(List.of(fileName));
            return dto;
        }).toList();

        long totalElements = objects.getTotalElements();

        return PageResponseDTO.<AddressDTO>withAll()
                .dtoList(addressDTOList)
                .totalCount(totalElements)
                .pageRequestDTO(pageRequestDTO)
                .build();
    }

    private Address dtoToEntity(AddressDTO dto) {

        Address address = Address.builder()
                .age(dto.getAge())
                .name(dto.getName())
                .city(dto.getCity())
                .gu(dto.getGu())
                .dong(dto.getDong())
                .build();

        List<String> uploadFileNames = dto.getUploadFileNames();
        if(uploadFileNames == null) return address;
        uploadFileNames.forEach(i -> address.addImageString(i));
        return address;
    }

    @Override
    public Long register(AddressDTO dto) {
        Address entity = dtoToEntity(dto);
        Address savedAddress = repository.save(entity);

        return savedAddress.getAno();
    }


    private AddressDTO entityToDTO(Address address) {
        AddressDTO dto = AddressDTO.builder()
                .ano(address.getAno())
                .age(address.getAge())
                .name(address.getName())
                .city(address.getCity())
                .gu(address.getGu())
                .dong(address.getDong())
                .build();

        List<AddressImage> imageList = address.getImageList();
        if(imageList==null || imageList.size()==0) return dto;

        List<String> fileNameList
                = imageList.stream().map(i -> i.getFileName()).toList();

        dto.setUploadFileNames(fileNameList);
        return dto;
    }

    @Override
    public AddressDTO get(Long ano) {
        Address address = repository.selectOne(ano).orElseThrow();
        AddressDTO addressDTO = entityToDTO(address);
        return addressDTO;
    }
}
