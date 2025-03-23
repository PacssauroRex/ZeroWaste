package com.zerowaste.services.donations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.zerowaste.dtos.donations.CreateDonationDTO;
import com.zerowaste.models.donation.Donation;
import com.zerowaste.models.product.Product;
import com.zerowaste.repositories.DonationsRepository;
import com.zerowaste.repositories.ProductsRepository;
import com.zerowaste.services.products.exceptions.ProductNotFoundException;

@Service
public class CreateDonationService {
    
    private final DonationsRepository donationsRepository;
    private final ProductsRepository productsRepository;

    public CreateDonationService (DonationsRepository donationsRepository, ProductsRepository productsRepository) {
        this.donationsRepository = donationsRepository;
        this.productsRepository = productsRepository;
    }

    public void execute(CreateDonationDTO dto) throws ProductNotFoundException {
        Donation donation = new Donation();
        
        donation.setName(dto.name());
        donation.setDate(dto.date());
        
        List<Product> products = new ArrayList<>();
        for (Long id : dto.productsId()) {
            Optional<Product> product = productsRepository.findById(id);
            if(product.isPresent())
                products.add(product.get());
            else 
                throw new ProductNotFoundException("Produto com id " + id + " não encontrado");
        }
        
        donation.setProducts(products);
        donation.setCreatedAt(LocalDate.now());

        donationsRepository.save(donation);
    }
}
