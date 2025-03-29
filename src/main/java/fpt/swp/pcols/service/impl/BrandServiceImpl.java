package fpt.swp.pcols.service.impl;

import fpt.swp.pcols.entity.Brand;
import fpt.swp.pcols.repository.BrandRepository;
import fpt.swp.pcols.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;

    @Override
    public List<Brand> getAllBrands() {
        return this.brandRepository.findAll();
    }

    @Override
    public Brand getBrandById(Long brandId) {
        return this.brandRepository.findBrandById(brandId);
    }
}
