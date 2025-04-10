package fpt.swp.pcols.service;

import fpt.swp.pcols.entity.Brand;

import java.util.List;
import java.util.Optional;

public interface BrandService {

    List<Brand> findAll();

    Optional<Brand> findById(Long brandId);
}
