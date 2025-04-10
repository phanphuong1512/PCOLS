package fpt.swp.pcols.service.impl;

import fpt.swp.pcols.entity.Brand;
import fpt.swp.pcols.repository.BrandRepository;
import fpt.swp.pcols.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;

    @Override
    public List<Brand> findAll() {
        return this.brandRepository.findAll();
    }

    @Override
    public Optional<Brand> findById(Long brandId) {
        return this.brandRepository.findById(brandId);
    }
}
