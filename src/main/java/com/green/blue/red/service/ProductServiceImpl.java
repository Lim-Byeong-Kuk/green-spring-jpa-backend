package com.green.blue.red.service;

import com.green.blue.red.domain.Product;
import com.green.blue.red.domain.ProductImage;
import com.green.blue.red.dto.PageRequestDTO;
import com.green.blue.red.dto.PageResponseDTO;
import com.green.blue.red.dto.ProductDTO;
import com.green.blue.red.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService{

    private final ProductRepository repository;
    private final ModelMapper mapper;

    @Override
    public PageResponseDTO<ProductDTO> getList(PageRequestDTO pageRequestDTO) {
        log.info("product service 전체 조회 : dto: {}",pageRequestDTO);
        Pageable pageable = PageRequest.of(
                pageRequestDTO.getPage()-1,
                pageRequestDTO.getSize(),
                Sort.by("pno").descending()
        );

        Page<Object[]> result = repository.selectList(pageable);

        List<ProductDTO> dtoList = result.get().map(object -> {
            Product product = (Product) object[0];
            ProductImage productImage = (ProductImage) object[1];
            ProductDTO productDTO = ProductDTO.builder()
                    .pno(product.getPno())
                    .pname(product.getPname())
                    .pdesc(product.getPdesc())
                    .price(product.getPrice())
                    .build();
            String imageStr = productImage.getFileName();
            productDTO.setUploadFileNames(List.of(imageStr));
            return productDTO;
        }).toList();

        long totalCount = result.getTotalElements();

        return PageResponseDTO.<ProductDTO>withAll()
                .dtoList(dtoList)
                .totalCount(totalCount)
                .pageRequestDTO(pageRequestDTO)
                .build();
    }

    private Product dtoToEntity(ProductDTO dto) {
        Product product = Product.builder()
                .pno(dto.getPno())
                .pname(dto.getPname())
                .pdesc(dto.getPdesc())
                .price(dto.getPrice())
                .build();

        List<String> uploadFileNames = dto.getUploadFileNames();
        if(uploadFileNames==null) return product;
        uploadFileNames.stream().forEach(i -> {
            product.addImageString(i);

        });
        return product;
    }

    @Override
    public Long register(ProductDTO dto) {
        log.info("service 등록 : dto: {}",dto);
        Product product = dtoToEntity(dto);
        Product result = repository.save(product);
        return result.getPno();
    }


    private ProductDTO entityToDTO(Product product) {
        ProductDTO productDTO = ProductDTO.builder()
                .pno(product.getPno())
                .pname(product.getPname())
                .pdesc(product.getPdesc())
                .price(product.getPrice())
                .build();

        List<ProductImage> imageList = product.getImageList();
        if(imageList == null || imageList.size()==0) return productDTO;
        List<String> fileNameList = imageList.stream().map(i -> i.getFileName()
        ).toList();

        log.info("fileNameList: {}", fileNameList);
        productDTO.setUploadFileNames(fileNameList);
        return productDTO;
    }

    @Override
    public ProductDTO get(Long pno) {
        Optional<Product> result = repository.selectOne(pno);
        Product product = result.orElseThrow();
        ProductDTO productDTO = entityToDTO(product);
        return productDTO;
    }

    @Override
    public void modify(ProductDTO dto) {
        log.info("service 수정 dto:{}", dto);
        Optional<Product> result = repository.findById(dto.getPno());
        Product product = result.orElseThrow();

        product.changeName(dto.getPname());
        product.changeDesc(dto.getPdesc());
        product.changePrice(dto.getPrice());
        product.clearList();

        List<String> uploadFileNames = dto.getUploadFileNames();
        if(uploadFileNames != null && uploadFileNames.size()>0) {
            uploadFileNames.forEach(i -> product.addImageString(i));
        }

        repository.save(product);
    }

    @Override
    public void remove(Long pno) {
        log.info("service 삭제 pno:{}",pno);
        repository.updateToDelete(pno,true);
    }
}
