package com.green.blue.red.repository;

import com.green.blue.red.domain.Address;
import com.green.blue.red.domain.AddressImage;
import com.green.blue.red.dto.AddressDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@SpringBootTest
@Slf4j
public class AddressRepositoryTests {

    @Autowired
    AddressRepository repository;


    @Test
    public void insert() {
        for(int i=0; i<100; i++) {
            Address ad = Address.builder()
                    .age(10 + i)
                    .name("k" + i)
                    .city("서울")
                    .gu("구" + i)
                    .dong("dong" + i)
                    .build();
            ad.addImageString("address_image"+i+".jpg");
            ad.addImageString("address_image"+i+1+".jpg");
            repository.save(ad);
        }
    }

    @Test
    public void selectOne() {
        Address address = repository.selectOne(1l).get();
        log.info("address : {}",address);
        log.info("address imageList : {}", address.getImageList());
    }

    @Test
    public void selectList() {
        Pageable pageable = PageRequest.of(1,10, Sort.by("ano").descending());
        Page<Object[]> objects = repository.selectList(pageable);

        objects.getContent().forEach(i -> {
            log.info("data => {}",i[0]);
            log.info("image => {}",i[1]);
        });

    }

//    @Test
//    public void selectList() {
//
//        Pageable pageable = PageRequest.of(1,10, Sort.by("ano").descending());
//        Page<Object[]> objects = repository.selectList(pageable);
//
//        objects.getContent().forEach(i -> {
//            log.info("data => {}",i[0]);
//            log.info("image => {}",i[1]);
//        });

//        objects.get().map(i -> {
//            Address address = (Address) i[0];
//            AddressImage addressImage = (AddressImage) i[1];
//
//            AddressDTO addressDto = AddressDTO.builder()
//                    .name(address.getName())
//                    .age(address.getAge())
//                    .city(address.getCity())
//                    .gu(address.getGu())
//                    .dong(address.getDong())
//                    .build();
//
//            String fileName = addressImage.getFileName();
//            if(fileName )
//
//            addressDto.setUploadFileNames();
//
//
//        })
//    }

}
