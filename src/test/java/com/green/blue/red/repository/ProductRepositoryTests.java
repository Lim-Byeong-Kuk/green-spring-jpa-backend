package com.green.blue.red.repository;

import com.green.blue.red.domain.Product;
import com.green.blue.red.domain.ProductImage;
import com.green.blue.red.dto.NaDTO;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;

import java.util.*;

@SpringBootTest
@Slf4j
public class ProductRepositoryTests {

    @Autowired
    ProductRepository repository;

    @Test
    public void testInsert() {
        for(int i=0; i<10; i++) {
            Product p = Product.builder()
                    .pname("상품"+i)
                    .price(100*i)
                    .pdesc("상품 설명"+i)
                    .build();
            p.addImageString("image1.jpg");
            p.addImageString("image2.jpg");
            repository.save(p);
            log.info("========");
        }
    }


    // Product 100개, Product 1개당 이미지 3~7개
    @Test
    public void testInsert2() {
        for(int i=0; i<100; i++) {
            Product product = Product.builder()
                    .pname("상품 이름"+i)
                    .price((int)(Math.random()*10000)+1)
                    .pdesc("상품 설명:"+i)
                    .build();

            int num = (int)(Math.random()*5)+3;

            for(int j=0; j<num; j++) {
                product.addImageString("image_name"+j+".jpg");
            }
            repository.save(product);
        }
    }

    @Test
    public void testInsert3(){
        int i =0;
        int cnt=0;
        do{
            Product p= Product.builder()
                    .pname("상품" +i)
                    .price(100*i)
                    .pdesc("상품 설명" +i)
                    .build();
            do{
                cnt=0;
                int z =(int)(Math.random()*10);
                if(z>=3&& z<=7){
                    for (int j = 0; j <z ; j++) {
                        p.addImageString(UUID.randomUUID() +"_image.jpg");
                        cnt++;
                    }
                    break;
                }
                log.info("i={},z={},cnt={}",i,z,cnt);
            } while (cnt<100);
            repository.save(p);
            log.info("==========================");
            i++;
        } while (i<100);
    }

    @Commit
    @Transactional
    @Test
    public void testDeleteImage() {
        repository.updateToDelete(11l, true);
    }

    @Test
    public void testRead2() {
        Optional<Product> result = repository.selectOne(11l);
        Product product = result.orElseThrow();
        log.info("product: {}",product);
        log.info("product.getImageList: {}",product.getImageList());
    }


    //dto 패키지에 클래스 생성
    //NaDTO

    @Test
    public void testNaDTO() {
        Long[] dtoPnoList = {11l, 12l, 13l, 14l, 15l};
        List<NaDTO> naDTOList = new ArrayList<>();
        // db 에서 selectOne 으로 5개 조회하여 naDTOList 에 담기

        Arrays.stream(dtoPnoList).forEach( i -> {

            Product product = repository.selectOne(i).orElseThrow();

            List<String> fileNames = new ArrayList<>();
            for(ProductImage image : product.getImageList()) {
                fileNames.add(image.getFileName());
            }

            NaDTO dto = NaDTO.builder()
                    .pno(product.getPno())
                    .pname(product.getPname())
                    .price(product.getPrice())
                    .pdesc(product.getPdesc())
                    .fileNames(fileNames)
                    .build();
            naDTOList.add(dto);
        });

        log.info("naDTOList : {}",naDTOList);

    }

    @Test
    public void testDelete() {
        repository.deleteById(21l);
    }

    @Test
    public void testUpdate() {
        Long pno = 12l;
        Product product = repository.selectOne(pno).get();
        product.changeName("12번 상품");
        product.changeDesc("12번 상품 설명입니다.");
        product.changePrice(3400);

        //첨부 파일 수정
        product.clearList();
        product.addImageString(UUID.randomUUID().toString()+"_"+"NewiMAGE1.jpg");
        product.addImageString(UUID.randomUUID().toString()+"_"+"NewiMAGE2.jpg");
        product.addImageString(UUID.randomUUID().toString()+"_"+"NewiMAGE3.jpg");
        repository.save(product);
        log.info("product: {}",product);
    }

    @Test
    public void testList() {
        Pageable pageable = PageRequest.of(0,10, Sort.by("pno").descending());
        Page<Object[]> result = repository.selectList(pageable);
        result.getContent().forEach(i -> log.info("data=> {}", i));
    }

    @Test
    public void testList2() {
        int size = repository.findAll().size();


        int num = (int)(Math.ceil(size/10f));
        for (int i=0; i<num ; i++) {
            Pageable pageable = PageRequest.of(i,10, Sort.by("pno").descending());
            Page<Object[]> result = repository.selectList(pageable);
            result.getContent().forEach(p -> log.info("data => {}",p));
        }

    }

}
