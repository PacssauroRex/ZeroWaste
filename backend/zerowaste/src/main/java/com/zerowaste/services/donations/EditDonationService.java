package com.zerowaste.services.donations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.zerowaste.dtos.donations.EditDonationDTO;
import com.zerowaste.models.donation.Donation;
import com.zerowaste.models.product.Product;
import com.zerowaste.repositories.DonationsRepository;
import com.zerowaste.repositories.ProductsRepository;
import com.zerowaste.services.donations.exceptions.DonationNotFoundException;
import com.zerowaste.services.products.exceptions.ProductNotFoundException;

@Service
public class EditDonationService {
    
    private final DonationsRepository donationsRepository;
    private final ProductsRepository productsRepository;

    public EditDonationService (DonationsRepository donationsRepository, ProductsRepository productsRepository) {
        this.donationsRepository = donationsRepository;
        this.productsRepository = productsRepository;
    }

    public void execute (Long id, EditDonationDTO dto) throws ProductNotFoundException, DonationNotFoundException {
        Optional<Donation> donationOpt = donationsRepository.findById(id);

        if(!donationOpt.isPresent() || donationOpt.get().getDeletedAt() != null)
            throw new DonationNotFoundException("Doação não encontrada!");
        
        Donation donation = donationOpt.get();

        donation.setName(dto.name());
        donation.setDate(dto.date());
        
        List<Product> products = new ArrayList<>();
        for (Long idProduct : dto.productsId()) {
            Optional<Product> product = productsRepository.findById(idProduct);
            if(product.isPresent() && product.get().getDeletedAt() == null)
                products.add(product.get());
            else 
                throw new ProductNotFoundException("Produto com id " + idProduct + " não encontrado");
        }
        
        donation.setProducts(products);
        donation.setUpdatedAt(LocalDate.now());

        donationsRepository.save(donation);
    }
}
