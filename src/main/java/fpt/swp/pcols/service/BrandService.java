package fpt.swp.pcols.service;

import fpt.swp.pcols.entity.Brand;

import java.util.List;

public interface BrandService {
    List<Brand> getAllBrands();

    Brand getBrandById(Long brandId);
}
