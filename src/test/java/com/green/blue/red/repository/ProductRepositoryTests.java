package com.green.blue.red.repository;

import com.green.blue.red.domain.Product;
import com.green.blue.red.domain.ProductImage;
import com.green.blue.red.dto.NaDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

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
        for(int i=0; i<50; i++) {
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

        // 한페이지에 몇개를 보여줄지, 몇번째 페이지를 보여줘야할지, 정렬은 어떤 기준으로 어떻게 할지 pageable 이 담고 있음
//        List<Object[]> content = result.getContent();
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


    @Test
    @DisplayName("Product 2개")
    public void insertTest() {
        for(int i=0; i<2; i++) {
            Product p = Product.builder()
                    .pname("상품 이름"+i)
                    .price(100*(i+5))
                    .pdesc("상품 설명"+i)
                    .build();
            p.addImageString("image1.jpg");
            p.addImageString("image2.jpg");
            repository.save(p);
            log.info("========");
        }
    }

    @Test
    @DisplayName("Product 수정")
    public void updateTest() {
        Product product = repository.selectOne(1l).get();

        List<ProductImage> list = new ArrayList<>();
        list.add(new ProductImage("fileName111",0));

        Product updatedProduct = Product.builder()
                .pname("상품이름111")
                .pdesc("상품설명111")
                .price(10000)
                .pno(1l)
                .imageList(list)
                .build();

        repository.save(updatedProduct);
    }

    @Test
    @DisplayName("Product 수정, Dirty Checking 이용")
    @Transactional // 테스트코드에서 @Transactional 을 붙이면 반영했다가 다시 롤백해,
    @Commit
    public void updateTest_DirtyCheckin() {
        Product product = repository.selectOne(1l).get();
        product.changeDesc("상품설명2222");
        /* JPA는 같은 트랜잭션 안에서 한번 조회한 Entity 를 Snapshopt 으로 기억한다.
        *  Entity 의 필드 값이 바뀌면 감지한다. -- Dirty Checking 이라고 불림
        *  */
    }

    @Test
    @DisplayName("Product 1개 삭제 테스트: 값타입의 종속성 테스트")
    @Commit
    @Transactional
    public void deleteTest() {
        repository.deleteById(2l);
    }

    @Test
    @DisplayName("Lazy Loading Test1")
    @Transactional
    public void findById_LazyLoading() {
        Product product = repository.findById(1l).get();
    }

    @Test
    @DisplayName("Lazy Loading Test2: 지연 로딩")
    @Transactional
    public void findById_LazyLoading2() {

        // 일단 findById 할때는 product 만 가져와
        Product product = repository.findById(3l).get();
        List<ProductImage> imageList = product.getImageList();
        System.out.println("============================");
        for(ProductImage image : imageList) {
            // 안에 내용물을 가져오려고 할 때, 추가로 쿼리를 날려서 데이터 가져오기 -> 지연 로딩 (Lazy Loading)
            String fileName = image.getFileName();
        }
    }

    @Test
    @DisplayName("Eager Loading Test1")
    @Transactional
    public void selectOne_EagerLoading() {
        Product product = repository.selectOne(3l).get();
    }


}
