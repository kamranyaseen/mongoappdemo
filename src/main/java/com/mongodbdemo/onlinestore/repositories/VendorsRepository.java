package com.mongodbdemo.onlinestore.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.mongodbdemo.onlinestore.models.Vendor;

import java.util.List;

public interface VendorsRepository extends MongoRepository<Vendor, String>
{
    List<Vendor> findByName(String name);
    List<Vendor> findVendorByName(String name);
    List<Vendor> findByEmail(String email);
}
